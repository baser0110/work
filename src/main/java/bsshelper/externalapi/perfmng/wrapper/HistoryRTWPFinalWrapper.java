package bsshelper.externalapi.perfmng.wrapper;

import bsshelper.externalapi.perfmng.entity.HistoryRTWP;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryRTWPFinalWrapper {
    private Map<String, HistoryRTWP.Values> dataRTWP;
}
