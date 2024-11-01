package bsshelper.externalapi.configurationmng.currentmng.to.sdr;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.DryContactDeviceMoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DryContactDeviceMocTo {
    private int code;
    private String message;
    private List<DryContactDeviceMocResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DryContactDeviceMocResultTo {
        private List<DryContactDeviceMoc> moData;
        private String ManagedElementType;
        private String ne;
    }
}

