package bsshelper.externalapi.configurationmng.currentmng.entity.itbbu;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class IpMoc {
    private final String prefixLength;
    private final String lastModifiedTime;
    private final String mocName;
    private final String refInterface;
    private final String ldn;
    private final String ipAddress;
    private final String gatewayIp;
    private final String moId;
}
