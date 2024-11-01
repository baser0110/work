package bsshelper.externalapi.configurationmng.plannedserv.wrapper;

import bsshelper.externalapi.configurationmng.plannedserv.entity.MocData;
import bsshelper.externalapi.configurationmng.plannedserv.mapper.DryContactDeviceMocDataMapper;
import bsshelper.externalapi.configurationmng.plannedserv.to.DryContactDeviceMocDataTo;
import bsshelper.externalapi.configurationmng.plannedserv.util.Operation;
import bsshelper.externalapi.configurationmng.plannedserv.util.drycontactenums.AlmStatus;
import bsshelper.externalapi.configurationmng.plannedserv.util.drycontactenums.AlmUserLabel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Data
@RequiredArgsConstructor
public class DryContactDeviceMocDataWrapper implements MocDataWrapper {
    private List<DryContactDeviceMocDataTo> data;
    private final List<Integer> constMoIds = new ArrayList<>();

    public DryContactDeviceMocDataWrapper(List<DryContactDeviceMocDataTo> list) {
        if (list == null) data = new ArrayList<>();
        else {
            list.sort(Comparator.comparingInt(DryContactDeviceMocDataTo::getDryNo));
            data = list;
            list.forEach((entry) -> constMoIds.add(entry.getMoId()));
        }
    }

    public void addNew() {
        DryContactDeviceMocDataTo newData = new DryContactDeviceMocDataTo(
                Operation.A.toString(),
                32,
                15,
                AlmUserLabel.AC_MAINS_FAILURE.toString(),
                AlmStatus.OPEN.toString());
        data.add(newData);
    }

    public List<MocData> getDelFinalData() {
        List<MocData> result = new ArrayList<>();
        if (data != null) {
            data.forEach((entry) -> {
                if (entry.getMoOp().equals(Operation.D.toString()))
                    result.add(DryContactDeviceMocDataMapper.toDryContactDeviceMocData(entry));
            });
        }
        return result;
    }

    public List<MocData> getModAndAddFinalData() {
        List<MocData> result = new ArrayList<>();
        if (data != null) {
            data.forEach((entry) -> {
                if (entry.getMoOp().equals(Operation.A.toString()) || entry.getMoOp().equals(Operation.M.toString()))
                    result.add(DryContactDeviceMocDataMapper.toDryContactDeviceMocData(entry));
            });
        }
        return result;
    }
}
