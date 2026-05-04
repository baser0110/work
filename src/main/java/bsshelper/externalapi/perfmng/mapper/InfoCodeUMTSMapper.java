package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMoc;
import bsshelper.externalapi.perfmng.entity.InfoCodeUMTS;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class InfoCodeUMTSMapper {
    static public InfoCodeUMTS toFinalEntity(UUtranCellFDDMoc cell) {
        if (cell == null) {return null;}
        return new InfoCodeUMTS(
                cell.getPrimaryScramblingCode(),
                cell.getUserLabel());
    }

    static public List<InfoCodeUMTS> toFinalEntity(List<UUtranCellFDDMoc> list) {
        if (list == null) return null;

        list.sort(Comparator.comparing(UUtranCellFDDMoc::getUserLabel));
        HashSet<Integer> codes = new HashSet<>();
        List<InfoCodeUMTS> result = new ArrayList<>();
        for (UUtranCellFDDMoc cell : list) {
            if (!codes.contains(cell.getPrimaryScramblingCode())) {
                result.add(toFinalEntity(cell));
                codes.add(cell.getPrimaryScramblingCode());
            }
        }

        return result;
    }
}
