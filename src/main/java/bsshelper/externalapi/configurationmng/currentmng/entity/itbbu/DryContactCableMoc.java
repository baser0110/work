package bsshelper.externalapi.configurationmng.currentmng.entity.itbbu;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DryContactCableMoc {
    private final String refEmb;
    private final String userLabel;
    private final String lastModifiedTime;
    private final int dryContactType;
    private final String mocName;
    private final String refDryContactPort;
    private final String alarmNameOfOutput;
    private final int moId;
    private final int alarmStatus;
    private final String refReplaceableUnit;
    private final int dryContactNo;
    private final String ldn;
    private final int alarmNameOfInput;
}
