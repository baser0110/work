package bsshelper.externalapi.configurationmng.currentmng.entity;

import bsshelper.globalutil.ManagedElementType;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@Data
@RequiredArgsConstructor
public class ManagedElement {
    private final String userLabel;
    private final ManagedElementType ManagedElementType;
    private final String ne;

    public String getParsedNe() {
        return ne.replace("=", ": ").replace("ManagedElement:", " NE:");
    }
}
