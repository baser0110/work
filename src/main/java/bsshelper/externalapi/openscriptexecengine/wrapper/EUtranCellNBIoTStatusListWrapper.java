package bsshelper.externalapi.openscriptexecengine.wrapper;

import bsshelper.externalapi.openscriptexecengine.entity.CellStatus;
import bsshelper.externalapi.openscriptexecengine.entity.EUtranCellNBIoTStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EUtranCellNBIoTStatusListWrapper {
    private List<EUtranCellNBIoTStatus> dataNBIOT;

    public boolean isAnySelected() {
        for (EUtranCellNBIoTStatus data : dataNBIOT) {
            if (data.isSelected()) {return true;}
        }
        return false;
    }

    public List<CellStatus> getExtensionData() {return dataNBIOT == null ? null : new ArrayList<>(dataNBIOT);}
}
