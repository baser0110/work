package bsshelper.maincontroller;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.service.CurrentMgnService;
import bsshelper.externalapi.configurationmng.plannedmng.service.PlanMgnService;
import bsshelper.externalapi.configurationmng.plannedserv.entity.DryContactDeviceMocData;
import bsshelper.externalapi.configurationmng.plannedserv.entity.MocData;
import bsshelper.externalapi.configurationmng.plannedserv.entity.PlannedServBodySettings;
import bsshelper.externalapi.configurationmng.plannedserv.mapper.DryContactCableMocDataMapper;
import bsshelper.externalapi.configurationmng.plannedserv.mapper.DryContactDeviceMocDataMapper;
import bsshelper.externalapi.configurationmng.plannedserv.repository.DryContactCableMocDataRepository;
import bsshelper.externalapi.configurationmng.plannedserv.repository.DryContactDeviceMocDataRepository;
import bsshelper.externalapi.configurationmng.plannedserv.repository.MocDataRepository;
import bsshelper.externalapi.configurationmng.plannedserv.service.PlanServService;
import bsshelper.globalutil.ManagedElementType;
import bsshelper.globalutil.Severity;
import bsshelper.globalutil.entity.MessageEntity;
import bsshelper.service.LocalCacheService;
import bsshelper.service.TokenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


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


//    @GetMapping("/helper")
//    public String homepage(Model model) {
//        return "home";
//    }

    @GetMapping("/dryContact")
    public String dryContact(Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        model.addAttribute("managedElement", null);
        model.addAttribute("repo", null);
        return "drycontact";
    }

    @GetMapping("/dryContact/{userLabel}")
    public String search(@PathVariable(value = "userLabel") String userLabel, Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        ManagedElement managedElement = currentMgnService.getManagedElementByNeName(tokenService.getToken(), userLabel);
        MocDataRepository mocDataRepository = null;
        if (managedElement == null) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "userLabel '" + userLabel + "' couldn't be found"));
            return "redirect:/helper/dryContact";
        }
        if (managedElement.getManagedElementType() == ManagedElementType.SDR) {
            mocDataRepository = new DryContactDeviceMocDataRepository(DryContactDeviceMocDataMapper.
                    toDryContactDeviceMocDataTo(currentMgnService.getDryContactDeviceMoc(tokenService.getToken(), managedElement)));
        } else {
            mocDataRepository = new DryContactCableMocDataRepository(DryContactCableMocDataMapper.
                    toDryContactCableMocDataTo(currentMgnService.getDryContactCableMoc(tokenService.getToken(), managedElement)));
        }
        localCacheService.mocDataRepositoryMap.put(id, mocDataRepository);
        localCacheService.managedElementMap.put(id, managedElement);

        model.addAttribute("managedElement", managedElement);
        model.addAttribute("repo", mocDataRepository);
        return "drycontact";
    }

    @PostMapping ("/dryContact/add")
    public String dryContactAdd(Model model, HttpSession session) {
        String id = session.getId();
        localCacheService.mocDataRepositoryMap.get(id).addNew();
        model.addAttribute("managedElement", localCacheService.managedElementMap.get(id));
        model.addAttribute("repo", localCacheService.mocDataRepositoryMap.get(id));
        return "drycontact";
    }

    @PostMapping ("/dryContact/updateSDR")
    public String dryContactUpdateSDR(@ModelAttribute("repo") DryContactDeviceMocDataRepository repo, Model model, HttpSession session) {
        String id = session.getId();
        MessageEntity resultD = null;
        MessageEntity resultAM = null;

        List<MocData> deleteDataToSend = repo.getDelFinalData();
        List<MocData> modifyAndAddDataToSend = repo.getModAndAddFinalData();

        if (!deleteDataToSend.isEmpty()) {
            resultD = areaActivating(id, deleteDataToSend);
        }

        if (!modifyAndAddDataToSend.isEmpty()) {
            resultAM = areaActivating(id, modifyAndAddDataToSend);
        }

        localCacheService.messageMap.put(id, computeResultMessage(resultD, resultAM));

        return "redirect:/helper/dryContact/" + localCacheService.managedElementMap.get(id).getUserLabel();
    }

    @PostMapping ("/dryContact/updateITBBU")
    public String dryContactUpdateITBBU(@ModelAttribute("repo") DryContactCableMocDataRepository repo, Model model, HttpSession session) {
        String id = session.getId();
        MessageEntity resultD = null;
        MessageEntity resultAM = null;

        List<MocData> deleteDataToSend = repo.getDelFinalData();
        List<MocData> modifyAndAddDataToSend = repo.getModAndAddFinalData();

        if (!deleteDataToSend.isEmpty()) {
            resultD = areaActivating(id, deleteDataToSend);
        }

        if (!modifyAndAddDataToSend.isEmpty()) {
            resultAM = areaActivating(id, modifyAndAddDataToSend);
        }

        localCacheService.messageMap.put(id, computeResultMessage(resultD, resultAM));

        return "redirect:/helper/dryContact/" + localCacheService.managedElementMap.get(id).getUserLabel();
    }

    private MessageEntity areaActivating(String sessionId, List<MocData> toActivate) {
        String dataAreaId = planMgnService.newArea(tokenService.getToken());
        PlannedServBodySettings bodySettingsToAddAndModify = PlannedServBodySettings.builder()
                .ManagedElementType(localCacheService.managedElementMap.get(sessionId).getManagedElementType().toString())
                .ne(localCacheService.managedElementMap.get(sessionId).getNe())
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
}
