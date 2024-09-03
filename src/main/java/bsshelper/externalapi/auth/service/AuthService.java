package bsshelper.externalapi.auth.service;

import bsshelper.externalapi.auth.entity.Token;

public interface AuthService {
    Token getToken();
    void updateToken(Token token);
    void deleteToken(Token token);
}
