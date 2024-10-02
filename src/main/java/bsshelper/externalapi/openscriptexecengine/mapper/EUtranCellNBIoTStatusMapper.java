package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.EUtranCellNBIoTMoc;
import bsshelper.externalapi.configurationmng.plannedserv.util.eutrancellnbiotemuns.AdminState;
import bsshelper.externalapi.openscriptexecengine.entity.EUtranCellNBIoTStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EUtranCellNBIoTStatusMapper {
    static public EUtranCellNBIoTStatus toEUtranCellNBIoTStatusEntity(EUtranCellNBIoTMoc EUtranCellNBIoTMoc) {
        if (EUtranCellNBIoTMoc == null) {return null;}
        return new EUtranCellNBIoTStatus(
                false,
                EUtranCellNBIoTMoc.getUserLabel(),
                EUtranCellNBIoTMoc.getLdn(),
                AdminState.getTextAdminState(EUtranCellNBIoTMoc.getAdminState()));
    }

    static public List<EUtranCellNBIoTStatus> toEUtranCellNBIoTStatusEntity(List<EUtranCellNBIoTMoc> EUtranCellNBIoTMocList) {
        if (EUtranCellNBIoTMocList == null) return null;
        List<EUtranCellNBIoTStatus> result = new ArrayList<>();
        for (EUtranCellNBIoTMoc EUtranCellNBIoTMoc : EUtranCellNBIoTMocList) {
            result.add(toEUtranCellNBIoTStatusEntity(EUtranCellNBIoTMoc));
        }
        result.sort(Comparator.comparing(EUtranCellNBIoTStatus::getUserLabel));
        return result;
    }
}
