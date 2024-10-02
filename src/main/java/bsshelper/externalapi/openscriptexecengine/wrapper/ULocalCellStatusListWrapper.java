package bsshelper.externalapi.openscriptexecengine.wrapper;

import bsshelper.externalapi.openscriptexecengine.entity.CellStatus;
import bsshelper.externalapi.openscriptexecengine.entity.EUtranCellNBIoTStatus;
import bsshelper.externalapi.openscriptexecengine.entity.ULocalCellStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ULocalCellStatusListWrapper {
    private List<ULocalCellStatus> dataUMTS;

    public boolean isAnySelected() {
        for (ULocalCellStatus data : dataUMTS) {
            if (data.isSelected()) {return true;}
        }
        return false;
    }

    public List<CellStatus> getExtensionData() {return dataUMTS == null ? null : new ArrayList<>(dataUMTS);}
}
