package bsshelper.externalapi.configurationmng.nemoactserv.to;

import bsshelper.externalapi.configurationmng.nemoactserv.entity.ClockGNSSInformationITBBU;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClockGNSSInformationITBBUTo {
    private int code;
    private ClockGNSSInformationITBBU output;
}
