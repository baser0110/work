package bsshelper.globalutil;

import bsshelper.externalapi.configurationmng.plannedserv.util.drycontactenums.AlmUserLabel;
import lombok.Getter;

@Getter
public enum SubnetworkToBSC {
    MN1 (411,116),
    MN2 (412,116),
    MN3 (413,114),
    MN4 (414,114),
    MN5 (415,114),
    MN6 (416,116),
    MG1 (423,123),
    MG2 (424,123),
    VT1 (422,122),
    GR1 (421,121),
    BR2 (433,103),
    PN2 (434,104),
    GM0 (201,100),
    GM1 (435,101),
    GM2 (431,101),
    KL1 (432,101);

    private final int subnetwork;
    private final int bsc;

    SubnetworkToBSC(int subnetwork, int bsc) {
        this.subnetwork = subnetwork;
        this.bsc = bsc;
    }

    public static int getBSCbySubnetwork(int subnetwork) {
        for (SubnetworkToBSC s : SubnetworkToBSC.values()) {
            if (s.subnetwork == subnetwork) return s.bsc;
        }
        return 0;
    }
}
