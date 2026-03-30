package bsshelper.externalapi.configurationmng.plannedserv.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.DryContactCableMoc;
import bsshelper.externalapi.configurationmng.plannedserv.entity.DryContactCableMocData;
import bsshelper.externalapi.configurationmng.plannedserv.to.DryContactCableMocDataTo;
import bsshelper.externalapi.configurationmng.plannedserv.util.Operation;
import bsshelper.externalapi.configurationmng.plannedserv.util.drycontactenums.AlmStatus;
import bsshelper.externalapi.configurationmng.plannedserv.util.drycontactenums.AlmUserLabel;
import bsshelper.localservice.externalcustomdata.service.CustomDataService;

import java.util.ArrayList;
import java.util.List;

public class DryContactCableMocDataMapper {
    static public DryContactCableMocDataTo toDryContactCableMocDataTo(DryContactCableMoc data) {
        return new DryContactCableMocDataTo(
                Operation.NO.toString(),
                data.getMoId(),
//                AlmUserLabel.valueOfCode(data.getAlarmNameOfInput()).toString(),
                CustomDataService.alarmCodeToAlarmUserLabelMap.get(String.valueOf(data.getAlarmNameOfInput())).getUserLabel(),
                AlmStatus.valueOfAlmStatus(data.getAlarmStatus()).toString());
    }

    static public List<DryContactCableMocDataTo> toDryContactCableMocDataTo(List<DryContactCableMoc> data) {
        if (data == null) return null;
        List<DryContactCableMocDataTo> result = new ArrayList<>();
        for (DryContactCableMoc data1 : data) {
            result.add(toDryContactCableMocDataTo(data1));
        }
        return result;
    }

    static public DryContactCableMocData toDryContactCableMocData(DryContactCableMocDataTo data) {
        return new DryContactCableMocData(
                Operation.valueOf(data.getMoOp()),
                data.getMoId(),
//                AlmUserLabel.valueOf(data.getUserLabel()),
                CustomDataService.alarmUserLabelToAlarmUserLabelMap.get(data.getUserLabel()),
                AlmStatus.valueOf(data.getAlarmStatus()));
    }

    static public List<DryContactCableMocData> toDryContactCableMocData(List<DryContactCableMocDataTo> data) {
        if (data == null) return null;
        List<DryContactCableMocData> result = new ArrayList<DryContactCableMocData>();
        for (DryContactCableMocDataTo data1 : data) {
            result.add(toDryContactCableMocData(data1));
        }
        return result;
    }
}
