package bsshelper.externalapi.configurationmng.currentmng.to.sdr;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.EUtranCellNBIoTMocSimplified;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EUtranCellNBIoTMocSimplifiedTo {
    private int code;
    private String message;
    private List<EUtranCellNBIoTMocSimplifiedResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EUtranCellNBIoTMocSimplifiedResultTo {
        private List<EUtranCellNBIoTMocSimplified> moData;
        private String ManagedElementType;
        private String ne;
    }
}
