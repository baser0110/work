package bsshelper.maincontroller;

import bsshelper.service.logger.LoggerUtil;
import bsshelper.service.user.config.PasswordConfig;
import bsshelper.service.user.entity.User;
import bsshelper.service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping(value = "/helper")
@RequiredArgsConstructor
public class LoginPageController {
    private final UserService userService;
    private static final Logger operationLog = LoggerUtil.getOperationLogger();
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/change-password")
    public String changePasswordPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("passInfo",
                "The password must have lowercase & uppercase letters, digits; symbols like !?@#$%^&*() can be included optional and must be at least 8 characters long");
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String password,
                                 @RequestParam String confirmPassword, Model model) {
        if (!password.equals(confirmPassword)) {
            return "redirect:/helper/change-password?error=true";
        }
        if (PasswordConfig.isPasswordValid(password)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Optional<User> user = userService.findByUsernameAndIsDeletedFalse(username);
            user.ifPresent(value -> userService.updateUserFirstPass(value, password));
            return "redirect:/helper/login?success";
        }
        return "redirect:/helper/change-password?invalid-pass";
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("error","Access Denied!");
        return "error";
    }
}
