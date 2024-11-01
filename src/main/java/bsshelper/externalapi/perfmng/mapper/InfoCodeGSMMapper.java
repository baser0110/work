package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GGsmCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.perfmng.entity.InfoCodeGSM;
import bsshelper.externalapi.perfmng.entity.InfoCodeUMTS;

import java.util.ArrayList;
import java.util.List;

public class InfoCodeGSMMapper {
    static public InfoCodeGSM toFinalEntity(GGsmCellMocSimplified cell) {
        if (cell == null) {return null;}
        return new InfoCodeGSM(
                cell.getBcchFrequency(),
                cell.getUserLabel());
    }

    static public List<InfoCodeGSM> toFinalEntity(List<GGsmCellMocSimplified> list) {
        if (list == null) return null;
        int currentCode = -1;
        List<InfoCodeGSM> result = new ArrayList<>();
        for (GGsmCellMocSimplified cell : list) {
            result.add(toFinalEntity(cell));
        }
//        result.sort(Comparator.comparing(InfoCodeUMTS::getSector));
        return result;
    }
}
