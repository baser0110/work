package bsshelper.externalapi.auth.mapper;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.auth.to.TokenTo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TokenMapper {

    static public Token toEntity(TokenTo tokenTo) {
        return new Token(tokenTo.getAccessToken(), tokenTo.getExpires(), LocalDateTime.now());
    }
}
