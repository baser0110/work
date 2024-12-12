package bsshelper.maincontroller;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ReplaceableUnitMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.RiCableMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GGsmCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GTrxMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.FiberCableMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.SdrDeviceGroupMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.ULocalCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.service.CurrentMgnService;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.OpticInfoFinal;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.VSWRTestFinal;
import bsshelper.externalapi.configurationmng.nemoactserv.mapper.FiberTableITBBUMapper;
import bsshelper.externalapi.configurationmng.nemoactserv.mapper.FiberTableMapper;
import bsshelper.externalapi.configurationmng.nemoactserv.service.ExecNeActService;
import bsshelper.externalapi.configurationmng.nemoactserv.wrapper.FiberTableWrapper;
import bsshelper.externalapi.configurationmng.nemoactserv.wrapper.VSWRListWrapper;
import bsshelper.externalapi.perfmng.entity.HistoryForUMTSCell;
import bsshelper.externalapi.perfmng.entity.HistoryRTWP;
import bsshelper.externalapi.perfmng.entity.InfoCellUMTS;
import bsshelper.externalapi.perfmng.mapper.InfoCellUMTSMapper;
import bsshelper.externalapi.perfmng.mapper.InfoCodeGSMMapper;
import bsshelper.externalapi.perfmng.mapper.InfoCodeUMTSMapper;
import bsshelper.externalapi.perfmng.service.HistoryQueryService;
import bsshelper.externalapi.perfmng.to.CellSelectedTo;
import bsshelper.externalapi.perfmng.to.KPISelectedTo;
import bsshelper.externalapi.perfmng.to.QueryTypeTo;
import bsshelper.externalapi.perfmng.util.KPI;
import bsshelper.externalapi.perfmng.util.QueryType;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Slf4j
@Controller
@RequestMapping(value = "/helper")
@RequiredArgsConstructor
public class AcceptMeasureController {
    private final CurrentMgnService currentMgnService;
    private final TokenService tokenService;
    private final LocalCacheService localCacheService;
    private final ExecNeActService execNeActService;

    @GetMapping("/acceptanceMeasurement")
    public String cellStatus(Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        model.addAttribute("managedElement", null);
        model.addAttribute("isSelected",new ArrayList<>(List.of(false,false,false,false)));
        model.addAttribute("title", "Acceptance Measurement");
        model.addAttribute("repoQueryType", new QueryTypeToWrapper(QueryTypeTo.getDefaultQueryTypeSelectedList()));
        return "measurement";
    }

    @GetMapping("/acceptanceMeasurement/{userLabel}")
    public String search(@PathVariable(value = "userLabel") String userLabel, Model model, HttpSession session,
                         @RequestParam(name = "measurementQuerySet", required = false) Set<String> measurementQuerySet) {

        String id = session.getId();
        setMessage(id, model);

        Set<String> isSelected = new HashSet<>();

        if (measurementQuerySet.isEmpty()) {
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
        InfoCellUMTSWrapper infoCellUMTSWrapper = null;
        List<UUtranCellFDDMocSimplified> umts = null;
        List<GGsmCellMocSimplified> gsm = null;
        List<GTrxMocSimplified> trxMRNC = null;

        if (measurementQuerySet.contains(QueryType.CELL_INFO.getInfo())) {
            umts = currentMgnService.getUUtranCellFDDMocSimplified(tokenService.getToken(), managedElement);
            gsm = currentMgnService.getGGsmCellMocSimplified(tokenService.getToken(), managedElement);

            List<ULocalCellMocSimplified> umtsSDR = null;
            List<ITBBUULocalCellMocSimplified> umtsITBBU = null;
            List<InfoCellUMTS> infoCellUMTS = null;
            if (managedElement.getManagedElementType().equals(ManagedElementType.SDR)) {
                umtsSDR = currentMgnService.getULocalCellMocSimplified(tokenService.getToken(), managedElement);
                infoCellUMTS = InfoCellUMTSMapper.toEntityForSDR(InfoCellUMTSMapper.cellRNCtoMap(umts),InfoCellUMTSMapper.cellSDRtoMap(umtsSDR));
            } else {
                umtsITBBU = currentMgnService.getITBBUULocalCellMocSimplified(tokenService.getToken(), managedElement);
                infoCellUMTS = InfoCellUMTSMapper.toEntityForITBBU(InfoCellUMTSMapper.cellRNCtoMap(umts),InfoCellUMTSMapper.cellITBBUtoMap(umtsITBBU));
            }

            infoCellUMTSWrapper = new InfoCellUMTSWrapper(infoCellUMTS);
            infoCodesGSMWrapper = new InfoCodesGSMWrapper(InfoCodeGSMMapper.toFinalEntity(gsm));
            infoCodesUMTSWrapper = new InfoCodesUMTSWrapper(InfoCodeUMTSMapper.toFinalEntity(umts));

            isSelected.add(QueryType.CELL_INFO.getInfo());

            trxMRNC = currentMgnService.getGTrxMocSimplified(tokenService.getToken(), managedElement, gsm);


        }

        List<ReplaceableUnitMoc> replaceableUnitMocList = null;
        List<SdrDeviceGroupMoc> sdrDeviceGroupMocList = null;
        VSWRListWrapper vswrListWrapper = null;

        if (measurementQuerySet.contains(QueryType.VSWR.getInfo())) {
            List<VSWRTestFinal> vswrTestFinalList = null;
            if (managedElement.getManagedElementType().equals(ManagedElementType.SDR)) {
                sdrDeviceGroupMocList = currentMgnService.getSdrDeviceGroupMoc(tokenService.getToken(), managedElement);
                vswrTestFinalList = execNeActService.vswrTestFinalDataQuery(tokenService.getToken(), managedElement, sdrDeviceGroupMocList);
            } else {
                if (managedElement.getManagedElementType().equals(ManagedElementType.ITBBU)) {
                    replaceableUnitMocList = currentMgnService.getReplaceableUnitMoc(tokenService.getToken(), managedElement);
                    vswrTestFinalList = execNeActService.vswrTestFinalITBBUDataQuery(tokenService.getToken(), managedElement, replaceableUnitMocList);
                }
            }
            vswrListWrapper = new VSWRListWrapper(vswrTestFinalList);
            isSelected.add(QueryType.VSWR.getInfo());
        }

        FiberTableWrapper fiberTableWrapper =
                new FiberTableWrapper(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                        new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        if (measurementQuerySet.contains(QueryType.OPTIC_LEVELS.getInfo())) {
            if (managedElement.getManagedElementType().equals(ManagedElementType.SDR)) {
                if (sdrDeviceGroupMocList == null) {
                    sdrDeviceGroupMocList = currentMgnService.getSdrDeviceGroupMoc(tokenService.getToken(), managedElement);
                }
                List<FiberCableMoc> fiberCableMocsList = currentMgnService.getFiberCableMoc(tokenService.getToken(), managedElement);
                List<OpticInfoFinal> opticInfoFinalList = execNeActService.opticInfoFinalDataQuery(tokenService.getToken(), managedElement, sdrDeviceGroupMocList);
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
            } else {
                if (replaceableUnitMocList == null) {
                    replaceableUnitMocList = currentMgnService.getReplaceableUnitMoc(tokenService.getToken(), managedElement);
                }
                List<RiCableMoc> riCableMocList = currentMgnService.getRiCableMoc(tokenService.getToken(), managedElement);
                List<OpticInfoFinal> opticInfoFinalList = execNeActService.opticInfoFinalITBBUDataQuery(tokenService.getToken(), managedElement, replaceableUnitMocList);

                if (riCableMocList != null) {
                    Map<String, List<String>> map = FiberTableITBBUMapper.getFiberTableMap(riCableMocList, opticInfoFinalList);
                    if (!map.isEmpty()) {
                        int count = 0;
                        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                            if (count < 6) {
                                fiberTableWrapper.getDataOpticDevItbbu1_1().add(entry.getValue());
                                count++;
                                continue;
                            }
                            if (count < 10) {
                                fiberTableWrapper.getDataOpticDevItbbu1_2().add(entry.getValue());
                                count++;
                                continue;
                            }
                            if (count < 16) {
                                fiberTableWrapper.getDataOpticDevItbbu2_1().add(entry.getValue());
                                count++;
                                continue;
                            }
                            fiberTableWrapper.getDataOpticDevItbbu2_2().add(entry.getValue());
                            count++;
                        }
                    }
                }
                Map<String, List<String>> map = FiberTableITBBUMapper.getLinkTableMap(opticInfoFinalList);
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    fiberTableWrapper.getDataOpticLinkItbbu().add(entry.getValue());
                }
                fiberTableWrapper.getMaxSizeItbbu().add(fiberTableWrapper.getDataOpticDevItbbu1_1().stream().mapToInt(List::size).max().orElse(0));
                fiberTableWrapper.getMaxSizeItbbu().add(fiberTableWrapper.getDataOpticDevItbbu1_2().stream().mapToInt(List::size).max().orElse(0));
                fiberTableWrapper.getMaxSizeItbbu().add(fiberTableWrapper.getDataOpticDevItbbu2_1().stream().mapToInt(List::size).max().orElse(0));
                fiberTableWrapper.getMaxSizeItbbu().add(fiberTableWrapper.getDataOpticDevItbbu2_2().stream().mapToInt(List::size).max().orElse(0));
                fiberTableWrapper.getMaxSizeItbbu().add(fiberTableWrapper.getDataOpticLinkItbbu().stream().mapToInt(List::size).max().orElse(0));
            }
            isSelected.add(QueryType.OPTIC_LEVELS.getInfo());
        }

        CellSelectedToWrapper cellSelectedToWrapper = null;
        KPISelectedToWrapper kpiSelectedToWrapper = null;

        if (measurementQuerySet.contains(QueryType.CUSTOM_HISTORY.getInfo())) {
            if (umts == null) {
                umts = currentMgnService.getUUtranCellFDDMocSimplified(tokenService.getToken(), managedElement);
            }
            if (umts != null) {
                localCacheService.UMTSCellMap.put(id, umts);
                cellSelectedToWrapper = new CellSelectedToWrapper(CellSelectedTo.getCellSelectedTo(umts));
            }
            kpiSelectedToWrapper = new KPISelectedToWrapper(KPISelectedTo.getDefaultKpiSelectedList());
            isSelected.add(QueryType.CUSTOM_HISTORY.getInfo());
        }

        localCacheService.managedElementMap.put(id, managedElement);

        model.addAttribute("repoInfoCell", infoCellUMTSWrapper);
        model.addAttribute("repoQueryType", new QueryTypeToWrapper(QueryTypeTo.getDefaultQueryTypeSelectedList()));
        model.addAttribute("repoCell", cellSelectedToWrapper);
        model.addAttribute("repoKPI", kpiSelectedToWrapper);
        model.addAttribute("repoOptic", fiberTableWrapper);
        model.addAttribute("repoGSMCodes", infoCodesGSMWrapper);
        model.addAttribute("repoUMTSCodes", infoCodesUMTSWrapper);
        model.addAttribute("repoVSWR", vswrListWrapper);
        model.addAttribute("managedElement", managedElement);
        model.addAttribute("isSelected", isSelected);
        model.addAttribute("acceptanceMeasurementId", id);
        model.addAttribute("title", "Acceptance Measurement");
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
