package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMocSimplified;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.AdminStateITBBU;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.OperState;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.SmoothlyBlock;
import bsshelper.externalapi.openscriptexecengine.entity.ULocalCellStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ITBBUULocalCellStatusMapper {
    static public ULocalCellStatus toULocalCellStatusEntity(ITBBUULocalCellMocSimplified iTBBUULocalCellMocSimplified) {
        if (iTBBUULocalCellMocSimplified == null) {return null;}
        return new ULocalCellStatus(
                false,
                iTBBUULocalCellMocSimplified.getUserLabel(),
                iTBBUULocalCellMocSimplified.getLdn(),
                AdminStateITBBU.getTextAdminState(iTBBUULocalCellMocSimplified.getAdminState()) + " / "
                        + SmoothlyBlock.getTextSmoothlyBlock(iTBBUULocalCellMocSimplified.getSmoothlyBlock()),
                OperState.getTextOperState(iTBBUULocalCellMocSimplified.getOperState()));

    }

    static public List<ULocalCellStatus> toULocalCellStatusEntity(List<ITBBUULocalCellMocSimplified> iTBBUULocalCellMocSimplifiedList) {
        if (iTBBUULocalCellMocSimplifiedList == null) return null;
        List<ULocalCellStatus> result = new ArrayList<>();
        for (ITBBUULocalCellMocSimplified iTBBUULocalCellMoc : iTBBUULocalCellMocSimplifiedList) {
            result.add(toULocalCellStatusEntity(iTBBUULocalCellMoc));
        }
        result.sort(Comparator.comparing(ULocalCellStatus::getUserLabel));
        return result;
    }
}
