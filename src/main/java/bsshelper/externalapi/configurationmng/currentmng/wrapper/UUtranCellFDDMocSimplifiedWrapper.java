package bsshelper.externalapi.configurationmng.currentmng.wrapper;


import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UUtranCellFDDMocSimplifiedWrapper {
    private List<UUtranCellFDDMocSimplified> dataCellUMTS;
}
