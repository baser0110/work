package bsshelper.externalapi.configurationmng.plannedserv.entity;

import bsshelper.externalapi.configurationmng.plannedserv.util.Operation;
import bsshelper.externalapi.configurationmng.plannedserv.util.drycontactenums.AlmStatus;
import bsshelper.externalapi.configurationmng.plannedserv.util.drycontactenums.AlmUserLabel;
import lombok.Data;

@Data
public class DryContactCableMocData implements MocData{
    private String moOp;
    private String ldn;
    private int moId;
    private String refDryContactPort;
    private int dryContactNo;
    private int dryContactType;
    private int alarmStatus;
    private String userLabel;
    private int alarmNameOfInput;
    private String mocName;

    public DryContactCableMocData(Operation moOp, int moId, AlmUserLabel almUserLabel, AlmStatus alarmStatus) {
        this.moOp = moOp.getOperation();
        this.ldn = "Equipment=1,DryContactCable=" + moId;
        this.moId = moId;
        this.refDryContactPort = "Equipment=1,ReplaceableUnit=VEM_1_13,DryContactPort=EAM" + (moId % 4 == 0 ? moId/4 : moId/4 + 1);
        this.dryContactNo = moId % 4 == 0 ? 4 : moId % 4;
        this.dryContactType = 0;
        this.alarmStatus = alarmStatus.getCode();
        this.userLabel = almUserLabel.getUserLabel();
        this.alarmNameOfInput = almUserLabel.getCode();
        this.mocName = "DryContactCable";
    }
}
