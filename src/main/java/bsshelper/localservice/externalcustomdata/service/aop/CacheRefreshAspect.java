package bsshelper.localservice.externalcustomdata.service.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
public class CacheRefreshAspect {

    @AfterReturning("@annotation(bsshelper.localservice.externalcustomdata.service.aop.RefreshCache) && target(service)")
    public void refresh(CachePopulator service) {

        if (TransactionSynchronizationManager.isActualTransactionActive()) {

            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            service.populate();
                        }
                    }
            );
        } else {
            service.populate();
        }
    }
}
