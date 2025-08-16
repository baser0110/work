package bsshelper.externalapi.configurationmng.nemoactserv.to;

import bsshelper.externalapi.configurationmng.nemoactserv.entity.ClockSyncEInformationITBBU;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClockSyncEInformationITBBUTo {
    private int code;
    private ClockSyncEInformationITBBU output;
}
