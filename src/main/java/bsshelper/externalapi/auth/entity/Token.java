package bsshelper.externalapi.auth.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private String accessToken;
    private long expires;
    @Setter
    private LocalDateTime updated;
}
