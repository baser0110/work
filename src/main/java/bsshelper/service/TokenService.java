package bsshelper.service;

import bsshelper.externalapi.auth.entity.Token;

public interface TokenService {
    Token getToken();
    void setTokenWithHandSake();
}
