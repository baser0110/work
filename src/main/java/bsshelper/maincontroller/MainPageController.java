package bsshelper.maincontroller;

import bsshelper.service.user.entity.User;
import bsshelper.service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping(value = "/helper")
@RequiredArgsConstructor
public class MainPageController {
    private final UserService userService;

    @GetMapping("")
    public String homepage(Authentication authentication, Model model) {
        if (authentication != null) {
            String username = authentication.getName();
            Optional<User> user = userService.findByUsernameAndIsDeletedFalse(username);
            if (user.isPresent() && user.get().getIsFirstLogin())
                return "redirect:/helper/change-password";
        }
        model.addAttribute("title", "Main Page");
        model.addAttribute("userAuth", authentication);
//        System.out.println(authentication);
//        System.out.println(authentication.getAuthorities().toString().contains("USER_MNG_NO"));
        return "main";
    }

    @GetMapping("/vasily-tools")
    public String vasilyTools(Model model) {
        return "redirect:http://srv-noc-001:8181/";
    }
}
