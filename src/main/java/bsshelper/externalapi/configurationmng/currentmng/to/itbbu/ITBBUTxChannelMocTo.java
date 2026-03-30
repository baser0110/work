package bsshelper.externalapi.configurationmng.currentmng.to.itbbu;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUTxChannelMoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ITBBUTxChannelMocTo {
    private int code;
    private String message;
    private List<ITBBUTxChannelMocResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ITBBUTxChannelMocResultTo {
        private List<ITBBUTxChannelMoc> moData;
        private String ManagedElementType;
        private String ne;
    }
}
