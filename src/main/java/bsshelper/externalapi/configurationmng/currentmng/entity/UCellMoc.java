package bsshelper.externalapi.configurationmng.currentmng.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class UCellMoc {
    private final String lastModifiedTime;
    private final String mocName;
    private final String ldn;
    private final int cellId;
    private final String operState;
    private final int adminState;
    private final String availStatus;

    public static Map<Integer, UCellMoc> toMap(List<UCellMoc> cellMocSimplifiedList) {
        if (cellMocSimplifiedList == null || cellMocSimplifiedList.isEmpty()) {
            return null;
        }
        Map<Integer, UCellMoc> map = new HashMap<>();
        for (UCellMoc cell : cellMocSimplifiedList) {
            map.put(cell.getCellId(), cell);
        }
        return map;
    }
}
