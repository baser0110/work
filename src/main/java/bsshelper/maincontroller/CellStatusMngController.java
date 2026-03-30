package bsshelper.maincontroller;

import bsshelper.externalapi.alarmmng.activealarm.entity.AlarmEntity;
import bsshelper.externalapi.alarmmng.activealarm.service.ActiveAlarmService;
import bsshelper.externalapi.alarmmng.activealarm.util.Comments;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.UCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.ULocalCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.service.CurrentMgnService;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.PAStatus;
import bsshelper.externalapi.configurationmng.nemoactserv.mapper.PAStatusMapper;
import bsshelper.externalapi.configurationmng.nemoactserv.service.ExecNeActService;
import bsshelper.externalapi.configurationmng.nemoactserv.util.Action;
import bsshelper.externalapi.configurationmng.nemoactserv.wrapper.PAStatusWrapper;
import bsshelper.externalapi.openscriptexecengine.entity.CellStatus;
import bsshelper.externalapi.openscriptexecengine.entity.CellStatusDetails;
import bsshelper.externalapi.openscriptexecengine.entity.GCellStatus;
import bsshelper.externalapi.openscriptexecengine.mapper.*;
import bsshelper.externalapi.openscriptexecengine.service.ExecuteUCLIBatchScriptService;
import bsshelper.externalapi.openscriptexecengine.util.BatchFileBuilder;
import bsshelper.externalapi.openscriptexecengine.util.ExecuteStatus;
import bsshelper.externalapi.openscriptexecengine.util.StringFileEntity;
import bsshelper.externalapi.openscriptexecengine.wrapper.EUtranCellFDDStatusListWrapper;
import bsshelper.externalapi.openscriptexecengine.wrapper.EUtranCellNBIoTStatusListWrapper;
import bsshelper.externalapi.openscriptexecengine.wrapper.GCellStatusListWrapper;
import bsshelper.externalapi.openscriptexecengine.wrapper.ULocalCellStatusListWrapper;
import bsshelper.globalutil.ManagedElementType;
import bsshelper.globalutil.Severity;
import bsshelper.globalutil.entity.MessageEntity;
import bsshelper.localservice.externalcustomdata.service.CustomDataService;
import bsshelper.localservice.localcache.LocalCacheService;
import bsshelper.localservice.searchcache.SearchCacheService;
import bsshelper.localservice.token.TokenService;
import bsshelper.globalutil.logger.LoggerUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Controller
@RequestMapping(value = "/helper")
@RequiredArgsConstructor
public class CellStatusMngController {
    private final CurrentMgnService currentMgnService;
    private final TokenService tokenService;
    private final LocalCacheService localCacheService;
    private final ExecuteUCLIBatchScriptService executeUCLIBatchScriptService;
    private static final Logger operationLog = LoggerUtil.getOperationLogger();
    private final ActiveAlarmService activeAlarmService;
    private final SearchCacheService searchCacheService;
    private final ExecNeActService execNeActService;
    private final CustomDataService customDataService;

    @GetMapping("/cellStatus")
    public String cellStatus(Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        model.addAttribute("managedElement", null);
        model.addAttribute("title", "Cell Status Manager (Single NE)");
        model.addAttribute("searchCache", searchCacheService.getList());
        return "cellstatus";
    }

    @GetMapping("/cellStatus/{userLabel}")
    public String search(@PathVariable(value = "userLabel") String userLabel, Model model, HttpSession session) {

        String id = session.getId();
        setMessage(id, model);

        ManagedElement managedElement = null;
        if (localCacheService.managedElementMap.containsKey(userLabel.trim().toUpperCase())) {
            managedElement = localCacheService.managedElementMap.get(userLabel.trim().toUpperCase());
        } else {
            managedElement = currentMgnService.getManagedElementByNeName(tokenService.getToken(), userLabel);
        }
        if (managedElement == null) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "userLabel '" + userLabel + "' couldn't be found"));
            return "redirect:/helper/cellStatus";
        }

        localCacheService.managedElementMap.put(managedElement.getUserLabel(), managedElement);
        searchCacheService.add(managedElement.getUserLabel());

//        System.out.println(currentMgnService.rawDataQuery(tokenService.getToken(),managedElement,"EUtranCellNBIoT"));

        ULocalCellStatusListWrapper uLocalCellMocListWrapper = null;
        EUtranCellNBIoTStatusListWrapper eUtranCellNBIoTMocListWrapper = null;
        EUtranCellFDDStatusListWrapper eUtranCellFDDMocListWrapper = null;
        PAStatusWrapper paStatusWrapper = null;
        Map<Integer, UCellMocSimplified> uCellMap = new TreeMap<>();
        List<ULocalCellMocSimplified> uLocalCellList = null;
        List<ITBBUULocalCellMocSimplified> iTBBUULocalCellList = null;
        List<CellStatusDetails> details = null;

//        System.out.println(activeAlarmService.rawDataQuery(tokenService.getToken(),managedElement));

        if (managedElement.getManagedElementType() == ManagedElementType.SDR) {
            uCellMap = UCellMocSimplified.toMap(currentMgnService.getUCellMocSimplified(tokenService.getToken(), managedElement));
            uLocalCellList = currentMgnService.getULocalCellMocSimplified(tokenService.getToken(), managedElement);
            uLocalCellMocListWrapper = new ULocalCellStatusListWrapper(ULocalCellStatusMapper.toULocalCellStatusEntity(uLocalCellList, uCellMap));
            eUtranCellNBIoTMocListWrapper = new EUtranCellNBIoTStatusListWrapper(EUtranCellNBIoTStatusMapper.
                    toEUtranCellNBIoTStatusEntity(currentMgnService.getEUtranCellNBIoTMocSimplified(tokenService.getToken(), managedElement)));
            eUtranCellFDDMocListWrapper = new EUtranCellFDDStatusListWrapper(EUtranCellFDDStatusMapper.
                    toEUtranCellFDDStatusEntity(currentMgnService.getEUtranCellFDDMocSimplified(tokenService.getToken(), managedElement)));
            paStatusWrapper = new PAStatusWrapper(PAStatusMapper.
                    toPAStatusSDR(currentMgnService.getTxChannelMoc(tokenService.getToken(), managedElement)));
        } else {
            uCellMap = UCellMocSimplified.toMap(currentMgnService.getUCellMocSimplified(tokenService.getToken(), managedElement));
            iTBBUULocalCellList = currentMgnService.getITBBUULocalCellMocSimplified(tokenService.getToken(), managedElement);
            uLocalCellMocListWrapper = new ULocalCellStatusListWrapper(ITBBUULocalCellStatusMapper.
                    toULocalCellStatusEntity(iTBBUULocalCellList, uCellMap));
            eUtranCellNBIoTMocListWrapper = new EUtranCellNBIoTStatusListWrapper(ITBBUCUEUtranCellNBIoTStatusMapper.
                    toEUtranCellNBIoTStatusEntity(currentMgnService.getITBBUCUEUtranCellNBIoTMocSimplified(tokenService.getToken(), managedElement)));
            eUtranCellFDDMocListWrapper = new EUtranCellFDDStatusListWrapper(ITBBUCUEUtranCellFDDLTEStatusMapper.
                    toEUtranCellFDDStatusEntity(currentMgnService.getITBBUCUEUtranCellFDDLTEMocSimplified(tokenService.getToken(), managedElement)));
            paStatusWrapper = new PAStatusWrapper(PAStatusMapper.
                    toPAStatusITBBU(currentMgnService.getITBBUTxChannelMoc(tokenService.getToken(), managedElement)));
        }
        GCellStatusListWrapper gCellStatusListWrapper = new GCellStatusListWrapper(
                GCellStatusMapper.toGCellStatusEntity(
                        currentMgnService.getGGsmCellMocSimplified(
                                tokenService.getToken(), managedElement), activeAlarmService.getHasAlarmSetByMEonBSC(
                                        tokenService.getToken(), managedElement)));

//        System.out.println(uLocalCellMocListWrapper);
//        System.out.println(gCellStatusListWrapper);

        if (uLocalCellList != null) {
            details = CellStatusDetailsMapper.toULocalCellStatusEntitySDR(uLocalCellList, uCellMap);
        } else {
            if (iTBBUULocalCellList != null) {
                details = CellStatusDetailsMapper.toULocalCellStatusEntityITBBU(iTBBUULocalCellList, uCellMap);
            }
        }

//        activeAlarmService.alarmDataExport(tokenService.getToken(), managedElement);

        model.addAttribute("managedElement", managedElement);
        model.addAttribute("repoUMTS", uLocalCellMocListWrapper);
        model.addAttribute("repoNBIoT", eUtranCellNBIoTMocListWrapper);
        model.addAttribute("repoLTEFDD", eUtranCellFDDMocListWrapper);
        model.addAttribute("repoGSM", gCellStatusListWrapper);
        model.addAttribute("repoPA", paStatusWrapper);
        model.addAttribute("details", details);
        model.addAttribute("title", "Cell Status Manager (Single NE)");
        model.addAttribute("comments", new HashSet<>(customDataService.CommentsMap.values()));
        model.addAttribute("searchCache", searchCacheService.getList());
        return "cellstatus";
    }

    @PostMapping("/cellStatus/changeStatus")
    public String cellChangeStatus(@ModelAttribute("repoUMTS") ULocalCellStatusListWrapper repoUMTS, Integer operationUMTS,
                                   @ModelAttribute("repoNBIoT") EUtranCellNBIoTStatusListWrapper repoNBIoT, Integer operationNBIoT,
                                   @ModelAttribute("repoLTEFDD") EUtranCellFDDStatusListWrapper repoLTEFDD, Integer operationLTEFDD,
                                   @ModelAttribute("repoGSM") GCellStatusListWrapper repoGSM, Integer operationGSM,
                                   @ModelAttribute("userLabel") String userLabel,
                                   @ModelAttribute("comment") String comment,
                                   Model model, HttpSession session, Authentication authentication) {
        String id = session.getId();
        setMessage(id, model);
        String execResult = null;
        if ((operationUMTS == null || operationUMTS == 0)
                && (operationNBIoT == null || operationNBIoT == 0)
                && (operationGSM == null || operationGSM == 0)
                && (operationLTEFDD == null || operationLTEFDD == 0)) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "No any operation selected!"));
            return "redirect:/helper/cellStatus/" + userLabel;
        }
        if ((repoUMTS.getDataUMTS() == null || !repoUMTS.isAnySelected())
                && (repoNBIoT.getDataNBIOT() == null || !repoNBIoT.isAnySelected())
                && (repoGSM.getDataGSM() == null || !repoGSM.isAnySelected())
                && (repoLTEFDD.getDataLTEFDD() == null || !repoLTEFDD.isAnySelected())) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "No any data selected!"));
            return "redirect:/helper/cellStatus/" + userLabel;
        }
//        if (operationUMTS == 3 || operationUMTS == 6) {}
        else {
            String userName = "(" + authentication.getName() + ")";
            execResult = oneOperationWithResponse(localCacheService.managedElementMap.get(userLabel),
                    repoUMTS.getExtensionData(), operationUMTS,
                    repoNBIoT.getExtensionData(), operationNBIoT,
                    repoLTEFDD.getExtensionData(), operationLTEFDD,
                    repoGSM.getDataGSM(), operationGSM,
                    comment.isBlank() ? userName : comment + " " + userName);
            if (execResult.equals("SUCCEEDED")) {
                localCacheService.messageMap.put(id, new MessageEntity(Severity.SUCCESS, "Script execution result: " + execResult));
            } else {
                localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "Script execution result: " + execResult));
            }

            getLogForCell(localCacheService.managedElementMap.get(userLabel),
                    repoUMTS.getExtensionData(), operationUMTS,
                    repoNBIoT.getExtensionData(), operationNBIoT,
                    repoLTEFDD.getExtensionData(), operationLTEFDD,
                    repoGSM.getDataGSM(), operationGSM,
                    execResult, authentication);
        }

        return "redirect:/helper/cellStatus/" + userLabel;
    }

    @PostMapping("/cellStatus/changeStatusPA")
    public String cellChangeStatus(@ModelAttribute("repoPA") PAStatusWrapper repoPA, Integer operationPA,
                                   @ModelAttribute("userLabel") String userLabel,
                                   @ModelAttribute("comment") String comment,
                                   Model model, HttpSession session, Authentication authentication) {
        String id = session.getId();
        setMessage(id, model);
        StringBuilder execResult = new StringBuilder();
        ManagedElement managedElement = localCacheService.managedElementMap.get(userLabel);
        String userName = "(" + authentication.getName() + ")";
        if (operationPA == null || operationPA == 0) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "No any operation selected!"));
            return "redirect:/helper/cellStatus/" + userLabel;
        }
        if (repoPA.getDataPA() == null || !repoPA.isAnySelected()) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "No any data selected!"));
            return "redirect:/helper/cellStatus/" + userLabel;
        }
        else {
            if (operationPA == 1) {
                boolean isSuccessOnce = false;
                String result = "";
                for (PAStatus pa : repoPA.getDataPA()) {
                    if (pa.isSelected()) {
                        result = execNeActService.paActionQuery(
                                tokenService.getToken(),
                                managedElement,
                                pa.getLdn(),
                                Action.PA_OFF);
                        execResult
                                .append(pa.getUserLabel())
                                .append(" > ")
                                .append(result)
                                .append("\n");
                    }
                    if (!isSuccessOnce && result.contains("successfully")) {
                        isSuccessOnce = true;
                    }
                }
                if (isSuccessOnce) {
                    setCommentForPAAlarmsScheduler(managedElement, repoPA.getDataPA(), comment.isBlank() ? userName : comment + " " + userName);
                }
            }
            if (operationPA == 4) {
                for (PAStatus pa : repoPA.getDataPA()) {
                    if (pa.isSelected()) {
                        execResult
                                .append(pa.getUserLabel())
                                .append(" > ")
                                .append(execNeActService.paActionQuery(
                                        tokenService.getToken(),
                                        managedElement,
                                        pa.getLdn(),
                                        Action.PA_ON))
                                .append("\n");
                    }
                }
            }
        }

        if (!execResult.isEmpty()) {
            execResult.setLength(execResult.length() - 1);
            localCacheService.messageMap.put(id, new MessageEntity(Severity.INFO, execResult.toString()));
            getLogForPA(
                    managedElement,
                    execResult.toString(),
                    authentication,
                    operationPA == 1 ? Action.PA_OFF : Action.PA_ON);
        }

        return "redirect:/helper/cellStatus/" + userLabel;
    }

    @PostMapping("/cellStatus/reset")
    public String reset(@ModelAttribute("userLabel") String userLabel,
                        Model model, HttpSession session, Authentication authentication) {
        String id = session.getId();
        ManagedElement managedElement = null;
        String responce = "";

        if (localCacheService.managedElementMap.containsKey(userLabel.trim().toUpperCase())) {
            managedElement = localCacheService.managedElementMap.get(userLabel.trim().toUpperCase());
        } else {
            managedElement = currentMgnService.getManagedElementByNeName(tokenService.getToken(), userLabel);
        }

        if (managedElement != null) {
            responce = execNeActService.resetBoardOrNeQuery(tokenService.getToken(), managedElement, null, Action.RESET_NE);
            getLogForReset(managedElement, responce, authentication);
        }

        localCacheService.messageMap.put(id, computeResultMessageForReset(responce));

        return "redirect:/helper/cellStatus/" + userLabel;
    }

    private String operate(ManagedElement managedElement,
                           List<CellStatus> UMTSData, Integer UMTSCellOperation,
                           List<CellStatus> NBIoTData, Integer NBIoTCellOperation,
                           List<CellStatus> LTEFDDData, Integer LTEFDDCellOperation,
                           List<GCellStatus> GSMData, Integer GSMCellOperation,
                           String comment) {
        StringFileEntity file = BatchFileBuilder.buildAllData(managedElement,
                UMTSData, UMTSCellOperation,
                NBIoTData, NBIoTCellOperation,
                LTEFDDData, LTEFDDCellOperation,
                GSMData, GSMCellOperation);
        if (!comment.isBlank())
            setCommentForCellAlarmsScheduler(managedElement,
                UMTSData,UMTSCellOperation,
                NBIoTData,NBIoTCellOperation,
                LTEFDDData,LTEFDDCellOperation,
                GSMData,GSMCellOperation,
                comment);

//        System.out.println(file.getFile());

        String path = executeUCLIBatchScriptService.uploadParamFile(file, tokenService.getToken());
//        System.out.println(path);
        return executeUCLIBatchScriptService.executeBatch(path,tokenService.getToken());
    }

    private String oneOperationWithResponse(ManagedElement managedElement,
                                            List<CellStatus> UMTSData, Integer UMTSCellOperation,
                                            List<CellStatus> NBIoTData, Integer NBIoTCellOperation,
                                            List<CellStatus> LTEFDDData, Integer LTEFDDCellOperation,
                                            List<GCellStatus> GSMData, Integer GSMCellOperation,
                                            String comment) {
        int tryings = 10;
        int response = -1;
        String execId = operate(managedElement,
                UMTSData, UMTSCellOperation,
                NBIoTData, NBIoTCellOperation,
                LTEFDDData,LTEFDDCellOperation,
                GSMData, GSMCellOperation,
                comment);
        try {
            while ((response == -1 || response == 2) && tryings > 0) {
                Thread.sleep(5000);
                response = executeUCLIBatchScriptService.queryExecStatus(execId, tokenService.getToken());
                tryings--;
            }
        } catch (InterruptedException e) {
            log.error(" >> executeBatch method failed: {} ", e.toString());
        }
        log.info(" >> execution status: {}", response);
        return ExecuteStatus.getStatusByCode(response);
    }

    private void setMessage(String sessionId, Model model) {
        MessageEntity message = localCacheService.messageMap.get(sessionId);
        if (message != null) {
            model.addAttribute("message", localCacheService.messageMap.get(sessionId));
            localCacheService.messageMap.remove(sessionId);
        } else model.addAttribute("message", null);
    }

    private void getLogForCell(ManagedElement managedElement,
                               List<CellStatus> UMTSData, Integer UMTSCellOperation,
                               List<CellStatus> NBIoTData, Integer NBIoTCellOperation,
                               List<CellStatus> LTEFDDData, Integer LTEFDDCellOperation,
                               List<GCellStatus> GSMData, Integer GSMCellOperation,
                               String result, Authentication authentication) {
        StringBuilder umts = new StringBuilder();
        StringBuilder nBIoT = new StringBuilder();
        StringBuilder lteFDD = new StringBuilder();
        StringBuilder gsm = new StringBuilder();
        if (UMTSData != null && !UMTSData.isEmpty()) {
            for (CellStatus cellStatus : UMTSData) {
                if (cellStatus.isSelected()) {
                    umts.append(cellStatus.getUserLabel()).append(", ");
                }
            }
            if (umts.toString().endsWith(", ")) {
                umts = new StringBuilder(umts.substring(0, umts.length() - 2));
            }
        }
        if (GSMData != null && !GSMData.isEmpty()) {
            for (GCellStatus gCellStatus : GSMData) {
                if (gCellStatus.isSelected()) {
                    gsm.append(gCellStatus.getUserLabel()).append(", ");
                }
            }
            if (gsm.toString().endsWith(", ")) {
                gsm = new StringBuilder(gsm.substring(0, gsm.length() - 2));
            }
        }
        if (LTEFDDData != null && !LTEFDDData.isEmpty()) {
            for (CellStatus cellStatus : LTEFDDData) {
                if (cellStatus.isSelected()) {
                    lteFDD.append(cellStatus.getUserLabel()).append(", ");
                }
            }
            if (lteFDD.toString().endsWith(", ")) {
                lteFDD = new StringBuilder(lteFDD.substring(0, lteFDD.length() - 2));
            }
        }
        if (NBIoTData != null && !NBIoTData.isEmpty()) {
            for (CellStatus cellStatus : NBIoTData) {
                if (cellStatus.isSelected()) {
                    nBIoT.append(cellStatus.getUserLabel()).append(", ");
                }
            }
            if (nBIoT.toString().endsWith(", ")) {
                nBIoT = new StringBuilder(nBIoT.substring(0, nBIoT.length() - 2));
            }
        }
        StringBuilder log = new StringBuilder();
        log.append("User: ")
                .append(authentication.getName())
                .append(" (")
                .append(authentication.getDetails().toString())
                .append(") change cell status of ")
                .append(managedElement.getUserLabel())
                .append(": ");
        String l = String.format("UMTS: [%s] %s, GSM: [%s] %s, LTEFDD: [%s] %s, NBIoT: [%s] %s; result: %s",
                umts, getOperation(UMTSCellOperation),
                gsm, getOperation(GSMCellOperation),
                lteFDD, getOperation(LTEFDDCellOperation),
                nBIoT, getOperation(NBIoTCellOperation),
                result);
        log.append(l);

        operationLog.warn(log.toString());
    }

    private String getOperation(Integer operation) {
        if (operation == null) {return "Technology Not Supported";}
        return switch (operation) {
            case 0 -> "No Operation";
            case 1 -> "Block";
            case 2 -> "Smoothly Block";
            case 4 -> "Unblock";
            case 5 -> "Smoothly Unblock";
            default -> "Unknown";
        };
    }

    private void setCommentForCellAlarmsScheduler(ManagedElement managedElement,
                                              List<CellStatus> UMTSData, Integer UMTSCellOperation,
                                              List<CellStatus> NBIoTData, Integer NBIoTCellOperation,
                                              List<CellStatus> LTEFDDData, Integer LTEFDDCellOperation,
                                              List<GCellStatus> GSMData, Integer GSMCellOperation,
                                              String comment) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule( () -> {
            try {
                setCommentForCellAlarms(
                        managedElement,
                        UMTSData, UMTSCellOperation,
                        NBIoTData, NBIoTCellOperation,
                        LTEFDDData, LTEFDDCellOperation,
                        GSMData, GSMCellOperation,
                        comment);
            } catch (Exception e) {
                log.error(" >> error in alarmCommentProcess: {}", e.toString());
            }
        }, 60, TimeUnit.SECONDS);
    }

    private void setCommentForCellAlarms(ManagedElement managedElement,
                                         List<CellStatus> UMTSData, Integer UMTSCellOperation,
                                         List<CellStatus> NBIoTData, Integer NBIoTCellOperation,
                                         List<CellStatus> LTEFDDData, Integer LTEFDDCellOperation,
                                         List<GCellStatus> GSMData, Integer GSMCellOperation,
                                         String comment) {
        Set<String> expectedNBIoTCells = new HashSet<>();
        Set<String> expectedLTEFDDCells = new HashSet<>();
        Set<String> expectedGSMCells = new HashSet<>();
        Set<String> expectedUMTSCells = new HashSet<>();
        List<String> ids = new ArrayList<>();

        if (NBIoTCellOperation != null && NBIoTCellOperation == 1) {
            for (CellStatus cell : NBIoTData) {
                if (cell.isSelected()) expectedNBIoTCells.add(cell.getUserLabel());
            }
            if (!expectedNBIoTCells.isEmpty()) {
                List<AlarmEntity> site = activeAlarmService.alarmDataExport(tokenService.getToken(),
                        activeAlarmService.getActiveAlarmBySDRSite(tokenService.getToken(), managedElement), managedElement);
                if (site != null && !site.isEmpty()) {
                    for (AlarmEntity alm : site) {
                        if (expectedNBIoTCells.contains(alm.getRan_fm_alarm_object_name().getDisplayname()))
                            ids.add(alm.getId());
                    }
                }
            }
        }
        if (LTEFDDCellOperation != null && LTEFDDCellOperation == 1) {
            for (CellStatus cell : LTEFDDData) {
                if (cell.isSelected()) expectedLTEFDDCells.add(cell.getUserLabel());
            }
            if (!expectedLTEFDDCells.isEmpty()) {
                List<AlarmEntity> site = activeAlarmService.alarmDataExport(tokenService.getToken(),
                        activeAlarmService.getActiveAlarmBySDRSite(tokenService.getToken(), managedElement), managedElement);
                if (site != null && !site.isEmpty()) {
                    for (AlarmEntity alm : site) {
                        if (expectedLTEFDDCells.contains(alm.getRan_fm_alarm_object_name().getDisplayname()))
                            ids.add(alm.getId());
                    }
                }
            }
        }
        if (GSMCellOperation != null && GSMCellOperation == 1) {
            for (GCellStatus cell : GSMData) {
                if (cell.isSelected()) expectedGSMCells.add(cell.getUserLabel());
            }
            if (!expectedGSMCells.isEmpty()) {
                List<AlarmEntity> bsc = activeAlarmService.alarmDataExport(tokenService.getToken(),
                        activeAlarmService.getActiveAlarmByBSC(tokenService.getToken(), managedElement), managedElement);
                if (bsc != null && !bsc.isEmpty()) {
                    for (AlarmEntity alm : bsc) {
                        if (expectedGSMCells.contains(alm.getRan_fm_alarm_object_name().getDisplayname())
                                && alm.getCodename().equals("Cell interruption alarm"))
                            ids.add(alm.getId());
                    }
                }
            }
        }
        if (UMTSCellOperation != null && (UMTSCellOperation == 1 || UMTSCellOperation == 2)) {
            for (CellStatus cell : UMTSData) {
                if (cell.isSelected()) expectedUMTSCells.add(cell.getUserLabel());
            }
            if (!expectedUMTSCells.isEmpty()) {
                List<AlarmEntity> rnc = activeAlarmService.alarmDataExport(tokenService.getToken(),
                        activeAlarmService.getActiveAlarmByRNC(tokenService.getToken(), managedElement), managedElement);
                if (rnc != null && !rnc.isEmpty()) {
                    for (AlarmEntity alm : rnc) {
                        if (expectedUMTSCells.contains(alm.getRan_fm_alarm_object_name().getDisplayname())
                                && alm.getCodename().equals("Cell is out of service"))
                            ids.add(alm.getId());
                    }
                }
            }
        }

        if (!ids.isEmpty()) {
            activeAlarmService.setAlarmComment(tokenService.getToken(),
                    activeAlarmService.setAlarmCommentRequest(tokenService.getToken(), ids, comment), managedElement);
        }
    }

    private void setCommentForPAAlarmsScheduler(ManagedElement managedElement,
                                                List<PAStatus> PAData,
                                                String comment) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule( () -> {
            try {
                setCommentForPAAlarms(
                        managedElement,
                        PAData,
                        comment);
            } catch (Exception e) {
                log.error(" >> error in alarmCommentProcess: {}", e.toString());
            }
        }, 60, TimeUnit.SECONDS);
    }

    private void setCommentForPAAlarms(ManagedElement managedElement,
                                         List<PAStatus> PAData,
                                         String comment) {
        Set<String> expectedPA = new HashSet<>();
        List<String> ids = new ArrayList<>();

        if (managedElement.getManagedElementType().equals(ManagedElementType.SDR)) {
            for (PAStatus pa : PAData) {
                if (pa.isSelected()) expectedPA.add(pa.getUserLabel());
            }
        } else if (managedElement.getManagedElementType().equals(ManagedElementType.ITBBU)) {
            for (PAStatus pa : PAData) {
                if (pa.isSelected()) expectedPA.add(pa.getLdn());
            }
        }

        if (!expectedPA.isEmpty() && managedElement.getManagedElementType().equals(ManagedElementType.SDR)) {
            String ant = "";
            String alarmUserLabel = "";
            List<AlarmEntity> site = activeAlarmService.alarmDataExport(tokenService.getToken(),
                    activeAlarmService.getActiveAlarmBySDRSite(tokenService.getToken(), managedElement), managedElement);
            if (site != null && !site.isEmpty()) {
                for (AlarmEntity alm : site) {
                    if (alm.getAlarmcode().equals("198098440")) {
                        int start = alm.getAdditionaltext().indexOf("PA-") + 3;
                        int end = alm.getAdditionaltext().indexOf(";", start);
                        ant = alm.getAdditionaltext().substring(start, end);
                        alarmUserLabel = alm.getRan_fm_alarm_object_type().getDisplayname() + alm.getRan_fm_alarm_object_id().getDisplayname() + ":" + ant;
                        if (expectedPA.contains(alarmUserLabel))
                            ids.add(alm.getId());
                    }
                }
            }
        }

        if (!expectedPA.isEmpty() && managedElement.getManagedElementType().equals(ManagedElementType.ITBBU)) {
            List<AlarmEntity> site = activeAlarmService.alarmDataExport(tokenService.getToken(),
                    activeAlarmService.getActiveAlarmBySDRSite(tokenService.getToken(), managedElement), managedElement);
            if (site != null && !site.isEmpty()) {
                for (AlarmEntity alm : site) {
                    if (alm.getAlarmcode().equals("198098440")) {
                        if (expectedPA.contains(alm.getPosition()))
                            ids.add(alm.getId());
                    }
                }
            }
        }

        if (!ids.isEmpty()) {
            activeAlarmService.setAlarmComment(tokenService.getToken(),
                    activeAlarmService.setAlarmCommentRequest(tokenService.getToken(), ids, comment), managedElement);
        }
    }

    private MessageEntity computeResultMessageForReset(String response) {
        if (response.isEmpty() || response.equals("Unprocessed response.")) {
            return new MessageEntity(Severity.INFO, response);
        }
        if (response.contains("successfully")) {
            return new MessageEntity(Severity.SUCCESS, response);
        } else {
            return new MessageEntity(Severity.ERROR, response);
        }
    }

    private void getLogForReset(ManagedElement managedElement,
                        String response, Authentication authentication) {
        StringBuilder log = new StringBuilder();
        log.append("User: ")
                .append(authentication.getName())
                .append(" (")
                .append(authentication.getDetails().toString())
                .append(") reset NE: ")
                .append(managedElement.getUserLabel())
                .append(". ")
                .append("result: ")
                .append(response);

        operationLog.warn(log.toString());
    }

    private void getLogForPA(ManagedElement managedElement,
                                String response, Authentication authentication, Action action) {
        StringBuilder log = new StringBuilder();
        log.append("User: ")
                .append(authentication.getName())
                .append(" (")
                .append(authentication.getDetails().toString())
                .append(") " + managedElement.getUserLabel() + ": ")
                .append(action)
                .append(" result: ")
                .append(response.replaceAll("\n"," "));
        operationLog.warn(log.toString());
    }
}
