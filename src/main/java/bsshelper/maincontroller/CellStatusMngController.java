package bsshelper.maincontroller;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.service.CurrentMgnService;
import bsshelper.externalapi.openscriptexecengine.entity.CellStatus;
import bsshelper.externalapi.openscriptexecengine.mapper.EUtranCellNBIoTStatusMapper;
import bsshelper.externalapi.openscriptexecengine.mapper.ULocalCellStatusMapper;
import bsshelper.externalapi.openscriptexecengine.service.ExecuteUCLIBatchScriptService;
import bsshelper.externalapi.openscriptexecengine.util.BatchFileBuilder;
import bsshelper.externalapi.openscriptexecengine.util.ExecuteStatus;
import bsshelper.externalapi.openscriptexecengine.util.StringFileEntity;
import bsshelper.externalapi.openscriptexecengine.wrapper.EUtranCellNBIoTStatusListWrapper;
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

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/helper")
@RequiredArgsConstructor
public class CellStatusMngController {
    private final CurrentMgnService currentMgnService;
    private final TokenService tokenService;
    private final LocalCacheService localCacheService;
    private final ExecuteUCLIBatchScriptService executeUCLIBatchScriptService;

    @GetMapping("/cellStatus")
    public String cellStatus(Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        model.addAttribute("managedElement", null);
        model.addAttribute("repoUMTS", null);
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

        if (managedElement == null) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "userLabel '" + userLabel + "' couldn't be found"));
            return "redirect:/helper/cellStatus";
        }
        if (managedElement.getManagedElementType() == ManagedElementType.SDR) {
            uLocalCellMocListWrapper = new ULocalCellStatusListWrapper(ULocalCellStatusMapper.
                    toULocalCellStatusEntity(currentMgnService.getULocalCellMoc(tokenService.getToken(), managedElement)));
            eUtranCellNBIoTMocListWrapper = new EUtranCellNBIoTStatusListWrapper(EUtranCellNBIoTStatusMapper.
                    toEUtranCellNBIoTStatusEntity(currentMgnService.getEUtranCellNBIoTMoc(tokenService.getToken(), managedElement)));
        } else {
                    // for ITBBU
        }

        System.out.println(uLocalCellMocListWrapper);

        localCacheService.managedElementMap.put(id, managedElement);

        model.addAttribute("managedElement", managedElement);
        model.addAttribute("repoUMTS", uLocalCellMocListWrapper);
        model.addAttribute("repoNBIoT", eUtranCellNBIoTMocListWrapper);
        return "cellStatus";
    }

    @PostMapping("/cellStatus/changeStatus")
    public String cellChangeStatus(@ModelAttribute("repoUMTS") ULocalCellStatusListWrapper repoUMTS, Integer operationUMTS,
                                   @ModelAttribute("repoNBIoT") EUtranCellNBIoTStatusListWrapper repoNBIoT, Integer operationNBIoT,
                                   Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        String execResult = null;
        System.out.println(operationUMTS);
        System.out.println(repoUMTS.getDataUMTS());
        System.out.println(operationNBIoT);
        System.out.println(repoNBIoT.getDataNBIOT());
        if ((operationUMTS == null || operationUMTS == 0) && (operationNBIoT == null || operationNBIoT == 0)) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "No any operation selected!"));
            return "redirect:/helper/cellStatus/" + localCacheService.managedElementMap.get(id).getUserLabel();
        }
        if ((repoUMTS.getDataUMTS() == null || !repoUMTS.isAnySelected()) && (repoNBIoT.getDataNBIOT() == null || !repoNBIoT.isAnySelected())) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "No any data selected!"));
            return "redirect:/helper/cellStatus/" + localCacheService.managedElementMap.get(id).getUserLabel();
        }
        if (operationUMTS == 3 || operationUMTS == 6) {}
        else {
            execResult = oneOperationWithResponse(localCacheService.managedElementMap.get(id),
                    repoUMTS.getExtensionData(), operationUMTS, repoNBIoT.getExtensionData(), operationNBIoT);
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
                           List<CellStatus> NBIoTData, Integer NBIoTCellOperation) {
        StringFileEntity file = BatchFileBuilder.buildAllData(managedElement,
                UMTSData, UMTSCellOperation,
                NBIoTData, NBIoTCellOperation);
        String path = executeUCLIBatchScriptService.uploadParamFile(file, tokenService.getToken());
//        System.out.println(path);
        return executeUCLIBatchScriptService.executeBatch(path,tokenService.getToken());
    }

    private String oneOperationWithResponse(ManagedElement managedElement,
                                            List<CellStatus> UMTSData, Integer UMTSCellOperation,
                                            List<CellStatus> NBIoTData, Integer NBIoTCellOperation) {
        int tryings = 10;
        int response = -1;
        String execId = operate(managedElement, UMTSData, UMTSCellOperation, NBIoTData, NBIoTCellOperation);
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
