package bsshelper.externalapi.configurationmng.currentmng.to.sdr;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.FiberCableMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.ULocalCellMocSimplified;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiberCableMocTo {
    private int code;
    private String message;
    private List<FiberCableMocResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FiberCableMocResultTo {
        private List<FiberCableMoc> moData;
        private String ManagedElementType;
        private String ne;
    }
}
