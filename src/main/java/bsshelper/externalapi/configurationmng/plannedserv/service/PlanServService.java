package bsshelper.externalapi.configurationmng.plannedserv.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.plannedserv.entity.PlannedServBodySettings;

public interface PlanServService {
    void activateArea(String dataAreaId, Token token);
    void dataConfigUnassociated(String dataAreaId, Token token, PlannedServBodySettings bodySettings);
}
