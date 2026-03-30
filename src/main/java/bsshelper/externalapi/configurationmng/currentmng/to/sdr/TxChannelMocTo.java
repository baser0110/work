package bsshelper.externalapi.configurationmng.currentmng.to.sdr;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.TxChannelMoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TxChannelMocTo {
    private int code;
    private String message;
    private List<TxChannelMocResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TxChannelMocResultTo {
        private List<TxChannelMoc> moData;
        private String ManagedElementType;
        private String ne;
    }
}
