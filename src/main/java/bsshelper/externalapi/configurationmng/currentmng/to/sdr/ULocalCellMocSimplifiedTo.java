package bsshelper.externalapi.configurationmng.currentmng.to.sdr;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.ULocalCellMocSimplified;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ULocalCellMocSimplifiedTo {
    private int code;
    private String message;
    private List<ULocalCellMocSimplifiedResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ULocalCellMocSimplifiedResultTo {
        private List<ULocalCellMocSimplified> moData;
        private String ManagedElementType;
        private String ne;
    }
}
