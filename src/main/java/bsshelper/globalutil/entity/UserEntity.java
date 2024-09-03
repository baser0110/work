package bsshelper.globalutil.entity;

import com.google.gson.Gson;

public class UserEntity {

    private final String userName = "ran_api";
    private final String value = "Test_987";
    private final String grantType = "password";

    public static String getUser() {
        return new Gson().toJson(new UserEntity());
    }

    private UserEntity() {}
}
