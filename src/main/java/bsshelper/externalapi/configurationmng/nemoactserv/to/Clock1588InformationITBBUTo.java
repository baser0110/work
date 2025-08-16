package bsshelper.externalapi.configurationmng.nemoactserv.to;

import bsshelper.externalapi.configurationmng.nemoactserv.entity.Clock1588InformationITBBU;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clock1588InformationITBBUTo {
    private int code;
    private Clock1588InformationITBBU output;
}
