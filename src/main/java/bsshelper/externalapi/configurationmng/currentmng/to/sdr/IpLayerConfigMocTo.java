package bsshelper.externalapi.configurationmng.currentmng.to.sdr;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.IpLayerConfigMoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpLayerConfigMocTo {
    private int code;
    private String message;
    private List<IpLayerConfigMocResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IpLayerConfigMocResultTo {
        private List<IpLayerConfigMoc> moData;
        private String ManagedElementType;
        private String ne;
    }
}
