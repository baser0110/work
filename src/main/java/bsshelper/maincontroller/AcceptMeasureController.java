package bsshelper.maincontroller;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GGsmCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.FiberCableMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.SdrDeviceGroupMoc;
import bsshelper.externalapi.configurationmng.currentmng.service.CurrentMgnService;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.OpticInfoFinal;
import bsshelper.externalapi.configurationmng.nemoactserv.mapper.FiberTableMapper;
import bsshelper.externalapi.configurationmng.nemoactserv.service.ExecNeActSDRService;
import bsshelper.externalapi.configurationmng.nemoactserv.wrapper.FiberTableWrapper;
import bsshelper.externalapi.perfmng.entity.HistoryRTWP;
import bsshelper.externalapi.perfmng.entity.HistoryVSWR;
import bsshelper.externalapi.perfmng.mapper.InfoCodeGSMMapper;
import bsshelper.externalapi.perfmng.mapper.InfoCodeUMTSMapper;
import bsshelper.externalapi.perfmng.perfquery.HistoryQueryService;
import bsshelper.externalapi.perfmng.wrapper.*;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/helper")
@RequiredArgsConstructor
public class AcceptMeasureController {
    private final CurrentMgnService currentMgnService;
    private final TokenService tokenService;
    private final LocalCacheService localCacheService;
    private final HistoryQueryService historyQueryService;
    private final ExecNeActSDRService execNeActSDRService;

    @GetMapping("/acceptanceMeasurement")
    public String cellStatus(Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        model.addAttribute("managedElement", null);
        model.addAttribute("isSelected",new ArrayList<>(List.of(false,false,false,false)));
        return "measurement";
    }

    @GetMapping("/acceptanceMeasurement/{userLabel}")
    public String search(@PathVariable(value = "userLabel") String userLabel, Model model, HttpSession session,
                         String cells, String rtwp, String vswr, String optic) {

        String id = session.getId();
        setMessage(id, model);

        List<Boolean> isSelected = new ArrayList<>();
        isSelected.add(cells != null);
        isSelected.add(rtwp != null);
        isSelected.add(vswr != null);
        isSelected.add(optic != null);

        if (!isSelected.contains(true)) {
            localCacheService.messageMap.put(id, new MessageEntity(
                    Severity.ERROR, "No any data is requested, please checkbox the data you are interested in!"));
            return "redirect:/helper/acceptanceMeasurement";
        }

        ManagedElement managedElement = currentMgnService.getManagedElementByNeName(tokenService.getToken(), userLabel);
        if (managedElement == null) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "userLabel '" + userLabel + "' couldn't be found"));
            return "redirect:/helper/acceptanceMeasurement";
        }

        InfoCodesGSMWrapper infoCodesGSMWrapper = null;
        InfoCodesUMTSWrapper infoCodesUMTSWrapper = null;
        List<UUtranCellFDDMocSimplified> umts = null;
        List<GGsmCellMocSimplified> gsm = null;
        if (isSelected.get(0)) {
            umts = currentMgnService.getUUtranCellFDDMocSimplified(tokenService.getToken(), managedElement);
            gsm = currentMgnService.getGGsmCellMocSimplified(tokenService.getToken(), managedElement);
            infoCodesGSMWrapper = new InfoCodesGSMWrapper(InfoCodeGSMMapper.toFinalEntity(gsm));
            infoCodesUMTSWrapper = new InfoCodesUMTSWrapper(InfoCodeUMTSMapper.toFinalEntity(umts));

//            historyQueryService.getHistoryRTWP(tokenService.getToken(), managedElement, umts);
        }

        HistoryRTWPFinalWrapper historyRTWPRepoWrapper = null;
        HistoryRTWPWrapper historyRTWPWrapper = null;
        if (isSelected.get(1)) {
            if (umts == null) {
                umts = currentMgnService.getUUtranCellFDDMocSimplified(tokenService.getToken(), managedElement);
            }
            List<HistoryRTWP> historyRTWP = historyQueryService.getHistoryRTWP(tokenService.getToken(), managedElement, umts);
            historyRTWPRepoWrapper = new HistoryRTWPFinalWrapper(
                    HistoryRTWP.getFinal(historyRTWP));
            historyRTWPWrapper = new HistoryRTWPWrapper(historyRTWP);

        }

        HistoryVSWRFinalWrapper historyVSWRRepoWrapper = null;
        if (isSelected.get(2)) {
            historyVSWRRepoWrapper = new HistoryVSWRFinalWrapper(
                    HistoryVSWR.getFinal(historyQueryService.getHistoryVSWR(tokenService.getToken(), managedElement)));
        }

        FiberTableWrapper fiberTableWrapper =
                new FiberTableWrapper(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        if (isSelected.get(3)) {
            if (managedElement.getManagedElementType().equals(ManagedElementType.SDR)) {
                List<FiberCableMoc> fiberCableMocsList = currentMgnService.getFiberCableMoc(tokenService.getToken(), managedElement);
                List<SdrDeviceGroupMoc> sdrDeviceGroupMocList = currentMgnService.getSdrDeviceGroupMoc(tokenService.getToken(), managedElement);
                List<OpticInfoFinal> opticInfoFinalList = execNeActSDRService.opticInfoFinalDataQuery(tokenService.getToken(), managedElement, sdrDeviceGroupMocList);
                if (fiberCableMocsList != null) {
                    Map<String, List<String>> map = FiberTableMapper.getFiberTableMap(fiberCableMocsList, opticInfoFinalList);
                    if (!map.isEmpty()) {
                        int count = 0;
                        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                            if (count < 6) {
                                fiberTableWrapper.getDataOpticDev1().add(entry.getValue());
                                count++;
                                continue;
                            }
                            if (count < 12) {
                                fiberTableWrapper.getDataOpticDev2().add(entry.getValue());
                                count++;
                                continue;
                            }
                            fiberTableWrapper.getDataOpticDev3().add(entry.getValue());
                            count++;
                        }
                    }
                }
                Map<String, List<String>> map = FiberTableMapper.getLinkTableMap(opticInfoFinalList);
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    fiberTableWrapper.getDataOpticLink().add(entry.getValue());
                }
                fiberTableWrapper.getMaxSize().add(fiberTableWrapper.getDataOpticDev1().stream().mapToInt(List::size).max().orElse(0));
                fiberTableWrapper.getMaxSize().add(fiberTableWrapper.getDataOpticDev2().stream().mapToInt(List::size).max().orElse(0));
                fiberTableWrapper.getMaxSize().add(fiberTableWrapper.getDataOpticDev3().stream().mapToInt(List::size).max().orElse(0));
                fiberTableWrapper.getMaxSize().add(fiberTableWrapper.getDataOpticLink().stream().mapToInt(List::size).max().orElse(0));
            }
        }


//        System.out.println(fiberTableWrapper);

        localCacheService.managedElementMap.put(id, managedElement);

//        System.out.println(historyRTWPRepoWrapper);

        model.addAttribute("repoOptic", fiberTableWrapper);
        model.addAttribute("repoRTWPChart", historyRTWPWrapper);
        model.addAttribute("repoRTWP", historyRTWPRepoWrapper);
        model.addAttribute("repoGSMCodes", infoCodesGSMWrapper);
        model.addAttribute("repoUMTSCodes", infoCodesUMTSWrapper);
        model.addAttribute("repoVSWR", historyVSWRRepoWrapper);
        model.addAttribute("managedElement", managedElement);
        model.addAttribute("isSelected", isSelected);
        return "measurement";
    }

    private void setMessage(String sessionId, Model model) {
        MessageEntity message = localCacheService.messageMap.get(sessionId);
        if (message != null) {
            model.addAttribute("message", localCacheService.messageMap.get(sessionId));
            localCacheService.messageMap.remove(sessionId);
        } else model.addAttribute("message", null);
    }
}
