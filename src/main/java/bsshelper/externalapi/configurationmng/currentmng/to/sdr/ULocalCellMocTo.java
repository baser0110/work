package bsshelper.externalapi.configurationmng.currentmng.to.sdr;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.ULocalCellMoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ULocalCellMocTo {
    private int code;
    private String message;
    private List<ULocalCellMocResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ULocalCellMocResultTo {
        private List<ULocalCellMoc> moData;
        private String ManagedElementType;
        private String ne;
    }
}