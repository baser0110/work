package bsshelper.localservice.localcache;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
@RequiredArgsConstructor
public class LocalCacheServiceImpl implements LocalCacheService {

    @PostConstruct
    @Override
    public void clearCache() {
        clearCacheDaily();
    }

    private void clearCacheDaily() {
        log.info(" >> clearCacheDaily is started");
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime nextRun = now.withHour(5).withMinute(0).withSecond(0);

            if (now.compareTo(nextRun) > 0) {
                nextRun = nextRun.plusDays(1);
            }

            long initialDelay = ChronoUnit.SECONDS.between(now, nextRun);
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

            scheduler.scheduleAtFixedRate(() -> {
                log.info(" >> mocDataRepositoryMap size " + mocDataRepositoryMap.size());
                log.info(" >> managedElementMap size " + managedElementMap.size());
                log.info(" >> historyRTWPMap size " + historyRTWPMap.size());
                log.info(" >> historyRTWPMap size " + cellStatusDetailsMap.size());
                log.info(" >> historyRTWPMap size " + UMTSCellMap.size());
                log.info(" >> historyRTWPMap size " + messageMap.size());

                mocDataRepositoryMap.clear();
                log.info(" >> mocDataRepositoryMap cache has been cleared");
                managedElementMap.clear();
                log.info(" >> managedElementMap cache has been cleared");
                historyRTWPMap.clear();
                log.info(" >> historyRTWPMap cache has been cleared");
                cellStatusDetailsMap.clear();
                log.info(" >> cellStatusDetailsMap cache has been cleared");
                UMTSCellMap.clear();
                log.info(" >> UMTSCellMap cache has been cleared");
                messageMap.clear();
                log.info(" >> messageMap cache has been cleared");

                log.info(" >> mocDataRepositoryMap size " + mocDataRepositoryMap.size());
                log.info(" >> managedElementMap size " + managedElementMap.size());
                log.info(" >> historyRTWPMap size " + historyRTWPMap.size());
                log.info(" >> historyRTWPMap size " + cellStatusDetailsMap.size());
                log.info(" >> historyRTWPMap size " + UMTSCellMap.size());
                log.info(" >> historyRTWPMap size " + messageMap.size());
            }, initialDelay, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(" >> error in clearCache: {}", e.toString());
        }
    }
}
