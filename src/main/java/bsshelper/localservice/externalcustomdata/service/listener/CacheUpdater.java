package bsshelper.localservice.externalcustomdata.service.listener;

public interface CacheUpdater {
    void populate();
    default boolean supports(Class<?> clazz) { return false; }
}
