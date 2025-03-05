package bsshelper.externalapi.perfmng.util;

import lombok.Data;
import lombok.Getter;

@Getter
public enum KPI {

    RTWP ("300840", "Average RTWP"),
    RRC_ATTEMPT ("300002", "Number of RRC connection establishment Attempt"),
    RRC ("300020", "Ratio of successful RRC connection establishment"),
    RAB ("300043", "Ratio of successful RAB establishment"),
    HSDPA ("990075", "HSDPA Average Throughput Per User"),
    HSUPA ("900873", "HSUPA Average Throughput Per User"),
    RLC ("9010321020747714", "HSDPA DL Retransmit Rate (RLC)"),
    VSWR ("C370150000", "VSWR"),
    ANT_RSSI_1 ("C372480040", "Average RSSI of antenna 1"),
    ANT_RSSI_2 ("C372480043", "Average RSSI of antenna 2"),
    ANT_RSSI_1AND2 ("0", "Average RSSI of antenna 1&2"),
    CELL_DIVERSITY ("307241", "Cell Diversity Reception"),
    NUMBER_USER_IN_CELL ("C372480162", "Total number of users in a cell"),
    MAX_OPTIC_ERROR_SDR ("C370050001", "Maximum Ratio of Optical Interface Error"),
    MAX_OPTIC_ERROR_ITBBU ("C370380001", "Maximum Ratio of Optical Interface Error"),
    MAX_OPTIC_ERROR ("0", "Maximum Ratio of Optical Interface Error");

    private final String code;
    private final String info;

    KPI(String code, String info) {
        this.code = code;
        this.info = info;
    }
}
