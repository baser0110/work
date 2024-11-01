package bsshelper.externalapi.configurationmng.nemoactserv.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.SdrDeviceGroupMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.SdrDeviceGroupMocSimpl;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.OpticInfoFinal;

import java.util.List;

public interface ExecNeActSDRService {
    String diagnosisOpticInfoDataQuery(Token token, ManagedElement managedElement, String ldn);
    public List<OpticInfoFinal> opticInfoFinalDataQuery(Token token, ManagedElement managedElement, List<SdrDeviceGroupMoc> sdrDeviceGroupMocList);
}
