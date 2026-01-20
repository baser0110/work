package bsshelper.externalapi.configurationmng.nemoactserv.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ReplaceableUnitMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.SdrDeviceGroupMoc;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.OpticInfoFinal;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.SyncFinal;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.VSWRTestFinal;
import bsshelper.externalapi.configurationmng.nemoactserv.util.DiagnosisAction;

import java.util.List;

public interface ExecNeActService {
    String diagnosisSDRDataQuery(Token token, ManagedElement managedElement, String ldn, DiagnosisAction action);
    List<OpticInfoFinal> opticInfoFinalDataQuery(Token token, ManagedElement managedElement, List<SdrDeviceGroupMoc> sdrDeviceGroupMocList);
    List<VSWRTestFinal> vswrTestFinalDataQuery(Token token, ManagedElement managedElement, List<SdrDeviceGroupMoc> sdrDeviceGroupMocList);
    String ITBBUDataQuery(Token token, ManagedElement managedElement, String ldn, String query, String description);

    String powerOffResetBoardQuery(Token token, ManagedElement managedElement, String ldn);

    String resetBoardQuery(Token token, ManagedElement managedElement, String ldn);

    String resetNEQuery(Token token, ManagedElement managedElement);

    List<OpticInfoFinal> opticInfoFinalITBBUDataQuery(Token token, ManagedElement managedElement, List<ReplaceableUnitMoc> replaceableUnitMocList);
    List<VSWRTestFinal> vswrTestFinalITBBUDataQuery(Token token, ManagedElement managedElement, List<ReplaceableUnitMoc> replaceableUnitMocList);
    List<SyncFinal> getSync(Token token, ManagedElement managedElement, List<SdrDeviceGroupMoc> sdrDeviceGroupMocList);
}
