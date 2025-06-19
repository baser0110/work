package bsshelper.maincontroller;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.service.CurrentMgnService;
import bsshelper.externalapi.perfmng.entity.*;
import bsshelper.externalapi.perfmng.service.HistoryQueryService;
import bsshelper.externalapi.perfmng.util.KPI;
import bsshelper.globalutil.entity.MessageEntity;
import bsshelper.localservice.localcache.LocalCacheService;
import bsshelper.localservice.token.TokenService;
import bsshelper.localservice.paketlossstat.entity.DomainStat;
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
    private final CurrentMgnService currentMgnService;
    private final LocalCacheService localCacheService;
    private final TokenService tokenService;
    private final HistoryQueryService historyQueryService;

    @PostMapping("/acceptanceMeasurement/chartVSWR")
    public String cellStatus(String userLabel, Integer timeVSWR, Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        ManagedElement managedElement = localCacheService.managedElementMap.get(userLabel);

        Map<String, List<HistoryVSWR>> VSWRDataMap = new TreeMap<>();
        VSWRDataMap = HistoryVSWR.getChart(historyQueryService.getHistoryVSWR(tokenService.getToken(), managedElement, getTime(timeVSWR)));

        model.addAttribute("chartVSWRData", VSWRDataMap);
        model.addAttribute("managedElement", managedElement);
        model.addAttribute("title", null);
        return "customcharts";
    }

    @PostMapping("/packetLossStat/chartPacketLossForSite")
    public String packetLossForSite(@RequestParam(name = "siteName", required = false) String siteName,
                                    Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        ManagedElement managedElement = currentMgnService.getManagedElementByNeName(tokenService.getToken(), siteName);

        Map<String, List<HistoryOfficeLink>> LOST_PACKETDataMap = new TreeMap<>();
        LOST_PACKETDataMap = historyQueryService.getOfficeLinkHistory(tokenService.getToken(), managedElement, 168, KPI.LOST_PACKET);

        model.addAttribute("chartLOST_PACKETData", LOST_PACKETDataMap);
        model.addAttribute("managedElement", managedElement);
        model.addAttribute("title", null);
        return "customcharts";
    }

    @PostMapping("/packetLossStat/chartPacketLossForDomain")
    public String packetLossForDomain(@RequestParam(name = "domain", required = false) String domain,
                                      Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);

        DomainStat domainStat = localCacheService.packetLostCache.get(domain);

        Map<String, List<HistoryOfficeLink>> history2g = getMRNCSiteHistoryMap(domainStat.getIdMap2g());
        Map<String, List<HistoryOfficeLink>> history3g = getMRNCSiteHistoryMap(domainStat.getIdMap3g());

        model.addAttribute("data2g", getPoints(history2g));
        model.addAttribute("data3g", getPoints(history3g));
        model.addAttribute("domain", domain);

        return "chartpacketloss";
    }

    @PostMapping("/acceptanceMeasurement/customCharts")
    public String cellStatus(String userLabel, Integer time,
                             @RequestParam(name = "checkedCustomCellsList", required = false) List<String> checkedCustomCellsList,
                             @RequestParam(name = "checkedKPIsList", required = false) Set<String> checkedKPIsList,
                             @RequestParam(name = "checkedKPIsNoCellList", required = false) Set<String> checkedKPIsNoCellList,
                             Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        List<UUtranCellFDDMocSimplified> checkedCellList = new ArrayList<>();
        ManagedElement managedElement = localCacheService.managedElementMap.get(userLabel);
        if (checkedKPIsList != null) {
            List<UUtranCellFDDMocSimplified> allCellList = localCacheService.UMTSCellMap.get(userLabel);
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
                        .getChart(uploadHistoryWithIgnoreRestrictionOnStringCapacityForUMTSCell(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.RTWP));
            }
            if (checkedKPIsList.contains(KPI.RRC_ATTEMPT.getInfo())) {
                checkedRRCAttemptDataMap = HistoryForUMTSCell
                        .getChart(uploadHistoryWithIgnoreRestrictionOnStringCapacityForUMTSCell(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.RRC_ATTEMPT));
            }
            if (checkedKPIsList.contains(KPI.RRC.getInfo())) {
                checkedRRCDataMap = HistoryForUMTSCell
                        .getChart(uploadHistoryWithIgnoreRestrictionOnStringCapacityForUMTSCell(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.RRC));
            }
            if (checkedKPIsList.contains(KPI.RAB.getInfo())) {
                checkedRABDataMap = HistoryForUMTSCell
                        .getChart(uploadHistoryWithIgnoreRestrictionOnStringCapacityForUMTSCell(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.RAB));
            }
            if (checkedKPIsList.contains(KPI.HSUPA.getInfo())) {
                checkedHSUPADataMap = HistoryForUMTSCell
                        .getChart(uploadHistoryWithIgnoreRestrictionOnStringCapacityForUMTSCell(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.HSUPA));
            }
            if (checkedKPIsList.contains(KPI.HSDPA.getInfo())) {
                checkedHSDPADataMap = HistoryForUMTSCell
                        .getChart(uploadHistoryWithIgnoreRestrictionOnStringCapacityForUMTSCell(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.HSDPA));
            }
            if (checkedKPIsList.contains(KPI.RLC.getInfo())) {
                checkedRLCDataMap = HistoryForUMTSCell
                        .getChart(uploadHistoryWithIgnoreRestrictionOnStringCapacityForUMTSCell(tokenService.getToken(), managedElement, checkedCellList, getTime(time), KPI.RLC));
            }
            if (checkedKPIsList.contains(KPI.ANT_RSSI_1.getInfo())) {
                allANT_RSSI_1DataMap = HistoryForULocalCell
                        .getChart(historyQueryService.getHistoryCellWithIgnoreRestrictionOnStringCapacity(tokenService.getToken(), managedElement, getTime(time), KPI.ANT_RSSI_1));
                checkedANT_RSSI_1DataMap = allToChecked(allANT_RSSI_1DataMap, checkedCellList);
            }
            if (checkedKPIsList.contains(KPI.ANT_RSSI_2.getInfo())) {
                allANT_RSSI_2DataMap = HistoryForULocalCell
                        .getChart(historyQueryService.getHistoryCellWithIgnoreRestrictionOnStringCapacity(tokenService.getToken(), managedElement, getTime(time), KPI.ANT_RSSI_2));
                checkedANT_RSSI_2DataMap = allToChecked(allANT_RSSI_2DataMap, checkedCellList);
            }
            if (checkedKPIsList.contains(KPI.ANT_RSSI_1AND2.getInfo())) {
                if (allANT_RSSI_1DataMap.isEmpty()) {
                    allANT_RSSI_1DataMap = HistoryForULocalCell
                            .getChart(historyQueryService.getHistoryCellWithIgnoreRestrictionOnStringCapacity(tokenService.getToken(), managedElement, getTime(time), KPI.ANT_RSSI_1));
                }
                if (allANT_RSSI_2DataMap.isEmpty()) {
                    allANT_RSSI_2DataMap = HistoryForULocalCell
                            .getChart(historyQueryService.getHistoryCellWithIgnoreRestrictionOnStringCapacity(tokenService.getToken(), managedElement, getTime(time), KPI.ANT_RSSI_2));
                }
                checkedANT_RSSI_1AND2DataMap = allToCheckedForAntenna1and2(allANT_RSSI_1DataMap, allANT_RSSI_2DataMap, checkedCellList);
            }
            if (checkedKPIsList.contains(KPI.CELL_DIVERSITY.getInfo())) {
                allCELL_DIVERSITYDataMap = HistoryForULocalCell
                        .getChart(historyQueryService.getHistoryCellWithIgnoreRestrictionOnStringCapacity(tokenService.getToken(), managedElement, getTime(time), KPI.CELL_DIVERSITY));
                checkedCELL_DIVERSITYDataMap = allToChecked(allCELL_DIVERSITYDataMap, checkedCellList);
            }
            if (checkedKPIsList.contains(KPI.NUMBER_USER_IN_CELL.getInfo())) {
                allNUMBER_USER_IN_CELLDataMap = HistoryForULocalCell
                        .getChart(historyQueryService.getHistoryCellWithIgnoreRestrictionOnStringCapacity(tokenService.getToken(), managedElement, getTime(time), KPI.NUMBER_USER_IN_CELL));
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
            if (time > 0 && time < 169) {
                timeToUse = time;
            } else {
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

    private Map<String, List<HistoryOfficeLink>> getMRNCSiteHistoryMap(Map<String, List<String>> mrncIdMap) {
        return historyQueryService.getCustomListOfficeLinkHistory(
                tokenService.getToken(),
                mrncIdMap,
                168,
                KPI.LOST_PACKET,
                60
        );
    }

    private List<Map<String, Object>> getPoints(Map<String, List<HistoryOfficeLink>> history) {
        List<Map<String, Object>> chartData = new ArrayList<>();

        for (Map.Entry<String, List<HistoryOfficeLink>> entry : history.entrySet()) {
            String site = entry.getKey();
            for (HistoryOfficeLink link : entry.getValue()) {
                Map<String, Object> point = new HashMap<>();
                point.put("category", site);                      // site = series name
                point.put("x", link.getTime().toString());        // time as string
                point.put("value", link.getValue());              // value for the bar
                chartData.add(point);
            }
        }

        return chartData;
    }

    private List<? extends HistoryForUMTSCell> uploadHistoryWithIgnoreRestrictionOnStringCapacityForUMTSCell(
            Token token, ManagedElement managedElement, List<UUtranCellFDDMocSimplified> checkedCellList, Integer time, KPI kpi) {
        int step = 25;
        int startIndex = 0;
        int residualSize = checkedCellList.size();
        List<HistoryForUMTSCell> result = new ArrayList<>();
        if (residualSize <= step) {
            return historyQueryService.getUMTSCellHistory(token, managedElement, checkedCellList, getTime(time), kpi);
        }
        while (residualSize != 0) {
            List<UUtranCellFDDMocSimplified> stepList = checkedCellList.subList(startIndex, residualSize > step ? startIndex + step : startIndex + residualSize);
            result.addAll(historyQueryService.getUMTSCellHistory(token, managedElement, stepList, getTime(time), kpi));
            startIndex = startIndex + step;
            residualSize = residualSize > step ? residualSize - step : 0;
        }
        return result;
    }
}
