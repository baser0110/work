package bsshelper.localservice.externalcustomdata.service.listener;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CacheRefreshEvent<T> {
    private final T entity;
}
