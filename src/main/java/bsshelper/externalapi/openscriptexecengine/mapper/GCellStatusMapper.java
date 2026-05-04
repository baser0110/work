package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GGsmCellMoc;
import bsshelper.externalapi.openscriptexecengine.entity.GCellStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class GCellStatusMapper {
    static public GCellStatus toGCellStatusEntity(GGsmCellMoc gGsmCellMoc, Set<String> hasAlarmCells) {
        if (gGsmCellMoc == null) {return null;}
        return new GCellStatus(
                false,
                gGsmCellMoc.getUserLabel(),
                gGsmCellMoc.getLdn(),
                hasAlarmCells.contains(gGsmCellMoc.getUserLabel()) ? "Cell interruption alarm" : "No alarm");
    }

    static public List<GCellStatus> toGCellStatusEntity(List<GGsmCellMoc> gGsmCellMocList, Set<String> hasAlarmCells) {
        if (gGsmCellMocList == null) return null;
        List<GCellStatus> result = new ArrayList<>();
        for (GGsmCellMoc gGsmCellMoc : gGsmCellMocList) {
            result.add(toGCellStatusEntity(gGsmCellMoc, hasAlarmCells));
        }
        result.sort(Comparator.comparing(GCellStatus::getUserLabel));
        return result;
    }
}
