package bsshelper.service.paketlossstat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SiteStat {
    private final String siteName;
    private final String id;
    private final String mrnc;

    private ValueStat values = new ValueStat();
}
