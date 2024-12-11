package bsshelper.externalapi.openscriptexecengine.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.UCellMocSimplified;
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
import java.util.Map;

@Data
@RequiredArgsConstructor
public class ULocalCellStatusMapper {
    static public ULocalCellStatus toULocalCellStatusEntity(ULocalCellMocSimplified uLocalCellMocSimplified, UCellMocSimplified uCellMocSimplified) {
        if (uLocalCellMocSimplified == null) {return null;}
        return new ULocalCellStatus(
                false,
                uLocalCellMocSimplified.getUserLabel(),
                uLocalCellMocSimplified.getLdn(),
                uCellMocSimplified != null,
                uCellMocSimplified != null ? AdminStateSDR.getTextAdminState(uCellMocSimplified.getAdminState()) : AdminStateSDR.getTextAdminState(uLocalCellMocSimplified.getAdminState()),
                uCellMocSimplified != null ? OperState.getTextOperState(uCellMocSimplified.getOperState()) : OperState.getTextOperState(uLocalCellMocSimplified.getOperState()),
                uCellMocSimplified != null ? String.valueOf(uCellMocSimplified.getAvailStatus()) : String.valueOf(uLocalCellMocSimplified.getAvailStatus()));
    }

    static public List<ULocalCellStatus> toULocalCellStatusEntity(List<ULocalCellMocSimplified> uLocalCellMocSimplifiedList, Map<Integer,UCellMocSimplified> uCellMocSimplifiedMap) {
        if (uLocalCellMocSimplifiedList == null) return null;
        List<ULocalCellStatus> result = new ArrayList<>();
        for (ULocalCellMocSimplified uLocalCellMocSimplified : uLocalCellMocSimplifiedList) {
            result.add(toULocalCellStatusEntity(uLocalCellMocSimplified, uCellMocSimplifiedMap != null ? uCellMocSimplifiedMap.get(uLocalCellMocSimplified.getLocalCellId()) : null));
        }
        result.sort(Comparator.comparing(ULocalCellStatus::getUserLabel));
        return result;
    }
}
