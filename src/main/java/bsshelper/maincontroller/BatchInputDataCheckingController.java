package bsshelper.maincontroller;

import bsshelper.localservice.localcache.LocalCacheService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Slf4j
@Controller
@RequestMapping(value = "/helper")
@RequiredArgsConstructor
public class BatchInputDataCheckingController {
    private final LocalCacheService localCacheService;

    @PostMapping("/cellStatusBatch/checkBatchData")
    public String cellStatusBatch(String separator, String umts, String gsm, String lteFDD, String nbiot, Model model, HttpSession session) {
        List<String> umtsCells = Arrays.asList(umts.trim().split(separator));
        List<String> gsmCells = Arrays.asList(gsm.trim().split(separator));
        List<String> lteFDDCells = Arrays.asList(lteFDD.trim().split(separator));
        List<String> nbiotCells = Arrays.asList(nbiot.trim().split(separator));
        StringBuilder umtsFailCells = new StringBuilder();
        StringBuilder gsmFailCells = new StringBuilder();
        StringBuilder lteFDDFailCells = new StringBuilder();
        StringBuilder nbiotFailCells = new StringBuilder();
        int umtsOkCount = 0;
        int gsmOkCount = 0;
        int lteFDDOkCount = 0;
        int nbiotOkCount = 0;
        Set<String> nes = new TreeSet<>();
        for (String umtsCell : umtsCells) {
            if (localCacheService.umtsSDRMap.containsKey(umtsCell.trim().toUpperCase())) {
                umtsOkCount++;
                nes.add(localCacheService.meByNEMap.get(localCacheService.umtsSDRMap.get(umtsCell.trim().toUpperCase()).getNe()));
                continue;
            }
            else if (localCacheService.umtsITBBUMap.containsKey(umtsCell.trim().toUpperCase())) {
                umtsOkCount++;
                nes.add(localCacheService.meByNEMap.get(localCacheService.umtsITBBUMap.get(umtsCell.trim().toUpperCase()).getNe()));
                continue;
            }
            umtsFailCells.append(umtsCell.trim().toUpperCase()).append(",");
        }
        if (!umtsFailCells.isEmpty()) umtsFailCells.deleteCharAt(umtsFailCells.length() - 1); else umtsFailCells.append("-");
        for (String gsmCell : gsmCells) {
            if (localCacheService.gsmMRNCMap.containsKey(gsmCell.trim().toUpperCase())) {
                gsmOkCount++;
                nes.add(localCacheService.gsmMRNCMap.get(gsmCell.trim().toUpperCase()).getNe());
                continue;
            }
            gsmFailCells.append(gsmCell.trim().toUpperCase()).append(",");
        }
        if (!gsmFailCells.isEmpty()) gsmFailCells.deleteCharAt(gsmFailCells.length() - 1); else gsmFailCells.append("-");
        for (String nbiotCell : nbiotCells) {
            if (localCacheService.nbiotSDRMap.containsKey(nbiotCell.trim().toUpperCase())) {
                nbiotOkCount++;
                nes.add(localCacheService.meByNEMap.get(localCacheService.nbiotSDRMap.get(nbiotCell.trim().toUpperCase()).getNe()));
                continue;
            }
            else if(localCacheService.nbiotITBBUMap.containsKey(nbiotCell.trim().toUpperCase())) {
                nbiotOkCount++;
                nes.add(localCacheService.meByNEMap.get(localCacheService.nbiotITBBUMap.get(nbiotCell.trim().toUpperCase()).getNe()));
                continue;
            }
            nbiotFailCells.append(nbiotCell.trim().toUpperCase()).append(",");
        }
        if (!nbiotFailCells.isEmpty()) nbiotFailCells.deleteCharAt(nbiotFailCells.length() - 1); else nbiotFailCells.append("-");
        for (String lteFDDCell : lteFDDCells) {
            if (localCacheService.lteFDDSDRMap.containsKey(lteFDDCell.trim().toUpperCase())) {
                lteFDDOkCount++;
                nes.add(localCacheService.meByNEMap.get(localCacheService.lteFDDSDRMap.get(lteFDDCell.trim().toUpperCase()).getNe()));
                continue;
            }
            else if(localCacheService.lteFDDITBBUMap.containsKey(lteFDDCell.trim().toUpperCase())) {
                lteFDDOkCount++;
                nes.add(localCacheService.meByNEMap.get(localCacheService.lteFDDITBBUMap.get(lteFDDCell.trim().toUpperCase()).getNe()));
                continue;
            }
            lteFDDFailCells.append(lteFDDCell.trim().toUpperCase()).append(",");
        }
        if (!lteFDDFailCells.isEmpty()) lteFDDFailCells.deleteCharAt(lteFDDFailCells.length() - 1); else lteFDDFailCells.append("-");

        model.addAttribute("OK", new ArrayList<>(List.of(umtsOkCount, gsmOkCount, lteFDDOkCount, nbiotOkCount)));
        model.addAttribute("NOK", new ArrayList<>(List.of(umtsFailCells.toString(), gsmFailCells.toString(),
                lteFDDFailCells.toString(), nbiotFailCells.toString())));
        model.addAttribute("title", "Batch Data Check");
        model.addAttribute("nes", nes.toString().substring(1, nes.toString().length() - 1));
        return "batchdataresult";
    }
}
