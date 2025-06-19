package bsshelper.localservice.paketlossstat.service;

import bsshelper.localservice.paketlossstat.to.DomainTo;

import java.util.List;

public interface PaketLossStatService {
    List<DomainTo> populateDomainData();
    void getPacketLossInDomainStat();
    void startUpdatePacketLossCacheDaily();
}
