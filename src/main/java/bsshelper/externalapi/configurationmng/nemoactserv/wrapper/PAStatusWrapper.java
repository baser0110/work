package bsshelper.externalapi.configurationmng.nemoactserv.wrapper;

import bsshelper.externalapi.configurationmng.nemoactserv.entity.PAStatus;
import bsshelper.externalapi.openscriptexecengine.entity.ULocalCellStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PAStatusWrapper {
    private List<PAStatus> dataPA;

    public boolean isAnySelected() {
        for (PAStatus data : dataPA) {
            if (data.isSelected()) {return true;}
        }
        return false;
    }
}
