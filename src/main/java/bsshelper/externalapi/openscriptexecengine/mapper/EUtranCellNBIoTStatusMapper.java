package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.EUtranCellNBIoTMoc;
import bsshelper.externalapi.configurationmng.plannedserv.util.eutrancellnbiotemuns.AdminState;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.OperState;
import bsshelper.externalapi.openscriptexecengine.entity.EUtranCellNBIoTStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EUtranCellNBIoTStatusMapper {
    static public EUtranCellNBIoTStatus toEUtranCellNBIoTStatusEntity(EUtranCellNBIoTMoc eutranCellNBIoTMoc) {
        if (eutranCellNBIoTMoc == null) {return null;}
        return new EUtranCellNBIoTStatus(
                false,
                eutranCellNBIoTMoc.getUserLabel(),
                eutranCellNBIoTMoc.getLdn(),
                AdminState.getTextAdminState(eutranCellNBIoTMoc.getAdminState()),
                OperState.getTextOperState(eutranCellNBIoTMoc.getOperState()));
    }

    static public List<EUtranCellNBIoTStatus> toEUtranCellNBIoTStatusEntity(List<EUtranCellNBIoTMoc> eutranCellNBIoTMocList) {
        if (eutranCellNBIoTMocList == null) return null;
        List<EUtranCellNBIoTStatus> result = new ArrayList<>();
        for (EUtranCellNBIoTMoc eutranCellNBIoTMoc : eutranCellNBIoTMocList) {
            result.add(toEUtranCellNBIoTStatusEntity(eutranCellNBIoTMoc));
        }
        result.sort(Comparator.comparing(EUtranCellNBIoTStatus::getUserLabel));
        return result;
    }
}
