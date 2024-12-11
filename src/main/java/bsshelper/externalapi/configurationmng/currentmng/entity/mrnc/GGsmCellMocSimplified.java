package bsshelper.externalapi.configurationmng.currentmng.entity.mrnc;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GGsmCellMocSimplified {
    private final String userLabel;
    private final String lastModifiedTime;
    private final String ldn;
    private final int bcchFrequency;
    private final int cellIdentity;
    private final int moId;
}
