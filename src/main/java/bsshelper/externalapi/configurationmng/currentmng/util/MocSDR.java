package bsshelper.externalapi.configurationmng.currentmng.util;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElementMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.UCellMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.*;
import bsshelper.globalutil.ManagedElementType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MocSDR implements MocMEType{

    DRY_CONTACT_DEVICE ("DryContactDevice", DryContactDeviceMoc.class),
    ETHERNET_SWITCH_DEVICE ("EthernetSwitchDevice", EthernetSwitchDeviceMoc.class),
    EUTRAN_CELL_FDD_LTE("EUtranCellFDD", EUtranCellFDDMoc.class),
    EUTRAN_CELL_NBIOT ("EUtranCellNBIoT", EUtranCellNBIoTMoc.class),
    FIBER_CABLE ("FiberCable", FiberCableMoc.class),
    G_TRX("GTrx", GTrxMoc.class),
    IP_LAYER_CONFIG ("IpLayerConfig", IpLayerConfigMoc.class),
    MANAGED_ELEMENT("ManagedElement", ManagedElementMoc.class),
    SCTP ("Sctp", SctpMoc.class),
    SDR_DEVICE_GROUP ("SdrDeviceGroup", SdrDeviceGroupMoc.class),
    TX_CHANNEL ("TxChannel", TxChannelMoc.class),
    U_CELL("UCell", UCellMoc.class),
    U_LOCAL_CELL("ULocalCell", ULocalCellMoc.class);

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
        return ManagedElementType.SDR;
    }
}
