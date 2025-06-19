package bsshelper.localservice.paketlossstat.mapper;

import bsshelper.localservice.paketlossstat.to.DomainTo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DomainMapper {
    static public Map<String, DomainTo> toSiteName_DomainMap (List<DomainTo> domain) {
        Map<String, DomainTo> result = new TreeMap<>();
        domain.forEach(d -> result.put(d.getSiteName(),d));
        return result;
    }

//    static public Map<String,List<String>> toRNCName_IdMap(List<DomainTo> domain) {
//        Map<String,List<String>> result = new TreeMap<>();
//        domain.forEach(d -> {
//            String mrnc = d.getMrnc3g();
//            if (!mrnc.isBlank()) {
//                if (!result.containsKey(mrnc)) {
//                    List<String> siteIdList = new ArrayList<>();
//                    siteIdList.add(d.getId());
//                    result.put(mrnc, siteIdList);
//                } else {
//                    result.get(mrnc).add(d.getId());
//                }
//            }
//        });
//        return result;
//    }
//
//    static public Map<String,List<String>> toBNCName_IdMap(List<DomainTo> domain) {
//        Map<String,List<String>> result = new TreeMap<>();
//        domain.forEach(d -> {
//            String mrnc = d.getMrnc2g();
//            if (!mrnc.isBlank()) {
//                if (!result.containsKey(mrnc)) {
//                    List<String> siteIdList = new ArrayList<>();
//                    siteIdList.add(d.getId());
//                    result.put(mrnc, siteIdList);
//                } else {
//                    result.get(mrnc).add(d.getId());
//                }
//            }
//        });
//        return result;
//    }
}
