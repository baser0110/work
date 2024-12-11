package bsshelper.externalapi.perfmng.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
@RequiredArgsConstructor
public class HistoryVSWR {
    private final String object;
    private final double value;
    private final LocalDateTime time;

    public static Map<String, List<HistoryVSWR>> getChart(List<HistoryVSWR> data) {
        if (data == null || data.isEmpty()) { return null; }
        Map<String, List<HistoryVSWR>> map = new TreeMap<>();
        List<HistoryVSWR> mapObject = null;
        for (HistoryVSWR h : data) {
            String object = h.getObject();
            if (map.containsKey(object)) {
                mapObject = map.get(object);
                mapObject.add(h);
            } else {
                map.put(h.getObject(), new ArrayList<>(List.of(h)));
            }
        }
        return map;
    }

    // FOR TABLE, NOT USE

    public static Map<String, Values> getFinal(List<HistoryVSWR> data) {
        if (data == null || data.isEmpty()) { return null; }
        Map<String, Values> map = new TreeMap<>();
        Values mapObject = null;
        for (HistoryVSWR h : data) {
            String object = h.getObject();
            double value = h.getValue();
//            if (value == 0.0 && map.containsKey(object)) {continue;}
            if (map.containsKey(object)) {
                mapObject = map.get(object);
                if (mapObject.getMax() < value) { mapObject.setMax(value); }
                if (h.time.isAfter(mapObject.getTime())) {
                    mapObject.setLast(value);
                    mapObject.setTime(h.time);
                }
                mapObject.setAvg((mapObject.getAvg() + value) / 2);
            } else {
                map.put(object, new Values(value, value, value, h.time));
            }
        }
        for (Map.Entry<String, Values> entry : map.entrySet()) {
            String object = entry.getKey();
            entry.getValue().setAvg(Math.round(entry.getValue().getAvg() * 100.0) / 100.0);
            entry.getValue().setMax(Math.round(entry.getValue().getMax() * 100.0) / 100.0);
            entry.getValue().setLast(Math.round(entry.getValue().getLast() * 100.0) / 100.0);
        }
        return map;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Values {
        private double avg;
        private double max;
        private double last;
        private LocalDateTime time;
    }
}
