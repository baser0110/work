package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.perfmng.entity.HistoryForUMTSCell;
import bsshelper.externalapi.perfmng.entity.HistoryOfficeLink;
import bsshelper.externalapi.perfmng.util.KPI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HistoryOfficeLinkMapper {
    static public HistoryOfficeLink toFinalEntity(String raw) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (raw == null) {return null;}
        String[] split = raw.split(",");
        return new HistoryOfficeLink(
                split[6],
                split[9],
                split[10],
                Double.parseDouble(split[11]),
                LocalDateTime.parse(split[1], formatter));
    }

    static public List<HistoryOfficeLink> toFinalEntity(List<String> rawList) {
        if (rawList == null) return null;
        List<HistoryOfficeLink> result = new ArrayList<>();
        for (String raw : rawList) {
            result.add(toFinalEntity(raw));
        }
//        result.sort(Comparator.comparing(HistoryOfficeLink::getMrnc));
        return result;
    }
}
