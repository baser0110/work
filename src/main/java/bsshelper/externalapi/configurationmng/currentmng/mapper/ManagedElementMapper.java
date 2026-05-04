package bsshelper.externalapi.configurationmng.currentmng.mapper;


import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElementMoc;
import bsshelper.externalapi.configurationmng.currentmng.to.MocTo;
import bsshelper.globalutil.ManagedElementType;


public class ManagedElementMapper {
    static public ManagedElement toManagedElement(MocTo<ManagedElementMoc> managedElementMocTo) {
        try {
            MocTo.MocResultTo<ManagedElementMoc> result = managedElementMocTo.getResult().get(0);
            return new ManagedElement(
                    result.getMoData().get(0).getUserLabel(),
                    ManagedElementType.valueOf(result.getManagedElementType()),
                    result.getNe());
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }
}
