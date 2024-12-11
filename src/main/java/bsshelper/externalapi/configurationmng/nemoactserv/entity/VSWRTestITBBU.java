package bsshelper.externalapi.configurationmng.nemoactserv.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class VSWRTestITBBU {
    private String antNo;
    private String band;
    private String vswrValue;
    private String alarmStatus;
}
