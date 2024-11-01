package bsshelper.externalapi.openscriptexecengine.wrapper;

import bsshelper.externalapi.openscriptexecengine.entity.GCellStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GCellStatusListWrapper {
    private List<GCellStatus> dataGSM;

    public boolean isAnySelected() {
        for (GCellStatus data : dataGSM) {
            if (data.isSelected()) {return true;}
        }
        return false;
    }
}
