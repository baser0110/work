package bsshelper.externalapi.perfmng.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

public class HistoryRTWP extends HistoryForUMTSCell {
    public HistoryRTWP(String rnc, String cellName, int cellId, double value, LocalDateTime time) {
        super(rnc, cellName, cellId, value, time);
    }

    public static Map<String, HistoryRTWP.Values> getFinal(List<HistoryForUMTSCell> data) {
        if (data == null || data.isEmpty()) { return null; }
        Set<String> uniqValues = new HashSet<>();
        Map<String, HistoryRTWP.Values> map = new TreeMap<>();
        HistoryRTWP.Values mapObject = null;
        for (HistoryForUMTSCell h : data) {
            String object = h.getCellName();
            uniqValues.add(object);
            double value = h.getValue();
            if (map.containsKey(object)) {
                mapObject = map.get(object);
                if (value == -112.0) {
                    if (h.getTime().isAfter(mapObject.getTime())) {
                        mapObject.setLast(value);
                        mapObject.setTime(h.getTime());
                    }
                    continue;}
                if (mapObject.getMax() < value) { mapObject.setMax(value); }
                mapObject.setAvg((mapObject.getAvg() + value) / 2);
                if (h.getTime().isAfter(mapObject.getTime())) {
                    mapObject.setLast(value);
                    mapObject.setTime(h.getTime());
                }
            } else {
                map.put(object, new HistoryRTWP.Values(true, value, value, value, h.getTime()));
            }
        }
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
