package bsshelper.externalapi.perfmng.util;

import lombok.Getter;

@Getter
public enum QueryType {
    GENERAL_INFO("General Info"),
    CELL_INFO("Cell Info"),
    VSWR("VSWR"),
    OPTIC_LEVELS("Optic Levels"),
//    SYNCHRONIZATION("Sync"),
    CUSTOM_HISTORY("Custom History");

    private final String info;

    QueryType(String info) {
        this.info = info;
    }
}
