package bsshelper.security.security;

import bsshelper.security.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class AppUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final boolean active;
    private final boolean firstLogin;
    private final Collection<? extends GrantedAuthority> authorities;

    public AppUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.active = !user.getIsDeleted();
        this.firstLogin = user.getIsFirstLogin();
        if (firstLogin) {
            this.authorities = null;
        }
        else {
            this.authorities = user.getProfile().getAuthorities();
        }
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}