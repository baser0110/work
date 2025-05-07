package bsshelper.service.paketlossstat.service;

import bsshelper.service.paketlossstat.entity.DomainStat;
import bsshelper.service.paketlossstat.to.DomainTo;

import java.util.List;
import java.util.Map;

public interface PaketLossStatService {
    List<DomainTo> populateDomainData();
    void getPacketLossInDomainStat();
    void startUpdatePacketLossCacheDaily();
}
