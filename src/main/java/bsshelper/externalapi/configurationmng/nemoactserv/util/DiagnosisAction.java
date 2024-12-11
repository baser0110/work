package bsshelper.externalapi.configurationmng.nemoactserv.util;

import lombok.Getter;

@Getter
public enum DiagnosisAction {
    RRU_VSWR_TEST ("16777420", "ru vswr test"),
    OPTICAL_ELECTRIC_INTERFACE_STATUS_TEST ("16777243", "optical interface status test");

    private final String code;
    private final String info;

    DiagnosisAction(String code, String info) {
        this.code = code;
        this.info = info;
    }
}
