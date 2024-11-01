package bsshelper.externalapi.configurationmng.currentmng.to.itbbu;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.DryContactCableMoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DryContactCableMocTo {
    private int code;
    private String message;
    private List<DryContactCableMocResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DryContactCableMocResultTo {
        private List<DryContactCableMoc> moData;
        private String ManagedElementType;
        private String ne;
    }
}
