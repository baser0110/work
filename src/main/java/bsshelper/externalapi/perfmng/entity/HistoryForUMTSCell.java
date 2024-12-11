package bsshelper.externalapi.perfmng.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
@RequiredArgsConstructor
public class HistoryForUMTSCell {
    private final String rnc;
    private final String cellName;
    private final int cellId;
    private final double value;
    private final LocalDateTime time;

    public static Map<String, List<HistoryForUMTSCell>> getChart(List<? extends HistoryForUMTSCell> data) {
        if (data == null || data.isEmpty()) { return null; }
        Map<String, List<HistoryForUMTSCell>> map = new TreeMap<>();
        List<HistoryForUMTSCell> mapObject = null;
        for (HistoryForUMTSCell h : data) {
            String object = h.getCellName();
            if (map.containsKey(object)) {
                mapObject = map.get(object);
                mapObject.add(h);
            } else {
                map.put(h.getCellName(), new ArrayList<>(List.of(h)));
            }
        }
        return map;
    }
}
