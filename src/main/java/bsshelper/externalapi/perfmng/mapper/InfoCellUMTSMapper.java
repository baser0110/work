package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.ULocalCellMocSimplified;
import bsshelper.externalapi.perfmng.entity.InfoCellUMTS;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class InfoCellUMTSMapper {
    static public Map<String, UUtranCellFDDMocSimplified> cellRNCtoMap(List<UUtranCellFDDMocSimplified> cells) {
        if (cells == null || cells.isEmpty()) { return null; }
        Map<String, UUtranCellFDDMocSimplified> map = new TreeMap<>();
        for (UUtranCellFDDMocSimplified cell : cells) {
            map.put(cell.getUserLabel(), cell);
        }
        return map;
    }

    static public Map<String, ULocalCellMocSimplified> cellSDRtoMap(List<ULocalCellMocSimplified> cells) {
        if (cells == null || cells.isEmpty()) { return null; }
        Map<String, ULocalCellMocSimplified> map = new TreeMap<>();
        for (ULocalCellMocSimplified cell : cells) {
            map.put(cell.getUserLabel(), cell);
        }
        return map;
    }

    static public Map<String, ITBBUULocalCellMocSimplified> cellITBBUtoMap(List<ITBBUULocalCellMocSimplified> cells) {
        if (cells == null || cells.isEmpty()) { return null; }
        Map<String, ITBBUULocalCellMocSimplified> map = new TreeMap<>();
        for (ITBBUULocalCellMocSimplified cell : cells) {
            map.put(cell.getUserLabel(), cell);
        }
        return map;
    }

    static public List<InfoCellUMTS> toEntityForSDR(Map<String, UUtranCellFDDMocSimplified> rnc, Map<String, ULocalCellMocSimplified> sdr) {
        if (rnc == null || rnc.isEmpty()) { return null; }
        List<InfoCellUMTS> list = new ArrayList<>();
        for (Map.Entry<String, UUtranCellFDDMocSimplified> cell: rnc.entrySet()) {
            list.add(new InfoCellUMTS(
                    cell.getKey(),
                    cell.getValue().getMoId(),
                    sdr.containsKey(cell.getKey()) ? sdr.get(cell.getKey()).getLocalCellId() : null,
                    BigDecimal.valueOf(Math.pow(10.0, (cell.getValue().getMaximumTransmissionPower() / 10.0)) / 1000).setScale(1, RoundingMode.HALF_UP).doubleValue(),
                    sdr.containsKey(cell.getKey()) ? sdr.get(cell.getKey()).getMaxDlPwr() : null,
                    cell.getValue().getCellRadius(),
                    sdr.containsKey(cell.getKey()) ? sdr.get(cell.getKey()).getCellRadius() : null));
        }
        return list;
    }

    static public List<InfoCellUMTS> toEntityForITBBU(Map<String, UUtranCellFDDMocSimplified> rnc, Map<String, ITBBUULocalCellMocSimplified> sdr) {
        if (rnc == null || rnc.isEmpty()) { return null; }
        List<InfoCellUMTS> list = new ArrayList<>();
        for (Map.Entry<String, UUtranCellFDDMocSimplified> cell: rnc.entrySet()) {
            list.add(new InfoCellUMTS(
                    cell.getKey(),
                    cell.getValue().getMoId(),
                    sdr.containsKey(cell.getKey()) ? sdr.get(cell.getKey()).getLocalCellId() : null,
                    BigDecimal.valueOf(Math.pow(10.0, (cell.getValue().getMaximumTransmissionPower() / 10.0)) / 1000).setScale(1, RoundingMode.HALF_UP).doubleValue(),
                    sdr.containsKey(cell.getKey()) ? sdr.get(cell.getKey()).getMaxDlPwr() : null,
                    cell.getValue().getCellRadius(),
                    sdr.containsKey(cell.getKey()) ? sdr.get(cell.getKey()).getCellRadius() : null));
        }
        return list;
    }
}
