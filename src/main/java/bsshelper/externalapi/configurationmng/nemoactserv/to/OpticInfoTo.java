package bsshelper.externalapi.configurationmng.nemoactserv.to;

import bsshelper.externalapi.configurationmng.nemoactserv.entity.OpticInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpticInfoTo {
    private int code;
    private OpricInfoResultTo output;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OpricInfoResultTo {
        private List<OpticInfo> resultInfo;
    }
}
