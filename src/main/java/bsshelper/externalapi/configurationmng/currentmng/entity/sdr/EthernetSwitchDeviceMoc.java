package bsshelper.externalapi.configurationmng.currentmng.entity.sdr;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EthernetSwitchDeviceMoc {
    private final String lastModifiedTime;
    private final String portMode;
    private final String operatingMode;
    private final String mocName;
    private final String egressBitRate;
    private final String egressRateSwitch;
    private final String moId;
    private final String isEnable;
    private final String port;
    private final String ldn;
    private final String ingressRateSwitch;
    private final String vlanMembership;
    private final String ingressBitRate;
    private final String ingressRateLimitMode;
}