package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.UCellMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.ULocalCellMoc;
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
public class ULocalCellStatusMapper {
    static public ULocalCellStatus toULocalCellStatusEntity(ULocalCellMoc uLocalCellMoc, UCellMoc uCellMoc) {
        if (uLocalCellMoc == null) {return null;}
        return new ULocalCellStatus(
                false,
                uLocalCellMoc.getUserLabel(),
                uLocalCellMoc.getLdn(),
                uCellMoc != null,
                uCellMoc != null ? AdminStateSDR.getTextAdminState(uCellMoc.getAdminState()) : AdminStateSDR.getTextAdminState(uLocalCellMoc.getAdminState()),
                uCellMoc != null ? OperState.getTextOperState(uCellMoc.getOperState()) : OperState.getTextOperState(uLocalCellMoc.getOperState()),
                uCellMoc != null ? String.valueOf(uCellMoc.getAvailStatus()) : String.valueOf(uLocalCellMoc.getAvailStatus()));
    }

    static public List<ULocalCellStatus> toULocalCellStatusEntity(List<ULocalCellMoc> uLocalCellMocList, Map<Integer, UCellMoc> uCellMocSimplifiedMap) {
        if (uLocalCellMocList == null) return null;
        List<ULocalCellStatus> result = new ArrayList<>();
        for (ULocalCellMoc uLocalCellMoc : uLocalCellMocList) {
            result.add(toULocalCellStatusEntity(uLocalCellMoc, uCellMocSimplifiedMap != null ? uCellMocSimplifiedMap.get(uLocalCellMoc.getLocalCellId()) : null));
        }
        result.sort(Comparator.comparing(ULocalCellStatus::getUserLabel));
        return result;
    }
}
