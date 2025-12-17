package bsshelper.externalapi.openscriptexecengine.wrapper;

import bsshelper.externalapi.openscriptexecengine.entity.CellStatus;
import bsshelper.externalapi.openscriptexecengine.entity.EUtranCellFDDStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EUtranCellFDDStatusListWrapper {
    private List<EUtranCellFDDStatus> dataLTEFDD;

    public boolean isAnySelected() {
        for (EUtranCellFDDStatus data : dataLTEFDD) {
            if (data.isSelected()) {return true;}
        }
        return false;
    }

    public List<CellStatus> getExtensionData() {return dataLTEFDD == null ? null : new ArrayList<>(dataLTEFDD);}
}
