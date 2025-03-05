package bsshelper.globalutil.entity;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;

public class UserEntity {
//    @Value("${custom.api.user}")
//    private String userName;
//    @Value("${custom.api.pass}")
//    private String value;
    private final String userName = "ran_api";
    private final String value = "Test_987";
    private final String grantType = "password";

    public static String getUser() {
        return new Gson().toJson(new UserEntity());
    }

    private UserEntity() {}
}
