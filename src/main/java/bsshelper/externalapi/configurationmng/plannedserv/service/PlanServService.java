package bsshelper.externalapi.configurationmng.plannedserv.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.plannedserv.entity.PlannedServBodySettings;
import bsshelper.globalutil.entity.MessageEntity;

public interface PlanServService {
    MessageEntity activateArea(String dataAreaId, Token token);
    MessageEntity dataConfigUnassociated(String dataAreaId, Token token, PlannedServBodySettings bodySettings);
}
