package bsshelper.externalapi.configurationmng.nemoactserv.to;

import bsshelper.externalapi.configurationmng.nemoactserv.entity.OpticInfoITBBU;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.VSWRTestITBBU;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VSWRTestITBBUTo {
    private int code;
    private VSWRTestITBBUResultTo output;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VSWRTestITBBUResultTo {
        private List<VSWRTestITBBU> paAntList;
    }
}
