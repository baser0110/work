package bsshelper.externalapi.configurationmng.currentmng.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.*;

import java.util.List;

public interface CurrentMgnService {
    String rawDataQuery (Token token, ManagedElement managedElement, String mocName);
    ManagedElement getManagedElementByNeName (Token token, String NE_Name);
    List<DryContactDeviceMoc> getDryContactDeviceMoc (Token token, ManagedElement managedElement);
    List<DryContactCableMoc> getDryContactCableMoc(Token token, ManagedElement managedElement);
    List<ULocalCellMoc> getULocalCellMoc(Token token, ManagedElement managedElement);
    List<EUtranCellNBIoTMoc> getEUtranCellNBIoTMoc(Token token, ManagedElement managedElement);
}
