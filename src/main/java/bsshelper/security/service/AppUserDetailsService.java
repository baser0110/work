package bsshelper.security.service;

import bsshelper.security.entity.User;
import bsshelper.security.repository.UserRepository;
import bsshelper.security.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new AppUserDetails((User) user)) // Explicit lambda usage
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
