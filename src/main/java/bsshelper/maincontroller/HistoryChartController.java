package bsshelper.maincontroller;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.perfmng.entity.HistoryRTWP;
import bsshelper.externalapi.perfmng.entity.HistoryForUMTSCell;
import bsshelper.externalapi.perfmng.entity.HistoryVSWR;
import bsshelper.externalapi.perfmng.service.HistoryQueryService;
import bsshelper.externalapi.perfmng.to.KPISelectedTo;
import bsshelper.externalapi.perfmng.util.KPI;
import bsshelper.globalutil.Severity;
import bsshelper.globalutil.entity.MessageEntity;
import bsshelper.service.LocalCacheService;
import bsshelper.service.TokenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Slf4j
@Controller
@RequestMapping(value = "/helper")
@RequiredArgsConstructor
public class HistoryChartController {
    private final LocalCacheService localCacheService;
    private final TokenService tokenService;
    private final HistoryQueryService historyQueryService;

    @PostMapping("/acceptanceMeasurement/chartVSWR")
    public String cellStatus(String acceptanceMeasurementId, Integer timeVSWR, Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        ManagedElement managedElement = localCacheService.managedElementMap.get(acceptanceMeasurementId);
        List<HistoryVSWR> historyVSWRList = historyQueryService.getHistoryVSWR(tokenService.getToken(), managedElement, getTime(timeVSWR));
        Map<String, List<HistoryVSWR>> allDataMap = HistoryVSWR.getChart(historyVSWRList);
//        HistoryVSWRFinalWrapper historyVSWRRepoWrapper = new HistoryVSWRFinalWrapper(HistoryVSWR.getFinal(historyVSWRList));
//        System.out.println(allDataMap);
        model.addAttribute("chartData", allDataMap);
//        model.addAttribute("repoVSWR", historyVSWRRepoWrapper);
        model.addAttribute("managedElement", managedElement);
        return "chartVSWR";
    }

    @PostMapping("/acceptanceMeasurement/customCharts")
    public String cellStatus(String acceptanceMeasurementId, Integer time,
                             @RequestParam(name = "checkedCustomCellsList", required = false) List<String> checkedCustomCellsList,
                             @RequestParam(name = "checkedKPIsList", required = false) Set<String> checkedKPIsList,
                             Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        ManagedElement managedElement = localCacheService.managedElementMap.get(acceptanceMeasurementId);
        List<UUtranCellFDDMocSimplified> allCellList = localCacheService.UMTSCellMap.get(acceptanceMeasurementId);
        List<UUtranCellFDDMocSimplified> checkedCellList = new ArrayList<>();
        for (UUtranCellFDDMocSimplified cell : allCellList) {
            if (checkedCustomCellsList.contains(cell.getUserLabel())) {
                checkedCellList.add(cell);
            }
        }

        Map<String, List<HistoryForUMTSCell>> checkedRTWPDataMap = new TreeMap<>();
        Map<String, List<HistoryForUMTSCell>> checkedRRCDataMap = new TreeMap<>();
        Map<String, List<HistoryForUMTSCell>> checkedRABDataMap = new TreeMap<>();
        Map<String, List<HistoryForUMTSCell>> checkedHSUPADataMap = new TreeMap<>();
        Map<String, List<HistoryForUMTSCell>> checkedHSDPADataMap = new TreeMap<>();
        Map<String, List<HistoryForUMTSCell>> checkedRLCDataMap = new TreeMap<>();

        if (checkedKPIsList.contains(KPI.RTWP.getInfo())) {
            checkedRTWPDataMap = HistoryRTWP
                    .getChart(historyQueryService.getUMTSCellHistory(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.RTWP));
        }

        if (checkedKPIsList.contains(KPI.RRC.getInfo())) {
            checkedRRCDataMap = HistoryRTWP
                    .getChart(historyQueryService.getUMTSCellHistory(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.RRC));
        }

        if (checkedKPIsList.contains(KPI.RAB.getInfo())) {
            checkedRABDataMap = HistoryRTWP
                    .getChart(historyQueryService.getUMTSCellHistory(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.RAB));
        }

        if (checkedKPIsList.contains(KPI.HSUPA.getInfo())) {
            checkedHSUPADataMap = HistoryRTWP
                    .getChart(historyQueryService.getUMTSCellHistory(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.HSUPA));
        }

        if (checkedKPIsList.contains(KPI.HSDPA.getInfo())) {
            checkedHSDPADataMap = HistoryRTWP
                    .getChart(historyQueryService.getUMTSCellHistory(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.HSDPA));
        }

        if (checkedKPIsList.contains(KPI.RLC.getInfo())) {
            checkedRLCDataMap = HistoryRTWP
                    .getChart(historyQueryService.getUMTSCellHistory(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.RLC));
        }

        model.addAttribute("chartRTWPData", checkedRTWPDataMap);
        model.addAttribute("chartRRCData", checkedRRCDataMap);
        model.addAttribute("chartRABData", checkedRABDataMap);
        model.addAttribute("chartHSUPAData", checkedHSUPADataMap);
        model.addAttribute("chartHSDPAData", checkedHSDPADataMap);
        model.addAttribute("chartRLCData", checkedRLCDataMap);
        model.addAttribute("managedElement", managedElement);
        return "customcharts";
    }

    private void setMessage(String sessionId, Model model) {
        MessageEntity message = localCacheService.messageMap.get(sessionId);
        if (message != null) {
            model.addAttribute("message", localCacheService.messageMap.get(sessionId));
            localCacheService.messageMap.remove(sessionId);
        } else model.addAttribute("message", null);
    }

    private Integer getTime(Integer time) {
        int timeToUse = 168;
        if (time != null) {
            if (time > 0 && time < 169) { timeToUse = time; }
            else {
                if (time < 1) {
                    timeToUse = 1;
                }
            }
        }
        return timeToUse;
    }
}
