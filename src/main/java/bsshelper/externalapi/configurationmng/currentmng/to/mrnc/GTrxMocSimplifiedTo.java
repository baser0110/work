package bsshelper.externalapi.configurationmng.currentmng.to.mrnc;

import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GGsmCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GTrxMocSimplified;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GTrxMocSimplifiedTo {
    private int code;
    private String message;
    private List<GTrxMocSimplifiedResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GTrxMocSimplifiedResultTo {
        private List<GTrxMocSimplified> moData;
        private String ManagedElementType;
        private String ne;
    }
}
