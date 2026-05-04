package bsshelper.externalapi.openscriptexecengine.mapper;


import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.CUEUtranCellFDDLTEMoc;
import bsshelper.externalapi.configurationmng.plannedserv.util.eutrancellnbiotemuns.AdminState;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.OperState;
import bsshelper.externalapi.openscriptexecengine.entity.EUtranCellFDDStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ITBBUCUEUtranCellFDDLTEStatusMapper {
    static public EUtranCellFDDStatus toEUtranCellFDDStatusEntity(CUEUtranCellFDDLTEMoc CUEUtranCellFDDLTEMoc) {
        if (CUEUtranCellFDDLTEMoc == null) {return null;}
        return new EUtranCellFDDStatus(
                false,
                CUEUtranCellFDDLTEMoc.getUserLabel(),
                CUEUtranCellFDDLTEMoc.getLdn(),
                AdminState.getTextAdminState(CUEUtranCellFDDLTEMoc.getAdminState()),
                OperState.getTextOperState(CUEUtranCellFDDLTEMoc.getOperState()));
    }

    static public List<EUtranCellFDDStatus> toEUtranCellFDDStatusEntity(List<CUEUtranCellFDDLTEMoc> CUEUtranCellFDDLTEMocList) {
        if (CUEUtranCellFDDLTEMocList == null) return null;
        List<EUtranCellFDDStatus> result = new ArrayList<>();
        for (CUEUtranCellFDDLTEMoc CUEUtranCellFDDLTEMoc : CUEUtranCellFDDLTEMocList) {
            result.add(toEUtranCellFDDStatusEntity(CUEUtranCellFDDLTEMoc));
        }
        result.sort(Comparator.comparing(EUtranCellFDDStatus::getUserLabel));
        return result;
    }
}
