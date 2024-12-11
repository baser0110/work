package bsshelper.externalapi.configurationmng.currentmng.to.itbbu;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.RiCableMoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiCableMocTo {
    private int code;
    private String message;
    private List<RiCableMocResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiCableMocResultTo {
        private List<RiCableMoc> moData;
        private String ManagedElementType;
        private String ne;
    }
}
