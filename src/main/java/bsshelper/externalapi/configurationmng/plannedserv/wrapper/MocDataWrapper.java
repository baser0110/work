package bsshelper.externalapi.configurationmng.plannedserv.wrapper;

import bsshelper.externalapi.configurationmng.plannedserv.entity.MocData;

import java.util.List;

public interface MocDataWrapper {
    void addNew();
    List<MocData> getDelFinalData();
    List<MocData> getModAndAddFinalData();
}
