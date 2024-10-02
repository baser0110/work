package bsshelper.externalapi.configurationmng.currentmng.to;

import bsshelper.externalapi.configurationmng.currentmng.entity.ULocalCellMoc;
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
    private List<ULocalCellMocToResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ULocalCellMocToResultTo {
        private List<ULocalCellMoc> moData;
        private String ManagedElementType;
        private String ne;
    }
}
