package bsshelper.externalapi.configurationmng.currentmng.to.itbbu;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUCUEUtranCellFDDLTEMocSimplified;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ITBBUCUEUtranCellFDDLTEMocSimplifiedTo {
    private int code;
    private String message;
    private List<ITBBUCUEUtranCellFDDLTEMocSimplifiedTo.ITBBUCUEUtranCellFDDLTEMocSimplifiedResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ITBBUCUEUtranCellFDDLTEMocSimplifiedResultTo {
        private List<ITBBUCUEUtranCellFDDLTEMocSimplified> moData;
        private String ManagedElementType;
        private String ne;
    }
}
