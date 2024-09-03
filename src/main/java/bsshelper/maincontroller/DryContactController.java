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
    private boolean isErrorInSearching = false;


//    @GetMapping("/helper")
//    public String homepage(Model model) {
//        return "home";
//    }

    @GetMapping("/dryContact")
    public String dryContact(Model model, HttpSession session) {
        if (!isErrorInSearching) model.addAttribute("errorInSearchingMessage", null);
        model.addAttribute("managedElement", null);
        model.addAttribute("repo", null);
        return "drycontact";
    }

    @GetMapping("/dryContact/{userLabel}")
    public String search(@PathVariable(value = "userLabel") String userLabel, Model model, HttpSession session) {
        ManagedElement managedElement = currentMgnService.getManagedElementByNeName(tokenService.getToken(), userLabel);
        MocDataRepository mocDataRepository = null;
        if (managedElement == null) {
            isErrorInSearching = true;
            model.addAttribute("errorInSearchingMessage",  "userLabel '" + userLabel + "' couldn't be found");
            return "redirect:/helper/dryContact";
        }
        if (managedElement.getManagedElementType() == ManagedElementType.SDR) {
            mocDataRepository = new DryContactDeviceMocDataRepository(DryContactDeviceMocDataMapper.
                    toDryContactDeviceMocDataTo(currentMgnService.getDryContactDeviceMoc(tokenService.getToken(), managedElement)));
        } else {
            mocDataRepository = new DryContactCableMocDataRepository(DryContactCableMocDataMapper.
                    toDryContactCableMocDataTo(currentMgnService.getDryContactCableMoc(tokenService.getToken(), managedElement)));
        }
        String id = session.getId();
        localCacheService.mocDataRepositoryMap.put(id, mocDataRepository);
        localCacheService.managedElementMap.put(id, managedElement);

        isErrorInSearching = false;
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

        List<MocData> deleteDataToSend = repo.getDelFinalData();
        List<MocData> modifyAndAddDataToSend = repo.getModAndAddFinalData();

        if (!deleteDataToSend.isEmpty()) {
            areaActivating(id, deleteDataToSend);
        }

        if (!modifyAndAddDataToSend.isEmpty()) {
            areaActivating(id, modifyAndAddDataToSend);
        }

        return "redirect:/helper/dryContact/" + localCacheService.managedElementMap.get(id).getUserLabel();
    }

    @PostMapping ("/dryContact/updateITBBU")
    public String dryContactUpdateITBBU(@ModelAttribute("repo") DryContactCableMocDataRepository repo, Model model, HttpSession session) {
        String id = session.getId();

        List<MocData> deleteDataToSend = repo.getDelFinalData();
        List<MocData> modifyAndAddDataToSend = repo.getModAndAddFinalData();

        if (!deleteDataToSend.isEmpty()) {
            areaActivating(id, deleteDataToSend);
        }

        if (!modifyAndAddDataToSend.isEmpty()) {
            areaActivating(id, modifyAndAddDataToSend);
        }

        return "redirect:/helper/dryContact/" + localCacheService.managedElementMap.get(id).getUserLabel();
    }

    private void areaActivating(String sessionId, List<MocData> toActivate) {
        String dataAreaId = planMgnService.newArea(tokenService.getToken());
        PlannedServBodySettings bodySettingsToAddAndModify = PlannedServBodySettings.builder()
                .ManagedElementType(localCacheService.managedElementMap.get(sessionId).getManagedElementType().toString())
                .ne(localCacheService.managedElementMap.get(sessionId).getNe())
                .moData(toActivate)
                .build();
        planServService.dataConfigUnassociated(dataAreaId, tokenService.getToken(), bodySettingsToAddAndModify);
        planServService.activateArea(dataAreaId, tokenService.getToken());
        areaDeleting(dataAreaId);
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
}
