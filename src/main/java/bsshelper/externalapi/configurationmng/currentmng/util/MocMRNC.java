package bsshelper.externalapi.configurationmng.currentmng.util;

import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.*;
import bsshelper.globalutil.ManagedElementType;
import lombok.AllArgsConstructor;

import lombok.Getter;
@AllArgsConstructor
public enum MocMRNC implements MocMEType{

    G_GSM_CELL ("GGsmCell", GGsmCellMoc.class),
    G_TRX ("GTrx", GTrxMoc.class),
    U_IUB_LINK ("UIubLink", UIubLinkMoc.class),
    U_UTRAN_CELL_FDD ("UUtranCellFDD", UUtranCellFDDMoc.class);

    private final String mocName;
    private final Class<?> clazz;

    @Override
    public String getMocName() { return mocName; }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<T> getEntityClass() {
        return (Class<T>) this.clazz;
    }

    @Override
    public ManagedElementType getManaManagedElementType() {
        return ManagedElementType.MRNC;
    }
}
