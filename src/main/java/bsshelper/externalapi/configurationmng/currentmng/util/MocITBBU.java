package bsshelper.externalapi.configurationmng.currentmng.util;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElementMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.UCellMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.*;
import bsshelper.globalutil.ManagedElementType;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public enum MocITBBU implements MocMEType{

    CU_EUTRAN_CELL_FDD_LTE("CUEUtranCellFDDLTE", CUEUtranCellFDDLTEMoc.class),
    CU_EUTRAN_CELL_NBIOT("CUEUtranCellNBIoT", CUEUtranCellNBIoTMoc.class),
    DRY_CONTACT_CABLE ("DryContactCable", DryContactCableMoc.class),
    G_TRX("GTrx", GTrxMoc.class),
    IP ("Ip", IpMoc.class),
    MANAGED_ELEMENT("ManagedElement", ManagedElementMoc.class),
    REPLACEABLE_UNIT ("ReplaceableUnit", ReplaceableUnitMoc.class),
    RI_CABLE ("RiCable", RiCableMoc.class),
    SCTP ("Sctp", SctpMoc.class),
    TX_CHANNEL ("TxChannel", TxChannelMoc.class),
    U_CELL("UCell", UCellMoc.class),
    U_LOCAL_CELL("ULocalCell", ULocalCellMoc.class);

    private final String mocName;
    private final Class<?> clazz;

    @Override
    public String getMocName() { return mocName; }

    @SuppressWarnings("unchecked")
    public <T> Class<T> getEntityClass() {
        return (Class<T>) this.clazz;
    }

    @Override
    public ManagedElementType getManaManagedElementType() {
        return ManagedElementType.ITBBU;
    }
}
