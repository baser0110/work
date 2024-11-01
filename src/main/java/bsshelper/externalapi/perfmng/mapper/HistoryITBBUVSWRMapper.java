package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.perfmng.entity.HistoryVSWR;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HistoryITBBUVSWRMapper {
    static public HistoryVSWR toFinalEntity(String raw) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (raw == null) {return null;}
        String[] split = raw.split(",");
        return new HistoryVSWR(
                "RU" + split[11] + ":" + split[13],
                Double.parseDouble(split[15]),
                LocalDateTime.parse(split[1], formatter));
    }

    static public List<HistoryVSWR> toFinalEntity(List<String> rawList) {
        if (rawList == null) return null;
        List<HistoryVSWR> result = new ArrayList<>();
        for (String raw : rawList) {
            result.add(toFinalEntity(raw));
        }
        result.sort(Comparator.comparing(HistoryVSWR::getObject));
        return result;
    }
}
