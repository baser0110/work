package bsshelper.externalapi.configurationmng.currentmng.to.itbbu;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ReplaceableUnitMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.RiCableMoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplaceableUnitMocTo {
    private int code;
    private String message;
    private List<ReplaceableUnitMocResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReplaceableUnitMocResultTo {
        private List<ReplaceableUnitMoc> moData;
        private String ManagedElementType;
        private String ne;
    }
}
