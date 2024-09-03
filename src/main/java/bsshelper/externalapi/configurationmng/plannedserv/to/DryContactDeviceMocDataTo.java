package bsshelper.externalapi.configurationmng.plannedserv.to;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DryContactDeviceMocDataTo {
    private String moOp;
    private int moId;
    private int dryNo;
    private String userLabel;
    private String almStatus;
}
