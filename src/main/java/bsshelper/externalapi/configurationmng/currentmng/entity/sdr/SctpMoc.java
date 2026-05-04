package bsshelper.externalapi.configurationmng.currentmng.entity.sdr;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SctpMoc {
    private final String minRTO;
    private final String userLabel;
    private final String lastModifiedTime;
    private final String sctpNo;
    private final String maxAssoRetran;
    private final String sctpType;
    private final String dscp;
    private final String delayAckTime;
    private final String maxInitRetran;
    private final String maxPathRetran;
    private final String radioMode;
    private final String refBandwidthResource;
    private final String adminState;
    private final String initRTO;
    private final String availStatus;
    private final String remoteAddr;
    private final String localPort;
    private final String mocName;
    private final String maxRTO;
    private final String remotePort;
    private final String moId;
    private final String refIpLayerConfig;
    private final String operState;
    private final String ldn;
    private final String congestEndure;
    private final String inOutStreamNum;
    private final String hbInterval;
    private final String primaryPathNo;
}
