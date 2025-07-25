package bsshelper.externalapi.inventorymng.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.inventorymng.entity.InventoryEntity;

import java.util.List;

public interface InventoryMngService {
    List<InventoryEntity> getHWInventory(Token token, ManagedElement managedElement);
}
