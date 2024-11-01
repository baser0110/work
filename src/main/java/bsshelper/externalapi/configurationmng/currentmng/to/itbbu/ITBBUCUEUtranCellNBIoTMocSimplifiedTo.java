package bsshelper.externalapi.configurationmng.currentmng.to.itbbu;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUCUEUtranCellNBIoTMocSimplified;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ITBBUCUEUtranCellNBIoTMocSimplifiedTo {
    private int code;
    private String message;
    private List<ITBBUCUEUtranCellNBIoTMocSimplifiedResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ITBBUCUEUtranCellNBIoTMocSimplifiedResultTo {
        private List<ITBBUCUEUtranCellNBIoTMocSimplified> moData;
        private String ManagedElementType;
        private String ne;
    }
}
