package bsshelper.externalapi.configurationmng.currentmng.entity.itbbu;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ITBBUTxChannelMoc {
    private final String userLabel;
    private final String freqBand;
    private final String lastModifiedTime;
    private final String mocName;
    private final String ldn;
    private final String adminState;
    private final String detectSwitch;
    private final String reversePowerDetect;
    private final String groupNo;
    private final String moId;
}