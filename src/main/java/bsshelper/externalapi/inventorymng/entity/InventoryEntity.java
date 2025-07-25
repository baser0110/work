package bsshelper.externalapi.inventorymng.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class InventoryEntity {
    private final String packfunction;
    private final String productcode;
    private final String unitposition;
    private final String inventoryunittype;
    private final String inventoryunitid;
    private final String vendorunitfamilytype;
    private final String dateofmanufacture;
    private final String netype;
    private final String dateoflastrepair;
    private final String moid;
    private final String producttype;
    private final String networkaccesstime;
    private final String neid;
    private final String userlabel;
    private final String serialnumber;
    private final String ip;
    private final String vendorunittypename;
    private final String subnetworkid;
    private final String inventorystatus;
    private final String versionnumber;
    private final String subcardinfo;
    private final String packsilkscreen;
    private final String updatetime;
    private final String vendorname;
    private final String manualdataentry;
    private final String manufacturerdata;
}
