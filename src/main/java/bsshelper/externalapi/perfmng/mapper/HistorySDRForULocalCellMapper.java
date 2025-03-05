package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.perfmng.entity.HistoryForULocalCell;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HistorySDRForULocalCellMapper {
    static public HistoryForULocalCell toFinalEntity(String raw) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (raw == null) {return null;}
        String[] split = raw.split(",");
        return new HistoryForULocalCell(
                split[10],
                Double.parseDouble(split[11]),
                LocalDateTime.parse(split[1], formatter));
    }

    static public List<HistoryForULocalCell> toFinalEntity(List<String> rawList) {
        if (rawList == null) return null;
        List<HistoryForULocalCell> result = new ArrayList<>();
        for (String raw : rawList) {
            result.add(toFinalEntity(raw));
        }
        result.sort(Comparator.comparing(HistoryForULocalCell::getCellName));
        return result;
    }
}
