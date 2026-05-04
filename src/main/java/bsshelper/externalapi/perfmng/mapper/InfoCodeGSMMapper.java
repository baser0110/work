package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GGsmCellMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMoc;
import bsshelper.externalapi.perfmng.entity.InfoCodeGSM;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InfoCodeGSMMapper {
    static public InfoCodeGSM toFinalEntity(GGsmCellMoc cell) {
        if (cell == null) {return null;}
        return new InfoCodeGSM(
                cell.getBcchFrequency(),
                cell.getUserLabel());
    }

    static public List<InfoCodeGSM> toFinalEntity(List<GGsmCellMoc> list) {
        if (list == null) return null;

        list.sort(Comparator.comparing(GGsmCellMoc::getUserLabel));
        List<InfoCodeGSM> result = new ArrayList<>();
        for (GGsmCellMoc cell : list) {
            result.add(toFinalEntity(cell));
        }
        return result;
    }
}
