package bsshelper.service;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.plannedserv.repository.MocDataWrapper;
import bsshelper.globalutil.entity.MessageEntity;

import java.util.concurrent.ConcurrentHashMap;

public interface LocalCacheService {
    ConcurrentHashMap<String, MocDataWrapper> mocDataRepositoryMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, ManagedElement> managedElementMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, MessageEntity> messageMap = new ConcurrentHashMap<>();
    void clearCache();
}
