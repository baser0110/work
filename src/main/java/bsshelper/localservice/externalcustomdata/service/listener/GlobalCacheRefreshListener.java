package bsshelper.localservice.externalcustomdata.service.listener;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import java.util.List;

@Component
public class GlobalCacheRefreshListener {

    private final List<CacheUpdater> updaters;

    public GlobalCacheRefreshListener(@Lazy List<CacheUpdater> updaters) {
        this.updaters = updaters;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCacheRefresh(CacheRefreshEvent<?> event) {
        Object entity = event.getEntity();
        if (entity == null) return;

        updaters.stream()
                .filter(u -> u.supports(entity.getClass()))
                .findFirst()
                .ifPresent(CacheUpdater::populate);
    }
}

