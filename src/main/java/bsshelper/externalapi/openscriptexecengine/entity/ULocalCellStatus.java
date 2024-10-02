package bsshelper.externalapi.openscriptexecengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ULocalCellStatus implements CellStatus{
    private boolean selected;
    private String userLabel;
    private String ldn;
    private String adminState;
}
