package bsshelper.externalapi.openscriptexecengine.util;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.openscriptexecengine.entity.CellStatus;
import bsshelper.externalapi.openscriptexecengine.entity.ULocalCellStatus;

import java.util.List;
import java.util.UUID;

public class BatchFileBuilder {

    public static String FILE_CONTENT_DISPOSITION = "Content-Disposition: form-data; name=\"file\"; filename=\"result.ucli\"";
    public static String FILE_CONTENT_TYPE = "Content-Type: text/plain";
    public static String LINE_BREAK = "\r\n";
    public static String DOUBLE_HYPHEN = "--";
    public static String SPACE = " ";
    public static String COMMA = ",";
    public static String UCLI_STARTER = "%%UCLI";
    public static String UCLI_END = "%%";
    public static String ACCEPT_OPERATION = "-y";
    public static String UMTS_SMOOTH_BLOCK = "rem execute-batch-action --action smoothBlockULocalCell --sysSmBlkTotalTime 15 --molist ";
    public static String UMTS_AND_NBIOT_BLOCK = "rem execute-batch-block --molist ";
    public static String UMTS_SMOOTH_UNBLOCK = "rem execute-batch-action --action smoothUnBlockULocalCell --sysSmUnBlkTotalTime 1 --molist ";
    public static String UMTS_AND_NBIOT_UNBLOCK = "rem execute-batch-unblock --molist ";



    public static StringFileEntity buildAllData(ManagedElement managedElement,
                                                                       List<CellStatus> UMTSData, Integer UMTSCellOperation,
                                                                       List<CellStatus> NBIoTData, Integer NBIoTCellOperation) {
        String boundary = "Boundary-" + UUID.randomUUID();

        StringBuilder result = new StringBuilder();
        result.append(DOUBLE_HYPHEN).append(boundary).append(LINE_BREAK)
                .append(FILE_CONTENT_DISPOSITION).append(LINE_BREAK)
                .append(FILE_CONTENT_TYPE).append(LINE_BREAK).append(LINE_BREAK)
                .append(UCLI_STARTER).append(LINE_BREAK)
                .append(buildData(managedElement, UMTSData, UMTSCellOperation))
                .append(buildData(managedElement, NBIoTData, NBIoTCellOperation))
                .append(UCLI_END).append(LINE_BREAK)
                .append(DOUBLE_HYPHEN).append(boundary).append(DOUBLE_HYPHEN).append(LINE_BREAK);
        return new StringFileEntity(result.toString().replaceAll("SubNetwork","SubNetWork"), boundary);
    }

    private static String buildData(ManagedElement managedElement, List<CellStatus> requestData, Integer cellOperation) {
        if (requestData == null || cellOperation == 0 || requestData.isEmpty()) {return "";}
        StringBuilder result = new StringBuilder();
        StringBuilder body = new StringBuilder();
        for (CellStatus cell : requestData) {
            if (cell.isSelected()) {
                body.append("ManagedElementType=")
                        .append(managedElement.getManagedElementType().toString())
                        .append(COMMA)
                        .append(managedElement.getNe())
                        .append(COMMA)
                        .append(cell.getLdn())
                        .append(SPACE)
                        .append(ACCEPT_OPERATION)
                        .append(LINE_BREAK);
            }
        }
        if (body.isEmpty()) return "";
        else {
            switch (cellOperation) {
                case 1: {
                    result.append(UMTS_AND_NBIOT_BLOCK);
                    break;
                }
                case 2: {
                    result.append(UMTS_SMOOTH_BLOCK);
                    break;
                }
                case 4: {
                    result.append(UMTS_AND_NBIOT_UNBLOCK);
                    break;
                }
                case 5: {
                    result.append(UMTS_SMOOTH_UNBLOCK);
                    break;
                }

            }
            result.append(body);
        }
        return result.toString();
    }
}
