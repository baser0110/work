package bsshelper.externalapi.configurationmng.currentmng.util;

import bsshelper.globalutil.ManagedElementType;

public interface MocMEType {
    String getMocName();
    <T> Class<T> getEntityClass();
    ManagedElementType getManaManagedElementType();

}
