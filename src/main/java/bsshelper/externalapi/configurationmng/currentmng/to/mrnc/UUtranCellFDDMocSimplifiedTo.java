package bsshelper.externalapi.configurationmng.currentmng.to.mrnc;

import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UUtranCellFDDMocSimplifiedTo {
    private int code;
    private String message;
    private List<UUtranCellFDDMocSimplifiedResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UUtranCellFDDMocSimplifiedResultTo {
        private List<UUtranCellFDDMocSimplified> moData;
        private String ManagedElementType;
        private String ne;
    }
}
