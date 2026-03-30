package bsshelper.externalapi.configurationmng.currentmng.entity.sdr;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TxChannelMoc {
    private final String txChannelNo;
    private final String lastModifiedTime;
    private final String mocName;
    private final String channelGroupNo;
    private final String bandInfo;
    private final String moId;
    private final String txCenterFreq;
    private final String operState;
    private final String ldn;
    private final String adminState;
    private final String detectSwitch;
    private final String availStatus;
    private final String suppBand;
}