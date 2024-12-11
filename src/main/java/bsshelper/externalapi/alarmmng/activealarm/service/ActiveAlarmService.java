package bsshelper.externalapi.alarmmng.activealarm.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;

public interface ActiveAlarmService {
    String rawDataQuery(Token token, ManagedElement managedElement);
}
