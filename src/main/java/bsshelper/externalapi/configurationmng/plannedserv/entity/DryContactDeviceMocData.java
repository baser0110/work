package bsshelper.externalapi.configurationmng.plannedserv.entity;

import bsshelper.externalapi.configurationmng.plannedserv.util.Operation;
import bsshelper.externalapi.configurationmng.plannedserv.util.drycontactenums.AlmStatus;
import bsshelper.externalapi.configurationmng.plannedserv.util.drycontactenums.AlmUserLabel;
import bsshelper.externalapi.configurationmng.plannedserv.util.drycontactenums.IsSelfDefineAlm;
import lombok.Data;

import static bsshelper.globalutil.GlobalUtil.DRY_CONTACT_LDN_SDR_SLOT13;


@Data
public class DryContactDeviceMocData implements MocData{
    private String moOp;
    private String mocName;
    private int moId;
    private String ldn;
    private int dryNo;
    private String userLabel;
    private int almStatus;
    private int almNo;
    private int isSelfDefineAlm;


    public DryContactDeviceMocData(Operation moOp, int moId, int dryNo, AlmUserLabel almUserLabel, AlmStatus almStatus) {
        this.moOp = moOp.getOperation();
        this.mocName = "DryContactDevice";
        this.moId = moId;
        this.ldn = DRY_CONTACT_LDN_SDR_SLOT13 + moId;
        this.dryNo = dryNo;
        this.userLabel = almUserLabel.getUserLabel();
        this.almStatus = almStatus.getCode();

        this.almNo = almUserLabel.getCode();
        this.isSelfDefineAlm = IsSelfDefineAlm.YES.getCode();
    }

    public int getLdnNumber() {
        return Integer.parseInt(ldn.substring(ldn.lastIndexOf("=") + 1));
    }

    public void setLdn(int moId) {
        this.ldn = DRY_CONTACT_LDN_SDR_SLOT13 + ldn;
    }
}
