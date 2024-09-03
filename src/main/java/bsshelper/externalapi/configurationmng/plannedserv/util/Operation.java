package bsshelper.externalapi.configurationmng.plannedserv.util;

import lombok.Getter;

@Getter
public enum Operation {
    NO ("no operation"),
    M ("update"),
    A ("create"),
    D ("delete");


    private final String operation;

    Operation(String operation) {
        this.operation = operation;
    }
}
