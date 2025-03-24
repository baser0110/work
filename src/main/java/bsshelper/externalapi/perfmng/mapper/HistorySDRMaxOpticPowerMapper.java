package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.perfmng.entity.HistoryMaxOpticPower;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HistorySDRMaxOpticPowerMapper {
    static public HistoryMaxOpticPower toFinalEntity(String raw) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (raw == null) {return null;}
        String[] split = raw.split(",");
        if (split[9].equals("1")) {
            return new HistoryMaxOpticPower(
                    "Slot" + split[13] + ":OF" + (Integer.parseInt(split[15]) - 1),
                    Double.parseDouble(split[17]),
                    LocalDateTime.parse(split[1], formatter));
        } else return new HistoryMaxOpticPower(
                "RU" + split[9] + ":" + (Integer.parseInt(split[15]) + 1),
                Double.parseDouble(split[17]),
                LocalDateTime.parse(split[1], formatter));
    }

    static public List<HistoryMaxOpticPower> toFinalEntity(List<String> rawList) {
        if (rawList == null) return null;
        List<HistoryMaxOpticPower> result = new ArrayList<>();
        for (String raw : rawList) {
            result.add(toFinalEntity(raw));
        }
        result.sort(Comparator.comparing(HistoryMaxOpticPower::getPort));
        return result;
    }
}
