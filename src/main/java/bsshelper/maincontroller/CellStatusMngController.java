package bsshelper.maincontroller;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.UCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.ULocalCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.service.CurrentMgnService;
import bsshelper.externalapi.openscriptexecengine.entity.CellStatus;
import bsshelper.externalapi.openscriptexecengine.entity.GCellStatus;
import bsshelper.externalapi.openscriptexecengine.mapper.*;
import bsshelper.externalapi.openscriptexecengine.service.ExecuteUCLIBatchScriptService;
import bsshelper.externalapi.openscriptexecengine.util.BatchFileBuilder;
import bsshelper.externalapi.openscriptexecengine.util.ExecuteStatus;
import bsshelper.externalapi.openscriptexecengine.util.StringFileEntity;
import bsshelper.externalapi.openscriptexecengine.wrapper.EUtranCellNBIoTStatusListWrapper;
import bsshelper.externalapi.openscriptexecengine.wrapper.GCellStatusListWrapper;
import bsshelper.externalapi.openscriptexecengine.wrapper.ULocalCellStatusListWrapper;
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

import java.util.*;

@Slf4j
@Controller
@RequestMapping(value = "/helper")
@RequiredArgsConstructor
public class CellStatusMngController {
    private final CurrentMgnService currentMgnService;
    private final TokenService tokenService;
    private final LocalCacheService localCacheService;
    private final ExecuteUCLIBatchScriptService executeUCLIBatchScriptService;
//    private final ActiveAlarmService activeAlarmService;

    @GetMapping("/cellStatus")
    public String cellStatus(Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        model.addAttribute("managedElement", null);
        model.addAttribute("title", "Cell Status Manager (Single NE)");
        return "cellstatus";
    }

    @GetMapping("/cellStatus/{userLabel}")
    public String search(@PathVariable(value = "userLabel") String userLabel, Model model, HttpSession session) {

        String id = session.getId();
        setMessage(id, model);
        ManagedElement managedElement = currentMgnService.getManagedElementByNeName(tokenService.getToken(), userLabel);
        if (managedElement == null) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "userLabel '" + userLabel + "' couldn't be found"));
            return "redirect:/helper/cellStatus";
        }

//        System.out.println(currentMgnService.rawDataQuery(tokenService.getToken(),managedElement,"EUtranCellNBIoT"));
        ULocalCellStatusListWrapper uLocalCellMocListWrapper = null;
        EUtranCellNBIoTStatusListWrapper eUtranCellNBIoTMocListWrapper = null;
        Map<Integer, UCellMocSimplified> uCellMap = new TreeMap<>();
        List<ULocalCellMocSimplified> uLocalCellList = null;
        List<ITBBUULocalCellMocSimplified> iTBBUULocalCellList = null;

        if (managedElement == null) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "userLabel '" + userLabel + "' couldn't be found"));
            return "redirect:/helper/cellStatus";
        }

//        System.out.println(activeAlarmService.rawDataQuery(tokenService.getToken(),managedElement));

        if (managedElement.getManagedElementType() == ManagedElementType.SDR) {
            uCellMap = UCellMocSimplified.toMap(currentMgnService.getUCellMocSimplified(tokenService.getToken(), managedElement));
            uLocalCellList = currentMgnService.getULocalCellMocSimplified(tokenService.getToken(), managedElement);
            uLocalCellMocListWrapper = new ULocalCellStatusListWrapper(ULocalCellStatusMapper.toULocalCellStatusEntity(uLocalCellList, uCellMap));
            eUtranCellNBIoTMocListWrapper = new EUtranCellNBIoTStatusListWrapper(EUtranCellNBIoTStatusMapper.
                    toEUtranCellNBIoTStatusEntity(currentMgnService.getEUtranCellNBIoTMocSimplified(tokenService.getToken(), managedElement)));
        } else {
            uCellMap = UCellMocSimplified.toMap(currentMgnService.getUCellMocSimplified(tokenService.getToken(), managedElement));
            iTBBUULocalCellList = currentMgnService.getITBBUULocalCellMocSimplified(tokenService.getToken(), managedElement);
            uLocalCellMocListWrapper = new ULocalCellStatusListWrapper(ITBBUULocalCellStatusMapper.
                    toULocalCellStatusEntity(iTBBUULocalCellList, uCellMap));
            eUtranCellNBIoTMocListWrapper = new EUtranCellNBIoTStatusListWrapper(ITBBUCUEUtranCellNBIoTStatusMapper.
                    toEUtranCellNBIoTStatusEntity(currentMgnService.getITBBUCUEUtranCellNBIoTMocSimplified(tokenService.getToken(), managedElement)));
        }
        GCellStatusListWrapper gCellStatusListWrapper = new GCellStatusListWrapper(
                GCellStatusMapper.toGCellStatusEntity(currentMgnService.getGGsmCellMocSimplified(tokenService.getToken(), managedElement)));

//        System.out.println(uLocalCellMocListWrapper);
//        System.out.println(gCellStatusListWrapper);
        if (uLocalCellList != null) {
            localCacheService.cellStatusDetailsMap.put(id, CellStatusDetailsMapper.toULocalCellStatusEntitySDR(uLocalCellList, uCellMap));
        } else {
            if (iTBBUULocalCellList != null) {
                localCacheService.cellStatusDetailsMap.put(id, CellStatusDetailsMapper.toULocalCellStatusEntityITBBU(iTBBUULocalCellList, uCellMap));
            }
        }
        localCacheService.managedElementMap.put(id, managedElement);

        model.addAttribute("managedElement", managedElement);
        model.addAttribute("repoUMTS", uLocalCellMocListWrapper);
        model.addAttribute("repoNBIoT", eUtranCellNBIoTMocListWrapper);
        model.addAttribute("repoGSM", gCellStatusListWrapper);
        model.addAttribute("title", "Cell Status Manager (Single NE)");
        return "cellstatus";
    }

    @PostMapping("/cellStatus/changeStatus")
    public String cellChangeStatus(@ModelAttribute("repoUMTS") ULocalCellStatusListWrapper repoUMTS, Integer operationUMTS,
                                   @ModelAttribute("repoNBIoT") EUtranCellNBIoTStatusListWrapper repoNBIoT, Integer operationNBIoT,
                                   @ModelAttribute("repoGSM") GCellStatusListWrapper repoGSM, Integer operationGSM,
                                   Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        String execResult = null;
//        System.out.println(operationUMTS);
//        System.out.println(repoUMTS.getDataUMTS());
//        System.out.println(operationNBIoT);
//        System.out.println(repoNBIoT.getDataNBIOT());
//        System.out.println(operationGSM);
//        System.out.println(repoGSM.getDataGSM());
        if ((operationUMTS == null || operationUMTS == 0)
                && (operationNBIoT == null || operationNBIoT == 0)
                && (operationGSM == null || operationGSM == 0)) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "No any operation selected!"));
            return "redirect:/helper/cellStatus/" + localCacheService.managedElementMap.get(id).getUserLabel();
        }
        if ((repoUMTS.getDataUMTS() == null || !repoUMTS.isAnySelected())
                && (repoNBIoT.getDataNBIOT() == null || !repoNBIoT.isAnySelected())
                && (repoGSM.getDataGSM() == null || !repoGSM.isAnySelected())) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "No any data selected!"));
            return "redirect:/helper/cellStatus/" + localCacheService.managedElementMap.get(id).getUserLabel();
        }
//        if (operationUMTS == 3 || operationUMTS == 6) {}
        else {
            execResult = oneOperationWithResponse(localCacheService.managedElementMap.get(id),
                    repoUMTS.getExtensionData(), operationUMTS, repoNBIoT.getExtensionData(), operationNBIoT, repoGSM.getDataGSM(), operationGSM);
            if (execResult.equals("SUCCEEDED")) {
                localCacheService.messageMap.put(id, new MessageEntity(Severity.SUCCESS, "Script execution result: " + execResult));
            } else {
                localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "Script execution result: " + execResult));
            }
        }
        return "redirect:/helper/cellStatus/" + localCacheService.managedElementMap.get(id).getUserLabel();
    }

    private String operate(ManagedElement managedElement,
                           List<CellStatus> UMTSData, Integer UMTSCellOperation,
                           List<CellStatus> NBIoTData, Integer NBIoTCellOperation,
                           List<GCellStatus> GSMData, Integer GSMCellOperation) {
        StringFileEntity file = BatchFileBuilder.buildAllData(managedElement,
                UMTSData, UMTSCellOperation,
                NBIoTData, NBIoTCellOperation,
                GSMData, GSMCellOperation);
        System.out.println(file.getFile());
        String path = executeUCLIBatchScriptService.uploadParamFile(file, tokenService.getToken());
//        System.out.println(path);
        return executeUCLIBatchScriptService.executeBatch(path,tokenService.getToken());
    }

    private String oneOperationWithResponse(ManagedElement managedElement,
                                            List<CellStatus> UMTSData, Integer UMTSCellOperation,
                                            List<CellStatus> NBIoTData, Integer NBIoTCellOperation,
                                            List<GCellStatus> GSMData, Integer GSMCellOperation) {
        int tryings = 10;
        int response = -1;
        String execId = operate(managedElement, UMTSData, UMTSCellOperation, NBIoTData, NBIoTCellOperation, GSMData, GSMCellOperation);
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
}
