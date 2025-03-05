package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.perfmng.entity.HistoryMaxOpticError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HistoryITBBUMaxOpticErrorMapper {
    static public HistoryMaxOpticError toFinalEntity(String raw) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (raw == null) {return null;}
        String[] split = raw.split(",");
        if (split[11].contains("VBP")) {
            return new HistoryMaxOpticError(
                    split[11].replace("_1_","(Slot") + ")" + split[13],
                    Integer.parseInt(split[15]),
                    LocalDateTime.parse(split[1], formatter));
        } else return new HistoryMaxOpticError(
                "RU" + split[11] + ":" + split[13].replace("OPT",""),
                Integer.parseInt(split[15]),
                LocalDateTime.parse(split[1], formatter));
    }

    static public List<HistoryMaxOpticError> toFinalEntity(List<String> rawList) {
        if (rawList == null) return null;
        List<HistoryMaxOpticError> result = new ArrayList<>();
        for (String raw : rawList) {
            result.add(toFinalEntity(raw));
        }
        result.sort(Comparator.comparing(HistoryMaxOpticError::getPort));
        return result;
    }
}
