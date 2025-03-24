package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.perfmng.entity.HistoryMaxOpticPower;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HistoryITBBUMaxOpticPowerMapper {
    static public HistoryMaxOpticPower toFinalEntity(String raw) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (raw == null) {return null;}
        String[] split = raw.split(",");
        if (split[11].contains("_1_")) {
            return new HistoryMaxOpticPower(
                    split[11].replace("_1_","(Slot") + "):" + split[13],
                    Double.parseDouble(split[15]),
                    LocalDateTime.parse(split[1], formatter));
        } else return new HistoryMaxOpticPower(
                "RU" + split[11] + ":" + split[13].replace("OPT",""),
                Double.parseDouble(split[15]),
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
