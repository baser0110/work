package bsshelper.externalapi.configurationmng.currentmng.to.itbbu;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMocSimplified;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ITBBUULocalCellMocSimplifiedTo {

    private int code;
    private String message;
    private List<ITBBUULocalCellMocSimplifiedResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ITBBUULocalCellMocSimplifiedResultTo {
        private List<ITBBUULocalCellMocSimplified> moData;
        private String ManagedElementType;
        private String ne;
    }
}
