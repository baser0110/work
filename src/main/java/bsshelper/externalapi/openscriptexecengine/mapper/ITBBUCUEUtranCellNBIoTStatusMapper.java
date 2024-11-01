package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUCUEUtranCellNBIoTMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.EUtranCellNBIoTMocSimplified;
import bsshelper.externalapi.configurationmng.plannedserv.util.eutrancellnbiotemuns.AdminState;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.OperState;
import bsshelper.externalapi.openscriptexecengine.entity.EUtranCellNBIoTStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ITBBUCUEUtranCellNBIoTStatusMapper {
    static public EUtranCellNBIoTStatus toEUtranCellNBIoTStatusEntity(ITBBUCUEUtranCellNBIoTMocSimplified itbbucueUtranCellNBIoTMocSimplified) {
        if (itbbucueUtranCellNBIoTMocSimplified == null) {return null;}
        return new EUtranCellNBIoTStatus(
                false,
                itbbucueUtranCellNBIoTMocSimplified.getUserLabel(),
                itbbucueUtranCellNBIoTMocSimplified.getLdn(),
                AdminState.getTextAdminState(itbbucueUtranCellNBIoTMocSimplified.getAdminState()),
                OperState.getTextOperState(itbbucueUtranCellNBIoTMocSimplified.getOperState()));
    }

    static public List<EUtranCellNBIoTStatus> toEUtranCellNBIoTStatusEntity(List<ITBBUCUEUtranCellNBIoTMocSimplified> itbbucueUtranCellNBIoTMocSimplifiedList) {
        if (itbbucueUtranCellNBIoTMocSimplifiedList == null) return null;
        List<EUtranCellNBIoTStatus> result = new ArrayList<>();
        for (ITBBUCUEUtranCellNBIoTMocSimplified itbbucueUtranCellNBIoTMocSimplified : itbbucueUtranCellNBIoTMocSimplifiedList) {
            result.add(toEUtranCellNBIoTStatusEntity(itbbucueUtranCellNBIoTMocSimplified));
        }
        result.sort(Comparator.comparing(EUtranCellNBIoTStatus::getUserLabel));
        return result;
    }
}
