package bsshelper.maincontroller;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.openscriptexecengine.entity.CellStatusDetails;
import bsshelper.service.LocalCacheService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/helper")
@RequiredArgsConstructor
public class CellStatusDetailsController {
    private final LocalCacheService localCacheService;

    @PostMapping("/cellStatus/cellStatusDetails")
    public String cellStatusBatch(Model model, HttpSession session) {
        String id = session.getId();
        ManagedElement managedElement = localCacheService.managedElementMap.get(id);
        List<CellStatusDetails> details = localCacheService.cellStatusDetailsMap.get(id);
        model.addAttribute("managedElement", managedElement);
        model.addAttribute("details", details);
        return "cellstatusdetails";
    }
}