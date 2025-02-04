package bsshelper.service.user.service;

import bsshelper.service.user.entity.Profile;
import bsshelper.service.user.entity.User;
import bsshelper.service.user.repository.ProfileRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }
    public Optional<Profile> findByName(String name) {return profileRepository.findByName(name);}
    @Transactional
    public Profile createProfile(Profile profile) {
        profile.setName(profile.getName().toLowerCase());
        return profileRepository.save(profile);
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