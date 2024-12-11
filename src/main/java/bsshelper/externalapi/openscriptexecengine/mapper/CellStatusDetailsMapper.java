package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.UCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.ULocalCellMocSimplified;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.AdminStateSDR;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.AvailStatus;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.OperState;
import bsshelper.externalapi.openscriptexecengine.entity.CellStatusDetails;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CellStatusDetailsMapper {
    static public CellStatusDetails toCellStatusDetailsSDR(ULocalCellMocSimplified uLocalCellMocSimplified, UCellMocSimplified uCellMocSimplified) {
        if (uLocalCellMocSimplified == null) {return null;}
        return new CellStatusDetails(
                uLocalCellMocSimplified.getUserLabel(),
                uLocalCellMocSimplified.getLocalCellId(),
                AdminStateSDR.getTextAdminState(uLocalCellMocSimplified.getAdminState()),
                OperState.getTextOperState(uLocalCellMocSimplified.getOperState()),
                AvailStatus.getTextAvailStatus(uLocalCellMocSimplified.getAvailStatus()),
                uCellMocSimplified != null ? OperState.getTextOperState(uCellMocSimplified.getOperState()) : "-",
                uCellMocSimplified != null ? AvailStatus.getTextAvailStatus(uCellMocSimplified.getAvailStatus()) : "-"
        );
    }

    static public List<CellStatusDetails> toULocalCellStatusEntitySDR(List<ULocalCellMocSimplified> uLocalCellMocSimplifiedList, Map<Integer,UCellMocSimplified> uCellMocSimplifiedMap) {
        if (uLocalCellMocSimplifiedList == null) return null;
        List<CellStatusDetails> result = new ArrayList<>();
        for (ULocalCellMocSimplified uLocalCellMocSimplified : uLocalCellMocSimplifiedList) {
            result.add(toCellStatusDetailsSDR(uLocalCellMocSimplified, uCellMocSimplifiedMap != null ? uCellMocSimplifiedMap.get(uLocalCellMocSimplified.getLocalCellId()) : null));
        }
        result.sort(Comparator.comparing(CellStatusDetails::getUserLabel));
        return result;
    }

    static public CellStatusDetails toCellStatusDetailsITBBU(ITBBUULocalCellMocSimplified iTBBUULocalCellMocSimplified, UCellMocSimplified uCellMocSimplified) {
        if (iTBBUULocalCellMocSimplified == null) {return null;}
        return new CellStatusDetails(
                iTBBUULocalCellMocSimplified.getUserLabel(),
                iTBBUULocalCellMocSimplified.getLocalCellId(),
                AdminStateSDR.getTextAdminState(iTBBUULocalCellMocSimplified.getAdminState()),
                OperState.getTextOperState(iTBBUULocalCellMocSimplified.getOperState()),
                iTBBUULocalCellMocSimplified.getAvailStatus(),
                uCellMocSimplified != null ? OperState.getTextOperState(uCellMocSimplified.getOperState()) : "-",
                uCellMocSimplified != null ? uCellMocSimplified.getAvailStatus() : "-"
        );
    }

    static public List<CellStatusDetails> toULocalCellStatusEntityITBBU(List<ITBBUULocalCellMocSimplified> iTBBUULocalCellMocSimplifiedList, Map<Integer,UCellMocSimplified> uCellMocSimplifiedMap) {
        if (iTBBUULocalCellMocSimplifiedList == null) return null;
        List<CellStatusDetails> result = new ArrayList<>();
        for (ITBBUULocalCellMocSimplified iTBBUULocalCellMocSimplified : iTBBUULocalCellMocSimplifiedList) {
            result.add(toCellStatusDetailsITBBU(iTBBUULocalCellMocSimplified, uCellMocSimplifiedMap != null ? uCellMocSimplifiedMap.get(iTBBUULocalCellMocSimplified.getLocalCellId()) : null));
        }
        result.sort(Comparator.comparing(CellStatusDetails::getUserLabel));
        return result;
    }
}
