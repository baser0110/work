package bsshelper.externalapi.configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiUserEntityConfiguration {

    private final String userName;
    private final String value;

    public ApiUserEntityConfiguration(@Value("${custom.api.user}") String userName, @Value("${custom.api.pass}") String value) {
        this.userName = userName;
        this.value = value;
    }

    public ApiUserEntity create() {
        return new ApiUserEntity(userName, value);
    }

}
