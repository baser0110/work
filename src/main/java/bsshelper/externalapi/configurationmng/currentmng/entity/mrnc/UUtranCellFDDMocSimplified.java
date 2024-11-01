package bsshelper.externalapi.configurationmng.currentmng.entity.mrnc;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UUtranCellFDDMocSimplified {
    private final String userLabel;
    private final String lastModifiedTime;
    private final String ldn;
    private final int moId;
    private final double maximumTransmissionPower;
    private final int primaryScramblingCode;

    public String getRncNum() {
        int index1 = ldn.indexOf("=");
        int index2 = ldn.indexOf(",");
        return ldn.substring(index1 + 1, index2);
    }
}
