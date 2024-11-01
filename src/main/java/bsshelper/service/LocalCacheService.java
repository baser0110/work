package bsshelper.service;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GGsmCellMocSimplified;
import bsshelper.externalapi.configurationmng.plannedserv.wrapper.MocDataWrapper;
import bsshelper.globalutil.entity.MessageEntity;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface LocalCacheService {
    ConcurrentHashMap<String, MocDataWrapper> mocDataRepositoryMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, ManagedElement> managedElementMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, MessageEntity> messageMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, List<GGsmCellMocSimplified>> GSMCellMap = new ConcurrentHashMap<>();
    void clearCache();
//    void setGSMCellMapCache(List<GGsmCellMocSimplified> gsmCellMocSimplified);
}
