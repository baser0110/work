package bsshelper.externalapi.configurationmng.nemoactserv.to;

import bsshelper.externalapi.configurationmng.nemoactserv.entity.DiagnosisRow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisRowTo {
    private int code;
    private DiagnosisRowResultTo output;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiagnosisRowResultTo {
        private List<DiagnosisRow> resultInfo;
    }
}
