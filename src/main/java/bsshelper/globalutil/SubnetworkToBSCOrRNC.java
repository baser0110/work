package bsshelper.globalutil;

import lombok.Getter;

@Getter
public enum SubnetworkToBSCOrRNC {
    MN1 (411,116, 11),
    MN2 (412,116, 12),
    MN3 (413,114, 13),
    MN4 (414,114, 14),
    MN5 (415,114, 15),
    MN6 (416,116, 16),
    MG1 (423,123, 23),
    MG2 (424,123, 24),
    VT1 (422,122, 22),
    GR1 (421,121, 21),
    BR2 (433,103, 33),
    PN2 (434,104, 34),
    GM0 (201,100, 30),
    GM1 (435,101, 35),
    GM2 (431,101, 31),
    KL1 (432,101, 32);

    private final int subnetwork;
    private final int bsc;
    private final int rnc;

    SubnetworkToBSCOrRNC(int subnetwork, int bsc, int rnc) {
        this.subnetwork = subnetwork;
        this.bsc = bsc;
        this.rnc = rnc;
    }

    public static int getBSCbySubnetwork(int subnetwork) {
        for (SubnetworkToBSCOrRNC s : SubnetworkToBSCOrRNC.values()) {
            if (s.subnetwork == subnetwork) return s.bsc;
        }
        return 0;
    }

    public static int getRNCbySubnetwork(int subnetwork) {
        for (SubnetworkToBSCOrRNC s : SubnetworkToBSCOrRNC.values()) {
            if (s.subnetwork == subnetwork) return s.rnc;
        }
        return 0;
    }
}
