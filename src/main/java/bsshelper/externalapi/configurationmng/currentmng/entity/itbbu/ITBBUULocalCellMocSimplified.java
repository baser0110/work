package bsshelper.externalapi.configurationmng.currentmng.entity.itbbu;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ITBBUULocalCellMocSimplified {
    private final String userLabel;
    private final String lastModifiedTime;
    private final int adminState;
    private final String mocName;
    private final String operState;
    private final String ldn;
    private final String availStatus;
    private final int localCellId;

    private final double maxDlPwr;
    private final int cellRadius;

}
