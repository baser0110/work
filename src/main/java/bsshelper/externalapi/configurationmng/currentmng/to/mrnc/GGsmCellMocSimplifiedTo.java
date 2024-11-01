package bsshelper.externalapi.configurationmng.currentmng.to.mrnc;

import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GGsmCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.DryContactDeviceMoc;
import bsshelper.externalapi.configurationmng.currentmng.to.sdr.DryContactDeviceMocTo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GGsmCellMocSimplifiedTo {
    private int code;
    private String message;
    private List<GGsmCellMocSimplifiedResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GGsmCellMocSimplifiedResultTo {
        private List<GGsmCellMocSimplified> moData;
        private String ManagedElementType;
        private String ne;
    }
}
