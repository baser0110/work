package bsshelper.externalapi.configurationmng.nemoactserv.to;

import bsshelper.externalapi.configurationmng.nemoactserv.entity.ClockStatusITBBU;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClockStatusITBBUTo {
    private int code;
    private ClockStatusITBBU output;
}
