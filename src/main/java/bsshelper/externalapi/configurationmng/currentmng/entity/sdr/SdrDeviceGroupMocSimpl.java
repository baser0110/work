package bsshelper.externalapi.configurationmng.currentmng.entity.sdr;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SdrDeviceGroupMocSimpl {
    private final String lastModifiedTime;
    private final String ldn;
    private final String productData_productName;
//    private final int functionMode;
//    private final String physicalBrdType;
}
