package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.ULocalCellMoc;
import bsshelper.externalapi.configurationmng.plannedserv.util.ulocalcellenums.AdminStateSDR;
import bsshelper.externalapi.openscriptexecengine.entity.ULocalCellStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ULocalCellStatusMapper {
    static public ULocalCellStatus toULocalCellStatusEntity(ULocalCellMoc ULocalCellMoc) {
        if (ULocalCellMoc == null) {return null;}
        return new ULocalCellStatus(
                false,
                ULocalCellMoc.getUserLabel(),
                ULocalCellMoc.getLdn(),
                AdminStateSDR.getTextAdminState(ULocalCellMoc.getAdminState()));
    }

    static public List<ULocalCellStatus> toULocalCellStatusEntity(List<ULocalCellMoc> ULocalCellMocList) {
        if (ULocalCellMocList == null) return null;
        List<ULocalCellStatus> result = new ArrayList<>();
        for (ULocalCellMoc ULocalCellMoc : ULocalCellMocList) {
            result.add(toULocalCellStatusEntity(ULocalCellMoc));
        }
        result.sort(Comparator.comparing(ULocalCellStatus::getUserLabel));
        return result;
    }
}
