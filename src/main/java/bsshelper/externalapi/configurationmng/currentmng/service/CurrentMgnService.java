package bsshelper.externalapi.configurationmng.currentmng.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.*;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.*;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GGsmCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GTrxMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UIubLinkMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.*;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;

public interface CurrentMgnService {
    String rawDataQuery (Token token, ManagedElement managedElement, String mocName);
    ManagedElement getManagedElementByNeName (Token token, String NE_Name);
    List<DryContactDeviceMoc> getDryContactDeviceMoc (Token token, ManagedElement managedElement);
    List<DryContactCableMoc> getDryContactCableMoc(Token token, ManagedElement managedElement);
    List<SdrDeviceGroupMoc> getSdrDeviceGroupMoc(Token token, ManagedElement managedElement);
    List<EthernetSwitchDeviceMoc> getEthernetSwitchDevice(Token token, ManagedElement managedElement);
    List<IpLayerConfigMoc> getIpLayerConfigMoc(Token token, ManagedElement managedElement);
    List<FiberCableMoc> getFiberCableMoc(Token token, ManagedElement managedElement);
    List<RiCableMoc> getRiCableMoc(Token token, ManagedElement managedElement);
    List<ReplaceableUnitMoc> getReplaceableUnitMoc(Token token, ManagedElement managedElement);
    List<IpMoc> getIpMoc(Token token, ManagedElement managedElement);
    List<ITBBUGTrxMoc> getITBBUGTrxMoc(Token token, ManagedElement managedElement);
    List<ULocalCellMoc> getULocalCellMoc(Token token, ManagedElement managedElement);
    List<ITBBUULocalCellMoc> getITBBUULocalCellMoc(Token token, ManagedElement managedElement);
    List<SDRGTrxMoc> getSDRGTrxMoc(Token token, ManagedElement managedElement);
    String simplifiedRawDataQuery(Token token, ManagedElement managedElement, String mocName, HttpRequest httpRequest);
    List<ULocalCellMocSimplified> getULocalCellMocSimplified(Token token, ManagedElement managedElement);
    List<UCellMocSimplified> getUCellMocSimplified(Token token, ManagedElement managedElement);
    List<ITBBUULocalCellMocSimplified> getITBBUULocalCellMocSimplified(Token token, ManagedElement managedElement);
    List<EUtranCellFDDMocSimplified> getEUtranCellFDDMocSimplified(Token token, ManagedElement managedElement);
    List<ITBBUCUEUtranCellFDDLTEMocSimplified> getITBBUCUEUtranCellFDDLTEMocSimplified(Token token, ManagedElement managedElement);
    List<EUtranCellNBIoTMocSimplified> getEUtranCellNBIoTMocSimplified(Token token, ManagedElement managedElement);
    List<ITBBUCUEUtranCellNBIoTMocSimplified> getITBBUCUEUtranCellNBIoTMocSimplified(Token token, ManagedElement managedElement);
    List<GGsmCellMocSimplified> getGGsmCellMocSimplified(Token token, ManagedElement managedElement);
    List<GTrxMocSimplified> getGTrxMocSimplified(Token token, ManagedElement managedElement, List<GGsmCellMocSimplified> cells);
    List<UUtranCellFDDMocSimplified> getUUtranCellFDDMocSimplified(Token token, ManagedElement managedElement);
    List<UIubLinkMocSimplified> getUIubLinkMocSimplified(Token token, ManagedElement managedElement);

    Map<String,CurrentMgnServiceImpl.CellInfo> getCacheSDRCellsUMTS(Token token);
    Map<String,CurrentMgnServiceImpl.CellInfo> getCacheITBBUCellsUMTS(Token token);
    Map<String,CurrentMgnServiceImpl.CellInfo> getCacheSDRCellsNBIOT(Token token);
    Map<String,CurrentMgnServiceImpl.CellInfo> getCacheITBBUCellsNBIOT(Token token);
    Map<String,CurrentMgnServiceImpl.CellInfo> getCacheSDRCellsFDDLTE(Token token);
    Map<String,CurrentMgnServiceImpl.CellInfo> getCacheITBBUCellsFDDLTE(Token token);
    Map<String,CurrentMgnServiceImpl.CellInfo> getCacheMRNCCellsGSM(Token token);
    Map<String,String> getCacheManagedElement(Token token, CurrentMgnServiceImpl.Type type);
}
