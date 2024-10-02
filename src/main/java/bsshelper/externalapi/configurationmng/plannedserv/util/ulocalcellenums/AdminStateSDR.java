package bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum AdminStateSDR {
    NO_MANUAL_OPERATION(0,"No Manual Operation"),
    BLOCK(1,"Block"),
    POWERED_DOWN(4,"Powered-down"),
    READ_AND_WRITE_FLASH(8,"Read and Write Flash"),
    BLOCK_SMOOTHLY(65536,"Block Smoothly");

    private final int code;
    private final String userLabel;

    AdminStateSDR(int code, String userLabel) {
        this.code = code;
        this.userLabel = userLabel;
    }

    public static String getTextAdminState(int code) {
        if (code == 0) return NO_MANUAL_OPERATION.userLabel;
        if (code == 1) return BLOCK.userLabel;
        if (code == 4) return POWERED_DOWN.userLabel;
        if (code == 8) return READ_AND_WRITE_FLASH.userLabel;
        if (code == 65536) return BLOCK_SMOOTHLY.userLabel;
        List<String> stateList = new ArrayList<>();
        if (code - BLOCK_SMOOTHLY.getCode() >= 0) {
            stateList.add(BLOCK_SMOOTHLY.getUserLabel()); code -= BLOCK_SMOOTHLY.getCode(); }
        if (code - READ_AND_WRITE_FLASH.getCode() >= 0) {
            stateList.add(READ_AND_WRITE_FLASH.getUserLabel()); code -= READ_AND_WRITE_FLASH.getCode(); }
        if (code - POWERED_DOWN.getCode() >= 0) {
            stateList.add(POWERED_DOWN.getUserLabel()); code -= POWERED_DOWN.getCode(); }
        if (code - BLOCK.getCode() >= 0) stateList.add(BLOCK.getUserLabel());
        StringBuilder result = new StringBuilder(stateList.get(0));
        for (int i = 1; i < stateList.size(); i++) {
            result.append(" / ").append(stateList.get(i));
        }
        return result.toString();
    }
}

