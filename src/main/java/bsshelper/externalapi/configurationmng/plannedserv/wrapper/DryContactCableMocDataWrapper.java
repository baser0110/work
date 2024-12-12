package bsshelper.externalapi.configurationmng.plannedserv.wrapper;

import bsshelper.externalapi.configurationmng.plannedserv.entity.MocData;
import bsshelper.externalapi.configurationmng.plannedserv.mapper.DryContactCableMocDataMapper;
import bsshelper.externalapi.configurationmng.plannedserv.to.DryContactCableMocDataTo;
import bsshelper.externalapi.configurationmng.plannedserv.util.Operation;
import bsshelper.externalapi.configurationmng.plannedserv.util.drycontactenums.AlmStatus;
import bsshelper.externalapi.configurationmng.plannedserv.util.drycontactenums.AlmUserLabel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@RequiredArgsConstructor
public class DryContactCableMocDataWrapper implements MocDataWrapper {
    private List<DryContactCableMocDataTo> data;
    private final List<Integer> constMoIds = new ArrayList<>();

    public DryContactCableMocDataWrapper(List<DryContactCableMocDataTo> list) {
        if (list == null) data = new ArrayList<>();
        else {
            list.sort(Comparator.comparingInt(DryContactCableMocDataTo::getMoId));
            data = list;
            list.forEach((entry) -> constMoIds.add(entry.getMoId()));
        }
    }

    public void addNew() {
        if (data == null) data = new ArrayList<>();
        DryContactCableMocDataTo newData = new DryContactCableMocDataTo(
                Operation.A.toString(),
                16,
                AlmUserLabel.AC_MAINS_FAILURE.toString(),
                AlmStatus.OPEN.toString());
        data.add(newData);
    }

    public List<MocData> getDelFinalData() {
        List<MocData> result = new ArrayList<>();
        if (data != null) {
            data.forEach((entry) -> {
                if (entry.getMoOp().equals(Operation.D.toString()))
                    result.add(DryContactCableMocDataMapper.toDryContactCableMocData(entry));
            });
        }
        return result;
    }
    public List<MocData> getModAndAddFinalData() {
        List<MocData> result = new ArrayList<>();
        if (data != null) {
            data.forEach((entry) -> {
                if (entry.getMoOp().equals(Operation.A.toString()) || entry.getMoOp().equals(Operation.M.toString()))
                    result.add(DryContactCableMocDataMapper.toDryContactCableMocData(entry));
            });
        }
        return result;
    }
}
