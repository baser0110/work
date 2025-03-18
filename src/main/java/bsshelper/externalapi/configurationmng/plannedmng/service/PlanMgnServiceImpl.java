package bsshelper.externalapi.configurationmng.plannedmng.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.globalutil.GlobalUtil;
import bsshelper.globalutil.Verb;
import bsshelper.globalutil.entity.ErrorEntity;
import bsshelper.globalutil.exception.CustomNetworkConnectionException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
@Slf4j
public class PlanMgnServiceImpl implements  PlanMgnService {
    @Override
    public String newArea(Token token) {
        String dataAreaId = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(Calendar.getInstance().getTime());
        try {
            ErrorEntity error = null;
            HttpRequest httpRequest = createNewAreaRequest(dataAreaId, token);
            HttpResponse<String> httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String response = httpResponse.body();
//            System.out.println(response);
            if (response.contains("\"code\":0")) {
                log.info(" >> planned area successfully created: {}, code {}", dataAreaId, response);
            } else {
                try {
                    error = new Gson().fromJson(response, ErrorEntity.class);
                } catch (JsonSyntaxException e2) {
                    log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
                }
                if (error != null) {
                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
                }
            }
        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
            if (e instanceof ConnectException) throw new CustomNetworkConnectionException((e.toString()));
        }
        return dataAreaId;
    }
    // NO USED
    @Override
    public String openArea(String dataAreaId, Token token) {
        return "";
    }
    // NO USED
    @Override
    public String closeArea(String dataAreaId, Token token) {
        return "";
    }

    @Override
    public String deleteArea(String dataAreaId, Token token) {
        String response = "";

        try {
            ErrorEntity error = null;
            HttpRequest httpRequest = deleteAreaRequest(dataAreaId, token);
//            System.out.println(httpRequest);
            HttpResponse<String> httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            response = httpResponse.body();
//            System.out.println(response);
            if (response.contains("\"code\":0")) {
                log.info(" >> planned area successfully deleted: {}, code {}", dataAreaId, response);
            } else {
                try {
                    error = new Gson().fromJson(response, ErrorEntity.class);
                } catch (JsonSyntaxException e2) {
                    log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
                }
                if (error != null) {
                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
                }
            }
        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
            if (e instanceof ConnectException) throw new CustomNetworkConnectionException((e.toString()));
        }
        return response;
    }

    private HttpRequest createNewAreaRequest(String dataAreaId, Token token) {
//        System.out.println(new AreaSettings(dataAreaId).getAreaSettings());
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(new AreaSettings(dataAreaId).getAreaSettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_PLANNEDMNG + "/" + dataAreaId + GlobalUtil.PLANNEDMNG_NEW))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private HttpRequest deleteAreaRequest(String dataAreaId, Token token) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_PLANNEDMNG + "/" + dataAreaId + GlobalUtil.PLANNEDMNG_DELETE))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private static class AreaSettings {
        public final int authority = 1;
        public final String userLabel;

        public String getAreaSettings() {
            return new Gson().toJson(this);
        }

        public AreaSettings(String dataAreaId) {
            this.userLabel = "RAN-API_" + dataAreaId;
        }
    }
}
