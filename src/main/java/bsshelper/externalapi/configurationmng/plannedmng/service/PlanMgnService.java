package bsshelper.externalapi.configurationmng.plannedmng.service;

import bsshelper.externalapi.auth.entity.Token;

public interface PlanMgnService {
    String newArea(Token token);
    String openArea(String dataAreaId, Token token);
    String closeArea(String dataAreaId, Token token);
    String deleteArea(String dataAreaId, Token token);
}
