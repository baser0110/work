package bsshelper.externalapi.openscriptexecengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EUtranCellFDDStatus implements CellStatus{
    private boolean selected;
    private String userLabel;
    private String ldn;
    private String adminState;
    private String operState;
}
