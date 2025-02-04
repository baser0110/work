package bsshelper.service.user.service;

import bsshelper.service.user.entity.User;
import bsshelper.service.user.repository.UserRepository;
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
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

//    public Page<User> findUsers(String username, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        if (username == null || username.isEmpty()) {
//            return userRepository.findAll(pageable);
//        } else {
//            return userRepository.findByUsernameContainingIgnoreCase(username, pageable);
//        }
//    }

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
