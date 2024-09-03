package bsshelper.externalapi.configurationmng.plannedserv.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DryContactCableMocDataTo {
    private String moOp;
    private int moId;
    private String userLabel;
    private String alarmStatus;
}
