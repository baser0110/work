package bsshelper.externalapi.configurationmng.currentmng.to.sdr;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.EUtranCellFDDMocSimplified;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EUtranCellFDDMocSimplifiedTo {
    private int code;
    private String message;
    private List<EUtranCellFDDMocSimplifiedTo.EUtranCellFDDMocSimplifiedResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EUtranCellFDDMocSimplifiedResultTo {
        private List<EUtranCellFDDMocSimplified> moData;
        private String ManagedElementType;
        private String ne;
    }
}
