package bsshelper.maincontroller;

import bsshelper.externalapi.alarmmng.activealarm.entity.AlarmEntity;
import bsshelper.externalapi.alarmmng.activealarm.service.ActiveAlarmService;
import bsshelper.globalutil.Severity;
import bsshelper.globalutil.entity.MessageEntity;
import bsshelper.localservice.externalcustomdata.entity.AlarmLogEntity;
import bsshelper.localservice.externalcustomdata.entity.AlarmUserLabel;
import bsshelper.localservice.externalcustomdata.entity.Comment;
import bsshelper.localservice.externalcustomdata.entity.MECustomLink;
import bsshelper.localservice.externalcustomdata.entity.VLAN;
import bsshelper.localservice.externalcustomdata.service.AlarmUserLabelService;
import bsshelper.localservice.externalcustomdata.service.CommentService;
import bsshelper.localservice.externalcustomdata.service.MECustomLinkService;
import bsshelper.localservice.externalcustomdata.service.VLANService;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/helper")
@RequiredArgsConstructor
public class AppAccessMngController {
    private final UserService userService;
    private final ProfileService profileService;
    private final AlarmUserLabelService alarmUserLabelService;
    private final LocalCacheService localCacheService;
//    private final CustomDataService customDataService;
    private final MECustomLinkService meCustomLinkService;
    private final ActiveAlarmService activeAlarmService;
    private final CommentService commentService;
    private final VLANService vlanService;
    private final TokenService tokenService;

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
        return "redirect:/helper/appAccessMng/users";
    }

    @PostMapping("/appAccessMng/users/management/delete/{id}")
    public String deleteUser(@PathVariable String id, Authentication authentication) {
        userService.deleteUser(id);
        return "redirect:/helper/appAccessMng/users";
    }

    // PROFILES

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
        return "redirect:/helper/appAccessMng/profiles";
    }

    @PostMapping("/appAccessMng/profiles/management/delete/{id}")
    public String deleteProfile(@PathVariable String id, Authentication authentication) {
        profileService.deleteProfile(id);
        return "redirect:/helper/appAccessMng/profiles";
    }

    // ALARM_USER_LABEL

    @GetMapping("/appAccessMng/dataMng/alarmUserLabel")
    public String getAllAlarmUserLabels(Model model) {
        List<AlarmUserLabel> AlarmUserLabels = alarmUserLabelService.getAllAlarmUserLabel();
        model.addAttribute("alarmUserLabels", AlarmUserLabels);
        model.addAttribute("title", "AlarmUserLabel Manager");
        return "alarm-user-label";
    }

    @GetMapping("/appAccessMng/dataMng/alarmUserLabel/management/new")
    public String createAlarmUserLabelForm(Model model) {
        model.addAttribute("alarmUserLabel", new AlarmUserLabel());
        model.addAttribute("title", "AlarmUserLabel (New)");
        return "alarm-user-label-management";
    }

    @PostMapping("/appAccessMng/dataMng/alarmUserLabel/management/create")
    public String createAlarmUserLabel(@ModelAttribute AlarmUserLabel alarmUserLabel) {
        Optional<AlarmUserLabel> dbCode = alarmUserLabelService.findByCode(alarmUserLabel.getCode());
        if (dbCode.isPresent()) {
            return "redirect:/helper/appAccessMng/dataMng/alarmUserLabel/management/new?code-existed";
        }
        alarmUserLabel.setUserLabel(alarmUserLabel.getUserLabel().toUpperCase());
        alarmUserLabelService.createAlarmUserLabel(alarmUserLabel);
        return "redirect:/helper/appAccessMng/dataMng/alarmUserLabel";
    }

    @GetMapping("/appAccessMng/dataMng/alarmUserLabel/management/{id}")
    public String editAlarmUserLabelForm(@PathVariable String id, Model model) {
        if (id != null && !id.isEmpty()) {
            Optional<AlarmUserLabel> alarmUserLabel = alarmUserLabelService.getAlarmUserLabelById(id);
            alarmUserLabel.ifPresent(value -> model.addAttribute("alarmUserLabel", value));
            model.addAttribute("title", "AlarmUserLabel Manager (Edit)");
        }
        return "alarm-user-label-management";
    }

    @PostMapping("/appAccessMng/dataMng/alarmUserLabel/management/edit")
    public String updateAlarmUserLabel(@ModelAttribute AlarmUserLabel alarmUserLabel) {
        alarmUserLabel.setUserLabel(alarmUserLabel.getUserLabel().toUpperCase());
        alarmUserLabelService.updateAlarmUserLabel(alarmUserLabel);
        return "redirect:/helper/appAccessMng/dataMng/alarmUserLabel";
    }

    @PostMapping("/appAccessMng/dataMng/alarmUserLabel/management/delete/{id}")
    public String deleteAlarmUserLabel(@PathVariable String id) {
        alarmUserLabelService.deleteAlarmUserLabel(id);
        return "redirect:/helper/appAccessMng/dataMng/alarmUserLabel";
    }

    // VLAN

    @GetMapping("/appAccessMng/dataMng/vlan")
    public String getAllVLANs(Model model) {
        List<VLAN> vlans = vlanService.getAllVlan();
        model.addAttribute("vlans", vlans);
        model.addAttribute("title", "VLAN Manager");
        return "vlan";
    }

    @GetMapping("/appAccessMng/dataMng/vlan/management/new")
    public String createVLANForm(Model model) {
        model.addAttribute("vlan", new VLAN());
        model.addAttribute("title", "VLAN (New)");
        return "vlan-management";
    }

    @PostMapping("/appAccessMng/dataMng/vlan/management/create")
    public String createVLAN(@ModelAttribute VLAN vlan) {
        Optional<VLAN> dbVlan = vlanService.findByVlan(vlan.getVlan());
        if (dbVlan.isPresent()) {
            return "redirect:/helper/appAccessMng/dataMng/vlan/management/new?vlan-existed";
        }
        vlanService.createVlan(vlan);
        vlanService.populate();
        return "redirect:/helper/appAccessMng/dataMng/vlan";
    }

    @GetMapping("/appAccessMng/dataMng/vlan/management/{id}")
    public String editVLANForm(@PathVariable String id, Model model) {
        if (id != null && !id.isEmpty()) {
            Optional<VLAN> vlan = vlanService.getVlanById(id);
            vlan.ifPresent(value -> model.addAttribute("vlan", value));
            model.addAttribute("title", "VLAN (Edit)");
        }
        return "vlan-management";
    }

    @PostMapping("/appAccessMng/dataMng/vlan/management/edit")
    public String updateVLAN(@ModelAttribute VLAN vlan) {
        vlanService.updateVlan(vlan);
        return "redirect:/helper/appAccessMng/dataMng/vlan";
    }

    @PostMapping("/appAccessMng/dataMng/vlan/management/delete/{id}")
    public String deleteVLAN(@PathVariable String id) {
        vlanService.deleteVlan(id);
        return "redirect:/helper/appAccessMng/dataMng/vlan";
    }

    // ME_CUSTOM_LINK

    @GetMapping("/appAccessMng/dataMng/meCustomLink")
    public String getAllMECustomLinks(Model model) {
        List<MECustomLink> MECustomLinks = meCustomLinkService.getAllMECustomLink();
        model.addAttribute("meCustomLinks", MECustomLinks);
        model.addAttribute("title", "MECustomLink Manager");
        return "me-custom-link";
    }

    @GetMapping("/appAccessMng/dataMng/meCustomLink/management/new")
    public String createMECustomLinkForm(Model model) {
        model.addAttribute("meCustomLink", new MECustomLink());
        model.addAttribute("title", "MECustomLink (New)");
        return "me-custom-link-management";
    }

    @PostMapping("/appAccessMng/dataMng/meCustomLink/management/create")
    public String createMECustomLink(@ModelAttribute MECustomLink meCustomLink) {
        Optional<MECustomLink> dbMECustomLink = meCustomLinkService.findByUserLabel(meCustomLink.getUserLabel().toUpperCase());
        if (dbMECustomLink.isPresent()) {
            return "redirect:/helper/appAccessMng/dataMng/meCustomLink/management/new?userLabel-existed";
        }
        meCustomLinkService.createMECustomLink(meCustomLink);
        return "redirect:/helper/appAccessMng/dataMng/meCustomLink";
    }

    @GetMapping("/appAccessMng/dataMng/meCustomLink/management/{id}")
    public String editMECustomLinkForm(@PathVariable String id, Model model) {
        if (id != null && !id.isEmpty()) {
            Optional<MECustomLink> meCustomLink = meCustomLinkService.getMECustomLinkById(id);
            meCustomLink.ifPresent(value -> model.addAttribute("meCustomLink", value));
            model.addAttribute("title", "MECustomLink Manager (Edit)");
        }
        return "me-custom-link-management";
    }

    @PostMapping("/appAccessMng/dataMng/meCustomLink/management/edit")
    public String updateMECustomLink(@ModelAttribute MECustomLink meCustomLink) {
        meCustomLinkService.updateMECustomLink(meCustomLink);
        return "redirect:/helper/appAccessMng/dataMng/meCustomLink";
    }

    @PostMapping("/appAccessMng/dataMng/meCustomLink/management/delete/{id}")
    public String deleteMECustomLink(@PathVariable String id) {
        meCustomLinkService.deleteMECustomLink(id);
        return "redirect:/helper/appAccessMng/dataMng/meCustomLink";
    }

    // COMMENT

    @GetMapping("/appAccessMng/dataMng/comment")
    public String getAllComments(Model model) {
        List<Comment> Comments = commentService.getAllComment();
        model.addAttribute("comments", Comments);
        model.addAttribute("title", "Comment Manager");
        return "comment";
    }

    @GetMapping("/appAccessMng/dataMng/comment/management/new")
    public String createCommentForm(Model model) {
        model.addAttribute("comment", new Comment());
        model.addAttribute("title", "Comment (New)");
        return "comment-management";
    }

    @PostMapping("/appAccessMng/dataMng/comment/management/create")
    public String createComment(@ModelAttribute Comment comment) {
        Optional<Comment> dbComment = commentService.findByComment(comment.getComment());
        if (dbComment.isPresent()) {
            return "redirect:/helper/appAccessMng/dataMng/comment/management/new?comment-existed";
        }
        commentService.createComment(comment);
        return "redirect:/helper/appAccessMng/dataMng/comment";
    }

    @GetMapping("/appAccessMng/dataMng/comment/management/{id}")
    public String editCommentForm(@PathVariable String id, Model model) {
        if (id != null && !id.isEmpty()) {
            Optional<Comment> comment = commentService.getCommentById(id);
            comment.ifPresent(value -> model.addAttribute("comment", value));
            model.addAttribute("title", "Comment Manager (Edit)");
        }
        return "comment-management";
    }

    @PostMapping("/appAccessMng/dataMng/comment/management/delete/{id}")
    public String deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
        return "redirect:/helper/appAccessMng/dataMng/comment";
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