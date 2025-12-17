package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.EUtranCellFDDMocSimplified;
import bsshelper.externalapi.configurationmng.plannedserv.util.eutrancellnbiotemuns.AdminState;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.OperState;
import bsshelper.externalapi.openscriptexecengine.entity.EUtranCellFDDStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EUtranCellFDDStatusMapper {
    static public EUtranCellFDDStatus toEUtranCellFDDStatusEntity(EUtranCellFDDMocSimplified eUtranCellFDDMocSimplified) {
        if (eUtranCellFDDMocSimplified == null) {return null;}
        return new EUtranCellFDDStatus(
                false,
                eUtranCellFDDMocSimplified.getUserLabel(),
                eUtranCellFDDMocSimplified.getLdn(),
                AdminState.getTextAdminState(eUtranCellFDDMocSimplified.getAdminState()),
                OperState.getTextOperState(eUtranCellFDDMocSimplified.getOperState()));
    }

    static public List<EUtranCellFDDStatus> toEUtranCellFDDStatusEntity(List<EUtranCellFDDMocSimplified> eUtranCellFDDMocSimplifiedList) {
        if (eUtranCellFDDMocSimplifiedList == null) return null;
        List<EUtranCellFDDStatus> result = new ArrayList<>();
        for (EUtranCellFDDMocSimplified eUtranCellFDDMocSimplified : eUtranCellFDDMocSimplifiedList) {
            result.add(toEUtranCellFDDStatusEntity(eUtranCellFDDMocSimplified));
        }
        result.sort(Comparator.comparing(EUtranCellFDDStatus::getUserLabel));
        return result;
    }
}
