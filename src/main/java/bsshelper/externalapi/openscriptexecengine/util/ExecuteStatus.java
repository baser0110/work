package bsshelper.externalapi.openscriptexecengine.util;

public enum ExecuteStatus {
    NOT_STARTED (-1),
    FAILED (0),
    SUCCEEDED (1),
    RUNNING (2),
    PARTIALLY_SUCCESSFUL (4);

    ExecuteStatus(int i) {
    }

    public static String getStatusByCode(int code) {
        return switch (code) {
            case -1 -> NOT_STARTED.name();
            case 0 -> FAILED.name();
            case 1 -> SUCCEEDED.name();
            case 2 -> RUNNING.name();
            case 4 -> PARTIALLY_SUCCESSFUL.name();
            default -> "UNDEFINED CODE: " + code;
        };
    }
}
