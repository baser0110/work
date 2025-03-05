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
public class HistoryMaxOpticError {
    private final String port;
    private final int value;
    private final LocalDateTime time;

    public static Map<String, List<HistoryMaxOpticError>> getChart(List<? extends HistoryMaxOpticError> data) {
        if (data == null || data.isEmpty()) { return null; }
        Map<String, List<HistoryMaxOpticError>> map = new TreeMap<>();
        List<HistoryMaxOpticError> mapObject = null;
        for (HistoryMaxOpticError h : data) {
            String object = h.getPort();
            if (map.containsKey(object)) {
                mapObject = map.get(object);
                mapObject.add(h);
            } else {
                map.put(h.getPort(), new ArrayList<>(List.of(h)));
            }
        }
        return map;
    }
}
