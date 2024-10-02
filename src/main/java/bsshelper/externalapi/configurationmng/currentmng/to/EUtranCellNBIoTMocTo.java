package bsshelper.externalapi.configurationmng.currentmng.to;

import bsshelper.externalapi.configurationmng.currentmng.entity.EUtranCellNBIoTMoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EUtranCellNBIoTMocTo {
    private int code;
    private String message;
    private List<EUtranCellNBIoTMocToResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EUtranCellNBIoTMocToResultTo {
        private List<EUtranCellNBIoTMoc> moData;
        private String ManagedElementType;
        private String ne;
    }
}