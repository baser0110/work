package bsshelper.externalapi.configurationmng.currentmng.entity.itbbu;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SctpMoc {
    private final String minRTO;
    private final String userLabel;
    private final String lastModifiedTime;
    private final String groupId;
    private final String maxAssoRetran;
    private final String dscp;
    private final String delayAckTime;
    private final String maxInitRetran;
    private final String maxPathRetran;
    private final String radioMode;
    private final String refBandwidthResource;
    private final String refDtls;
    private final String adminState;
    private final String initRTO;
    private final String availStatus;
    private final String remoteIp;
    private final String localPort;
    private final String mocName;
    private final String maxRTO;
    private final String remotePort;
    private final String assoType;
    private final String moId;
    private final String refIp;
    private final String operState;
    private final String ldn;
    private final String congestEndure;
    private final String inOutStreamNum;
    private final String sctpControlState;
    private final String hbInterval;
//    private final String primaryPathNo;
}
