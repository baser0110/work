package bsshelper.externalapi.configurationmng.currentmng.entity.sdr;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DryContactDeviceMoc {
    private final String userLabel;
    private final int almStatus;
    private final String lastModifiedTime;
    private final String mocName;
    private final String ldn;
    private final int isSelfDefineAlm;
    private final int dryNo;
    private final int almNo;
    private final int dryType;
    private final String outDryName;
    private final int moId;
    private final String outDryType;

    public int getLdnNumber() {
        return Integer.parseInt(ldn.substring(ldn.lastIndexOf("=") + 1));
    }
}
