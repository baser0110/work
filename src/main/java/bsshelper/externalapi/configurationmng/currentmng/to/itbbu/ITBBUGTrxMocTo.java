package bsshelper.externalapi.configurationmng.currentmng.to.itbbu;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUGTrxMoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ITBBUGTrxMocTo {
    private int code;
    private String message;
    private List<ITBBUGTrxMocResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ITBBUGTrxMocResultTo {
        private List<ITBBUGTrxMoc> moData;
        private String ManagedElementType;
        private String ne;
    }
}
