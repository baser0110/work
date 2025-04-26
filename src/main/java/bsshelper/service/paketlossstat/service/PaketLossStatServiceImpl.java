package bsshelper.service.paketlossstat.service;

import bsshelper.externalapi.perfmng.entity.HistoryOfficeLink;
import bsshelper.externalapi.perfmng.service.HistoryQueryService;
import bsshelper.externalapi.perfmng.util.KPI;
import bsshelper.globalutil.SubnetworkToBSCOrRNC;
import bsshelper.service.LocalCacheService;
import bsshelper.service.TokenService;
import bsshelper.service.paketlossstat.entity.DomainStat;
import bsshelper.service.paketlossstat.entity.SiteStat;
import bsshelper.service.paketlossstat.entity.ValueStat;
import bsshelper.service.paketlossstat.mapper.DomainMapper;
import bsshelper.service.paketlossstat.to.DomainTo;
import bsshelper.service.paketlossstat.util.ExcelReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaketLossStatServiceImpl implements PaketLossStatService{
    private final HistoryQueryService historyQueryService;
    private final LocalCacheService localCacheService;
    private final TokenService tokenService;
    private final ExcelReaderService excelReaderService;

//    @Value("${custom.domain.file.path}")
    private static final String path = "C:/Users/sergey_b/Desktop/IPNodeBs_ZTE_last_version.xlsm";
    private static final List<String> sheetNames = List.of("Minsk","Brest","Gomel","Grodno","Mogilev","Vitebsk");

//    public PaketLossStatServiceImpl(@Value("${custom.domain.file.path}") String path) {
//        this.path = path;
//    }


//    public List<DomainFromFileTo> populateDomainData() {
//        List<String> rawData = FileReaderUtil.readFromFile(path);
//        if (rawData != null) {
//            return DomainFromFileToMapper.toDomainFileToList(rawData);
//        }
//        return null;
//    }

    public List<DomainTo> populateDomainData() {
//        List<String> rawData = FileReaderUtil.readFromFile(path);
//        if (rawData != null) {
//            return DomainFromFileToMapper.toDomainFileToList(rawData);
//        }
        return excelReaderService.readFromSheets(path, sheetNames);
    }

    @Override
    public void getPacketLossInDomainStat() {
        List<DomainTo> data = populateDomainData();
//        Map<String, List<String>> rncNameIdMap = DomainMapper.toRNCName_IdMap(data);
//        Map<String, List<String>> bncNameIdMap = DomainMapper.toBNCName_IdMap(data);
        Map<String, DomainTo> siteNameDomainMap = DomainMapper.toSiteName_DomainMap(data);

//        System.out.println(rncNameIdMap);
//        System.out.println(bncNameIdMap);
//        System.out.println(siteNameDomainMap);

        Map<String, List<HistoryOfficeLink>> siteHistoryRNCMap = getMRNCSiteHistoryMap(SubnetworkToBSCOrRNC.getRNCSet());
        Map<String, List<HistoryOfficeLink>> siteHistoryBSCMap = getMRNCSiteHistoryMap(SubnetworkToBSCOrRNC.getBSCSet());

//        System.out.println(siteHistoryRNCMap);
//        System.out.println(siteHistoryBSCMap);

        Map<String, DomainStat> result = new HashMap<>();

        for (String s : siteHistoryRNCMap.keySet()) {
            DomainTo domainTo = siteNameDomainMap.get(s);
            if (domainTo == null) continue;
            String domainName = domainTo.getDomain();
            String clusterName = domainTo.getCluster();
            String regionName = domainTo.getRegion();
            List<HistoryOfficeLink> siteHistory = siteHistoryRNCMap.get(s);
            String mrnc = "-";
            String id = "-";
            if (!siteHistory.isEmpty()) {
                mrnc = siteHistory.getFirst().getMrnc();
                id = siteHistory.getFirst().getId();
            }
            if (!result.containsKey(domainName)) {
                result.put(domainName, new DomainStat(domainName, clusterName, regionName));
                DomainStat domain = result.get(domainName);

                Map<String, List<String>> idMap = new HashMap<>();
                List<String> idList = new ArrayList<>();
                idList.add(id);
                idMap.put(parseMRNC(mrnc), idList);
                domain.setIdMap3g(idMap);

                Map<String, SiteStat> siteInDomainMap = new TreeMap<>();
                siteInDomainMap.put(s, new SiteStat(s, id, mrnc));
                domain.setSites3g(siteInDomainMap);
                SiteStat site = domain.getSites3g().get(s);
                setValueStat3g(siteHistory,domain,site);
            } else {
                DomainStat domain = result.get(domainName);
                if (domain.getSites3g() == null || domain.getSites3g().isEmpty()) {
                    Map<String, SiteStat> siteInDomainMap = new TreeMap<>();
                    siteInDomainMap.put(s, new SiteStat(s, id, mrnc));
                    domain.setSites3g(siteInDomainMap);
                }
                domain.getSites3g().put(s, new SiteStat(s, id, mrnc));
                SiteStat site = domain.getSites3g().get(s);
                setValueStat3g(siteHistory,domain,site);

                if (domain.getIdMap3g() == null || domain.getIdMap3g().isEmpty()) {
                    Map<String, List<String>> idMap = new HashMap<>();
                    List<String> idList = new ArrayList<>();
                    idList.add(id);
                    idMap.put(parseMRNC(mrnc), idList);
                    domain.setIdMap3g(idMap);
                } else {
                    if (domain.getIdMap3g().containsKey(parseMRNC(mrnc))) {
                        domain.getIdMap3g().get(parseMRNC(mrnc)).add(id);
                    } else {
                        List<String> idList = new ArrayList<>();
                        idList.add(id);
                        domain.getIdMap3g().put(parseMRNC(mrnc), idList);
                    }
                }
            }
        }
        for (String s : siteHistoryBSCMap.keySet()) {
            DomainTo domainTo = siteNameDomainMap.get(s);
            if (domainTo == null) continue;
            String domainName = domainTo.getDomain();
            String clusterName = domainTo.getCluster();
            String regionName = domainTo.getRegion();
            List<HistoryOfficeLink> siteHistory = siteHistoryBSCMap.get(s);
            String mrnc = "-";
            String id = "-";
            if (!siteHistory.isEmpty()) {
                mrnc = siteHistory.getFirst().getMrnc();
                id = siteHistory.getFirst().getId();
            }
            if (!result.containsKey(domainName)) {
                result.put(domainName, new DomainStat(domainName, clusterName, regionName));
                DomainStat domain = result.get(domainName);

                Map<String, List<String>> idMap = new HashMap<>();
                List<String> idList = new ArrayList<>();
                idList.add(id);
                idMap.put(parseMRNC(mrnc), idList);
                domain.setIdMap2g(idMap);

                Map<String, SiteStat> siteInDomainMap = new TreeMap<>();
                siteInDomainMap.put(s, new SiteStat(s, id, mrnc));
                domain.setSites2g(siteInDomainMap);
                SiteStat site = domain.getSites2g().get(s);
                setValueStat2g(siteHistory,domain,site);
            } else {
                DomainStat domain = result.get(domainName);
                if (domain.getSites2g() == null || domain.getSites2g().isEmpty()) {
                    Map<String, SiteStat> siteInDomainMap = new TreeMap<>();
                    siteInDomainMap.put(s, new SiteStat(s, id, mrnc));
                    domain.setSites2g(siteInDomainMap);
                }
                domain.getSites2g().put(s, new SiteStat(s, id, mrnc));
                SiteStat site = domain.getSites2g().get(s);
                setValueStat2g(siteHistory,domain,site);

                if (domain.getIdMap2g() == null || domain.getIdMap2g().isEmpty()) {
                    Map<String, List<String>> idMap = new HashMap<>();
                    List<String> idList = new ArrayList<>();
                    idList.add(id);
                    idMap.put(parseMRNC(mrnc),idList);
                    domain.setIdMap2g(idMap);
                } else {
                    if (domain.getIdMap2g().containsKey(parseMRNC(mrnc))) {
                        domain.getIdMap2g().get(parseMRNC(mrnc)).add(id);
                    } else {
                        List<String> idList = new ArrayList<>();
                        idList.add(id);
                        domain.getIdMap2g().put(parseMRNC(mrnc),idList);
                    }
                }
            }
        }

        normalizeValues(result);
        roundValues(result);
        localCacheService.packetLostCache.putAll(sortAndGet(result));
    }

    private Map<String, List<HistoryOfficeLink>> getMRNCSiteHistoryMap(Set<String> mrncSet) {
        Map<String, List<HistoryOfficeLink>> siteHistoryMRNCMap = new HashMap<>();
        for (String mrnc : mrncSet) {
            Map<String, List<HistoryOfficeLink>> officeLinkMRNCHistory =
                    historyQueryService.getFullMRNCOfficeLinkHistory(
                            tokenService.getToken(),
                            mrnc,
                            168,
                            KPI.LOST_PACKET,
                            1440
                    );
            siteHistoryMRNCMap.putAll(officeLinkMRNCHistory);
        }
        return  siteHistoryMRNCMap;
    }


    private void setValueStat3g(List<HistoryOfficeLink> siteHistory, DomainStat domain, SiteStat site) {
        LocalDateTime time = LocalDateTime.now();
        for (HistoryOfficeLink l : siteHistory) {
            if (time.isBefore(l.getTime()) || (time.getDayOfYear() + 1 == l.getTime().getDayOfYear())) return;
            switch (l.getTime().minusDays(1).getDayOfWeek()) {
                case MONDAY -> {
                    site.getValues().setMonday(l.getValue());
                    domain.getValues3g().setMonday(domain.getValues3g().getMonday() + l.getValue());
                }
                case TUESDAY -> {
                    site.getValues().setTuesday(l.getValue());
                    domain.getValues3g().setTuesday(domain.getValues3g().getTuesday() + l.getValue());
                }
                case WEDNESDAY -> {
                    site.getValues().setWednesday(l.getValue());
                    domain.getValues3g().setWednesday(domain.getValues3g().getWednesday() + l.getValue());
                }
                case THURSDAY -> {
                    site.getValues().setThursday(l.getValue());
                    domain.getValues3g().setThursday(domain.getValues3g().getThursday() + l.getValue());
                }
                case FRIDAY -> {
                    site.getValues().setFriday(l.getValue());
                    domain.getValues3g().setFriday(domain.getValues3g().getFriday() + l.getValue());
                }
                case SATURDAY -> {
                    site.getValues().setSaturday(l.getValue());
                    domain.getValues3g().setSaturday(domain.getValues3g().getSaturday() + l.getValue());
                }
                case SUNDAY -> {
                    site.getValues().setSunday(l.getValue());
                    domain.getValues3g().setSunday(domain.getValues3g().getSunday() + l.getValue());
                }
            }
            site.getValues().setWeek(site.getValues().getWeek() + (l.getValue() / 7));
            domain.getValues3g().setWeek(domain.getValues3g().getWeek() + (l.getValue() / 7));
        }
    }
    private void setValueStat2g(List<HistoryOfficeLink> siteHistory, DomainStat domain, SiteStat site) {
        LocalDateTime time = LocalDateTime.now();
        for (HistoryOfficeLink l : siteHistory) {
            if (time.isBefore(l.getTime()) || (time.getDayOfYear() + 1 == l.getTime().getDayOfYear())) return;
            switch (l.getTime().minusDays(1).getDayOfWeek()) {
                case MONDAY -> {
                    site.getValues().setMonday(l.getValue());
                    domain.getValues2g().setMonday(domain.getValues2g().getMonday() + l.getValue());
                }
                case TUESDAY -> {
                    site.getValues().setTuesday(l.getValue());
                    domain.getValues2g().setTuesday(domain.getValues2g().getTuesday() + l.getValue());
                }
                case WEDNESDAY -> {
                    site.getValues().setWednesday(l.getValue());
                    domain.getValues2g().setWednesday(domain.getValues2g().getWednesday() + l.getValue());
                }
                case THURSDAY -> {
                    site.getValues().setThursday(l.getValue());
                    domain.getValues2g().setThursday(domain.getValues2g().getThursday() + l.getValue());
                }
                case FRIDAY -> {
                    site.getValues().setFriday(l.getValue());
                    domain.getValues2g().setFriday(domain.getValues2g().getFriday() + l.getValue());
                }
                case SATURDAY -> {
                    site.getValues().setSaturday(l.getValue());
                    domain.getValues2g().setSaturday(domain.getValues2g().getSaturday() + l.getValue());
                }
                case SUNDAY -> {
                    site.getValues().setSunday(l.getValue());
                    domain.getValues2g().setSunday(domain.getValues2g().getSunday() + l.getValue());
                }
            }
            site.getValues().setWeek(site.getValues().getWeek() + (l.getValue() / 7));
            domain.getValues2g().setWeek(domain.getValues2g().getWeek() + (l.getValue() / 7));
        }
    }

    private void normalizeValues(Map<String, DomainStat> result) {
        for (DomainStat d : result.values()) {
            normalizeWeekValues(d.getValues2g(), d.getSites2g().size());
            normalizeWeekValues(d.getValues3g(), d.getSites3g().size());
        }
    }

    private void normalizeWeekValues(ValueStat valueStat, int size) {
        valueStat.setMonday(valueStat.getMonday() / size);
        valueStat.setTuesday(valueStat.getTuesday() / size);
        valueStat.setWednesday(valueStat.getWednesday() / size);
        valueStat.setThursday(valueStat.getThursday() / size);
        valueStat.setFriday(valueStat.getFriday() / size);
        valueStat.setSaturday(valueStat.getSaturday() / size);
        valueStat.setSunday(valueStat.getSunday() / size);
        valueStat.setWeek(valueStat.getWeek() / size);
    }

    private void roundValues(Map<String, DomainStat> result) {
        for (DomainStat d : result.values()) {
            roundAllValues(d.getValues2g());
            roundAllValues(d.getValues3g());
            for (SiteStat s : d.getSites2g().values()) {
                roundAllValues(s.getValues());
            }
            for (SiteStat s : d.getSites3g().values()) {
                roundAllValues(s.getValues());
            }
        }
    }

    private void roundAllValues(ValueStat valueStat) {
        double monday = valueStat.getMonday() * 100;
        double tuesday = valueStat.getTuesday() * 100;
        double wednesday = valueStat.getWednesday() * 100;
        double thursday = valueStat.getThursday() * 100;
        double friday = valueStat.getFriday() * 100;
        double saturday = valueStat.getSaturday() * 100;
        double sunday = valueStat.getSunday() * 100;
        double week = valueStat.getWeek() * 100;

        valueStat.setMonday(monday > 0.00049 ? BigDecimal.valueOf(monday).setScale(3, RoundingMode.HALF_UP).doubleValue() : (monday == 0 ? 0 : 0.00049));
        valueStat.setTuesday(tuesday > 0.00049 ? BigDecimal.valueOf(tuesday).setScale(3, RoundingMode.HALF_UP).doubleValue() : (tuesday == 0 ? 0 : 0.00049));
        valueStat.setWednesday(wednesday > 0.00049 ? BigDecimal.valueOf(wednesday).setScale(3, RoundingMode.HALF_UP).doubleValue() : (wednesday == 0 ? 0 : 0.00049));
        valueStat.setThursday(thursday > 0.00049 ? BigDecimal.valueOf(thursday).setScale(3, RoundingMode.HALF_UP).doubleValue() : (thursday == 0 ? 0 : 0.00049));
        valueStat.setFriday(friday > 0.00049 ? BigDecimal.valueOf(friday).setScale(3, RoundingMode.HALF_UP).doubleValue() : (friday == 0 ? 0 : 0.00049));
        valueStat.setSaturday(saturday > 0.00049 ? BigDecimal.valueOf(saturday).setScale(3, RoundingMode.HALF_UP).doubleValue() : (saturday == 0 ? 0 : 0.00049));
        valueStat.setSunday(sunday > 0.00049 ? BigDecimal.valueOf(sunday).setScale(3, RoundingMode.HALF_UP).doubleValue() : (sunday == 0 ? 0 : 0.00049));
        valueStat.setWeek(week > 0.00049 ? BigDecimal.valueOf(week).setScale(3, RoundingMode.HALF_UP).doubleValue() : (week == 0 ? 0 : 0.00049));
    }

    private Map<String, DomainStat> sortAndGet(Map<String, DomainStat> result) {
        return result.entrySet().stream().sorted((e1, e2) -> Double.compare((
                                e2.getValue().getValues2g().getWeek() + e2.getValue().getValues3g().getWeek()),
                        e1.getValue().getValues2g().getWeek() + e1.getValue().getValues3g().getWeek()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    private String parseMRNC(String mrnc) {
        return mrnc.substring(mrnc.indexOf("(") + 1, mrnc.length() - 1);
    }

}