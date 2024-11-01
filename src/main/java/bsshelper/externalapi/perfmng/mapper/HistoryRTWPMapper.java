package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.perfmng.entity.HistoryRTWP;
import bsshelper.externalapi.perfmng.entity.HistoryVSWR;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HistoryRTWPMapper {
    static public HistoryRTWP toFinalEntity(String raw) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (raw == null) {return null;}
        String[] split = raw.split(",");
        return new HistoryRTWP(
                split[8],
                split[12],
                Integer.parseInt(split[11]),
                Double.parseDouble(split[13]),
                LocalDateTime.parse(split[1], formatter));
    }

    static public List<HistoryRTWP> toFinalEntity(List<String> rawList) {
        if (rawList == null) return null;
        List<HistoryRTWP> result = new ArrayList<>();
        for (String raw : rawList) {
            result.add(toFinalEntity(raw));
        }
        result.sort(Comparator.comparing(HistoryRTWP::getCellName));
        return result;
    }
}
