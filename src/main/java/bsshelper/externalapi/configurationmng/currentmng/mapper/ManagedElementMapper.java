package bsshelper.externalapi.configurationmng.currentmng.mapper;


import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.to.ManagedElementMocTo;


public class ManagedElementMapper {
    static public ManagedElement toManagedElement(ManagedElementMocTo managedElementMocTo) {
        ManagedElementMocTo.ManagedElementMocResultTo result = managedElementMocTo.getResult().get(0);
        return new ManagedElement(result.getMoData().get(0).getUserLabel(), result.getManagedElementType(), result.getNe());
    }
}
