package bsshelper.maincontroller;

import bsshelper.externalapi.alarmmng.activealarm.entity.AlarmEntity;
import bsshelper.externalapi.alarmmng.activealarm.service.ActiveAlarmService;
import bsshelper.globalutil.Severity;
import bsshelper.globalutil.entity.MessageEntity;
import bsshelper.localservice.externalcustomdata.entity.AlarmLogEntity;
import bsshelper.localservice.externalcustomdata.service.CustomDataService;
import bsshelper.localservice.localcache.LocalCacheService;
import bsshelper.localservice.token.TokenService;
import bsshelper.security.config.PasswordConfig;
import bsshelper.security.entity.Profile;
import bsshelper.security.entity.User;
import bsshelper.security.service.ProfileService;
import bsshelper.security.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static bsshelper.localservice.localcache.LocalCacheService.managedElementMap;

@Slf4j
@Controller
@RequestMapping("/helper")
@RequiredArgsConstructor
public class AppAccessMngController {
    private final UserService userService;
    private final ProfileService profileService;
    private final LocalCacheService localCacheService;
    private final CustomDataService customDataService;

    private final ActiveAlarmService activeAlarmService;
    private final TokenService tokenService;
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

    // CUSTOM DATA MNG PART

    @GetMapping("/appAccessMng/dataMng")
    public String getDataMng(Model model,  HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        model.addAttribute("title", "Application Data Manager");
        return "app-data";
    }

    @PostMapping("/appAccessMng/dataMng/ClearMeCache")
    public String clearMeCache(Authentication authentication, HttpSession session) {
        String id = session.getId();
        MessageEntity message;
        try {
            log.info(" >> managedElementMap size " + LocalCacheService.managedElementMap.size());
            LocalCacheService.managedElementMap.clear();
            log.info(" >> managedElementMap cache has been cleared");
            log.info(" >> managedElementMap size " + LocalCacheService.managedElementMap.size());

            message = new MessageEntity(Severity.SUCCESS, "Update successfully");
        } catch (Exception e) {
            message = new MessageEntity(Severity.ERROR, "Error in Updating process");
        }

        localCacheService.messageMap.put(id, message);

        return "redirect:/helper/appAccessMng/dataMng";
    }

    @PostMapping("/appAccessMng/dataMng/updateMECustomLink")
    public String updateMECustomLink(Authentication authentication, HttpSession session) {
        String id = session.getId();
        MessageEntity message;
        try {
            customDataService.populateMECustomLink();

            message = new MessageEntity(Severity.SUCCESS, "Update successfully");
        } catch (Exception e) {
            message = new MessageEntity(Severity.ERROR, "Error in Updating process");
        }

        localCacheService.messageMap.put(id, message);

        return "redirect:/helper/appAccessMng/dataMng";
    }

    @PostMapping("/appAccessMng/dataMng/updateAlarmUserLabel")
    public String updateAlarmUserLabel(Authentication authentication, HttpSession session) {
        String id = session.getId();
        MessageEntity message;
        try {
            customDataService.populateAlarmUserLabel();

            message = new MessageEntity(Severity.SUCCESS, "Update successfully");
        } catch (Exception e) {
            message = new MessageEntity(Severity.ERROR, "Error in Updating process");
        }

        localCacheService.messageMap.put(id, message);

        return "redirect:/helper/appAccessMng/dataMng";
    }

    @PostMapping("/appAccessMng/dataMng/updateComments")
    public String updateComments(Authentication authentication, HttpSession session) {
        String id = session.getId();
        MessageEntity message;
        try {
            customDataService.populateComments();

            message = new MessageEntity(Severity.SUCCESS, "Update successfully");
        } catch (Exception e) {
            message = new MessageEntity(Severity.ERROR, "Error in Updating process");
        }

        localCacheService.messageMap.put(id, message);

        return "redirect:/helper/appAccessMng/dataMng";
    }

    @GetMapping("/appAccessMng/restore")
    public String getRestoreFunc(Model model,  HttpSession session) {
        String id = session.getId();
        setMessage(id, model);
        model.addAttribute("title", "Restore Comments SDR/ITBBU");
        return "restore-comments";
    }

    @PostMapping("/appAccessMng/restore/restoreCommentsFromAlarmLog")
    public String restoreCommentsFromAlarmLog(@RequestParam("csvFile") MultipartFile csvFile,
                                              @RequestParam("excelFile") MultipartFile excelFile,
                                              Authentication authentication, HttpSession session) {
        String id = session.getId();
        MessageEntity message;

        Map<String, List<AlarmLogEntity>> alarmLogEntityMap = populateFromExcelAlarmLog(excelFile);
        List<String> siteIdList = populateNEList(csvFile);

        if (siteIdList == null || siteIdList.isEmpty() || alarmLogEntityMap == null || alarmLogEntityMap.isEmpty()) {
            return "redirect:/helper/appAccessMng/restore";
        }

        try {
            List<AlarmEntity> alm = activeAlarmService.alarmDataExport(tokenService.getToken(),
                    activeAlarmService.getActiveAlarmBySDRSiteList(tokenService.getToken(), siteIdList),
                    null);

            for (AlarmEntity a: alm) {
                if (a.getCommenttext().isEmpty()) {
                    if (alarmLogEntityMap.containsKey(a.getMename())) {
                        for (AlarmLogEntity al: alarmLogEntityMap.get(a.getMename())) {
                            if (a.getAlarmcode().equals(al.getAlarmCode())
                                    && a.getPositionname().equals(al.getPosition())
                                    && a.getRan_fm_alarm_dn().getValue().equals(al.getDN())
                            ) {
                                activeAlarmService.setAlarmComment(tokenService.getToken(),
                                        activeAlarmService.setAlarmCommentRequest(tokenService.getToken(), List.of(a.getId()), al.getCommentInformation()),
                                        null);
                                break;
                            }
                        }
                    }
                }
            }

            message = new MessageEntity(Severity.SUCCESS, "Restore successfully");
        } catch (Exception e) {
            message = new MessageEntity(Severity.ERROR, "Error in Restoring process");
        }

        localCacheService.messageMap.put(id, message);

        return "redirect:/helper/appAccessMng/restore";
    }

    private Map<String, List<AlarmLogEntity>> populateFromExcelAlarmLog(MultipartFile file) {
        Map<String, List<AlarmLogEntity>> result = new ConcurrentHashMap<>();

        if (file == null || file.isEmpty()) {
            log.warn(" >> Excel file is null or empty");
            return null;
        }

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet1 = workbook.getSheetAt(0);
            if (sheet1 == null) return null;
            Iterator<Row> rows = sheet1.iterator();
            if (!rows.hasNext()) return null;
            int cellNum_ME = -1, cellNum_AlarmCode = -1, cellNum_Position = -1,
                    cellNum_CommentInformation = -1, cellNum_DN = -1;
            Row headerRow = rows.next();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                String name = getCellAlwaysString(headerRow.getCell(i));
                switch (name) {
                    case "ME" -> cellNum_ME = i;
                    case "Alarm Code" -> cellNum_AlarmCode = i;
                    case "Position" -> cellNum_Position = i;
                    case "Comment Information" -> cellNum_CommentInformation = i;
                    case "DN" -> cellNum_DN = i;
                }
            }
            if (cellNum_ME == -1 || cellNum_AlarmCode == -1 || cellNum_Position == -1 || cellNum_CommentInformation == -1 || cellNum_DN == -1 ) {
                log.warn(" >> Some of columns not found in Excel file");
                return null;
            }
            while (rows.hasNext()) {
                Row row = rows.next();
                if (row.getCell(cellNum_ME) == null) continue;

                String ME = getCellAlwaysString(row.getCell(cellNum_ME));
                String AlarmCode = getCellAlwaysString(row.getCell(cellNum_AlarmCode));
                String Position = getCellAlwaysString(row.getCell(cellNum_Position));
                String CommentInformation = getCellAlwaysString(row.getCell(cellNum_CommentInformation));
                String DN = getCellAlwaysString(row.getCell(cellNum_DN));

                if (CommentInformation.isEmpty()) continue;

                result.computeIfAbsent(ME, k -> new ArrayList<>())
                        .add(new AlarmLogEntity(ME, AlarmCode, Position, CommentInformation, DN));
            }
            log.info(" >> Excel AlarmLog loaded: {} MEs processed", result.size());
        } catch (IOException e) {
            log.error(" >> Error in Excel AlarmLog file reading: {}", e.getMessage());
        }
        return result;
    }

    private List<String> populateNEList(MultipartFile file) {
        List<String> result = new ArrayList<>();

        if (file == null || file.isEmpty()) {
            log.warn(" >> CSV file is empty or null");
            return null;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            List<String> allLines = br.lines().collect(Collectors.toList());
            if (allLines.size() <= 1) return null;
            allLines.stream().skip(1).forEach(line -> {
                if (line != null && !line.trim().isEmpty()) {
                    String[] columns = line.split(",");
                    if (columns.length > 4) {
                        String entry = columns[4].trim() + "," + columns[3].trim();
                        result.add(entry);
                    }
                }
            });
            log.info(" >> Successfully loaded {} sites from CSV", result.size());
        } catch (IOException e) {
            log.error(" >> Error in CSV file reading: {}", e.getMessage());
        }
        return result;
    }

    private String getCellAlwaysString(Cell cell) {
        String value;
        if (cell.getCellType() == CellType.NUMERIC) {
            value = String.valueOf((long) cell.getNumericCellValue());
        } else {
            value = cell.getStringCellValue();
        }
        return value;
    }

    private void setMessage(String sessionId, Model model) {
        MessageEntity message = localCacheService.messageMap.get(sessionId);
        if (message != null) {
            model.addAttribute("message", localCacheService.messageMap.get(sessionId));
            localCacheService.messageMap.remove(sessionId);
        } else model.addAttribute("message", null);
    }
}