package bsshelper.externalapi.configurationmng.currentmng.to.sdr;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.EUtranCellNBIoTMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.SdrDeviceGroupMocSimpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SdrDeviceGroupMocSimplTo {
    private int code;
    private String message;
    private List<SdrDeviceGroupMocSimplResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SdrDeviceGroupMocSimplResultTo {
        private List<SdrDeviceGroupMocSimpl> moData;
        private String ManagedElementType;
        private String ne;
    }
}
