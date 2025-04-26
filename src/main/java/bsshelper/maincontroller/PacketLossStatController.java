package bsshelper.maincontroller;

import bsshelper.service.LocalCacheService;
import bsshelper.service.paketlossstat.service.PaketLossStatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/helper")
@RequiredArgsConstructor
public class PacketLossStatController {
    private final PaketLossStatService paketLossStatService;
    private final LocalCacheService localCacheService;

    @GetMapping("/packetLossStat2")
    public void getPacketLossStat() {
        paketLossStatService.getPacketLossInDomainStat();
//        model.addAttribute("data", packetLossInDomainStat);
//        return "packetloss";
    }

    @GetMapping("/packetLossStat")
    public String getPacketLossStat(Model model, HttpSession session) {
        model.addAttribute("data", localCacheService.packetLostCache);
        model.addAttribute("dates", getDates());
        model.addAttribute("title", "Packet Loss Inspector");
        return "packetloss";
    }

    private Map<String, String> getDates() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> result = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");
        for (int i = 1; i < 8; i++) {
            LocalDateTime before = now.minusDays(i);
            result.put(before.getDayOfWeek().toString(), before.format(formatter));
        }
        return result;
    }
}
