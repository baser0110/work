package bsshelper.security.service;

import bsshelper.security.entity.User;
import bsshelper.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    private final ProfileService profileService;

//    @Transactional
//    @PostConstruct
//    public void initFistUser() {
//        Optional<User> userOptional = userRepository.findByUsername("fist_admin");
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            user.setIsFirstLogin(false);
//            user.setPassword(passwordEncoder.encode("1st_ADMIN"));
//            userRepository.save(user);
//        }
//    }

    public List<User> getAllUsers() {
//        System.out.println(userRepository.findAll());
        return userRepository.findAll();
    }

    public Optional<User> findByUsernameAndIsDeletedFalse(String username){
        return userRepository.findByUsernameAndIsDeletedFalse(username);
    }
    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
    @Transactional
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(user.getUsername().toLowerCase());
        user.setIsFirstLogin(true);
        return userRepository.save(user);
    }
    @Transactional
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        existingUser.setProfile(user.getProfile());
        return userRepository.save(existingUser);
    }

    @Transactional
    public User softDeleteUser(String id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        existingUser.setIsDeleted(true);
        return userRepository.save(existingUser);
    }

    @Transactional
    public User restoreUser(String id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        existingUser.setIsDeleted(false);
        return userRepository.save(existingUser);
    }

    @Transactional
    public User updateUserWithPassReset(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setProfile(user.getProfile());
        existingUser.setIsFirstLogin(true);
        return userRepository.save(existingUser);
    }

    @Transactional
    public User updateUserFirstPass(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setIsFirstLogin(false);
        return userRepository.save(user);
    }
}
