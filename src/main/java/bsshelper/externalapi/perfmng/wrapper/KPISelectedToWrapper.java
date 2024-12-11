package bsshelper.externalapi.perfmng.wrapper;

import bsshelper.externalapi.perfmng.to.KPISelectedTo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KPISelectedToWrapper {
    private List<KPISelectedTo> dataKPI;
}
