package bsshelper.externalapi.perfmng.wrapper;

import bsshelper.externalapi.perfmng.entity.HistoryForUMTSCell;
import bsshelper.externalapi.perfmng.util.ExternalKPI;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class KPI_UMTSCellWrapper {
    private final ExternalKPI kpi;
    private final Map<String, List<HistoryForUMTSCell>> data;
}
