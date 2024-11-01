package bsshelper.externalapi.perfmng.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryQueryErrorEntity {
    private int result;
    private String failReason;
}
