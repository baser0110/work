package bsshelper.externalapi.configurationmng.nemoactserv.wrapper;

import bsshelper.externalapi.configurationmng.nemoactserv.entity.VSWRTestFinal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VSWRListWrapper {
    private List<VSWRTestFinal> dataVSWR;
}
