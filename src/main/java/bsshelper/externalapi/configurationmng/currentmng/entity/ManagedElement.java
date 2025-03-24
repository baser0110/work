package bsshelper.externalapi.configurationmng.currentmng.entity;

import bsshelper.globalutil.ManagedElementType;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class ManagedElement {
    private final String userLabel;
    private final ManagedElementType ManagedElementType;
    private final String ne;

    public String getParsedNe() {
        return ne.replace("=", ": ").replace("ManagedElement:", " NE:");
    }

    public String getManagedElementNum() {
        int index = ne.lastIndexOf("=");
        return ne.substring(index + 1);
    }

    public String getBTSManagedElementNum() {
        int index = ne.lastIndexOf("=");
        if (ManagedElementType.equals(bsshelper.globalutil.ManagedElementType.SDR)) return ne.substring(index + 1);
        else {
            String result = ne.substring(index + 3);
            while (result.substring(0, 1).equals("0")) result = result.substring(1);
            return result;
        }
    }

    public Integer getSubNetworkNum() {
        int index = ne.indexOf("=");
        return Integer.parseInt(ne.substring(index + 1,index + 4));
    }

}
