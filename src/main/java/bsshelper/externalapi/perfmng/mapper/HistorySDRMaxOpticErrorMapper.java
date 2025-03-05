package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.perfmng.entity.HistoryForULocalCell;
import bsshelper.externalapi.perfmng.entity.HistoryMaxOpticError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HistorySDRMaxOpticErrorMapper {
    static public HistoryMaxOpticError toFinalEntity(String raw) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (raw == null) {return null;}
        String[] split = raw.split(",");
        if (split[9].equals("1")) {
            return new HistoryMaxOpticError(
                    "FS(Slot" + split[13] + "):OF" + (Integer.parseInt(split[15]) - 1),
                    Integer.parseInt(split[17]),
                    LocalDateTime.parse(split[1], formatter));
        } else return new HistoryMaxOpticError(
                "RU" + split[9] + ":" + (Integer.parseInt(split[15]) + 1),
                Integer.parseInt(split[17]),
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
