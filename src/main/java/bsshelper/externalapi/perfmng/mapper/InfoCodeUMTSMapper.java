package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.perfmng.entity.InfoCodeUMTS;


import java.util.ArrayList;
import java.util.List;

public class InfoCodeUMTSMapper {
    static public InfoCodeUMTS toFinalEntity(UUtranCellFDDMocSimplified cell) {
        if (cell == null) {return null;}
        return new InfoCodeUMTS(
                cell.getPrimaryScramblingCode(),
                cell.getUserLabel());
    }

    static public List<InfoCodeUMTS> toFinalEntity(List<UUtranCellFDDMocSimplified> list) {
        if (list == null) return null;
        int currentCode = -1;
        List<InfoCodeUMTS> result = new ArrayList<>();
        for (UUtranCellFDDMocSimplified cell : list) {
            if (cell.getPrimaryScramblingCode() == currentCode) { continue; }
            currentCode = cell.getPrimaryScramblingCode();
            result.add(toFinalEntity(cell));
        }
//        result.sort(Comparator.comparing(InfoCodeUMTS::getSector));
        return result;
    }
}
