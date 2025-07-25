package bsshelper.externalapi.configurationmng.currentmng.to.sdr;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.EthernetSwitchDeviceMoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EthernetSwitchDeviceMocTo {
    private int code;
    private String message;
    private List<EthernetSwitchDeviceMocResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EthernetSwitchDeviceMocResultTo {
        private List<EthernetSwitchDeviceMoc> moData;
        private String ManagedElementType;
        private String ne;
    }
}
