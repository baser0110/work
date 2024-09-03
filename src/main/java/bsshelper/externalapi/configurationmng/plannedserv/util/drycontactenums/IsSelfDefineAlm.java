package bsshelper.externalapi.configurationmng.plannedserv.util.drycontactenums;

import lombok.Getter;

@Getter
public enum IsSelfDefineAlm {
    NO (0),
    YES (1);

    private final int code;

    IsSelfDefineAlm(int i) {
        this.code = i;
    }
}
