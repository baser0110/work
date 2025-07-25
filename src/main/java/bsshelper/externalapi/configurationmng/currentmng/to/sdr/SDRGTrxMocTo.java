package bsshelper.externalapi.configurationmng.currentmng.to.sdr;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.SDRGTrxMoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SDRGTrxMocTo {
    private int code;
    private String message;
    private List< SDRGTrxMocResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SDRGTrxMocResultTo {
        private List<SDRGTrxMoc> moData;
        private String ManagedElementType;
        private String ne;
    }
}
