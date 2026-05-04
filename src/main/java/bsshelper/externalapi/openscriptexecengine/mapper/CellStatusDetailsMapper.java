package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.UCellMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.ULocalCellMoc;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.AdminStateSDR;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.AvailStatus;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.OperState;
import bsshelper.externalapi.openscriptexecengine.entity.CellStatusDetails;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class CellStatusDetailsMapper {
    static public CellStatusDetails toCellStatusDetailsSDR(ULocalCellMoc uLocalCellMoc, UCellMoc uCellMoc) {
        if (uLocalCellMoc == null) {return null;}
        return new CellStatusDetails(
                uLocalCellMoc.getUserLabel(),
                uLocalCellMoc.getLocalCellId(),
                AdminStateSDR.getTextAdminState(uLocalCellMoc.getAdminState()),
                OperState.getTextOperState(uLocalCellMoc.getOperState()),
                AvailStatus.getTextAvailStatus(uLocalCellMoc.getAvailStatus()),
                uCellMoc != null ? OperState.getTextOperState(uCellMoc.getOperState()) : "-",
                uCellMoc != null ? AvailStatus.getTextAvailStatus(uCellMoc.getAvailStatus()) : "-"
        );
    }

    static public List<CellStatusDetails> toULocalCellStatusEntitySDR(List<ULocalCellMoc> uLocalCellMocList, Map<Integer, UCellMoc> uCellMocSimplifiedMap) {
        if (uLocalCellMocList == null) return null;
        List<CellStatusDetails> result = new ArrayList<>();
        for (ULocalCellMoc uLocalCellMoc : uLocalCellMocList) {
            result.add(toCellStatusDetailsSDR(uLocalCellMoc, uCellMocSimplifiedMap != null ? uCellMocSimplifiedMap.get(uLocalCellMoc.getLocalCellId()) : null));
        }
        result.sort(Comparator.comparing(CellStatusDetails::getUserLabel));
        return result;
    }

    static public CellStatusDetails toCellStatusDetailsITBBU(bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ULocalCellMoc iTBBUULocalCellMoc, UCellMoc uCellMoc) {
        if (iTBBUULocalCellMoc == null) {return null;}
        return new CellStatusDetails(
                iTBBUULocalCellMoc.getUserLabel(),
                iTBBUULocalCellMoc.getLocalCellId(),
                AdminStateSDR.getTextAdminState(iTBBUULocalCellMoc.getAdminState()),
                OperState.getTextOperState(iTBBUULocalCellMoc.getOperState()),
                iTBBUULocalCellMoc.getAvailStatus(),
                uCellMoc != null ? OperState.getTextOperState(uCellMoc.getOperState()) : "-",
                uCellMoc != null ? uCellMoc.getAvailStatus() : "-"
        );
    }

    static public List<CellStatusDetails> toULocalCellStatusEntityITBBU(List<bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ULocalCellMoc> iTBBUULocalCellMocList, Map<Integer, UCellMoc> uCellMocSimplifiedMap) {
        if (iTBBUULocalCellMocList == null) return null;
        List<CellStatusDetails> result = new ArrayList<>();
        for (bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ULocalCellMoc iTBBUULocalCellMoc : iTBBUULocalCellMocList) {
            result.add(toCellStatusDetailsITBBU(iTBBUULocalCellMoc, uCellMocSimplifiedMap != null ? uCellMocSimplifiedMap.get(iTBBUULocalCellMoc.getLocalCellId()) : null));
        }
        result.sort(Comparator.comparing(CellStatusDetails::getUserLabel));
        return result;
    }
}
