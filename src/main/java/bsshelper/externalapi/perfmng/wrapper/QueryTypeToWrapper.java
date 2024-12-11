package bsshelper.externalapi.perfmng.wrapper;

import bsshelper.externalapi.perfmng.to.QueryTypeTo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryTypeToWrapper {
    private List<QueryTypeTo> dataQueryType;
}
