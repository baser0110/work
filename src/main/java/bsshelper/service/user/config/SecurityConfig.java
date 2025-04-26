package bsshelper.service.user.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
//    private final SessionActivityFilter sessionActivityFilter;
//    private final SessionRegistry sessionRegistry;

//    @PostConstruct
//        public static void main() {
//            StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();
//            String plainPassword = "1234"; // Replace with actual password
//            String encodedPassword = passwordEncoder.encode(plainPassword);
//
//            System.out.println("Encoded Password: " + encodedPassword);
//            // Update this encoded password in the database for the user.
//        }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.requiresChannel(channel ->
                        channel.anyRequest().requiresSecure()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/helper/login","/helper/access-denied").permitAll()
                        .requestMatchers("/helper/login","/helper/access-denied", "/helper/packetLossStat", "/helper/packetLossStat2", "/helper/acceptanceMeasurement/chartPacketLossForDomain").permitAll()
                        .requestMatchers("/helper", "/helper/change-password").authenticated()
                        .requestMatchers(HttpMethod.GET,"/helper/dryContact/**").hasAnyAuthority("EXT_ALARM_MNG_VIEW", "EXT_ALARM_MNG_FULL")
                        .requestMatchers("/helper/dryContact/**").hasAnyAuthority("EXT_ALARM_MNG_FULL")
                        .requestMatchers(HttpMethod.GET,"/helper/cellStatus/**").hasAnyAuthority("CELL_STAT_MNG_SINGLE_VIEW", "CELL_STAT_MNG_SINGLE_FULL")
                        .requestMatchers("/helper/cellStatus/cellStatusDetails").hasAnyAuthority("CELL_STAT_MNG_SINGLE_VIEW", "CELL_STAT_MNG_SINGLE_FULL")
                        .requestMatchers("/helper/cellStatus/**").hasAnyAuthority("CELL_STAT_MNG_SINGLE_FULL")
                        .requestMatchers("/helper/cellStatusBatch/**").hasAnyAuthority("CELL_STAT_MNG_BATCH_VIEW", "CELL_STAT_MNG_BATCH_FULL")
                        .requestMatchers("/helper/acceptanceMeasurement/**").hasAnyAuthority("ACCEPT_MEASUREMENT_VIEW", "ACCEPT_MEASUREMENT_FULL")
                        .requestMatchers("/helper/appAccessMng", "/helper/appAccessMng/logs").hasAnyAuthority("USER_MNG_VIEW", "USER_MNG_FULL")
                        .requestMatchers("/helper/appAccessMng/**").hasAnyAuthority("USER_MNG_FULL")
                        .requestMatchers("/helper/vasily-tools").hasAnyAuthority("VASILY_TOOLS_VIEW", "VASILY_TOOLS_FULL")
                )
                .formLogin(form -> form
                        .loginPage("/helper/login")
                        .defaultSuccessUrl("/helper", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/helper/logout")
                        .logoutSuccessUrl("/helper/login?logout")
                        .invalidateHttpSession(true) // Invalidate the session
                        .clearAuthentication(true) // Clear authentication
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler())
                )

//                .sessionManagement(session -> session
//                        .invalidSessionUrl("/session-expired")
//                        .sessionConcurrency(concurrency -> concurrency
//                            .maximumSessions(1)
//                            .expiredUrl("/session-expired") // Redirect to custom handler
//                        )
                .sessionManagement(session ->
                    session
                            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                            .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::newSession)
                            .sessionConcurrency(concurrency -> concurrency
                                .maximumSessions(10) // Allow only one session per user
                                .expiredUrl("/helper/login?expired") // Redirect to login on session expiry
                                .maxSessionsPreventsLogin(true)// If false, terminate the previous session
                                .sessionRegistry(sessionRegistry())
                            )

                );
        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.sendRedirect("/helper/access-denied");
        };
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy(SessionRegistry sessionRegistry) {
        return new RegisterSessionAuthenticationStrategy(sessionRegistry);
    }
}

