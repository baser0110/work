package bsshelper.externalapi.openscriptexecengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CellStatusDetails {
    private final String userLabel;
    private final int localCellId;
    private final String adminState;
    private final String uLocalCell_operState;
    private final String uLocalCell_availStatus;
    private final String uCell_operState;
    private final String uCell_availStatus;
}
