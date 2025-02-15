package bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums;

import lombok.Getter;

@Getter
public enum AdminStateITBBU {
    UNBLOCKED(0, "Unblocked"),
    BLOCKED(1, "Blocked");

    private final int code;
    private final String userLabel;

    AdminStateITBBU(int code, String userLabel) {
        this.code = code;
        this.userLabel = userLabel;
    }

    public static String getTextAdminState(int code) {
        if (code == 0) return UNBLOCKED.userLabel;
        if (code == 1) return BLOCKED.userLabel;
        return "Undefined(" + code + ")";
    }
}