package bsshelper.externalapi.configurationmng.currentmng.entity.sdr;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class IpLayerConfigMoc {
    private final String x2SelfSetupConfig;
    private final String lastModifiedTime;
    private final String mocName;
    private final String gatewayIp;
    private final String networkMask;
    private final String ipField;
    private final String moId;
    private final String ipAddr;
    private final String prefixLength;
    private final String vid;
    private final String refEthernetLink;
    private final String x2SelfSetup;
    private final String refEthernetLinkGroup;
    private final String ldn;
    private final String ipNo;
    private final String refPppLink;
}
