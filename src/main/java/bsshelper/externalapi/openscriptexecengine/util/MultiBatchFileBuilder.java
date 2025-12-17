package bsshelper.externalapi.openscriptexecengine.util;

import java.util.List;
import java.util.UUID;

public class MultiBatchFileBuilder {
    private static final String FILE_CONTENT_DISPOSITION = "Content-Disposition: form-data; name=\"file\"; filename=\"result.ucli\"";
    private static final String FILE_CONTENT_TYPE = "Content-Type: text/plain";
    private static final String LINE_BREAK = "\r\n";
    private static final String DOUBLE_HYPHEN = "--";
    private static final String SPACE = " ";
    private static final String UCLI_STARTER = "%%UCLI";
    private static final String UCLI_END = "%%";
    private static final String ACCEPT_OPERATION = "-y";
    private static final String UMTS_SMOOTH_BLOCK = "rem execute-batch-action --action smoothBlockULocalCell --sysSmBlkTotalTime 15 --molist ";
    private static final String UMTS_AND_NBIOT_AND_LTEFDD_BLOCK = "rem execute-batch-block --molist ";
    private static final String UMTS_SMOOTH_UNBLOCK = "rem execute-batch-action --action smoothUnBlockULocalCell --sysSmUnBlkTotalTime 1 --molist ";
    private static final String UMTS_AND_NBIOT_AND_LTEFDD_UNBLOCK = "rem execute-batch-unblock --molist ";
    private static final String GSM_UNB_BUILDER_START = "rem execute-rancli --cmd UNB ";
    private static final String GSM_BLK_BUILDER_START = "rem execute-rancli --cmd BLK ";
    private static final String GSM_BLK_BUILDER_END = ",BLOCKTYPE=HANDOVE_AND_BLOCK,DURATION=40;";



    public static StringFileEntity buildAllData(List<String> UMTSData, Integer UMTSCellOperation,
                                                List<String> LTEFDDData, Integer LTEFDDCellOperation,
                                                List<String> NBIoTData, Integer NBIoTCellOperation,
                                                List<String> GSMData, Integer GSMCellOperation) {
        String boundary = "Boundary-" + UUID.randomUUID();

        StringBuilder result = new StringBuilder();
        result.append(DOUBLE_HYPHEN).append(boundary).append(LINE_BREAK)
                .append(FILE_CONTENT_DISPOSITION).append(LINE_BREAK)
                .append(FILE_CONTENT_TYPE).append(LINE_BREAK).append(LINE_BREAK)
                .append(UCLI_STARTER).append(LINE_BREAK)
                .append(buildData(UMTSData, UMTSCellOperation))
                .append(buildData(LTEFDDData, LTEFDDCellOperation))
                .append(buildData(NBIoTData, NBIoTCellOperation))
                .append(buildGSMData(GSMData, GSMCellOperation))
                .append(UCLI_END).append(LINE_BREAK)
                .append(DOUBLE_HYPHEN).append(boundary).append(DOUBLE_HYPHEN).append(LINE_BREAK);
        return new StringFileEntity(result.toString().replaceAll("SubNetwork","SubNetWork"), boundary);
    }

    private static String buildData(List<String> commandData, Integer cellOperation) {
        if (commandData == null || cellOperation == 0 || commandData.isEmpty()) {return "";}
        System.out.println(commandData);
        StringBuilder result = new StringBuilder();
        StringBuilder bodyITBBU = new StringBuilder();
        StringBuilder bodySDR = new StringBuilder();
        for (String command : commandData) {
            if (command.contains("SDR")) {bodySDR.append(command).append(SPACE); continue;}
            if (command.contains("ITBBU")) bodyITBBU.append(command).append(SPACE);
        }
        if (bodyITBBU.isEmpty() && bodySDR.isEmpty()) return "";
        if (!bodySDR.isEmpty()) {
            switch (cellOperation) {
                case 1: {
                    result.append(UMTS_AND_NBIOT_AND_LTEFDD_BLOCK);
                    break;
                }
                case 2: {
                    result.append(UMTS_SMOOTH_BLOCK);
                    break;
                }
                case 4: {
                    result.append(UMTS_AND_NBIOT_AND_LTEFDD_UNBLOCK);
                    break;
                }
                case 5: {
                    result.append(UMTS_SMOOTH_UNBLOCK);
                    break;
                }
            }
            result.append(bodySDR).append(ACCEPT_OPERATION).append(LINE_BREAK);
        }
        if (!bodyITBBU.isEmpty()) {
            switch (cellOperation) {
                case 1, 2: {
                    result.append(UMTS_AND_NBIOT_AND_LTEFDD_BLOCK);
                    break;
                }
                case 4, 5: {
                    result.append(UMTS_AND_NBIOT_AND_LTEFDD_UNBLOCK);
                    break;
                }
            }
            result.append(bodyITBBU).append(ACCEPT_OPERATION).append(LINE_BREAK);
        }
        System.out.println(result);
        return result.toString();
    }

    private static String buildGSMData(List<String> commandData, Integer cellOperation) {
        if (commandData == null || cellOperation == 0 || commandData.isEmpty()) {return "";}
        StringBuilder result = new StringBuilder();
        String stringResult = "";
        switch (cellOperation) {
            case 1: {
                for (String command : commandData) {
                    result.append(GSM_BLK_BUILDER_START)
                            .append(command)
                            .append(LINE_BREAK);
                }
                stringResult = result.toString();
                stringResult = stringResult.replaceAll(";",GSM_BLK_BUILDER_END);
                break;
            }
            case 4: {
                for (String command : commandData) {
                    result.append(GSM_UNB_BUILDER_START)
                            .append(command)
                            .append(LINE_BREAK);
                }
                stringResult = result.toString();
                break;
            }
        }
        return stringResult;
    }
}
