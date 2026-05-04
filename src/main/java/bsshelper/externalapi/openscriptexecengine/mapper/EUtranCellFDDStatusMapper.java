package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.EUtranCellFDDMoc;
import bsshelper.externalapi.configurationmng.plannedserv.util.eutrancellnbiotemuns.AdminState;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.OperState;
import bsshelper.externalapi.openscriptexecengine.entity.EUtranCellFDDStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EUtranCellFDDStatusMapper {
    static public EUtranCellFDDStatus toEUtranCellFDDStatusEntity(EUtranCellFDDMoc eUtranCellFDDMoc) {
        if (eUtranCellFDDMoc == null) {return null;}
        return new EUtranCellFDDStatus(
                false,
                eUtranCellFDDMoc.getUserLabel(),
                eUtranCellFDDMoc.getLdn(),
                AdminState.getTextAdminState(eUtranCellFDDMoc.getAdminState()),
                OperState.getTextOperState(eUtranCellFDDMoc.getOperState()));
    }

    static public List<EUtranCellFDDStatus> toEUtranCellFDDStatusEntity(List<EUtranCellFDDMoc> eUtranCellFDDMocList) {
        if (eUtranCellFDDMocList == null) return null;
        List<EUtranCellFDDStatus> result = new ArrayList<>();
        for (EUtranCellFDDMoc eUtranCellFDDMoc : eUtranCellFDDMocList) {
            result.add(toEUtranCellFDDStatusEntity(eUtranCellFDDMoc));
        }
        result.sort(Comparator.comparing(EUtranCellFDDStatus::getUserLabel));
        return result;
    }
}
