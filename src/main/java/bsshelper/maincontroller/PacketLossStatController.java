package bsshelper.maincontroller;

import bsshelper.localservice.localcache.LocalCacheService;
import bsshelper.localservice.paketlossstat.entity.DomainStat;
import bsshelper.localservice.paketlossstat.service.PaketLossStatService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping(value = "/helper")
@RequiredArgsConstructor
public class PacketLossStatController {
    private final PaketLossStatService paketLossStatService;
    private final LocalCacheService localCacheService;

    @GetMapping("/packetLossStat")
    public String getPacketLossStat(@RequestParam(required = false) String query, Model model) {
        if (localCacheService.packetLostCache.isEmpty() || localCacheService.packetLostCache == null) {
            paketLossStatService.getPacketLossInDomainStat();
        }

        if (query == null) query = DataQuery.TOP_100.toString();
        Map<String, DomainStat> result;

        switch (DataQuery.valueOf(query)) {
            case MINSK, BREST, GRODNO, VITEBSK, MOGILEV, GOMEL -> result = filterByRegion(query);
            default -> result = filterByTopSize(100);
        }

        model.addAttribute("data", result);
        model.addAttribute("dates", localCacheService.packetLostDatesCache);
        model.addAttribute("queryList", DataQuery.values());
        model.addAttribute("currentQuery", query);
        model.addAttribute("title", "Packet Loss Inspector");
        return "packetloss";
    }

    private enum DataQuery {
        MINSK,
        BREST,
        GRODNO,
        VITEBSK,
        MOGILEV,
        GOMEL,
        TOP_100
    }

    public Map<String, DomainStat> filterByRegion(String region) {
        return localCacheService.packetLostCache.entrySet().stream()
                .filter(entry -> region.equals(entry.getValue().getRegion()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Map<String, DomainStat> filterByTopSize(int size) {
        return localCacheService.packetLostCache.entrySet().stream()
                .limit(size)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
