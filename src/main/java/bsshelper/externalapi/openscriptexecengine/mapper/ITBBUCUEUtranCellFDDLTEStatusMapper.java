package bsshelper.externalapi.openscriptexecengine.mapper;


import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUCUEUtranCellFDDLTEMocSimplified;
import bsshelper.externalapi.configurationmng.plannedserv.util.eutrancellnbiotemuns.AdminState;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.OperState;
import bsshelper.externalapi.openscriptexecengine.entity.EUtranCellFDDStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ITBBUCUEUtranCellFDDLTEStatusMapper {
    static public EUtranCellFDDStatus toEUtranCellFDDStatusEntity(ITBBUCUEUtranCellFDDLTEMocSimplified itbbucueUtranCellFDDLTEMocSimplified) {
        if (itbbucueUtranCellFDDLTEMocSimplified == null) {return null;}
        return new EUtranCellFDDStatus(
                false,
                itbbucueUtranCellFDDLTEMocSimplified.getUserLabel(),
                itbbucueUtranCellFDDLTEMocSimplified.getLdn(),
                AdminState.getTextAdminState(itbbucueUtranCellFDDLTEMocSimplified.getAdminState()),
                OperState.getTextOperState(itbbucueUtranCellFDDLTEMocSimplified.getOperState()));
    }

    static public List<EUtranCellFDDStatus> toEUtranCellFDDStatusEntity(List<ITBBUCUEUtranCellFDDLTEMocSimplified> itbbucueUtranCellFDDLTEMocSimplifiedList) {
        if (itbbucueUtranCellFDDLTEMocSimplifiedList == null) return null;
        List<EUtranCellFDDStatus> result = new ArrayList<>();
        for (ITBBUCUEUtranCellFDDLTEMocSimplified itbbucueUtranCellFDDLTEMocSimplified : itbbucueUtranCellFDDLTEMocSimplifiedList) {
            result.add(toEUtranCellFDDStatusEntity(itbbucueUtranCellFDDLTEMocSimplified));
        }
        result.sort(Comparator.comparing(EUtranCellFDDStatus::getUserLabel));
        return result;
    }
}
