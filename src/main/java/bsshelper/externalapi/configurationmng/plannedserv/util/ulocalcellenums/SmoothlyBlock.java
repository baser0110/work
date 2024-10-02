package bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums;

import lombok.Getter;

@Getter
public enum SmoothlyBlock {
    UNBLOCKED(0, "Unblocked"),
    BLOCKED(1, "Blocked");

    private final int code;
    private final String userLabel;

    SmoothlyBlock(int code, String userLabel) {
        this.code = code;
        this.userLabel = userLabel;
    }
}
