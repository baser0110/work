package bsshelper.service.user.service;

import bsshelper.service.user.entity.Permission;
import bsshelper.service.user.entity.Profile;
import bsshelper.service.user.entity.User;
import bsshelper.service.user.repository.ProfileRepository;
import bsshelper.service.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Profile> getAllProfiles() {
//        System.out.println(profileRepository.findAll());
        return profileRepository.findAll();
    }
    public Optional<Profile> findByName(String name) {return profileRepository.findByName(name);}

    @Transactional
    @PostConstruct
    public void initFistProfileAndUser() {
        Optional<Profile> profileOptional = profileRepository.findByName("bss_team");
        if (profileOptional.isEmpty()) {
            Profile profile = new Profile();
            profile.setName("bss_team");
            profile.setExtAlarmMng(Permission.FULL);
            profile.setCellStatMngSingle(Permission.FULL);
            profile.setCellStatMngBatch(Permission.FULL);
            profile.setAcceptMeasurement(Permission.FULL);
            profile.setVasilyTools(Permission.FULL);
            profile.setUserMng(Permission.FULL);
            profileRepository.save(profile);
        }
        Optional<User> userOptional = userRepository.findByUsername("fist_admin");
        if (userOptional.isEmpty()) {
            User user = new User();
            user.setUsername("fist_admin");
            user.setPassword(passwordEncoder.encode("1st_ADMIN"));
            user.setProfile(profileRepository.findByName("bss_team").get());
            user.setIsFirstLogin(false);
            userRepository.save(user);
        }
    }

    @Transactional
    public Profile createProfile(Profile profile) {
        profile.setName(profile.getName().toLowerCase());
        return profileRepository.save(profile);
    }

    public String getProfileIdByName(String name) {
        Optional<Profile> byName = profileRepository.findByName(name);
        return byName.map(Profile::getId).orElse(null);
    }

    public Optional<Profile> getProfileById(String id) {
        return profileRepository.findById(id);
    }
    @Transactional
    public Profile updateProfile(Profile profile) {
        Profile existingProfile = profileRepository.findById(profile.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        existingProfile.setName(profile.getName());
        existingProfile.setExtAlarmMng(profile.getExtAlarmMng());
        existingProfile.setCellStatMngSingle(profile.getCellStatMngSingle());
        existingProfile.setCellStatMngBatch(profile.getCellStatMngBatch());
        existingProfile.setAcceptMeasurement(profile.getAcceptMeasurement());
        existingProfile.setVasilyTools(profile.getVasilyTools());
        existingProfile.setUserMng(profile.getUserMng());
        return profileRepository.save(existingProfile);
    }
    @Transactional
    public void deleteProfile(String id) {
        profileRepository.deleteById(id);
    }
}