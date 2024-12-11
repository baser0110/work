package bsshelper.externalapi.configurationmng.nemoactserv.to;

import bsshelper.externalapi.configurationmng.nemoactserv.entity.DiagnosisRow;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.OpticInfoITBBU;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpticInfoITBBUTo {
    private int code;
    private OpticInfoITBBUResultTo output;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OpticInfoITBBUResultTo {
        private List<OpticInfoITBBU> sfpInfo;
        private List<OpticInfoITBBU> qsfpInfo;
    }
}