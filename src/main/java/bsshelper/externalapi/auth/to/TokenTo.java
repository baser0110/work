package bsshelper.externalapi.auth.to;

import lombok.Data;

@Data
public class TokenTo {
    private String accessToken;
    private long expires;
}
