package bsshelper.maincontroller;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.*;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GGsmCellMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.*;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.GTrxMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.ULocalCellMoc;
import bsshelper.externalapi.configurationmng.currentmng.service.CurrentMgnService;
import bsshelper.externalapi.configurationmng.currentmng.util.CurrentMngBodySettingsFactory;
import bsshelper.externalapi.configurationmng.currentmng.util.MocITBBU;
import bsshelper.externalapi.configurationmng.currentmng.util.MocMRNC;
import bsshelper.externalapi.configurationmng.currentmng.util.MocSDR;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.OpticInfoFinal;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.VSWRTestFinal;
import bsshelper.externalapi.configurationmng.nemoactserv.mapper.FiberTableITBBUMapper;
import bsshelper.externalapi.configurationmng.nemoactserv.mapper.FiberTableMapper;
import bsshelper.externalapi.configurationmng.nemoactserv.service.ExecNeActService;
import bsshelper.externalapi.configurationmng.nemoactserv.util.Action;
import bsshelper.externalapi.configurationmng.nemoactserv.wrapper.FiberTableWrapper;
import bsshelper.externalapi.configurationmng.nemoactserv.wrapper.VSWRListWrapper;
import bsshelper.externalapi.inventorymng.mapper.InventoryEntityMapper;
import bsshelper.externalapi.inventorymng.service.InventoryMngService;
import bsshelper.externalapi.openscriptexecengine.entity.ULocalCellStatus;
import bsshelper.externalapi.perfmng.entity.InfoCellUMTS;
import bsshelper.externalapi.perfmng.entity.InfoGeneral;
import bsshelper.externalapi.perfmng.mapper.*;
import bsshelper.externalapi.perfmng.to.CellSelectedTo;
import bsshelper.externalapi.perfmng.to.KPISelectedTo;
import bsshelper.externalapi.perfmng.to.QueryTypeTo;
import bsshelper.externalapi.perfmng.util.ExternalKPI;
import bsshelper.externalapi.perfmng.util.QueryType;
import bsshelper.externalapi.perfmng.wrapper.*;
import bsshelper.globalutil.ManagedElementType;
import bsshelper.globalutil.Severity;
import bsshelper.globalutil.entity.MessageEntity;
import bsshelper.globalutil.logger.LoggerUtil;
import bsshelper.localservice.localcache.LocalCacheService;
import bsshelper.localservice.searchcache.SearchCacheService;
import bsshelper.localservice.token.TokenService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private final SearchCacheService searchCacheService;
    private final InventoryMngService inventoryMngService;
    private static final Logger operationLog = LoggerUtil.getOperationLogger();
    private final String pageTitle = "Measurements & Info";

    @PostMapping("/acceptanceMeasurement/reset")
    public String reset(@ModelAttribute("ldn") String ldn,
                        @ModelAttribute("userLabel") String userLabel,
                        @ModelAttribute("position") String position,
                        @ModelAttribute("configuration") String configuration,
                        Model model, HttpSession session, Authentication authentication) {
        String id = session.getId();
        ManagedElement managedElement = null;
        String responce = "";

        if (localCacheService.managedElementMap.containsKey(userLabel.trim().toUpperCase())) {
            managedElement = localCacheService.managedElementMap.get(userLabel.trim().toUpperCase());
        } else {
            managedElement = currentMgnService.getManagedElementByNeName(tokenService.getToken(), userLabel);
        }
        if (!ldn.isEmpty()) {
            if (configuration.contains("VSW") || configuration.contains("CCC") || configuration.contains("CCE1B")) {
                responce = execNeActService.resetBoardOrNeQuery(tokenService.getToken(), managedElement, ldn, Action.RESET_NE);
                getLogForReset(managedElement, responce, "reset NE: ", authentication);
            } else
                if (configuration.contains("PM") || configuration.contains("FCE")) {
                    responce = execNeActService.resetBoardOrNeQuery(tokenService.getToken(), managedElement, ldn, Action.RESET);
                    getLogForReset(managedElement, responce, "reset board " + getNameAndPosition(position, configuration) + " NE: ", authentication);
                } else {
                    responce = execNeActService.resetBoardOrNeQuery(tokenService.getToken(), managedElement, ldn, Action.POWER_OFF_RESET);
                    getLogForReset(managedElement, responce, "power off reset board " + getNameAndPosition(position, configuration) + " NE: ", authentication);
                }
        }

        localCacheService.messageMap.put(id, computeResultMessageForReset(responce, position, configuration));

        setMessage(id, model);

        model.addAttribute("managedElement", managedElement);
        model.addAttribute("isSelected", new ArrayList<>(List.of(false,false,false,false,false,false)));
        model.addAttribute("title", pageTitle);
        model.addAttribute("repoQueryType", new QueryTypeToWrapper(QueryTypeTo.getDefaultQueryTypeSelectedList()));
        model.addAttribute("searchCache", searchCacheService.getList());
        return "measurement";
    }

    @GetMapping("/acceptanceMeasurement")
    public String cellStatus(Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        model.addAttribute("managedElement", null);
        model.addAttribute("isSelected", new ArrayList<>(List.of(false,false,false,false,false,false)));
        model.addAttribute("title", pageTitle);
        model.addAttribute("repoQueryType", new QueryTypeToWrapper(QueryTypeTo.getDefaultQueryTypeSelectedList()));
        model.addAttribute("searchCache", searchCacheService.getList());
        return "measurement";
    }

    @GetMapping("/acceptanceMeasurement/{userLabel}")
    public String search(@PathVariable(value = "userLabel") String userLabel, Model model, HttpSession session,
                         @RequestParam(name = "measurementQuerySet", required = false) Set<String> measurementQuerySet) {

        String id = session.getId();
        setMessage(id, model);

        Set<String> isSelected = new HashSet<>();
        ManagedElement managedElement = null;

        if (measurementQuerySet.isEmpty()) {
            localCacheService.messageMap.put(id, new MessageEntity(
                    Severity.ERROR, "No any data is requested, please checkbox the data you are interested in!"));
            return "redirect:/helper/acceptanceMeasurement";
        }

        if (localCacheService.managedElementMap.containsKey(userLabel.trim().toUpperCase())) {
            managedElement = localCacheService.managedElementMap.get(userLabel.trim().toUpperCase());
        } else {
            managedElement = currentMgnService.getManagedElementByNeName(tokenService.getToken(), userLabel);
        }

        if (managedElement == null) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "userLabel '" + userLabel + "' couldn't be found"));
            return "redirect:/helper/acceptanceMeasurement";
        }

        // GENERAL INFO

        List<SdrDeviceGroupMoc> sdrDeviceGroupMocList = null;
        List<ReplaceableUnitMoc> replaceableUnitMocList = null;

        if (measurementQuerySet.contains(QueryType.GENERAL_INFO.getInfo()) ||
                measurementQuerySet.contains(QueryType.OPTIC_LEVELS.getInfo()) ||
                measurementQuerySet.contains(QueryType.VSWR.getInfo())) {
            if (managedElement.getManagedElementType().equals(ManagedElementType.SDR)) sdrDeviceGroupMocList = setSdrDeviceGroupList(managedElement);
            else if (managedElement.getManagedElementType().equals(ManagedElementType.ITBBU)) replaceableUnitMocList = setReplaceableUnitList(managedElement);
        }

        List<ULocalCellMoc> umtsSDR = null;
        List<bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ULocalCellMoc> umtsITBBU = null;

        if (measurementQuerySet.contains(QueryType.GENERAL_INFO.getInfo()) ||
                measurementQuerySet.contains(QueryType.CELL_INFO.getInfo()) ||
                measurementQuerySet.contains(QueryType.CUSTOM_HISTORY.getInfo())) {
            if (managedElement.getManagedElementType().equals(ManagedElementType.SDR)) umtsSDR = currentMgnService.getMocList(
                    tokenService.getToken(),
                    managedElement.getUserLabel(),
                    MocSDR.U_LOCAL_CELL,
                    CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocSDR.U_LOCAL_CELL.getMocName()));
            else if (managedElement.getManagedElementType().equals(ManagedElementType.ITBBU)) umtsITBBU = currentMgnService.getMocList(
                    tokenService.getToken(),
                    managedElement.getUserLabel(),
                    MocITBBU.U_LOCAL_CELL,
                    CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocITBBU.U_LOCAL_CELL.getMocName()));
        }

        InfoGeneral infoGeneral = null;

        if (measurementQuerySet.contains(QueryType.GENERAL_INFO.getInfo())) {
            if (managedElement.getManagedElementType().equals(ManagedElementType.SDR)) {
                infoGeneral = setInfoGeneralSDR(managedElement, sdrDeviceGroupMocList, umtsSDR);
            } else if (managedElement.getManagedElementType().equals(ManagedElementType.ITBBU)) {
                infoGeneral = setInfoGeneralITBBU(managedElement, replaceableUnitMocList, umtsITBBU);
            }
            isSelected.add(QueryType.GENERAL_INFO.getInfo());
        }

        // CELL INFO
        InfoCodesGSMWrapper infoCodesGSMWrapper = null;
        InfoCodesUMTSWrapper infoCodesUMTSWrapper = null;
        InfoCellUMTSWrapper infoCellUMTSWrapper = null;

        List<UUtranCellFDDMoc> umts = null;

        if (measurementQuerySet.contains(QueryType.CELL_INFO.getInfo()) || measurementQuerySet.contains(QueryType.CUSTOM_HISTORY.getInfo())) {
            umts = setUMTSCellList(managedElement);
        }

        if (measurementQuerySet.contains(QueryType.CELL_INFO.getInfo())) {
            infoCodesGSMWrapper = setInfoCodesGSMForModel(managedElement);
            infoCodesUMTSWrapper = setInfoCodesUMTSForModel(umts);
            infoCellUMTSWrapper = setCellInfoForModel(managedElement, umts, umtsSDR, umtsITBBU);
            isSelected.add(QueryType.CELL_INFO.getInfo());
        }

        // VSWR
        VSWRListWrapper vswrListWrapper = null;

        if (measurementQuerySet.contains(QueryType.VSWR.getInfo())) {
            vswrListWrapper = setVSWRForModel(managedElement, sdrDeviceGroupMocList, replaceableUnitMocList);
            isSelected.add(QueryType.VSWR.getInfo());
        }

        // OPTIC LEVELS
        FiberTableWrapper fiberTableWrapper =
                new FiberTableWrapper(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                        new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        if (measurementQuerySet.contains(QueryType.OPTIC_LEVELS.getInfo())) {
            setFiberTableForModel(managedElement, fiberTableWrapper, sdrDeviceGroupMocList, replaceableUnitMocList);
            isSelected.add(QueryType.OPTIC_LEVELS.getInfo());
        }

        // SYNC
        SyncWrapper syncWrapper = null;

        if (measurementQuerySet.contains(QueryType.SYNC.getInfo())) {
            if (sdrDeviceGroupMocList == null && managedElement.getManagedElementType().equals(ManagedElementType.SDR)) {
                sdrDeviceGroupMocList = setSdrDeviceGroupList(managedElement);
            }
            syncWrapper = new SyncWrapper(execNeActService.getSync(tokenService.getToken(), managedElement, sdrDeviceGroupMocList));
            isSelected.add(QueryType.SYNC.getInfo());
        }

        // CHARTS
        CellSelectedToWrapper cellSelectedToWrapper = null;
        KPISelectedToWrapper kpiSelectedToWrapper = null;

        if (measurementQuerySet.contains(QueryType.CUSTOM_HISTORY.getInfo())) {
            cellSelectedToWrapper = setCellSelectedToForModel(managedElement, umts);
            kpiSelectedToWrapper = setKPISelectedToForModel();
            isSelected.add(QueryType.CUSTOM_HISTORY.getInfo());
        }

        if (!localCacheService.managedElementMap.containsKey(managedElement.getUserLabel())) {
            localCacheService.managedElementMap.put(managedElement.getUserLabel(), managedElement);
        }

        searchCacheService.add(managedElement.getUserLabel());

        model.addAttribute("repoInfoCell", infoCellUMTSWrapper);
        model.addAttribute("repoQueryType", new QueryTypeToWrapper(QueryTypeTo.getDefaultQueryTypeSelectedList()));
        model.addAttribute("repoGeneral", infoGeneral);
        model.addAttribute("repoCell", cellSelectedToWrapper);
        model.addAttribute("repoSync", syncWrapper);
        model.addAttribute("repoKPI", kpiSelectedToWrapper);
        model.addAttribute("repoOptic", fiberTableWrapper.isEmpty() ? null : fiberTableWrapper);
        model.addAttribute("repoGSMCodes", infoCodesGSMWrapper);
        model.addAttribute("repoUMTSCodes", infoCodesUMTSWrapper);
        model.addAttribute("repoVSWR", vswrListWrapper);
        model.addAttribute("managedElement", managedElement);
        model.addAttribute("isSelected", isSelected);
        model.addAttribute("acceptanceMeasurementId", id);
        model.addAttribute("title", pageTitle);
        model.addAttribute("searchCache", searchCacheService.getList());

        model.addAttribute("KPIList", ExternalKPI.getTest());
        return "measurement";
    }

    private InfoGeneral setInfoGeneralSDR(ManagedElement managedElement, List<SdrDeviceGroupMoc> sdrDeviceGroupMocList, List<ULocalCellMoc> umtsSDR) {
        List<EthernetSwitchDeviceMoc> switchPortList = currentMgnService.getMocList(
                tokenService.getToken(),
                managedElement.getUserLabel(),
                MocSDR.ETHERNET_SWITCH_DEVICE,
                CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocSDR.ETHERNET_SWITCH_DEVICE.getMocName()));
        Map<String,String> inventoryMap = InventoryEntityMapper.getInventoryEntityMap(inventoryMngService.getHWInventory(tokenService.getToken(), managedElement));

        InfoGeneral infoGeneral = new InfoGeneral(InfoPlatMapper.toInfoPlatForSDR(sdrDeviceGroupMocList, switchPortList, inventoryMap),
                InfoIpMapper.toInfoIpForSDR(currentMgnService.getMocList(
                        tokenService.getToken(),
                        managedElement.getUserLabel(),
                        MocSDR.IP_LAYER_CONFIG,
                        CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocSDR.IP_LAYER_CONFIG.getMocName()))));

        List<GTrxMoc> gsmSDR = currentMgnService.getMocList(
                tokenService.getToken(),
                managedElement.getUserLabel(),
                MocSDR.G_TRX,
                CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocSDR.G_TRX.getMocName()));
        List<EUtranCellNBIoTMoc> nbiotSDR = currentMgnService.getMocList(
                tokenService.getToken(),
                managedElement.getUserLabel(),
                MocSDR.EUTRAN_CELL_NBIOT,
                CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocSDR.EUTRAN_CELL_NBIOT.getMocName()));
        List<EUtranCellFDDMoc> lteFDDSDR = currentMgnService.getMocList(
                tokenService.getToken(),
                managedElement.getUserLabel(),
                MocSDR.EUTRAN_CELL_FDD_LTE,
                CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocSDR.EUTRAN_CELL_FDD_LTE.getMocName()));

        infoGeneral.populateCapacityList(infoGeneral.getPlatInfoList());
        infoGeneral.populateCellAmountList((gsmSDR == null || gsmSDR.isEmpty()) ? 0 : gsmSDR.size(),
                (umtsSDR == null || umtsSDR.isEmpty()) ? 0 : umtsSDR.size(),
                (nbiotSDR == null || nbiotSDR.isEmpty()) ? 0 : nbiotSDR.size(),
                (lteFDDSDR == null || lteFDDSDR.isEmpty()) ? 0 : lteFDDSDR.size());
        return infoGeneral;
    }

    private InfoGeneral setInfoGeneralITBBU(ManagedElement managedElement, List<ReplaceableUnitMoc> replaceableUnitMocList, List<bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ULocalCellMoc> umtsITBBU) {
        Map<String,String> inventoryMap = InventoryEntityMapper.getInventoryEntityMap(inventoryMngService.getHWInventory(tokenService.getToken(), managedElement));

        InfoGeneral infoGeneral = new InfoGeneral(InfoPlatMapper.toInfoPlatForITBBU(replaceableUnitMocList, inventoryMap),
                InfoIpMapper.toInfoIpForITBBU(currentMgnService.getMocList(
                        tokenService.getToken(),
                        managedElement.getUserLabel(),
                        MocITBBU.IP,
                        CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocITBBU.IP.getMocName()))));

        List<bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.GTrxMoc> gsmITBBU = currentMgnService.getMocList(
                tokenService.getToken(),
                managedElement.getUserLabel(),
                MocITBBU.G_TRX,
                CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocITBBU.G_TRX.getMocName()));
        List<CUEUtranCellNBIoTMoc> nbiotITBBU = currentMgnService.getMocList(
                tokenService.getToken(),
                managedElement.getUserLabel(),
                MocITBBU.CU_EUTRAN_CELL_NBIOT,
                CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocITBBU.CU_EUTRAN_CELL_NBIOT.getMocName()));
        List<CUEUtranCellFDDLTEMoc> lteFDDITBBU = currentMgnService.getMocList(
                tokenService.getToken(),
                managedElement.getUserLabel(),
                MocITBBU.CU_EUTRAN_CELL_FDD_LTE,
                CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocITBBU.CU_EUTRAN_CELL_FDD_LTE.getMocName()));

        infoGeneral.populateCapacityList(infoGeneral.getPlatInfoList());
        infoGeneral.populateCellAmountList((gsmITBBU == null || gsmITBBU.isEmpty()) ? 0 : gsmITBBU.size(),
                (umtsITBBU == null || umtsITBBU.isEmpty()) ? 0 : umtsITBBU.size(),
                (nbiotITBBU == null || nbiotITBBU.isEmpty()) ? 0 : nbiotITBBU.size(),
                (lteFDDITBBU == null || lteFDDITBBU.isEmpty()) ? 0 : lteFDDITBBU.size());
        return infoGeneral;
    }

    private InfoCellUMTSWrapper setCellInfoForModel(ManagedElement managedElement,
                                                    List<UUtranCellFDDMoc> umts,
                                                    List<ULocalCellMoc> umtsSDR, List<bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ULocalCellMoc> umtsITBBU) {

        List<InfoCellUMTS> infoCellUMTS = null;
        if (managedElement.getManagedElementType().equals(ManagedElementType.SDR)) {
            infoCellUMTS = InfoCellUMTSMapper.toEntityForSDR(InfoCellUMTSMapper.cellRNCtoMap(umts),InfoCellUMTSMapper.cellSDRtoMap(umtsSDR));
        } else {
            infoCellUMTS = InfoCellUMTSMapper.toEntityForITBBU(InfoCellUMTSMapper.cellRNCtoMap(umts),InfoCellUMTSMapper.cellITBBUtoMap(umtsITBBU));
        }
        return new InfoCellUMTSWrapper(infoCellUMTS);
    }

    private VSWRListWrapper setVSWRForModel(ManagedElement managedElement,
                                 List<SdrDeviceGroupMoc> sdrDeviceGroupMocList,
                                 List<ReplaceableUnitMoc> replaceableUnitMocList) {

        List<VSWRTestFinal> vswrTestFinalList = null;
        if (managedElement.getManagedElementType().equals(ManagedElementType.SDR)) {
            vswrTestFinalList = execNeActService.vswrTestFinalDataQuery(tokenService.getToken(), managedElement, sdrDeviceGroupMocList);
        } else {
            if (managedElement.getManagedElementType().equals(ManagedElementType.ITBBU)) {
                vswrTestFinalList = execNeActService.vswrTestFinalITBBUDataQuery(tokenService.getToken(), managedElement, replaceableUnitMocList);
            }
        }
        return vswrTestFinalList == null ? null : new VSWRListWrapper(vswrTestFinalList);
    }

    private void setFiberTableForModel(ManagedElement managedElement,
                                        FiberTableWrapper fiberTableWrapper,
                                        List<SdrDeviceGroupMoc> sdrDeviceGroupMocList,
                                        List<ReplaceableUnitMoc> replaceableUnitMocList) {

        if (managedElement.getManagedElementType().equals(ManagedElementType.SDR)) {
            if (sdrDeviceGroupMocList == null) {
                sdrDeviceGroupMocList = currentMgnService.getMocList(
                        tokenService.getToken(),
                        managedElement.getUserLabel(),
                        MocSDR.SDR_DEVICE_GROUP,
                        CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocSDR.SDR_DEVICE_GROUP.getMocName()));
            }
            List<FiberCableMoc> fiberCableMocsList = currentMgnService.getMocList(
                    tokenService.getToken(),
                    managedElement.getUserLabel(),
                    MocSDR.FIBER_CABLE,
                    CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocSDR.FIBER_CABLE.getMocName()));
            List<OpticInfoFinal> opticInfoFinalList = execNeActService.opticInfoFinalDataQuery(tokenService.getToken(), managedElement, sdrDeviceGroupMocList);
            if (fiberCableMocsList != null) {
                Map<String, List<String>> map = FiberTableMapper.getFiberTableMap(fiberCableMocsList, opticInfoFinalList);
                if (map != null && !map.isEmpty()) {
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
                        if (count < 18) {
                            fiberTableWrapper.getDataOpticDev3().add(entry.getValue());
                            count++;
                            continue;
                        }
                        if (count < 24) {
                            fiberTableWrapper.getDataOpticDev4().add(entry.getValue());
                            count++;
                            continue;
                        }
                        if (count < 30) {
                            fiberTableWrapper.getDataOpticDev5().add(entry.getValue());
                            count++;
                            continue;
                        }
                        fiberTableWrapper.getDataOpticDev6().add(entry.getValue());
                        count++;
                    }
                }
            }
            if (opticInfoFinalList != null && !opticInfoFinalList.isEmpty()) {
                Map<String, List<String>> map = FiberTableMapper.getLinkTableMap(opticInfoFinalList);
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    fiberTableWrapper.getDataOpticLink().add(entry.getValue());
                }
            }
            fiberTableWrapper.getMaxSize().add(fiberTableWrapper.getDataOpticDev1().stream().mapToInt(List::size).max().orElse(0));
            fiberTableWrapper.getMaxSize().add(fiberTableWrapper.getDataOpticDev2().stream().mapToInt(List::size).max().orElse(0));
            fiberTableWrapper.getMaxSize().add(fiberTableWrapper.getDataOpticDev3().stream().mapToInt(List::size).max().orElse(0));
            fiberTableWrapper.getMaxSize().add(fiberTableWrapper.getDataOpticDev4().stream().mapToInt(List::size).max().orElse(0));
            fiberTableWrapper.getMaxSize().add(fiberTableWrapper.getDataOpticDev5().stream().mapToInt(List::size).max().orElse(0));
            fiberTableWrapper.getMaxSize().add(fiberTableWrapper.getDataOpticDev6().stream().mapToInt(List::size).max().orElse(0));
            fiberTableWrapper.getMaxSize().add(fiberTableWrapper.getDataOpticLink().stream().mapToInt(List::size).max().orElse(0));
        } else {
            if (replaceableUnitMocList == null) {
                replaceableUnitMocList = currentMgnService.getMocList(
                        tokenService.getToken(),
                        managedElement.getUserLabel(),
                        MocITBBU.REPLACEABLE_UNIT,
                        CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocITBBU.REPLACEABLE_UNIT.getMocName()));
            }
            List<RiCableMoc> riCableMocList = currentMgnService.getMocList(
                    tokenService.getToken(),
                    managedElement.getUserLabel(),
                    MocITBBU.RI_CABLE,
                    CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocITBBU.RI_CABLE.getMocName()));
            List<OpticInfoFinal> opticInfoFinalList = execNeActService.opticInfoFinalITBBUDataQuery(tokenService.getToken(), managedElement, replaceableUnitMocList);
            if (riCableMocList != null) {
                Map<String, List<String>> map = FiberTableITBBUMapper.getFiberTableMap(riCableMocList, opticInfoFinalList);
                if (!map.isEmpty()) {
                    int count = 0;
                    Set<String> boards = new HashSet<>();
                    int boardCount = 0;
                    int portCount = 0;
                    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                        if (entry.getKey().contains("VBPc1") || entry.getKey().contains("VBPc7") || entry.getKey().contains("VBPc0")) {
                            boards.add(entry.getKey().substring(0,entry.getKey().indexOf(':')));
                            if (boardCount < boards.size()) {
                                boardCount = boards.size();
                                portCount = 0;
                            }
                            if (portCount < 6) {
                                switch (boards.size()) {
                                    case 1 ->  fiberTableWrapper.getDataOpticDevItbbu1_1().add(entry.getValue());
                                    case 2 ->  fiberTableWrapper.getDataOpticDevItbbu2_1().add(entry.getValue());
                                    case 3 ->  fiberTableWrapper.getDataOpticDevItbbu3_1().add(entry.getValue());
                                }
                                portCount++;
                                continue;
                            }
                            if (portCount < 12) {
                                switch (boards.size()) {
                                    case 1 ->  fiberTableWrapper.getDataOpticDevItbbu1_2().add(entry.getValue());
                                    case 2 ->  fiberTableWrapper.getDataOpticDevItbbu2_2().add(entry.getValue());
                                    case 3 ->  fiberTableWrapper.getDataOpticDevItbbu3_2().add(entry.getValue());
                                }
                                portCount++;
                            }
                        }
                    }
                }
            }
            if (opticInfoFinalList != null && !opticInfoFinalList.isEmpty()) {
                Map<String, List<String>> map = FiberTableITBBUMapper.getLinkTableMap(opticInfoFinalList);
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    fiberTableWrapper.getDataOpticLinkItbbu().add(entry.getValue());
                }
            }
            fiberTableWrapper.getMaxSizeItbbu().add(fiberTableWrapper.getDataOpticDevItbbu1_1().stream().mapToInt(List::size).max().orElse(0));
            fiberTableWrapper.getMaxSizeItbbu().add(fiberTableWrapper.getDataOpticDevItbbu1_2().stream().mapToInt(List::size).max().orElse(0));
            fiberTableWrapper.getMaxSizeItbbu().add(fiberTableWrapper.getDataOpticDevItbbu2_1().stream().mapToInt(List::size).max().orElse(0));
            fiberTableWrapper.getMaxSizeItbbu().add(fiberTableWrapper.getDataOpticDevItbbu2_2().stream().mapToInt(List::size).max().orElse(0));
            fiberTableWrapper.getMaxSizeItbbu().add(fiberTableWrapper.getDataOpticDevItbbu3_1().stream().mapToInt(List::size).max().orElse(0));
            fiberTableWrapper.getMaxSizeItbbu().add(fiberTableWrapper.getDataOpticDevItbbu3_2().stream().mapToInt(List::size).max().orElse(0));
            fiberTableWrapper.getMaxSizeItbbu().add(fiberTableWrapper.getDataOpticLinkItbbu().stream().mapToInt(List::size).max().orElse(0));
        }
    }

    private List<SdrDeviceGroupMoc> setSdrDeviceGroupList(ManagedElement managedElement) {
        return currentMgnService.getMocList(
                tokenService.getToken(),
                managedElement.getUserLabel(),
                MocSDR.SDR_DEVICE_GROUP,
                CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocSDR.SDR_DEVICE_GROUP.getMocName()));
    }

    private List<ReplaceableUnitMoc> setReplaceableUnitList(ManagedElement managedElement) {
        return currentMgnService.getMocList(
                tokenService.getToken(),
                managedElement.getUserLabel(),
                MocITBBU.REPLACEABLE_UNIT,
                CurrentMngBodySettingsFactory.queryMocByNE(managedElement, MocITBBU.REPLACEABLE_UNIT.getMocName()));
    }

    private List<UUtranCellFDDMoc> setUMTSCellList(ManagedElement managedElement) {
        List<UUtranCellFDDMoc> result = currentMgnService.getMocList(
                tokenService.getToken(),
                managedElement.getUserLabel(),
                MocMRNC.U_UTRAN_CELL_FDD,
                CurrentMngBodySettingsFactory.queryUUtranCellFDDMocByUIubLinkMoc(
                        currentMgnService.getMocList(
                                tokenService.getToken(),
                                managedElement.getUserLabel(),
                                MocMRNC.U_IUB_LINK,
                                CurrentMngBodySettingsFactory.queryUIubLinkMocByOMMBManagedElement(
                                        managedElement))));
        result.sort(Comparator.comparing(UUtranCellFDDMoc::getUserLabel));
        return result;
    }

    private InfoCodesUMTSWrapper setInfoCodesUMTSForModel(List<UUtranCellFDDMoc> umts) {
        return new InfoCodesUMTSWrapper(InfoCodeUMTSMapper.toFinalEntity(umts));
    }

    private InfoCodesGSMWrapper setInfoCodesGSMForModel (ManagedElement managedElement) {
        List<GGsmCellMoc> gsm = currentMgnService.getMocList(
                tokenService.getToken(),
                managedElement.getUserLabel(),
                MocMRNC.G_GSM_CELL,
                CurrentMngBodySettingsFactory.queryGGsmCellMocByOMMBManagedElement(managedElement));
        return new InfoCodesGSMWrapper(InfoCodeGSMMapper.toFinalEntity(gsm));
    }

    private CellSelectedToWrapper setCellSelectedToForModel(ManagedElement managedElement,List<UUtranCellFDDMoc> umts) {
        if (umts != null) {
            localCacheService.UMTSCellMap.put(managedElement.getUserLabel(), umts);
            return new CellSelectedToWrapper(CellSelectedTo.getCellSelectedTo(umts));
        }
        return null;
    }

    private KPISelectedToWrapper setKPISelectedToForModel() {
        return new KPISelectedToWrapper(KPISelectedTo.getDefaultKpiSelectedList(), KPISelectedTo.getDefaultNoCellKpiSelectedList());
    }

    private void setMessage(String sessionId, Model model) {
        MessageEntity message = localCacheService.messageMap.get(sessionId);
        if (message != null) {
            model.addAttribute("message", localCacheService.messageMap.get(sessionId));
            localCacheService.messageMap.remove(sessionId);
        } else model.addAttribute("message", null);
    }

    private MessageEntity computeResultMessageForReset(String response, String position, String configuration) {
        if (response.isEmpty() || response.equals("Unprocessed response.")) {
            return new MessageEntity(Severity.INFO, response + " " + getNameAndPosition(position, configuration));
        }
        if (response.contains("successfully")) {
            return new MessageEntity(Severity.SUCCESS, response + " " + getNameAndPosition(position, configuration));
        } else {
            return new MessageEntity(Severity.ERROR, response + " " + getNameAndPosition(position, configuration));
        }
    }

    private void getLogForReset(ManagedElement managedElement,
                                String response, String resetType, Authentication authentication) {
        StringBuilder log = new StringBuilder();
        log.append("User: ")
                .append(authentication.getName())
                .append(" (")
                .append(authentication.getDetails().toString())
                .append(") ")
                .append(resetType)
                .append(managedElement.getUserLabel())
                .append(". ")
                .append("result: ")
                .append(response);

        operationLog.warn(log.toString());
    }

    private String getNameAndPosition(String position, String configuration) {
        String[] names = configuration.split(" ");
        return position.contains("Slot") ? names[0] + " (" + position + ")" : "RU" + " (" + position + ")";
    }
}
