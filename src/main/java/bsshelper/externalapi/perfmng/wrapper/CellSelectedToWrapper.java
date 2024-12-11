package bsshelper.externalapi.perfmng.wrapper;

import bsshelper.externalapi.perfmng.to.CellSelectedTo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CellSelectedToWrapper {
    private List<CellSelectedTo> dataCell;
}
