package bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums;

public enum OperState {
    NORMAL(0, "Normal"),
    ABNORMAL(1, "Abnormal"),
    EMPTY(null,"Undefined");

    private final Integer code;
    private final String userLabel;

    OperState(Integer code, String userLabel) {
        this.code = code;
        this.userLabel = userLabel;
    }

    public static String getTextOperState(String code) {
        if (code.isEmpty()) return EMPTY.userLabel;
        if (Integer.parseInt(code) == 0) return NORMAL.userLabel;
        if (Integer.parseInt(code) == 1) return ABNORMAL.userLabel;
        return "Undefined(" + code + ")";
    }
}
