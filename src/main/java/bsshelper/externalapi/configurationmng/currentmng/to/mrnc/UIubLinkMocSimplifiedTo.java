package bsshelper.externalapi.configurationmng.currentmng.to.mrnc;

import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UIubLinkMocSimplified;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UIubLinkMocSimplifiedTo {
    private int code;
    private String message;
    private List<UIubLinkMocSimplifiedResultTo> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UIubLinkMocSimplifiedResultTo {
        private List<UIubLinkMocSimplified> moData;
        private String ManagedElementType;
        private String ne;
    }
}
