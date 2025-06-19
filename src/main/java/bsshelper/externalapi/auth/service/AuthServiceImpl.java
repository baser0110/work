package bsshelper.externalapi.auth.service;

import bsshelper.externalapi.configuration.ApiUserEntityConfiguration;
import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.auth.mapper.TokenMapper;
import bsshelper.externalapi.auth.to.TokenTo;
import bsshelper.globalutil.GlobalUtil;
import bsshelper.globalutil.entity.ErrorEntity;
import bsshelper.globalutil.Verb;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ApiUserEntityConfiguration apiUserEntityConfiguration;

    @Override
    public Token getToken() {
        return TokenMapper.toEntity(getTokenTo());
    }

    @Override
    public boolean updateToken(Token token) {
        HttpRequest httpRequest = null;
        HttpResponse<String> httpResponse = null;

        try {
            httpRequest = createUpdateRequest(token);
//            System.out.println(httpRequest);
//            System.out.println(httpRequest.headers());
//            System.out.println(UserEntity.getUser());
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
        }
        if (httpResponse != null) {
            String response = httpResponse.body();
            if (response.equals("{}")) {
                log.info(" >> token update success response: {}", response);
                return true;
            } else {
                log.info(" >> token update fail response: {}", response);
                return false;
            }
        } else return false;
//        System.out.println(httpResponse.body());
    }

    @Override
    public void deleteToken(Token token) {

    }

    private TokenTo getTokenTo() {
        TokenTo tokenTo = null;
        ErrorEntity error = null;
        HttpRequest httpRequest = null;
        HttpResponse<String> httpResponse = null;

        try {
            httpRequest = createGetRequest();
//            System.out.println(httpRequest);
//            System.out.println(httpRequest.headers());
//            System.out.println(UserEntity.getUser());
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
        }

//        System.out.println(httpResponse.body());
        if (httpResponse != null) {
            String json = httpResponse.body();

            try {
                tokenTo = new Gson().fromJson(json, TokenTo.class);
            } catch (JsonSyntaxException e1) {
                log.error(" >> error in TokenTo parsing >> {}", e1.toString());
                try {
                    error = new Gson().fromJson(json, ErrorEntity.class);
                } catch (JsonSyntaxException e2) {
                    log.error(" >> error in ErrorEntity parsing >> {}", e2.toString());
                }
                if (error != null) {
                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
                }
            }
        }
//        System.out.println(tokenTo);
        return tokenTo;
    }

    private HttpRequest createGetRequest() {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(apiUserEntityConfiguration.create().getUser()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_OAUTH + GlobalUtil.OAUTH_TOKEN))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=UTF-8")
                .build();
    }

    private HttpRequest createUpdateRequest(Token token) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_OAUTH + GlobalUtil.OAUTH_HANDSHAKE))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private HttpRequest createDeleteRequest(Token token) {
        return HttpRequest.newBuilder()
                .method(Verb.DELETE.toString(), HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_OAUTH + GlobalUtil.OAUTH_TOKEN))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }
}
