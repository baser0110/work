package bsshelper.configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserEntityConfiguration {

    private final String userName;
    private final String value;

    public UserEntityConfiguration(@Value("${custom.api.user}") String userName, @Value("${custom.api.pass}") String value) {
        this.userName = userName;
        this.value = value;
    }

    public UserEntity create() {
        return new UserEntity(userName, value);
    }

}
