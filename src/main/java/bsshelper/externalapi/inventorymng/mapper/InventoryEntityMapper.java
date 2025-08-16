package bsshelper.externalapi.inventorymng.mapper;

import bsshelper.externalapi.inventorymng.entity.InventoryEntity;
import bsshelper.globalutil.ManagedElementType;

import java.util.List;
import java.util.TreeMap;

public class InventoryEntityMapper {
    static public TreeMap<String,String> getInventoryEntityMap(List<InventoryEntity> inventoryEntityList) {
        TreeMap<String,String> result = new TreeMap<>();
        if (inventoryEntityList == null) return result;
        if (inventoryEntityList.get(0).getNetype().equals(ManagedElementType.SDR.toString())) {
            for (InventoryEntity inv : inventoryEntityList) {
                String position = inv.getUnitposition();
                String unitType = inv.getInventoryunittype();
                if (position.contains("rack=1,shelf=1,slot=")) {
                    result.put(position.replace("rack=1,shelf=1,slot=", "Slot"),
                            inv.getVendorunittypename() + " (" + inv.getSerialnumber() + ")");
                    continue;
                }
                if (unitType.equals("RRU")) {
                    result.put(position.replace("rack=", "").replace(",shelf=1,slot=1", ""),
                            inv.getVendorunittypename() + " (" + inv.getSerialnumber() + ")");
                }
            }
        } else  if (inventoryEntityList.get(0).getNetype().equals(ManagedElementType.ITBBU.toString())) {
            for (InventoryEntity inv : inventoryEntityList) {
                String position = inv.getUnitposition();
                if (position.contains("subrack=1,slot=") && !position.contains(",grade=1")) {
                    result.put(position.replace("subrack=1,slot=", "Slot"),
                            inv.getVendorunittypename() + " (" + inv.getSerialnumber() + ")");
                    continue;
                }
                if (position.contains(",grade=1") && !position.contains(",aisgid=")) {
                    result.put(inv.getMoid(), inv.getVendorunittypename() + " (" + inv.getSerialnumber() + ")");
                }
            }
        }
        return result;
    }
}
