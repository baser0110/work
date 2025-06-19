package bsshelper.maincontroller;

import bsshelper.security.config.PasswordConfig;
import bsshelper.security.entity.Profile;
import bsshelper.security.entity.User;
import bsshelper.security.service.ProfileService;
import bsshelper.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/helper")
@RequiredArgsConstructor
public class AppAccessMngController {
    private final UserService userService;
    private final ProfileService profileService;
//    private static final Logger operationLog = LoggerUtil.getOperationLogger();

    @GetMapping("/appAccessMng")
    public String getAccessMngMain(Model model) {
        model.addAttribute("title", "Application Access Manager");
        return "app-access";
    }

    @GetMapping("/appAccessMng/users")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("title", "User Manager");
        return "users";
    }

    @GetMapping("/appAccessMng/users/management/{id}")
    public String editUserForm(@PathVariable(value = "id") String id, Model model) {
        if (id != null && !id.isEmpty()) {
            Optional<User> user = userService.getUserById(id);
            user.ifPresent(value -> model.addAttribute("user", value));
        }
        model.addAttribute("profiles", profileService.getAllProfiles());
        model.addAttribute("title", "User Manager (Edit)");
        return "user-management";
    }

    @GetMapping("/appAccessMng/users/management/new")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("profiles", profileService.getAllProfiles());
        model.addAttribute("title", "User Manager (New)");
        return "user-management";
    }

    @PostMapping("/appAccessMng/users/management/create")
    public String createUser(@ModelAttribute User user, Authentication authentication) {
        if (user.getUsername().length() < 4 || user.getUsername().length() > 20) {
            return "redirect:/helper/appAccessMng/users/management/new?name-short";
        }
        Optional<User> dbUser = userService.findByUsername(user.getUsername().toLowerCase());
        if (dbUser.isPresent()) {
            return "redirect:/helper/appAccessMng/users/management/new?name-existed";
        }
        if (!PasswordConfig.isPasswordValid(user.getPassword())) {
            return "redirect:/helper/appAccessMng/users/management/new?invalid-pass";
        }
        userService.createUser(user);
//        operationLog.warn("User: {} \n({}) \ncreated a new user: {}",
//                authentication.getName(), authentication.getDetails(), user.getUsername());
        return "redirect:/helper/appAccessMng/users";
    }

    @PostMapping("/appAccessMng/users/management/edit")
    public String updateUser(@ModelAttribute User user,
                             @RequestParam("resetPassword") Optional<String> resetPassword,
                             Authentication authentication) {
        if (resetPassword.isPresent()) {
            userService.updateUserWithPassReset(user);
            if (!PasswordConfig.isPasswordValid(user.getPassword())) {
                return "redirect:/helper/appAccessMng/users/management/" + user.getId() + "?invalid-pass";
            }
        } else userService.updateUser(user);
//        operationLog.warn("User: {} ({}) edited user: {}",
//                authentication.getName(), authentication.getDetails(), user.getUsername());
        return "redirect:/helper/appAccessMng/users";
    }

    @PostMapping("/appAccessMng/users/management/delete/{id}")
    public String deleteUser(@PathVariable String id, Authentication authentication) {
        userService.deleteUser(id);
//        operationLog.warn("User: {} $$ ({}) $$ deleted user: {}",
//                authentication.getName(), authentication.getDetails(), id);
        return "redirect:/helper/appAccessMng/users";
    }

    @GetMapping("/appAccessMng/profiles")
    public String getAllProfiles(Model model) {
        List<Profile> profiles = profileService.getAllProfiles();
        model.addAttribute("profiles", profiles); // Add profiles to model
        model.addAttribute("title", "Profile Manager");
        return "profiles"; // This will render profiles.html
    }

    @GetMapping("/appAccessMng/profiles/management/new")
    public String createProfileForm(Model model) {
        model.addAttribute("profile", new Profile());
        model.addAttribute("title", "Profile Manager (New)");
        return "profile-management";
    }

    @PostMapping("/appAccessMng/profiles/management/create")
    public String createProfile(@ModelAttribute Profile profile, Authentication authentication) {
            Optional<Profile> dbProfile = profileService.findByName(profile.getName().toLowerCase());
        if (dbProfile.isPresent()) {
            return "redirect:/helper/appAccessMng/profiles/management/new?name-existed";
        }
        profileService.createProfile(profile);
//        operationLog.warn("User: {} ({}) created a new profile: {}",
//                authentication.getName(), authentication.getDetails(), profile.getName());
        return "redirect:/helper/appAccessMng/profiles";
    }

    @GetMapping("/appAccessMng/profiles/management/{id}")
    public String editProfileForm(@PathVariable String id, Model model) {
        if (id != null && !id.isEmpty()) {
            Optional<Profile> profile = profileService.getProfileById(id);
            profile.ifPresent(value -> model.addAttribute("profile", value));
            model.addAttribute("title", "Profile Manager (Edit)");
        }
        return "profile-management";
    }

    @PostMapping("/appAccessMng/profiles/management/edit")
    public String updateProfile(@ModelAttribute Profile profile, Authentication authentication) {
        profileService.updateProfile(profile);
//        operationLog.warn("User: {} ({}) edited profile: {}",
//                authentication.getName(), authentication.getDetails(), profile.getName());
        return "redirect:/helper/appAccessMng/profiles";
    }

    @PostMapping("/appAccessMng/profiles/management/delete/{id}")
    public String deleteProfile(@PathVariable String id, Authentication authentication) {
        profileService.deleteProfile(id);
//        operationLog.warn("User: {} ({}) delete profile: {}",
//                authentication.getName(), authentication.getDetails(), id);
        return "redirect:/helper/appAccessMng/profiles";
    }
}