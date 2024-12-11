package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.UCellMocSimplified;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.AdminStateITBBU;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.AdminStateSDR;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.OperState;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.SmoothlyBlock;
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
    static public ULocalCellStatus toULocalCellStatusEntity(ITBBUULocalCellMocSimplified iTBBUULocalCellMocSimplified, UCellMocSimplified uCellMocSimplified) {
        if (iTBBUULocalCellMocSimplified == null) {return null;}
        return new ULocalCellStatus(
                false,
                iTBBUULocalCellMocSimplified.getUserLabel(),
                iTBBUULocalCellMocSimplified.getLdn(),
                uCellMocSimplified != null,
                uCellMocSimplified != null ? AdminStateSDR.getTextAdminState(uCellMocSimplified.getAdminState()) : AdminStateITBBU.getTextAdminState(iTBBUULocalCellMocSimplified.getAdminState()),
                uCellMocSimplified != null ? OperState.getTextOperState(uCellMocSimplified.getOperState()) : OperState.getTextOperState(iTBBUULocalCellMocSimplified.getOperState()),
                uCellMocSimplified != null ? String.valueOf(uCellMocSimplified.getAvailStatus()) : String.valueOf(iTBBUULocalCellMocSimplified.getAvailStatus()));
    }

    static public List<ULocalCellStatus> toULocalCellStatusEntity(List<ITBBUULocalCellMocSimplified> iTBBUULocalCellMocSimplifiedList, Map<Integer,UCellMocSimplified> uCellMocSimplifiedMap) {
        if (iTBBUULocalCellMocSimplifiedList == null) return null;
        List<ULocalCellStatus> result = new ArrayList<>();
        for (ITBBUULocalCellMocSimplified iTBBUULocalCellMoc : iTBBUULocalCellMocSimplifiedList) {
            result.add(toULocalCellStatusEntity(iTBBUULocalCellMoc, uCellMocSimplifiedMap != null ? uCellMocSimplifiedMap.get(iTBBUULocalCellMoc.getLocalCellId()) : null));
        }
        result.sort(Comparator.comparing(ULocalCellStatus::getUserLabel));
        return result;
    }
}
