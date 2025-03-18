package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GGsmCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.EUtranCellNBIoTMocSimplified;
import bsshelper.externalapi.configurationmng.plannedserv.util.eutrancellnbiotemuns.AdminState;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.OperState;
import bsshelper.externalapi.openscriptexecengine.entity.EUtranCellNBIoTStatus;
import bsshelper.externalapi.openscriptexecengine.entity.GCellStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class GCellStatusMapper {
    static public GCellStatus toGCellStatusEntity(GGsmCellMocSimplified gGsmCellMocSimplified, Set<String> hasAlarmCells) {
        if (gGsmCellMocSimplified == null) {return null;}
        return new GCellStatus(
                false,
                gGsmCellMocSimplified.getUserLabel(),
                gGsmCellMocSimplified.getLdn(),
                hasAlarmCells.contains(gGsmCellMocSimplified.getUserLabel()) ? "Cell interruption alarm" : "No alarm");
    }

    static public List<GCellStatus> toGCellStatusEntity(List<GGsmCellMocSimplified> gGsmCellMocSimplifiedList, Set<String> hasAlarmCells) {
        if (gGsmCellMocSimplifiedList == null) return null;
        List<GCellStatus> result = new ArrayList<>();
        for (GGsmCellMocSimplified gGsmCellMocSimplified : gGsmCellMocSimplifiedList) {
            result.add(toGCellStatusEntity(gGsmCellMocSimplified, hasAlarmCells));
        }
        result.sort(Comparator.comparing(GCellStatus::getUserLabel));
        return result;
    }
}
