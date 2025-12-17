package bsshelper.externalapi.configurationmng.currentmng.entity.sdr;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EUtranCellFDDMocSimplified {
    private final String userLabel;
    private final String lastModifiedTime;
    private final int adminState;
    private final String mocName;
    private final String operState;
    private final String ldn;
}
