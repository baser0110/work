package bsshelper.service;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GGsmCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.service.CurrentMgnServiceImpl;
import bsshelper.externalapi.configurationmng.plannedserv.wrapper.MocDataWrapper;
import bsshelper.externalapi.openscriptexecengine.entity.CellStatusDetails;
import bsshelper.externalapi.perfmng.entity.HistoryRTWP;
import bsshelper.externalapi.perfmng.wrapper.HistoryRTWPWrapper;
import bsshelper.globalutil.entity.MessageEntity;
import bsshelper.service.paketlossstat.entity.DomainStat;
import org.apache.groovy.util.concurrent.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public interface LocalCacheService {
    ConcurrentHashMap<String, MocDataWrapper> mocDataRepositoryMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, ManagedElement> managedElementMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, MessageEntity> messageMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, HistoryRTWPWrapper> historyRTWPMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, List<CellStatusDetails>> cellStatusDetailsMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, List<UUtranCellFDDMocSimplified>> UMTSCellMap = new ConcurrentHashMap<>();

    ConcurrentHashMap<String, CurrentMgnServiceImpl.CellInfo> gsmMRNCMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, CurrentMgnServiceImpl.CellInfo> umtsSDRMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, CurrentMgnServiceImpl.CellInfo> umtsITBBUMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, CurrentMgnServiceImpl.CellInfo> nbiotSDRMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, CurrentMgnServiceImpl.CellInfo> nbiotITBBUMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, String> meByNEMap = new ConcurrentHashMap<>();

    Map<String, DomainStat> packetLostCache = Collections.synchronizedMap(new LinkedHashMap<>());
    Map<String, String> packetLostDatesCache = new ConcurrentHashMap<>();

    void clearCache();
}
