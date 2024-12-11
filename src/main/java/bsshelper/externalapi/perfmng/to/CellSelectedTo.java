package bsshelper.externalapi.perfmng.to;

import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
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
    private UUtranCellFDDMocSimplified cell;

    public static List<CellSelectedTo> getCellSelectedTo(List<UUtranCellFDDMocSimplified> cells) {
        List<CellSelectedTo> result = new ArrayList<>();
        if (cells == null || cells.isEmpty()) { return result; }
        for (UUtranCellFDDMocSimplified cell : cells) {
            result.add(new CellSelectedTo(true,cell));
        }
        return result;
    }
}
