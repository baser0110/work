package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.ULocalCellMoc;
import bsshelper.externalapi.perfmng.entity.InfoCellUMTS;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class InfoCellUMTSMapper {
    static public Map<String, UUtranCellFDDMoc> cellRNCtoMap(List<UUtranCellFDDMoc> cells) {
        if (cells == null || cells.isEmpty()) { return null; }
        Map<String, UUtranCellFDDMoc> map = new TreeMap<>();
        for (UUtranCellFDDMoc cell : cells) {
            map.put(cell.getUserLabel(), cell);
        }
        return map;
    }

    static public Map<String, ULocalCellMoc> cellSDRtoMap(List<ULocalCellMoc> cells) {
        if (cells == null || cells.isEmpty()) { return null; }
        Map<String, ULocalCellMoc> map = new TreeMap<>();
        for (ULocalCellMoc cell : cells) {
            map.put(cell.getUserLabel(), cell);
        }
        return map;
    }

    static public Map<String, bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ULocalCellMoc> cellITBBUtoMap(List<bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ULocalCellMoc> cells) {
        if (cells == null || cells.isEmpty()) { return null; }
        Map<String, bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ULocalCellMoc> map = new TreeMap<>();
        for (bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ULocalCellMoc cell : cells) {
            map.put(cell.getUserLabel(), cell);
        }
        return map;
    }

    static public List<InfoCellUMTS> toEntityForSDR(Map<String, UUtranCellFDDMoc> rnc, Map<String, ULocalCellMoc> sdr) {
        if (rnc == null || rnc.isEmpty()) { return null; }
        List<InfoCellUMTS> list = new ArrayList<>();
        for (Map.Entry<String, UUtranCellFDDMoc> cell: rnc.entrySet()) {
            boolean sdrExist = !(sdr == null || sdr.isEmpty());
            list.add(new InfoCellUMTS(
                    cell.getKey(),
                    cell.getValue().getMoId(),
                    (sdrExist && sdr.containsKey(cell.getKey())) ? sdr.get(cell.getKey()).getLocalCellId() : null,
                    BigDecimal.valueOf(Math.pow(10.0, (cell.getValue().getMaximumTransmissionPower() / 10.0)) / 1000).setScale(1, RoundingMode.HALF_UP).doubleValue(),
                    (sdrExist && sdr.containsKey(cell.getKey())) ? sdr.get(cell.getKey()).getMaxDlPwr() : null,
                    cell.getValue().getCellRadius(),
                    (sdrExist && sdr.containsKey(cell.getKey())) ? sdr.get(cell.getKey()).getCellRadius() : null));
        }
        return list;
    }

    static public List<InfoCellUMTS> toEntityForITBBU(Map<String, UUtranCellFDDMoc> rnc, Map<String, bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ULocalCellMoc> sdr) {
        if (rnc == null || rnc.isEmpty()) { return null; }
        List<InfoCellUMTS> list = new ArrayList<>();
        for (Map.Entry<String, UUtranCellFDDMoc> cell: rnc.entrySet()) {
            boolean sdrExist = !(sdr == null || sdr.isEmpty());
            list.add(new InfoCellUMTS(
                    cell.getKey(),
                    cell.getValue().getMoId(),
                    (sdrExist && sdr.containsKey(cell.getKey())) ? sdr.get(cell.getKey()).getLocalCellId() : null,
                    BigDecimal.valueOf(Math.pow(10.0, (cell.getValue().getMaximumTransmissionPower() / 10.0)) / 1000).setScale(1, RoundingMode.HALF_UP).doubleValue(),
                    (sdrExist && sdr.containsKey(cell.getKey())) ? sdr.get(cell.getKey()).getMaxDlPwr() : null,
                    cell.getValue().getCellRadius(),
                    (sdrExist && sdr.containsKey(cell.getKey())) ? sdr.get(cell.getKey()).getCellRadius() : null));
        }
        return list;
    }
}
