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

public class GCellStatusMapper {
    static public GCellStatus toGCellStatusEntity(GGsmCellMocSimplified gGsmCellMocSimplified) {
        if (gGsmCellMocSimplified == null) {return null;}
        return new GCellStatus(
                false,
                gGsmCellMocSimplified.getUserLabel(),
                gGsmCellMocSimplified.getLdn(),
                "No information");
    }

    static public List<GCellStatus> toGCellStatusEntity(List<GGsmCellMocSimplified> gGsmCellMocSimplifiedList) {
        if (gGsmCellMocSimplifiedList == null) return null;
        List<GCellStatus> result = new ArrayList<>();
        for (GGsmCellMocSimplified gGsmCellMocSimplified : gGsmCellMocSimplifiedList) {
            result.add(toGCellStatusEntity(gGsmCellMocSimplified));
        }
        result.sort(Comparator.comparing(GCellStatus::getUserLabel));
        return result;
    }
}
