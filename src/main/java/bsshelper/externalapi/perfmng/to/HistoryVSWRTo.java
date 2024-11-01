package bsshelper.externalapi.perfmng.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryVSWRTo {
    private int result;
    private List<String> data;
}
