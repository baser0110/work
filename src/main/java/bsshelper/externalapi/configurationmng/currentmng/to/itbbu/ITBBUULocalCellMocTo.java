package bsshelper.externalapi.configurationmng.currentmng.to.itbbu;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ITBBUULocalCellMocTo {

    private int code;
    private String message;
    private List<ITBBUULocalCellMocResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ITBBUULocalCellMocResultTo {
        private List<ITBBUULocalCellMoc> moData;
        private String ManagedElementType;
        private String ne;
    }
}
