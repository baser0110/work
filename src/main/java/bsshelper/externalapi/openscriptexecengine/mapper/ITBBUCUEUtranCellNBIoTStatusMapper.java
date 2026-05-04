package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.CUEUtranCellNBIoTMoc;
import bsshelper.externalapi.configurationmng.plannedserv.util.eutrancellnbiotemuns.AdminState;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.OperState;
import bsshelper.externalapi.openscriptexecengine.entity.EUtranCellNBIoTStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ITBBUCUEUtranCellNBIoTStatusMapper {
    static public EUtranCellNBIoTStatus toEUtranCellNBIoTStatusEntity(CUEUtranCellNBIoTMoc CUEUtranCellNBIoTMoc) {
        if (CUEUtranCellNBIoTMoc == null) {return null;}
        return new EUtranCellNBIoTStatus(
                false,
                CUEUtranCellNBIoTMoc.getUserLabel(),
                CUEUtranCellNBIoTMoc.getLdn(),
                AdminState.getTextAdminState(CUEUtranCellNBIoTMoc.getAdminState()),
                OperState.getTextOperState(CUEUtranCellNBIoTMoc.getOperState()));
    }

    static public List<EUtranCellNBIoTStatus> toEUtranCellNBIoTStatusEntity(List<CUEUtranCellNBIoTMoc> CUEUtranCellNBIoTMocList) {
        if (CUEUtranCellNBIoTMocList == null) return null;
        List<EUtranCellNBIoTStatus> result = new ArrayList<>();
        for (CUEUtranCellNBIoTMoc CUEUtranCellNBIoTMoc : CUEUtranCellNBIoTMocList) {
            result.add(toEUtranCellNBIoTStatusEntity(CUEUtranCellNBIoTMoc));
        }
        result.sort(Comparator.comparing(EUtranCellNBIoTStatus::getUserLabel));
        return result;
    }
}
