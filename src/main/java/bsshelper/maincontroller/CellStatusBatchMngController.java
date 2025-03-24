package bsshelper.maincontroller;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.service.CurrentMgnService;
import bsshelper.externalapi.configurationmng.currentmng.service.CurrentMgnServiceImpl;
import bsshelper.externalapi.openscriptexecengine.entity.CellStatus;
import bsshelper.externalapi.openscriptexecengine.entity.GCellStatus;
import bsshelper.externalapi.openscriptexecengine.service.ExecuteUCLIBatchScriptService;
import bsshelper.externalapi.openscriptexecengine.util.BatchFileBuilder;
import bsshelper.externalapi.openscriptexecengine.util.ExecuteStatus;
import bsshelper.externalapi.openscriptexecengine.util.MultiBatchFileBuilder;
import bsshelper.externalapi.openscriptexecengine.util.StringFileEntity;
import bsshelper.externalapi.openscriptexecengine.wrapper.EUtranCellNBIoTStatusListWrapper;
import bsshelper.externalapi.openscriptexecengine.wrapper.GCellStatusListWrapper;
import bsshelper.externalapi.openscriptexecengine.wrapper.ULocalCellStatusListWrapper;
import bsshelper.globalutil.Severity;
import bsshelper.globalutil.entity.MessageEntity;
import bsshelper.service.LocalCacheService;
import bsshelper.service.TokenService;
import bsshelper.service.logger.LoggerUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
@RequestMapping(value = "/helper")
@RequiredArgsConstructor
public class CellStatusBatchMngController {
    private final CurrentMgnService currentMgnService;
    private final TokenService tokenService;
    private final LocalCacheService localCacheService;
    private final ExecuteUCLIBatchScriptService executeUCLIBatchScriptService;
    private static final Logger operationLog = LoggerUtil.getOperationLogger();

    private String updateTime = "";

    @PostConstruct
    public void setCellCache() {
        localCacheService.meByNEMap.putAll(currentMgnService.getCacheManagedElement(tokenService.getToken(), CurrentMgnServiceImpl.Type.BY_NE));
        localCacheService.umtsSDRMap.putAll(currentMgnService.getCacheSDRCellsUMTS(tokenService.getToken()));
        localCacheService.umtsITBBUMap.putAll(currentMgnService.getCacheITBBUCellsUMTS(tokenService.getToken()));
        localCacheService.nbiotSDRMap.putAll(currentMgnService.getCacheSDRCellsNBIOT(tokenService.getToken()));
        localCacheService.nbiotITBBUMap.putAll(currentMgnService.getCacheITBBUCellsNBIOT(tokenService.getToken()));
        localCacheService.gsmMRNCMap.putAll(currentMgnService.getCacheMRNCCellsGSM(tokenService.getToken()));
        updateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    @GetMapping("/cellStatusBatch")
    public String cellStatusBatch(Model model, HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        model.addAttribute("updateTime", updateTime);
        model.addAttribute("title", "Cell Status Manager (Batch)");
        return "cellstatusbatch";
    }

    @PostMapping("/cellStatusBatch/updateCellsCache")
    public String updateCellCache(Model model, HttpSession session) {
        localCacheService.umtsSDRMap.clear();
        localCacheService.umtsITBBUMap.clear();
        localCacheService.nbiotSDRMap.clear();
        localCacheService.nbiotITBBUMap.clear();
        localCacheService.gsmMRNCMap.clear();
        setCellCache();
        model.addAttribute("updateTime", updateTime);
        model.addAttribute("title", "Cell Status Manager (Batch)");
        return "cellstatusbatch";
    }

    @PostMapping("/cellStatusBatch/executeBatch")
    public String executeBatch(String separator,
                               String umts, Integer operationUMTS,
                               String gsm, Integer operationGSM,
                               String nbiot, Integer operationNBIoT,
                               Model model, HttpSession session, Authentication authentication) {
        String id = session.getId();
        setMessage(id, model);
        String execResult = null;
//        System.out.println(separator);
//        System.out.println(umts);
//        System.out.println(operationUMTS);
//        System.out.println(gsm);
//        System.out.println(operationGSM);
//        System.out.println(nbiot);
//        System.out.println(operationNBIoT);
//        if (!pass.equals("room220")) {
//            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "Wrong pass!!!"));
//            return "redirect:/helper/cellStatusBatch";
//        }

        if ((operationUMTS == null || operationUMTS == 0)
                && (operationNBIoT == null || operationNBIoT == 0)
                && (operationGSM == null || operationGSM == 0)) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "No any operation selected!"));
            return "redirect:/helper/cellStatusBatch";
        }
        if (umts.isEmpty() && gsm.isEmpty() && nbiot.isEmpty()) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "No any data exist to execute!"));
            return "redirect:/helper/cellStatusBatch";
        }

        List<String> umtsCells = Arrays.asList(umts.trim().split(separator));
        List<String> gsmCells = Arrays.asList(gsm.trim().split(separator));
        List<String> nbiotCells = Arrays.asList(nbiot.trim().split(separator));
        List<String> umtsCommands = new ArrayList<>();
        List<String> gsmCommands = new ArrayList<>();
        List<String> nbiotCommands = new ArrayList<>();

        for (String umtsCell : umtsCells) {
            if (localCacheService.umtsSDRMap.containsKey(umtsCell.trim().toUpperCase())) {
                umtsCommands.add(localCacheService.umtsSDRMap.get(umtsCell.trim().toUpperCase()).getCommand());
                continue;
            }
            if (localCacheService.umtsITBBUMap.containsKey(umtsCell.trim().toUpperCase())) {
                umtsCommands.add(localCacheService.umtsITBBUMap.get(umtsCell.trim().toUpperCase()).getCommand());
            }
        }
//        System.out.println(umtsCommands);
        for (String gsmCell : gsmCells) {
            if (localCacheService.gsmMRNCMap.containsKey(gsmCell.trim().toUpperCase()))
                gsmCommands.add(localCacheService.gsmMRNCMap.get(gsmCell.trim().toUpperCase()).getCommand());
        }
        for (String nbiotCell : nbiotCells) {
            if (localCacheService.nbiotSDRMap.containsKey(nbiotCell.trim().toUpperCase())) {
                nbiotCommands.add(localCacheService.nbiotSDRMap.get(nbiotCell.trim().toUpperCase()).getCommand());
                continue;
            }
            if (localCacheService.nbiotITBBUMap.containsKey(nbiotCell.trim().toUpperCase())) {
                nbiotCommands.add(localCacheService.nbiotITBBUMap.get(nbiotCell.trim().toUpperCase()).getCommand());
            }
        }

        if (umtsCommands.isEmpty() && gsmCommands.isEmpty() && nbiotCommands.isEmpty()) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "No any cell data in batch!"));
            return "redirect:/helper/cellStatusBatch";
        }

        execResult = oneOperationWithResponse(umtsCommands, operationUMTS, nbiotCommands, operationNBIoT, gsmCommands, operationGSM);
        if (execResult.equals("SUCCEEDED")) {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.SUCCESS, "Script execution result: " + execResult));
        } else {
            localCacheService.messageMap.put(id, new MessageEntity(Severity.ERROR, "Script execution result: " + execResult));
        }

        getLog(umtsCells, operationUMTS,
                nbiotCells, operationNBIoT,
                gsmCells, operationGSM,
                execResult, authentication);

        model.addAttribute("updateTime", updateTime);
        return "redirect:/helper/cellStatusBatch";
    }

    private String operate(List<String> UMTSData, Integer UMTSCellOperation,
                           List<String> NBIoTData, Integer NBIoTCellOperation,
                           List<String> GSMData, Integer GSMCellOperation) {
        StringFileEntity file = MultiBatchFileBuilder.buildAllData(UMTSData, UMTSCellOperation, NBIoTData, NBIoTCellOperation, GSMData, GSMCellOperation);
        System.out.println(file.getFile());
        String path = executeUCLIBatchScriptService.uploadParamFile(file, tokenService.getToken());
//        System.out.println(path);
        return executeUCLIBatchScriptService.executeBatch(path,tokenService.getToken());
    }

    private String oneOperationWithResponse(List<String> UMTSData, Integer UMTSCellOperation,
                                            List<String> NBIoTData, Integer NBIoTCellOperation,
                                            List<String> GSMData, Integer GSMCellOperation) {
        int tryings = 10;
        int response = -1;
        String execId = operate(UMTSData, UMTSCellOperation, NBIoTData, NBIoTCellOperation, GSMData, GSMCellOperation);
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

    private void getLog(List<String> umtsCells, Integer UMTSCellOperation,
                        List<String> nbiotCells, Integer NBIoTCellOperation,
                        List<String> gsmCells, Integer GSMCellOperation,
                        String result, Authentication authentication) {
        StringBuilder umts = new StringBuilder();
        StringBuilder nBIoT = new StringBuilder();
        StringBuilder gsm = new StringBuilder();
        for (String cell : umtsCells) {
            umts.append(cell).append(", ");
        }
        if (umts.toString().endsWith(", ")) {
            umts = new StringBuilder(umts.substring(0, umts.length() - 2));
        }
        for (String cell : gsmCells) {
            gsm.append(cell).append(", ");
                    }
        if (gsm.toString().endsWith(", ")) {
            gsm = new StringBuilder(gsm.substring(0, gsm.length() - 2));
        }
        for (String cell : nbiotCells) {
            nBIoT.append(cell).append(", ");
        }
        if (nBIoT.toString().endsWith(", ")) {
            nBIoT = new StringBuilder(nBIoT.substring(0, nBIoT.length() - 2));
        }
        StringBuilder log = new StringBuilder();
        log.append("User: ")
                .append(authentication.getName())
                .append(" (")
                .append(authentication.getDetails().toString())
                .append(") change cell status in BATCH: ");
        String l = String.format("UMTS: [%s] %s, GSM: [%s] %s, NBIoT: [%s] %s; result: %s",
                umts, getOperation(UMTSCellOperation),
                gsm, getOperation(GSMCellOperation),
                nBIoT, getOperation(NBIoTCellOperation),
                result);
        log.append(l);

        operationLog.warn(log.toString());
    }

    private String getOperation(Integer operation) {
        switch (operation) {
            case 0: return "No Operation";
            case 1: return "Block";
            case 2: return "Smoothly Block";
            case 4: return "Unblock";
            case 5: return "Smoothly Unblock";
            default: return "Unknown";
        }
    }
}
