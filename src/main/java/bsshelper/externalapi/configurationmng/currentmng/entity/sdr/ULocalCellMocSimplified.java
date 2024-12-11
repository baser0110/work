package bsshelper.externalapi.configurationmng.currentmng.entity.sdr;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ULocalCellMocSimplified {
    private final String userLabel;
    private final String lastModifiedTime;
    private final int adminState;
    private final String mocName;
    private final String operState;
    private final String ldn;
    private final int localCellId;
    private final String availStatus;

    private final double maxDlPwr;
    private final int cellRadius;
}
