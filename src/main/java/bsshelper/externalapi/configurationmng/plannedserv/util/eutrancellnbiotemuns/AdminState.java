package bsshelper.externalapi.configurationmng.plannedserv.util.eutrancellnbiotemuns;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum AdminState {
    UNLOCKED(0, "Unlocked"),
    LOCKED(1, "Locked"),
    SHUTTING_DOWN(4194304, "Shutting Down");

    private final int code;
    private final String userLabel;

    AdminState(int code, String userLabel) {
        this.code = code;
        this.userLabel = userLabel;
    }
    public static String getTextAdminState(int code) {
        if (code == 0) return UNLOCKED.userLabel;
        if (code == 1) return LOCKED.userLabel;
        if (code == 4194304) return SHUTTING_DOWN.userLabel;
        return "Code: " + code;
    }
}
