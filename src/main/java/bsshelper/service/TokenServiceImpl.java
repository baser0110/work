package bsshelper.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.auth.service.AuthService;
import bsshelper.globalutil.exception.CustomNetworkConnectionException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
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
    private final AtomicReference<Boolean> isHandshakeFailed = new AtomicReference<>(false);

    @Override
    public Token getToken() {
        if (token.get() == null) {
            throw new CustomNetworkConnectionException("Token is null");
        }
        return token.get();
    }

    @PostConstruct
    @Override
    public void setTokenWithHandSake() {
        updateTokenDaily();
        handshake();
        tokenReestablish();
    }

    private void handshake() {
        log.info(" >> handshake is started");
        isHandshakeFailed.set(false);
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleWithFixedDelay(() -> {
            try {
                if (token.get() != null) {
                    if (authService.updateToken(token.get())) {
                        log.info(" >> token is updated");
                    } else {
                        setTrueIsHandshakeFailed(timer);
                    }
                } else {
                    setTrueIsHandshakeFailed(timer);
                }
            } catch (Exception e) {
                log.error(" >> error in token update: {}", e.toString());
                setTrueIsHandshakeFailed(timer);
            }
        }, 29, 29, TimeUnit.MINUTES);
    }

    private void setTrueIsHandshakeFailed(ScheduledExecutorService timer) {
        isHandshakeFailed.set(true);
        timer.shutdown();
        log.info(" >> handshake is failed");
    }

    private void updateTokenDaily() {
        log.info(" >> updateTokenDaily is started");
        token.set(authService.getToken());
        log.info(" >> new token is set");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.withHour(5).withMinute(0).withSecond(0);
        if (now.isAfter(nextRun)) {
            nextRun = nextRun.plusDays(1);
        }
        long initialDelay = ChronoUnit.SECONDS.between(now, nextRun);
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                token.set(authService.getToken());
                log.info(" >> new token is set");
            } catch (Exception e) {
                log.error(" >> error in updateTokenDaily: {}", e.toString());
            }
        }, initialDelay, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
    }

    private void tokenReestablish() {
        log.info(" >> tokenReestablish is started");
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleWithFixedDelay(() -> {
            try {
//                System.out.println("tokenReestablish : " + (token.get() == null) + " " + isHandshakeFailed.get());
                if (token.get() == null || isHandshakeFailed.get()) {
                    token.set(authService.getToken());
                    if (token.get() != null) {
                        log.info(" >> new token is reestablished");
                        handshake();
                    }
                }
            } catch (Exception e) {
                log.error(" >> error in token setting: {}", e.toString());
            }
        }, 15, 15, TimeUnit.SECONDS);
    }
}
