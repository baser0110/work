package bsshelper.externalapi.configurationmng.nemoactserv.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ClockSyncEInformationITBBU {
    private String syncEStatus;
    private String isEnableSSM;
    private String clockGrade;
    private String syncEclockFreqOffset;
    private String blockStatus;
}
