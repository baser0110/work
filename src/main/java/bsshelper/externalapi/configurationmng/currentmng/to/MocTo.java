package bsshelper.externalapi.configurationmng.currentmng.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MocTo<T> {
    private int code;
    private String message;
    private List<MocResultTo<T>> result;
    private List<String> failList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MocResultTo<T> {
        private List<T> moData;
        private String ManagedElementType;
        private String ne;
    }
}
