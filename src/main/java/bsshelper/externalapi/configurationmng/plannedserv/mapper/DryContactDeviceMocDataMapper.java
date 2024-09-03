package bsshelper.externalapi.configurationmng.plannedserv.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.DryContactDeviceMoc;
import bsshelper.externalapi.configurationmng.plannedserv.entity.DryContactDeviceMocData;
import bsshelper.externalapi.configurationmng.plannedserv.to.DryContactDeviceMocDataTo;
import bsshelper.externalapi.configurationmng.plannedserv.util.Operation;
import bsshelper.externalapi.configurationmng.plannedserv.util.drycontactenums.AlmStatus;
import bsshelper.externalapi.configurationmng.plannedserv.util.drycontactenums.AlmUserLabel;

import java.util.ArrayList;
import java.util.List;

public class DryContactDeviceMocDataMapper {
    static public DryContactDeviceMocDataTo toDryContactDeviceMocDataTo(DryContactDeviceMoc data) {
        return new DryContactDeviceMocDataTo(
                Operation.NO.toString(),
                data.getMoId(),
                data.getDryNo(),
                AlmUserLabel.valueOfUserLabel(data.getUserLabel()).toString(),
                AlmStatus.valueOfAlmStatus(data.getAlmStatus()).toString());
    }

    static public List<DryContactDeviceMocDataTo> toDryContactDeviceMocDataTo(List<DryContactDeviceMoc> data) {
        if (data == null) return null;
        List<DryContactDeviceMocDataTo> result = new ArrayList<>();
        for (DryContactDeviceMoc data1 : data) {
            if (data1.getLdn().contains("Slot=18")) continue;
            result.add(toDryContactDeviceMocDataTo(data1));
        }
        return result;
    }

    static public DryContactDeviceMocData toDryContactDeviceMocData(DryContactDeviceMocDataTo data) {
        return new DryContactDeviceMocData(
                Operation.valueOf(data.getMoOp()),
                data.getMoId(),
                data.getDryNo(),
                AlmUserLabel.valueOf(data.getUserLabel()),
                AlmStatus.valueOf(data.getAlmStatus()));
    }

    static public List<DryContactDeviceMocData> toDryContactDeviceMocData(List<DryContactDeviceMocDataTo> data) {
        if (data == null) return null;
        List<DryContactDeviceMocData> result = new ArrayList<DryContactDeviceMocData>();
        for (DryContactDeviceMocDataTo data1 : data) {
            result.add(toDryContactDeviceMocData(data1));
        }
        return result;
    }
}
