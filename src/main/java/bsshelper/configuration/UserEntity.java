package bsshelper.configuration;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
public class UserEntity {

    private final String userName;
    private final String value;
    private final String grantType = "password";

    public String getUser() {
        return new Gson().toJson(this);
    }
}
