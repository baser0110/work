package bsshelper.externalapi.perfmng.to;

import bsshelper.externalapi.perfmng.util.KPI;
import bsshelper.externalapi.perfmng.util.QueryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryTypeTo {

    private boolean selected;
    private String queryType;

    public static List<QueryTypeTo> getDefaultQueryTypeSelectedList() {
        List<QueryTypeTo> result = new ArrayList<>();
        result.add(new QueryTypeTo(false, QueryType.GENERAL_INFO.getInfo()));
        result.add(new QueryTypeTo(false, QueryType.CELL_INFO.getInfo()));
        result.add(new QueryTypeTo(false, QueryType.VSWR.getInfo()));
        result.add(new QueryTypeTo(false, QueryType.OPTIC_LEVELS.getInfo()));
        result.add(new QueryTypeTo(false, QueryType.SYNC.getInfo()));
        result.add(new QueryTypeTo(false, QueryType.CUSTOM_HISTORY.getInfo()));
        return result;
    }
}
