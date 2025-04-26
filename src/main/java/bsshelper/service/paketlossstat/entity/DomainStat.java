package bsshelper.service.paketlossstat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Data
@RequiredArgsConstructor
public class DomainStat {
    private final String domain;
    private final String cluster;
    private final String region;

    private Map<String, List<String>> idMap2g;
    private Map<String, List<String>> idMap3g;

    private Map<String, SiteStat> sites2g = new TreeMap<>();
    private Map<String, SiteStat> sites3g = new TreeMap<>();

    private ValueStat values2g = new ValueStat();
    private ValueStat values3g = new ValueStat();

    public Set<String> getSiteSet(Map<String, SiteStat> sites2g, Map<String, SiteStat> sites3g) {
        Set<String> result = new TreeSet<>();
        result.addAll(sites2g.keySet());
        result.addAll(sites3g.keySet());
        return result;
    }
}
