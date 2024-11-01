package bsshelper.externalapi.perfmng.wrapper;

import bsshelper.externalapi.perfmng.entity.HistoryRTWP;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryRTWPWrapper {
    private List<HistoryRTWP> dataRTWPChart;

    public String getJson() {
        return new Gson().toJson(dataRTWPChart);
    }
}
