package bsshelper.externalapi.configuration;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApiUserEntity {

    private final String userName;
    private final String value;
    private final String grantType = "password";

    public String getUser() {
        return new Gson().toJson(this);
    }
}
