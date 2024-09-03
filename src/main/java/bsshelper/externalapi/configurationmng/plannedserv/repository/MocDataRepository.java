package bsshelper.externalapi.configurationmng.plannedserv.repository;

import bsshelper.externalapi.configurationmng.plannedserv.entity.DryContactDeviceMocData;
import bsshelper.externalapi.configurationmng.plannedserv.entity.MocData;

import java.util.List;

public interface MocDataRepository {
    void addNew();
    List<MocData> getDelFinalData();
    List<MocData> getModAndAddFinalData();
}
