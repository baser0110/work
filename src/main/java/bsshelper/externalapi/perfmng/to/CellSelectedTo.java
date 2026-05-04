package bsshelper.externalapi.perfmng.to;

import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMoc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CellSelectedTo {
    private boolean selected;
    private UUtranCellFDDMoc cell;

    public static List<CellSelectedTo> getCellSelectedTo(List<UUtranCellFDDMoc> cells) {
        List<CellSelectedTo> result = new ArrayList<>();
        if (cells == null || cells.isEmpty()) { return result; }
        for (UUtranCellFDDMoc cell : cells) {
            result.add(new CellSelectedTo(true,cell));
        }
        return result;
    }
}
