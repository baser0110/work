package bsshelper.service;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.plannedserv.repository.MocDataRepository;

import java.util.concurrent.ConcurrentHashMap;

public interface LocalCacheService {
    ConcurrentHashMap<String, MocDataRepository> mocDataRepositoryMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, ManagedElement> managedElementMap = new ConcurrentHashMap<>();
    void clearCache();
}