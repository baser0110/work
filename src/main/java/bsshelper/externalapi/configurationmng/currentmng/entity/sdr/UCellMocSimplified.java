package bsshelper.externalapi.configurationmng.currentmng.entity.sdr;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class UCellMocSimplified {
    private final String lastModifiedTime;
    private final String mocName;
    private final String ldn;
    private final int cellId;
    private final String operState;
    private final int adminState;
    private final String availStatus;

    public static Map<Integer,UCellMocSimplified> toMap(List<UCellMocSimplified> cellMocSimplifiedList) {
        if (cellMocSimplifiedList == null || cellMocSimplifiedList.isEmpty()) {
            return null;
        }
        Map<Integer,UCellMocSimplified> map = new HashMap<>();
        for (UCellMocSimplified cell : cellMocSimplifiedList) {
            map.put(cell.getCellId(), cell);
        }
        return map;
    }
}
