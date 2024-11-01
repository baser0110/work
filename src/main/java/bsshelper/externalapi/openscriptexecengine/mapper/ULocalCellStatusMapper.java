package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.ULocalCellMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.ULocalCellMocSimplified;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.AdminStateSDR;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.OperState;
import bsshelper.externalapi.openscriptexecengine.entity.ULocalCellStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ULocalCellStatusMapper {
    static public ULocalCellStatus toULocalCellStatusEntity(ULocalCellMocSimplified uLocalCellMocSimplified) {
        if (uLocalCellMocSimplified == null) {return null;}
        return new ULocalCellStatus(
                false,
                uLocalCellMocSimplified.getUserLabel(),
                uLocalCellMocSimplified.getLdn(),
                AdminStateSDR.getTextAdminState(uLocalCellMocSimplified.getAdminState()),
                OperState.getTextOperState(uLocalCellMocSimplified.getOperState()));
    }

    static public List<ULocalCellStatus> toULocalCellStatusEntity(List<ULocalCellMocSimplified> uLocalCellMocSimplifiedList) {
        if (uLocalCellMocSimplifiedList == null) return null;
        List<ULocalCellStatus> result = new ArrayList<>();
        for (ULocalCellMocSimplified uLocalCellMocSimplified : uLocalCellMocSimplifiedList) {
            result.add(toULocalCellStatusEntity(uLocalCellMocSimplified));
        }
        result.sort(Comparator.comparing(ULocalCellStatus::getUserLabel));
        return result;
    }
}
