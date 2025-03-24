package bsshelper.externalapi.openscriptexecengine.util;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.openscriptexecengine.entity.CellStatus;
import bsshelper.externalapi.openscriptexecengine.entity.GCellStatus;
import bsshelper.globalutil.SubnetworkToBSCOrRNC;

import java.util.List;
import java.util.UUID;

public class BatchFileBuilder {

    private static final String FILE_CONTENT_DISPOSITION = "Content-Disposition: form-data; name=\"file\"; filename=\"result.ucli\"";
    private static final String FILE_CONTENT_TYPE = "Content-Type: text/plain";
    private static final String LINE_BREAK = "\r\n";
    private static final String DOUBLE_HYPHEN = "--";
    private static final String SPACE = " ";
    private static final String COMMA = ",";
    private static final String UCLI_STARTER = "%%UCLI";
    private static final String UCLI_END = "%%";
    private static final String ACCEPT_OPERATION = "-y";
    private static final String UMTS_SMOOTH_BLOCK = "rem execute-batch-action --action smoothBlockULocalCell --sysSmBlkTotalTime 15 --molist ";
    private static final String UMTS_AND_NBIOT_BLOCK = "rem execute-batch-block --molist ";
    private static final String UMTS_SMOOTH_UNBLOCK = "rem execute-batch-action --action smoothUnBlockULocalCell --sysSmUnBlkTotalTime 1 --molist ";
    private static final String UMTS_AND_NBIOT_UNBLOCK = "rem execute-batch-unblock --molist ";
    private static final String GSM_UNB_BUILDER_START = "rem execute-rancli --cmd UNB CELL:MEID=";
    private static final String GSM_BLK_BUILDER_START = "rem execute-rancli --cmd BLK CELL:MEID=";
    private static final String GSM_BUILDER_MIDDLE_1 = ",SITEID=";
    private static final String GSM_BUILDER_MIDDLE_2 = ",BTSID=";
    private static final String GSM_UNB_BUILDER_END = ";";
    private static final String GSM_BLK_BUILDER_END = ",BLOCKTYPE=HANDOVE_AND_BLOCK,DURATION=40;";
    private static final String GSM_BUILDER_POSTFIX = " --netype MRNC --neid ";


    public static StringFileEntity buildAllData(ManagedElement managedElement,
                                                List<CellStatus> UMTSData, Integer UMTSCellOperation,
                                                List<CellStatus> NBIoTData, Integer NBIoTCellOperation,
                                                List<GCellStatus> GSMData, Integer GSMCellOperation) {
        String boundary = "Boundary-" + UUID.randomUUID();

        StringBuilder result = new StringBuilder();
        result.append(DOUBLE_HYPHEN).append(boundary).append(LINE_BREAK)
                .append(FILE_CONTENT_DISPOSITION).append(LINE_BREAK)
                .append(FILE_CONTENT_TYPE).append(LINE_BREAK).append(LINE_BREAK)
                .append(UCLI_STARTER).append(LINE_BREAK)
                .append(buildData(managedElement, UMTSData, UMTSCellOperation))
                .append(buildData(managedElement, NBIoTData, NBIoTCellOperation))
                .append(buildGSMData(managedElement, GSMData, GSMCellOperation))
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
                        .append(SPACE);
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
            result.append(body).append(ACCEPT_OPERATION).append(LINE_BREAK);
        }
        return result.toString();
    }

    private static String buildGSMData(ManagedElement managedElement, List<GCellStatus> requestData, Integer cellOperation) {
        if (requestData == null || cellOperation == 0 || requestData.isEmpty()) {
            return "";
        }
        int numBSC = SubnetworkToBSCOrRNC.getBSCbySubnetwork(managedElement.getSubNetworkNum());
        StringBuilder result = new StringBuilder();
        switch (cellOperation) {
            case 1: {
                for (GCellStatus cell : requestData) {
                    if (cell.isSelected()) {
                        result.append(GSM_BLK_BUILDER_START)
                                .append(numBSC)
                                .append(GSM_BUILDER_MIDDLE_1)
                                .append(managedElement.getBTSManagedElementNum())
                                .append(GSM_BUILDER_MIDDLE_2)
                                .append(cell.getLdn().substring(cell.getLdn().length() - 1))
                                .append(GSM_BLK_BUILDER_END)
                                .append(GSM_BUILDER_POSTFIX)
                                .append(numBSC)
                                .append(LINE_BREAK);
                    }
                }
                break;
            }
            case 4: {
                for (GCellStatus cell : requestData) {
                    if (cell.isSelected()) {
                        result.append(GSM_UNB_BUILDER_START)
                                .append(SubnetworkToBSCOrRNC.getBSCbySubnetwork(managedElement.getSubNetworkNum()))
                                .append(GSM_BUILDER_MIDDLE_1)
                                .append(managedElement.getBTSManagedElementNum())
                                .append(GSM_BUILDER_MIDDLE_2)
                                .append(cell.getLdn().substring(cell.getLdn().length() - 1))
                                .append(GSM_UNB_BUILDER_END)
                                .append(GSM_BUILDER_POSTFIX)
                                .append(numBSC)
                                .append(LINE_BREAK);
                    }
                }
                break;
            }
        }
        return result.toString();
    }


}
