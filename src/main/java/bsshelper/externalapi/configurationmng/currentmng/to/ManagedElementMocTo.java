package bsshelper.externalapi.configurationmng.currentmng.to;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElementMoc;
import bsshelper.globalutil.ManagedElementType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ManagedElementMocTo {
    private int code;
    private String message;
    private List<ManagedElementMocResultTo> result;
    private List<String> failList;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ManagedElementMocResultTo {
        private List<ManagedElementMoc> moData;
        private ManagedElementType ManagedElementType;
        private String ne;
    }
}
