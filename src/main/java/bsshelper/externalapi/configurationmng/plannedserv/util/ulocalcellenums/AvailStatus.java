package bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums;

public enum AvailStatus {
    NORMAL(0, "Normal"),
    HAVE_ALARM(1, "Have Alarm"),
    SLAVE(2, "Slave"),
    NOT_AVAILABLE(4, "Not available"),
    INITIAL(8, "Initial"),
    ON_BUSINESS(16, "On Business"),
    TEST1(32, "Test"),
    LOGICAL_FAULT(64, "Logical Fault"),
    SAVING_ELECTRICITY(128, "Saving Electricity"),
    THE_RELIED_OBJECT_HAS_A_FAULT(65536, "The relied object has a fault"),
    REMOTELY_AND_MANUALLY_BLOCK(262144, "Remotely & manually block"),
    STACKING_FAULT(131072, "Stacking fault"),
    TEST2(524288, "Test"),
    POWERED_DOWM(1048576, "Powered-down"),
    UNCONNECTED_WITH_RNC(4194304, "Unconnected with RNC"),
    POWER_SAVING(8388608, "Power-saving"),
    SYNCHRONOUS(16777216, "Synchronous"),
    MMC_FAULT(33554432, "MMC Fault"),
    LICENCE_STATE_CONSTRAINED(67108864, "License state-constrained"),
    LOCAL_CELL_BLOCK_SAVING_BATTERY_MODE(268435456, "Local cell block(Saving battery mode)"),
    CONNECTION_CABLE_IS_FAULTY_IN_STACKING_STATUS(536870912, "Connecting cable is faulty in stacking status"),
    EMPTY(null,"Undefined");

    private final Integer code;
    private final String userLabel;

    AvailStatus(Integer code, String userLabel) {
        this.code = code;
        this.userLabel = userLabel;
    }

    public static String getTextAvailStatus(String code) {
        if (code.isEmpty()) return EMPTY.userLabel;
        if (Integer.parseInt(code) == 0) return NORMAL.userLabel;
        if (Integer.parseInt(code) == 8388608) return POWER_SAVING.userLabel;
        if (Integer.parseInt(code) == 1) return HAVE_ALARM.userLabel;
        if (Integer.parseInt(code) == 2) return SLAVE.userLabel;
        if (Integer.parseInt(code) == 4) return NOT_AVAILABLE.userLabel;
        if (Integer.parseInt(code) == 8) return INITIAL.userLabel;
        if (Integer.parseInt(code) == 16) return ON_BUSINESS.userLabel;
        if (Integer.parseInt(code) == 32) return TEST1.userLabel;
        if (Integer.parseInt(code) == 64) return LOGICAL_FAULT.userLabel;
        if (Integer.parseInt(code) == 128) return SAVING_ELECTRICITY.userLabel;
        if (Integer.parseInt(code) == 65536) return THE_RELIED_OBJECT_HAS_A_FAULT.userLabel;
        if (Integer.parseInt(code) == 262144) return REMOTELY_AND_MANUALLY_BLOCK.userLabel;
        if (Integer.parseInt(code) == 131072) return STACKING_FAULT.userLabel;
        if (Integer.parseInt(code) == 524288) return TEST2.userLabel;
        if (Integer.parseInt(code) == 1048576) return POWERED_DOWM.userLabel;
        if (Integer.parseInt(code) == 4194304) return UNCONNECTED_WITH_RNC.userLabel;
        if (Integer.parseInt(code) == 16777216) return SYNCHRONOUS.userLabel;
        if (Integer.parseInt(code) == 33554432) return MMC_FAULT.userLabel;
        if (Integer.parseInt(code) == 67108864) return LICENCE_STATE_CONSTRAINED.userLabel;
        if (Integer.parseInt(code) == 268435456) return LOCAL_CELL_BLOCK_SAVING_BATTERY_MODE.userLabel;
        if (Integer.parseInt(code) == 536870912) return CONNECTION_CABLE_IS_FAULTY_IN_STACKING_STATUS.userLabel;
        return code;
    }
}
