package bsshelper.externalapi.alarmmng.activealarm.service;

import bsshelper.externalapi.alarmmng.activealarm.entity.AlarmEntity;
import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Set;

public interface ActiveAlarmService {
    List<AlarmEntity> alarmDataExport(Token token, HttpRequest httpRequest, ManagedElement managedElement);
    HttpRequest getActiveAlarmByBSC(Token token, ManagedElement managedElement);
    Set<String> getHasAlarmSetByMEonBSC (Token token, ManagedElement managedElement);
}
