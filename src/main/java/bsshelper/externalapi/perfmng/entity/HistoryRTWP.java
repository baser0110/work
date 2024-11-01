package bsshelper.externalapi.perfmng.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Data
//@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class HistoryRTWP {
    private String rnc;
    private String cellName;
    private int cellId;
    private double value;
    private LocalDateTime time;

    public static Map<String, HistoryRTWP.Values> getFinal(List<HistoryRTWP> data) {
        if (data == null || data.isEmpty()) { return null; }
        Set<String> uniqValues = new HashSet<>();
        Map<String, HistoryRTWP.Values> map = new TreeMap<>();
        HistoryRTWP.Values mapObject = null;
        for (HistoryRTWP h : data) {
            String object = h.getCellName();
            uniqValues.add(object);
            double value = h.getValue();
            if (map.containsKey(object)) {
                mapObject = map.get(object);
                if (value == -112.0) {
                    if (h.time.isAfter(mapObject.getTime())) {
                        mapObject.setLast(value);
                        mapObject.setTime(h.time);
                    }
                    continue;}
                if (mapObject.getMax() < value) { mapObject.setMax(value); }
                mapObject.setAvg((mapObject.getAvg() + value) / 2);
                if (h.time.isAfter(mapObject.getTime())) {
                    mapObject.setLast(value);
                    mapObject.setTime(h.time);
                }
            } else {
                map.put(object, new HistoryRTWP.Values(true, value, value, value, h.time));
            }
        }
//        if (uniqValues.size() != map.size()) {
//            for (String v : uniqValues) {
//                if (!map.containsKey(v))
//                    map.put(v, new HistoryRTWP.Values(false,-112.0, -112.0, -112.0, LocalDateTime.now()));
//            }
//        }
        for (Map.Entry<String, HistoryRTWP.Values> entry : map.entrySet()) {
            String object = entry.getKey();
            if (entry.getValue().getAvg() == -112.0) {
                entry.getValue().setSelected(false);
            }
            entry.getValue().setAvg(Math.round(entry.getValue().getAvg() * 10.0) / 10.0);
            entry.getValue().setMax(Math.round(entry.getValue().getMax() * 10.0) / 10.0);
            entry.getValue().setLast(Math.round(entry.getValue().getLast() * 10.0) / 10.0);
        }
        return map;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Values {
        private boolean selected;
        private double avg;
        private double max;
        private double last;
        private LocalDateTime time;
    }
}
