package bsshelper.externalapi.perfmng.wrapper;

import bsshelper.externalapi.perfmng.entity.HistoryVSWR;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryVSWRFinalWrapper {
    private Map<String, HistoryVSWR.Values> dataVSWR;
}
