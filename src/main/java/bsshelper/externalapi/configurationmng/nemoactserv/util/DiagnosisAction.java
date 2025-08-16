package bsshelper.externalapi.configurationmng.nemoactserv.util;

import lombok.Getter;

@Getter
public enum DiagnosisAction {
    RRU_VSWR_TEST ("16777420", "ru vswr test"),
    OPTICAL_ELECTRIC_INTERFACE_STATUS_TEST ("16777243", "optical interface status test"),
    CLOCK_STATUS_QUERY ("16777234", "clock status query"),
    CLOCK_1588_INFORMATION_QUERY ("16777236", "1588 clock information query"),
    CLOCK_SYNCE_QUERY ("16777253", "syncE query"),
    CLOCK_GNSS_INFORMATION_QUERY ("16777275", "inner GNSS information query");


    private final String code;
    private final String info;

    DiagnosisAction(String code, String info) {
        this.code = code;
        this.info = info;
    }
}
