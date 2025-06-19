package bsshelper.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Pattern;

@Configuration
public class PasswordConfig {
    private final static String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d!?@#$%^&*()_]{8,}$";
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    public static boolean isPasswordValid(String  password) {
        return Pattern.matches(PASSWORD_PATTERN, password);
    }
}
