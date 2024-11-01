package bsshelper.externalapi.openscriptexecengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GCellStatus{
    private boolean selected;
    private String userLabel;
    private String ldn;
    private String hasAlarm;
}
