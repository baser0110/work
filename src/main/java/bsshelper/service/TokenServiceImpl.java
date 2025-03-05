package bsshelper.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.auth.service.AuthService;
import bsshelper.globalutil.entity.UserEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final AuthService authService;
    private final AtomicReference<Token> token = new AtomicReference<>();


    @Override
    public Token getToken() {
        return token.get();
    }

    @PostConstruct
    @Override
    public void setTokenWithHandSake() {
        updateTokenDaily();
        handshake();
    }

    private void handshake() {
        log.info(" >> handshake is started");
        try {
            ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleWithFixedDelay(() -> {
                authService.updateToken(token.get());
                log.info(" >> token is updated");
//                System.out.println(">> token is updated");
            }, 29, 29, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error(" >> error in token update: {}", e.toString());
        }
    }

    private void updateTokenDaily() {
        log.info(" >> updateTokenDaily is started");
        token.set(authService.getToken());
        log.info(" >> new token is set");
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime nextRun = now.withHour(5).withMinute(0).withSecond(0);

            if (now.compareTo(nextRun) > 0) {
                nextRun = nextRun.plusDays(1);
            }

            long initialDelay = ChronoUnit.SECONDS.between(now, nextRun);
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

            scheduler.scheduleAtFixedRate(() -> {
                token.set(authService.getToken());
                log.info(" >> new token is set");
            }, initialDelay, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(" >> error in token setting: {}", e.toString());
        }
    }
}
