package bsshelper.externalapi.configurationmng.currentmng.entity.itbbu;

import bsshelper.externalapi.configurationmng.currentmng.entity.AbstractCellMoc;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CUEUtranCellFDDLTEMoc implements AbstractCellMoc {
    private final String userLabel;
    private final String lastModifiedTime;
    private final int adminState;
    private final String mocName;
    private final String operState;
    private final String ldn;
}
