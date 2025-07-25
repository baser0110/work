package bsshelper.externalapi.inventorymng.to;

import bsshelper.externalapi.inventorymng.entity.InventoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEntityTo {
    List<InventoryEntity> inventoryEntityList;
}
