package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.perfmng.entity.HistoryForUMTSCell;
import bsshelper.externalapi.perfmng.util.KPI;
import bsshelper.externalapi.perfmng.util.KPIable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HistoryUMTSCellMapper {
    static public HistoryForUMTSCell toFinalEntity(String raw, KPIable kpi) {
        System.out.println(raw);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (raw == null) {return null;}
        String[] split = raw.split(",");
//        if (kpi.equals(KPI.RLC) || kpi.equals(KPI.HSUPA)) {
//            return new HistoryForUMTSCell(
//                    split[8],
//                    split[12],
//                    Integer.parseInt(split[11]),
//                    Double.parseDouble(split[17]),
//                    LocalDateTime.parse(split[1], formatter));
//        }
        return new HistoryForUMTSCell(
                split[8],
                split[12],
                Integer.parseInt(split[11]),
                Double.parseDouble(split[split.length - 1]),
                LocalDateTime.parse(split[1], formatter));
    }

    static public List<HistoryForUMTSCell> toFinalEntity(List<String> rawList, KPIable kpi) {
        if (rawList == null) return null;
        List<HistoryForUMTSCell> result = new ArrayList<>();
        for (String raw : rawList) {
            result.add(toFinalEntity(raw, kpi));
        }
        result.sort(Comparator.comparing(HistoryForUMTSCell::getCellName));
        return result;
    }
}

