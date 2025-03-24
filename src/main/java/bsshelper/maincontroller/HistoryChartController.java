package bsshelper.maincontroller;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.perfmng.entity.*;
import bsshelper.externalapi.perfmng.service.HistoryQueryService;
import bsshelper.externalapi.perfmng.util.KPI;
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
        model.addAttribute("title", null);
        return "chartVSWR";
    }

    @PostMapping("/acceptanceMeasurement/customCharts")
    public String cellStatus(String acceptanceMeasurementId, Integer time,
                             @RequestParam(name = "checkedCustomCellsList", required = false) List<String> checkedCustomCellsList,
                             @RequestParam(name = "checkedKPIsList", required = false) Set<String> checkedKPIsList,
                             @RequestParam(name = "checkedKPIsNoCellList", required = false) Set<String> checkedKPIsNoCellList,
                             Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        List<UUtranCellFDDMocSimplified> checkedCellList = new ArrayList<>();
        ManagedElement managedElement = localCacheService.managedElementMap.get(acceptanceMeasurementId);
        if (checkedKPIsList != null) {
            List<UUtranCellFDDMocSimplified> allCellList = localCacheService.UMTSCellMap.get(acceptanceMeasurementId);
            for (UUtranCellFDDMocSimplified cell : allCellList) {
                if (checkedCustomCellsList.contains(cell.getUserLabel())) {
                    checkedCellList.add(cell);
                }
            }
        }

        Map<String, List<HistoryForULocalCell>> allANT_RSSI_1DataMap = new TreeMap<>();
        Map<String, List<HistoryForULocalCell>> allANT_RSSI_2DataMap = new TreeMap<>();
        Map<String, List<HistoryForULocalCell>> allCELL_DIVERSITYDataMap = new TreeMap<>();
        Map<String, List<HistoryForULocalCell>> allNUMBER_USER_IN_CELLDataMap = new TreeMap<>();


        Map<String, List<HistoryForUMTSCell>> checkedRTWPDataMap = new TreeMap<>();
        Map<String, List<HistoryForUMTSCell>> checkedRRCAttemptDataMap = new TreeMap<>();
        Map<String, List<HistoryForUMTSCell>> checkedRRCDataMap = new TreeMap<>();
        Map<String, List<HistoryForUMTSCell>> checkedRABDataMap = new TreeMap<>();
        Map<String, List<HistoryForUMTSCell>> checkedHSUPADataMap = new TreeMap<>();
        Map<String, List<HistoryForUMTSCell>> checkedHSDPADataMap = new TreeMap<>();
        Map<String, List<HistoryForUMTSCell>> checkedRLCDataMap = new TreeMap<>();
        Map<String, List<HistoryForULocalCell>> checkedANT_RSSI_1DataMap = new TreeMap<>();
        Map<String, List<HistoryForULocalCell>> checkedANT_RSSI_2DataMap = new TreeMap<>();
        Map<String, List<HistoryForULocalCell>> checkedCELL_DIVERSITYDataMap = new TreeMap<>();
        Map<String, List<HistoryForULocalCell>> checkedNUMBER_USER_IN_CELLDataMap = new TreeMap<>();
        Map<String, List<HistoryForULocalCell>> checkedANT_RSSI_1AND2DataMap = new TreeMap<>();
        Map<String, List<HistoryVSWR>> VSWRDataMap = new TreeMap<>();
        Map<String, List<HistoryMaxOpticError>> MAX_OPTIC_ERRORDataMap = new TreeMap<>();
        Map<String, List<HistoryMaxOpticPower>> MAX_OPTIC_RX_POWERDataMap = new TreeMap<>();
        Map<String, List<HistoryMaxOpticPower>> MAX_OPTIC_TX_POWERDataMap = new TreeMap<>();
        Map<String, List<HistoryOfficeLink>> LOST_PACKETDataMap = new TreeMap<>();
        Map<String, List<HistoryOfficeLink>> MEAN_JITTERDataMap = new TreeMap<>();

        if (checkedKPIsList != null) {
            if (checkedKPIsList.contains(KPI.RTWP.getInfo())) {
                checkedRTWPDataMap = HistoryForUMTSCell
                        .getChart(historyQueryService.getUMTSCellHistory(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.RTWP));
            }
            if (checkedKPIsList.contains(KPI.RRC_ATTEMPT.getInfo())) {
                checkedRRCAttemptDataMap = HistoryForUMTSCell
                        .getChart(historyQueryService.getUMTSCellHistory(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.RRC_ATTEMPT));
            }
            if (checkedKPIsList.contains(KPI.RRC.getInfo())) {
                checkedRRCDataMap = HistoryForUMTSCell
                        .getChart(historyQueryService.getUMTSCellHistory(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.RRC));
            }
            if (checkedKPIsList.contains(KPI.RAB.getInfo())) {
                checkedRABDataMap = HistoryForUMTSCell
                        .getChart(historyQueryService.getUMTSCellHistory(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.RAB));
            }
            if (checkedKPIsList.contains(KPI.HSUPA.getInfo())) {
                checkedHSUPADataMap = HistoryForUMTSCell
                        .getChart(historyQueryService.getUMTSCellHistory(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.HSUPA));
            }
            if (checkedKPIsList.contains(KPI.HSDPA.getInfo())) {
                checkedHSDPADataMap = HistoryForUMTSCell
                        .getChart(historyQueryService.getUMTSCellHistory(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.HSDPA));
            }
            if (checkedKPIsList.contains(KPI.RLC.getInfo())) {
                checkedRLCDataMap = HistoryForUMTSCell
                        .getChart(historyQueryService.getUMTSCellHistory(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.RLC));
            }
            if (checkedKPIsList.contains(KPI.ANT_RSSI_1.getInfo())) {
                allANT_RSSI_1DataMap = HistoryForULocalCell
                        .getChart(historyQueryService.getHistoryCell(tokenService.getToken(), managedElement, getTime(time), KPI.ANT_RSSI_1));
                checkedANT_RSSI_1DataMap = allToChecked(allANT_RSSI_1DataMap, checkedCellList);
            }
            if (checkedKPIsList.contains(KPI.ANT_RSSI_2.getInfo())) {
                allANT_RSSI_2DataMap = HistoryForULocalCell
                        .getChart(historyQueryService.getHistoryCell(tokenService.getToken(), managedElement, getTime(time), KPI.ANT_RSSI_2));
                checkedANT_RSSI_2DataMap = allToChecked(allANT_RSSI_2DataMap, checkedCellList);
            }
            if (checkedKPIsList.contains(KPI.ANT_RSSI_1AND2.getInfo())) {
                if (allANT_RSSI_1DataMap.isEmpty()) {
                    allANT_RSSI_1DataMap = HistoryForULocalCell
                            .getChart(historyQueryService.getHistoryCell(tokenService.getToken(), managedElement, getTime(time), KPI.ANT_RSSI_1));
                }
                if (allANT_RSSI_2DataMap.isEmpty()) {
                    allANT_RSSI_2DataMap = HistoryForULocalCell
                            .getChart(historyQueryService.getHistoryCell(tokenService.getToken(), managedElement, getTime(time), KPI.ANT_RSSI_2));
                }
                checkedANT_RSSI_1AND2DataMap = allToCheckedForAntenna1and2(allANT_RSSI_1DataMap, allANT_RSSI_2DataMap, checkedCellList);
            }
            if (checkedKPIsList.contains(KPI.CELL_DIVERSITY.getInfo())) {
                allCELL_DIVERSITYDataMap = HistoryForULocalCell
                        .getChart(historyQueryService.getHistoryCell(tokenService.getToken(), managedElement, getTime(time), KPI.CELL_DIVERSITY));
                checkedCELL_DIVERSITYDataMap = allToChecked(allCELL_DIVERSITYDataMap, checkedCellList);
            }
            if (checkedKPIsList.contains(KPI.NUMBER_USER_IN_CELL.getInfo())) {
                allNUMBER_USER_IN_CELLDataMap = HistoryForULocalCell
                        .getChart(historyQueryService.getHistoryCell(tokenService.getToken(), managedElement, getTime(time), KPI.NUMBER_USER_IN_CELL));
                checkedNUMBER_USER_IN_CELLDataMap = allToChecked(allNUMBER_USER_IN_CELLDataMap, checkedCellList);
            }
        }

        if (checkedKPIsNoCellList != null) {
            if (checkedKPIsNoCellList.contains(KPI.VSWR.getInfo())) {
                VSWRDataMap = HistoryVSWR.getChart(historyQueryService.getHistoryVSWR(tokenService.getToken(), managedElement, getTime(time)));
            }
            if (checkedKPIsNoCellList.contains(KPI.MAX_OPTIC_ERROR.getInfo())) {
                MAX_OPTIC_ERRORDataMap = HistoryMaxOpticError.getChart(historyQueryService.getHistoryOpticError(tokenService.getToken(), managedElement, getTime(time)));
            }
            if (checkedKPIsNoCellList.contains(KPI.MAX_OPTIC_TX_POWER.getInfo())) {
                MAX_OPTIC_TX_POWERDataMap = HistoryMaxOpticPower.getChart(historyQueryService.getHistoryOpticTxPower(tokenService.getToken(), managedElement, getTime(time)));
            }
            if (checkedKPIsNoCellList.contains(KPI.MAX_OPTIC_RX_POWER.getInfo())) {
                MAX_OPTIC_RX_POWERDataMap = HistoryMaxOpticPower.getChart(historyQueryService.getHistoryOpticRxPower(tokenService.getToken(), managedElement, getTime(time)));
            }
            if (checkedKPIsNoCellList.contains(KPI.LOST_PACKET.getInfo())) {
                LOST_PACKETDataMap = historyQueryService.getOfficeLinkHistory(tokenService.getToken(), managedElement, getTime(time), KPI.LOST_PACKET);
            }
            if (checkedKPIsNoCellList.contains(KPI.MEAN_JITTER.getInfo())) {
                MEAN_JITTERDataMap = historyQueryService.getOfficeLinkHistory(tokenService.getToken(), managedElement, getTime(time), KPI.MEAN_JITTER);
            }
        }

        model.addAttribute("chartRTWPData", checkedRTWPDataMap);
        model.addAttribute("chartRRCAttemptData", checkedRRCAttemptDataMap);
        model.addAttribute("chartRRCData", checkedRRCDataMap);
        model.addAttribute("chartRABData", checkedRABDataMap);
        model.addAttribute("chartHSUPAData", checkedHSUPADataMap);
        model.addAttribute("chartHSDPAData", checkedHSDPADataMap);
        model.addAttribute("chartRLCData", checkedRLCDataMap);
        model.addAttribute("chartANT_RSSI_1Data", checkedANT_RSSI_1DataMap);
        model.addAttribute("chartANT_RSSI_2Data", checkedANT_RSSI_2DataMap);
        model.addAttribute("chartANT_RSSI_1AND2Data", checkedANT_RSSI_1AND2DataMap);
        model.addAttribute("chartCELL_DIVERSITYData", checkedCELL_DIVERSITYDataMap);
        model.addAttribute("chartNUMBER_USER_IN_CELLData", checkedNUMBER_USER_IN_CELLDataMap);
        model.addAttribute("chartVSWRData", VSWRDataMap);
        model.addAttribute("chartMAX_OPTIC_ERRORData", MAX_OPTIC_ERRORDataMap);
        model.addAttribute("chartMAX_OPTIC_TX_POWERData", MAX_OPTIC_TX_POWERDataMap);
        model.addAttribute("chartMAX_OPTIC_RX_POWERData", MAX_OPTIC_RX_POWERDataMap);
        model.addAttribute("chartLOST_PACKETData", LOST_PACKETDataMap);
        model.addAttribute("chartMEAN_JITTERData", MEAN_JITTERDataMap);
        model.addAttribute("managedElement", managedElement);
        model.addAttribute("title", null);
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

    private Map<String, List<HistoryForULocalCell>> allToChecked(Map<String, List<HistoryForULocalCell>> all,
                                                                 List<UUtranCellFDDMocSimplified> checkedCellList) {
        Map<String, List<HistoryForULocalCell>> result = new TreeMap<>();
        for (UUtranCellFDDMocSimplified cell : checkedCellList) {
            if (all.containsKey(cell.getUserLabel())) {
                result.put(cell.getUserLabel(), all.get(cell.getUserLabel()));
            }
        }
        return result;
    }

    private Map<String, List<HistoryForULocalCell>> allToCheckedForAntenna1and2(Map<String, List<HistoryForULocalCell>> all1,
                                                                                Map<String, List<HistoryForULocalCell>> all2,
                                                                 List<UUtranCellFDDMocSimplified> checkedCellList) {
        Map<String, List<HistoryForULocalCell>> result = new TreeMap<>();
        for (UUtranCellFDDMocSimplified cell : checkedCellList) {
            if (all1.containsKey(cell.getUserLabel())) {
                result.put(cell.getUserLabel() + ":1", all1.get(cell.getUserLabel()));
            }
        }
        for (UUtranCellFDDMocSimplified cell : checkedCellList) {
            if (all2.containsKey(cell.getUserLabel())) {
                result.put(cell.getUserLabel() + ":2", all2.get(cell.getUserLabel()));
            }
        }
        return result;
    }
}
