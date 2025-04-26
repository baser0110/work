package bsshelper.globalutil;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public enum SubnetworkToBSCOrRNC {
    MN1 (411,116, 11, "MN"),
    MN2 (412,116, 12, "MN"),
    MN3 (413,114, 13, "MN"),
    MN4 (414,114, 14, "MN"),
    MN5 (415,114, 15, "MN"),
    MN6 (416,116, 16, "MN"),
    MG1 (423,123, 23, "MG"),
    MG2 (424,123, 24, "MG"),
    VT1 (422,122, 22, "VT"),
    GR1 (421,121, 21, "GR"),
    BR2 (433,103, 33, "BR"),
    PN2 (434,104, 34, "BR"),
    GM0 (201,100, 30, "TST"),
    GM1 (435,101, 35, "GM"),
    GM2 (431,101, 31, "GM"),
    KL1 (432,101, 32, "GM");

    private final int subnetwork;
    private final int bsc;
    private final int rnc;
    private final String region;

    SubnetworkToBSCOrRNC(int subnetwork, int bsc, int rnc, String region) {
        this.subnetwork = subnetwork;
        this.bsc = bsc;
        this.rnc = rnc;
        this.region = region;
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

    public static Set<String> getRNCSet() {
        Set<String> result = new HashSet<>();
        for (SubnetworkToBSCOrRNC s : SubnetworkToBSCOrRNC.values()) {
            result.add(String.valueOf(s.rnc));
        }
        return result;
    }

    public static Set<String> getBSCSet() {
        Set<String> result = new HashSet<>();
        for (SubnetworkToBSCOrRNC s : SubnetworkToBSCOrRNC.values()) {
            result.add(String.valueOf(s.bsc));
        }
        return result;
    }
}
