package bsshelper.externalapi.configurationmng.currentmng.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.*;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.DryContactCableMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUCUEUtranCellNBIoTMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GGsmCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UIubLinkMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.*;

import java.net.http.HttpRequest;
import java.util.List;

public interface CurrentMgnService {
    String rawDataQuery (Token token, ManagedElement managedElement, String mocName);
    ManagedElement getManagedElementByNeName (Token token, String NE_Name);
    List<DryContactDeviceMoc> getDryContactDeviceMoc (Token token, ManagedElement managedElement);
    List<DryContactCableMoc> getDryContactCableMoc(Token token, ManagedElement managedElement);
    List<SdrDeviceGroupMoc> getSdrDeviceGroupMoc(Token token, ManagedElement managedElement);
    List<FiberCableMoc> getFiberCableMoc(Token token, ManagedElement managedElement);
    List<ULocalCellMoc> getULocalCellMoc(Token token, ManagedElement managedElement);
    List<ITBBUULocalCellMoc> getITBBUULocalCellMoc(Token token, ManagedElement managedElement);
    List<EUtranCellNBIoTMoc> getEUtranCellNBIoTMoc(Token token, ManagedElement managedElement);
    String simplifiedRawDataQuery(Token token, ManagedElement managedElement, String mocName, HttpRequest httpRequest);
    List<ULocalCellMocSimplified> getULocalCellMocSimplified(Token token, ManagedElement managedElement);
    List<ITBBUULocalCellMocSimplified> getITBBUULocalCellMocSimplified(Token token, ManagedElement managedElement);
    List<EUtranCellNBIoTMocSimplified> getEUtranCellNBIoTMocSimplified(Token token, ManagedElement managedElement);
    List<ITBBUCUEUtranCellNBIoTMocSimplified> getITBBUCUEUtranCellNBIoTMocSimplified(Token token, ManagedElement managedElement);
    List<GGsmCellMocSimplified> getGGsmCellMocSimplified(Token token, ManagedElement managedElement);
    List<UUtranCellFDDMocSimplified> getUUtranCellFDDMocSimplified(Token token, ManagedElement managedElement);
    List<UIubLinkMocSimplified> getUIubLinkMocSimplified(Token token, ManagedElement managedElement);
    List<SdrDeviceGroupMocSimpl> getSdrDeviceGroupMocSimpl(Token token, ManagedElement managedElement);
}
