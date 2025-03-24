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
public class HistoryMaxOpticPower {
    private final String port;
    private final double value;
    private final LocalDateTime time;

    public static Map<String, List<HistoryMaxOpticPower>> getChart(List<? extends HistoryMaxOpticPower> data) {
        if (data == null || data.isEmpty()) { return null; }
        Map<String, List<HistoryMaxOpticPower>> map = new TreeMap<>();
        List<HistoryMaxOpticPower> mapObject = null;
        for (HistoryMaxOpticPower h : data) {
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
