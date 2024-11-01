package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.EUtranCellNBIoTMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.EUtranCellNBIoTMocSimplified;
import bsshelper.externalapi.configurationmng.plannedserv.util.eutrancellnbiotemuns.AdminState;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.OperState;
import bsshelper.externalapi.openscriptexecengine.entity.EUtranCellNBIoTStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EUtranCellNBIoTStatusMapper {
    static public EUtranCellNBIoTStatus toEUtranCellNBIoTStatusEntity(EUtranCellNBIoTMocSimplified eutranCellNBIoTMocSimplified) {
        if (eutranCellNBIoTMocSimplified == null) {return null;}
        return new EUtranCellNBIoTStatus(
                false,
                eutranCellNBIoTMocSimplified.getUserLabel(),
                eutranCellNBIoTMocSimplified.getLdn(),
                AdminState.getTextAdminState(eutranCellNBIoTMocSimplified.getAdminState()),
                OperState.getTextOperState(eutranCellNBIoTMocSimplified.getOperState()));
    }

    static public List<EUtranCellNBIoTStatus> toEUtranCellNBIoTStatusEntity(List<EUtranCellNBIoTMocSimplified> eutranCellNBIoTMocSimplifiedList) {
        if (eutranCellNBIoTMocSimplifiedList == null) return null;
        List<EUtranCellNBIoTStatus> result = new ArrayList<>();
        for (EUtranCellNBIoTMocSimplified eutranCellNBIoTMocSimplified : eutranCellNBIoTMocSimplifiedList) {
            result.add(toEUtranCellNBIoTStatusEntity(eutranCellNBIoTMocSimplified));
        }
        result.sort(Comparator.comparing(EUtranCellNBIoTStatus::getUserLabel));
        return result;
    }
}
