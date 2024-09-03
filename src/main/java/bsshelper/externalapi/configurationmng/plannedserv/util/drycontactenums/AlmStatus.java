package bsshelper.externalapi.configurationmng.plannedserv.util.drycontactenums;

import lombok.Getter;

@Getter
public enum AlmStatus {
    CLOSED (0),
    OPEN (1);

    private final int code;

    AlmStatus(int i) {
        this.code = i;
    }

    public static AlmStatus valueOfAlmStatus(int i) {
        if (i == 0) return CLOSED; else return OPEN;
    }
}
