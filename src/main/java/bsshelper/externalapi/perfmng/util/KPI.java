package bsshelper.externalapi.perfmng.util;

import lombok.Data;
import lombok.Getter;

@Getter
public enum KPI {

    RTWP ("300840", "Average RTWP"),
    RRC ("300020", "Ratio of successful RRC connection establishment"),
    RAB ("300043", "Ratio of successful RAB establishment"),
    HSDPA ("990075", "HSDPA Average Throughput Per User"),
    HSUPA ("900873", "HSUPA Average Throughput Per User"),
    RLC ("9010321020747714", "HSDPA DL Retransmit Rate (RLC)"),
    VSWR ("C370150000", "VSWR");

    private final String code;
    private final String info;

    KPI(String code, String info) {
        this.code = code;
        this.info = info;
    }
}
