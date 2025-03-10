package bsshelper.externalapi.configurationmng.currentmng.to.sdr;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.SdrDeviceGroupMoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SdrDeviceGroupMocTo {
    private int code;
    private String message;
    private List<SdrDeviceGroupMocResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SdrDeviceGroupMocResultTo {
        private List<SdrDeviceGroupMoc> moData;
        private String ManagedElementType;
        private String ne;
    }
}
