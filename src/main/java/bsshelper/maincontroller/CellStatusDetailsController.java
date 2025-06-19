package bsshelper.maincontroller;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.openscriptexecengine.entity.CellStatusDetails;
import bsshelper.localservice.localcache.LocalCacheService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String cellStatusBatch(@ModelAttribute("userLabel") String userLabel, Model model, HttpSession session) {

        ManagedElement managedElement = localCacheService.managedElementMap.get(userLabel);
        List<CellStatusDetails> details = localCacheService.cellStatusDetailsMap.get(userLabel);
        model.addAttribute("managedElement", managedElement);
        model.addAttribute("details", details);
        model.addAttribute("title", null);
        return "cellstatusdetails";
    }
}