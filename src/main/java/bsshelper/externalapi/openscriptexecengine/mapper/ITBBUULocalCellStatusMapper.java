package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ULocalCellMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.UCellMoc;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.AdminStateITBBU;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.AdminStateSDR;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.OperState;
import bsshelper.externalapi.openscriptexecengine.entity.ULocalCellStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class ITBBUULocalCellStatusMapper {
    static public ULocalCellStatus toULocalCellStatusEntity(ULocalCellMoc iTBBUULocalCellMoc, UCellMoc uCellMoc) {
        if (iTBBUULocalCellMoc == null) {return null;}
        return new ULocalCellStatus(
                false,
                iTBBUULocalCellMoc.getUserLabel(),
                iTBBUULocalCellMoc.getLdn(),
                uCellMoc != null,
                uCellMoc != null ? AdminStateSDR.getTextAdminState(uCellMoc.getAdminState()) : AdminStateITBBU.getTextAdminState(iTBBUULocalCellMoc.getAdminState()),
                uCellMoc != null ? OperState.getTextOperState(uCellMoc.getOperState()) : OperState.getTextOperState(iTBBUULocalCellMoc.getOperState()),
                uCellMoc != null ? String.valueOf(uCellMoc.getAvailStatus()) : String.valueOf(iTBBUULocalCellMoc.getAvailStatus()));
    }

    static public List<ULocalCellStatus> toULocalCellStatusEntity(List<ULocalCellMoc> iTBBUULocalCellMocList, Map<Integer, UCellMoc> uCellMocSimplifiedMap) {
        if (iTBBUULocalCellMocList == null) return null;
        List<ULocalCellStatus> result = new ArrayList<>();
        for (ULocalCellMoc iTBBUULocalCellMoc : iTBBUULocalCellMocList) {
            result.add(toULocalCellStatusEntity(iTBBUULocalCellMoc, uCellMocSimplifiedMap != null ? uCellMocSimplifiedMap.get(iTBBUULocalCellMoc.getLocalCellId()) : null));
        }
        result.sort(Comparator.comparing(ULocalCellStatus::getUserLabel));
        return result;
    }
}
