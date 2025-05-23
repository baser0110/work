package bsshelper.maincontroller;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.service.CurrentMgnService;
import bsshelper.externalapi.configurationmng.plannedmng.service.PlanMgnService;
import bsshelper.externalapi.configurationmng.plannedserv.entity.DryContactCableMocData;
import bsshelper.externalapi.configurationmng.plannedserv.entity.DryContactDeviceMocData;
import bsshelper.externalapi.configurationmng.plannedserv.entity.MocData;
import bsshelper.externalapi.configurationmng.plannedserv.util.PlannedServBodySettings;
import bsshelper.externalapi.configurationmng.plannedserv.mapper.DryContactCableMocDataMapper;
import bsshelper.externalapi.configurationmng.plannedserv.mapper.DryContactDeviceMocDataMapper;
import bsshelper.externalapi.configurationmng.plannedserv.wrapper.DryContactCableMocDataWrapper;
import bsshelper.externalapi.configurationmng.plannedserv.wrapper.DryContactDeviceMocDataWrapper;
import bsshelper.externalapi.configurationmng.plannedserv.wrapper.MocDataWrapper;
import bsshelper.externalapi.configurationmng.plannedserv.service.PlanServService;
import bsshelper.globalutil.ManagedElementType;
import bsshelper.globalutil.Severity;
import bsshelper.globalutil.entity.MessageEntity;
import bsshelper.service.LocalCacheService;
import bsshelper.service.TokenService;
import bsshelper.service.logger.LoggerUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Slf4j
@Controller
@RequestMapping(value = "/helper")
@RequiredArgsConstructor
public class DryContactController {
    private final CurrentMgnService currentMgnService;
    private final PlanServService planServService;
    private final PlanMgnService planMgnService;
    private final TokenService tokenService;
    private final LocalCacheService localCacheService;
    private static final Logger operationLog = LoggerUtil.getOperationLogger();


    @GetMapping("/dryContact")
    public String dryContact(Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        model.addAttribute("managedElement", null);
        model.addAttribute("repo", null);
        model.addAttribute("title", "External Alarms Manager");
        return "drycontact";
    }

    @GetMapping("/dryContact/{userLabel}")
    public String search(@PathVariable(value = "userLabel") String userLabel, String clearUserLabel, Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        ManagedElement managedElement = null;
        if (localCacheService.managedElementMap.containsKey(userLabel.trim().toUpperCase())) {
            managedElement = localCacheService.managedElementMap.get(userLabel.trim().toUpperCase());
        } else {
            managedElement = currentMgnService.getManagedElementByNeName(tokenService.getToken(), userLabel);
        }
        MocDataWrapper mocDataWrapper = null;
        if (managedElement == null) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "userLabel '" + userLabel + "' couldn't be found"));
            return "redirect:/helper/dryContact";
        }
        if (managedElement.getManagedElementType() == ManagedElementType.SDR) {
            mocDataWrapper = new DryContactDeviceMocDataWrapper(DryContactDeviceMocDataMapper.
                    toDryContactDeviceMocDataTo(currentMgnService.getDryContactDeviceMoc(tokenService.getToken(), managedElement)));
        } else {
            mocDataWrapper = new DryContactCableMocDataWrapper(DryContactCableMocDataMapper.
                    toDryContactCableMocDataTo(currentMgnService.getDryContactCableMoc(tokenService.getToken(), managedElement)));
        }
        localCacheService.mocDataRepositoryMap.put(id + "_" + managedElement.getUserLabel(), mocDataWrapper);

        if (!localCacheService.managedElementMap.containsKey(managedElement.getUserLabel())) {
            localCacheService.managedElementMap.put(managedElement.getUserLabel(), managedElement);
        }

        model.addAttribute("managedElement", managedElement);
        model.addAttribute("repo", mocDataWrapper);
        model.addAttribute("title", "External Alarms Manager");
        return "drycontact";
    }

    @PostMapping ("/dryContact/addRowSDR")
    public String dryContactAdd(@ModelAttribute("repo") DryContactDeviceMocDataWrapper repo,
                                @ModelAttribute("userLabel") String userLabel, Model model, HttpSession session) {
        String id = session.getId();
        localCacheService.mocDataRepositoryMap.put(id + "_" + userLabel, repo);
        localCacheService.mocDataRepositoryMap.get(id + "_" + userLabel).addNew();
        model.addAttribute("managedElement", localCacheService.managedElementMap.get(userLabel));
        model.addAttribute("repo", localCacheService.mocDataRepositoryMap.get(id + "_" + userLabel));
        model.addAttribute("title", "External Alarms Manager");
        return "drycontact";
    }

    @PostMapping ("/dryContact/addRowITBBU")
    public String dryContactAdd(@ModelAttribute("repo") DryContactCableMocDataWrapper repo,
                                @ModelAttribute("userLabel") String userLabel, Model model, HttpSession session) {
        String id = session.getId();
        localCacheService.mocDataRepositoryMap.put(id + "_" + userLabel, repo);
        localCacheService.mocDataRepositoryMap.get(id + "_" + userLabel).addNew();
        model.addAttribute("managedElement", localCacheService.managedElementMap.get(userLabel));
        model.addAttribute("repo", localCacheService.mocDataRepositoryMap.get(id + "_" + userLabel));
        model.addAttribute("title", "External Alarms Manager");
        return "drycontact";
    }

    @PostMapping ("/dryContact/updateSDR")
    public String dryContactUpdateSDR(@ModelAttribute("repo") DryContactDeviceMocDataWrapper repo,
                                      @ModelAttribute("userLabel") String userLabel,
                                      Model model, HttpSession session, Authentication authentication) {
        String id = session.getId();
        MessageEntity resultD = null;
        MessageEntity resultAM = null;

        List<MocData> deleteDataToSend = repo.getDelFinalData();
        List<MocData> modifyAndAddDataToSend = repo.getModAndAddFinalData();

        if (!deleteDataToSend.isEmpty()) {
            resultD = areaActivating(deleteDataToSend, userLabel);
        }

        if (!modifyAndAddDataToSend.isEmpty()) {
            resultAM = areaActivating(modifyAndAddDataToSend, userLabel);
        }

        if (!modifyAndAddDataToSend.isEmpty() || !deleteDataToSend.isEmpty()) {
            getLog(userLabel, authentication, deleteDataToSend, modifyAndAddDataToSend, resultD, resultAM);
        }

        localCacheService.messageMap.put(id, computeResultMessage(resultD, resultAM));

        return "redirect:/helper/dryContact/" + userLabel;
    }

    @PostMapping ("/dryContact/updateITBBU")
    public String dryContactUpdateITBBU(@ModelAttribute("repo") DryContactCableMocDataWrapper repo,
                                        @ModelAttribute("userLabel") String userLabel,
                                        Model model, HttpSession session, Authentication authentication) {
        String id = session.getId();
        MessageEntity resultD = null;
        MessageEntity resultAM = null;

        List<MocData> deleteDataToSend = repo.getDelFinalData();
        List<MocData> modifyAndAddDataToSend = repo.getModAndAddFinalData();

        if (!deleteDataToSend.isEmpty()) {
            resultD = areaActivating(deleteDataToSend, userLabel);
        }

        if (!modifyAndAddDataToSend.isEmpty()) {
            resultAM = areaActivating(modifyAndAddDataToSend, userLabel);
        }

        if (!modifyAndAddDataToSend.isEmpty() || !deleteDataToSend.isEmpty()) {
            getLog(userLabel, authentication, deleteDataToSend, modifyAndAddDataToSend, resultD, resultAM);
        }

        localCacheService.messageMap.put(id, computeResultMessage(resultD, resultAM));

        return "redirect:/helper/dryContact/" + userLabel;
    }

    private MessageEntity areaActivating(List<MocData> toActivate, String userLabel) {
        String dataAreaId = planMgnService.newArea(tokenService.getToken());
        PlannedServBodySettings bodySettingsToAddAndModify = PlannedServBodySettings.builder()
                .ManagedElementType(localCacheService.managedElementMap.get(userLabel).getManagedElementType().toString())
                .ne(localCacheService.managedElementMap.get(userLabel).getNe())
                .moData(toActivate)
                .build();
        MessageEntity result = planServService.dataConfigUnassociated(dataAreaId, tokenService.getToken(), bodySettingsToAddAndModify);
        if (result.getSeverity() == Severity.SUCCESS) {
            result = planServService.activateArea(dataAreaId, tokenService.getToken());
        }
        areaDeleting(dataAreaId);
        return result;
    }

    private void areaDeleting(String dataAreaId) {
        int tryings = 10;
        String successResponse = "{\"code\":0}";
        String actualResponse = "";
        try {
            while (!actualResponse.equals(successResponse) && tryings > 0) {
                if (tryings == 10) Thread.sleep(10000);
                else Thread.sleep(5000);
                tryings--;
                actualResponse = planMgnService.deleteArea(dataAreaId, tokenService.getToken());
            }
        } catch (Exception e) {
            log.error(" >> areaDeleting method failed: {} ", e.toString());
        }
        log.info(" >> areaDeleting result code: {}", actualResponse);
    }

    private void setMessage(String sessionId, Model model) {
        MessageEntity message = localCacheService.messageMap.get(sessionId);
        if (message != null) {
            model.addAttribute("message", localCacheService.messageMap.get(sessionId));
            localCacheService.messageMap.remove(sessionId);
        } else model.addAttribute("message", null);
    }

    private MessageEntity computeResultMessage(MessageEntity resultD, MessageEntity resultAM) {
        if (resultD == null && resultAM == null) return new MessageEntity(Severity.INFO, "No modified data exist");
        if (resultD == null) return resultAM;
        if (resultAM == null) return resultD;
        if (resultD.getSeverity() == Severity.ERROR || resultAM.getSeverity() == Severity.ERROR) {
            return new MessageEntity(Severity.ERROR,
                    "Deleting data: " + resultD.getMessage() + "\n" +
                    "Modify & Adding data: " + resultAM.getMessage());
        }
        if (resultD.getSeverity() == Severity.SUCCESS && resultAM.getSeverity() == Severity.SUCCESS) {
            return new MessageEntity(Severity.SUCCESS,
            "Deleting data: " + resultD.getMessage() + "\n" +
                    "Modify & Adding data: " + resultAM.getMessage());
        }
        return null;
    }

    private void getLog(String userLabel, Authentication authentication,
                       List<MocData> del, List<MocData> mod_add,
                       MessageEntity delRes, MessageEntity mod_addRes) {

        ManagedElement managedElement = localCacheService.managedElementMap.get(userLabel);

        StringBuilder log = new StringBuilder();
        log.append("User: ")
                .append(authentication.getName())
                .append(" (")
                .append(authentication.getDetails().toString())
                .append(") changed external alarms config ")
                .append(managedElement.getUserLabel())
                .append(": ");

        if (managedElement.getManagedElementType().toString().equals("SDR")) {
            if (!del.isEmpty()) {
                for (MocData mocData : del) {
                    DryContactDeviceMocData data = (DryContactDeviceMocData) mocData;
                    String l = String.format("[operation: %s, moId/dryNo: %s/%s, name: %s, status: %s], ",
                            data.getMoOp(), data.getMoId(), data.getDryNo(), data.getUserLabel(), data.getAlmStatus());
                    log.append(l);
                }
                log.append(" ").append(delRes.getMessage()).append(" ");
            }
            if (!mod_add.isEmpty()) {
                for (MocData mocData : mod_add) {
                    DryContactDeviceMocData data = (DryContactDeviceMocData) mocData;
                    String l = String.format("[operation: %s, moId/dryNo: %s/%s, name: %s, status: %s], ",
                            data.getMoOp(), data.getMoId(), data.getDryNo(), data.getUserLabel(), data.getAlmStatus());
                    log.append(l);
                }
                log.append(" ").append(mod_addRes.getMessage()).append(" ");
            }
        }

        if (managedElement.getManagedElementType().toString().equals("ITBBU")) {
            if (!del.isEmpty()) {
                for (MocData mocData : del) {
                    DryContactCableMocData data = (DryContactCableMocData) mocData;
                    String l = String.format("[operation: %s, dryNo: %s, name: %s, status: %s], ",
                            data.getMoOp(), data.getMoId(), data.getUserLabel(), data.getAlarmStatus());
                    log.append(l);
                }
                log.append(" ").append(delRes.getMessage()).append(" ");
            }
            if (!mod_add.isEmpty()) {
                for (MocData mocData : mod_add) {
                    DryContactCableMocData data = (DryContactCableMocData) mocData;
                    String l = String.format("[operation: %s, dryNo: %s, name: %s, status: %s], ",
                            data.getMoOp(), data.getMoId(), data.getUserLabel(), data.getAlarmStatus());
                    log.append(l);
                }
                log.append(" ").append(mod_addRes.getMessage()).append(" ");
            }
        }

        operationLog.warn(log.toString());

    }
}
