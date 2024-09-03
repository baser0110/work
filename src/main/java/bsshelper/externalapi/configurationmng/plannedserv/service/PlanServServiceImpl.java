package bsshelper.externalapi.configurationmng.plannedserv.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.globalutil.GlobalUtil;
import bsshelper.globalutil.Verb;
import bsshelper.externalapi.configurationmng.plannedserv.entity.PlannedServBodySettings;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@Slf4j
public class PlanServServiceImpl implements PlanServService {

    @Override
    public void activateArea(String dataAreaId, Token token) {
        String result = "area successfully activated";
        try {
            HttpRequest httpRequest = activateAreaRequest(dataAreaId, token);
            HttpResponse<String> httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String response = httpResponse.body();
            if (response.contains("\"code\":0")) {
                log.info(" >> planned area successfully activated: {}: {}", dataAreaId, response);
            } else {
                log.info(" >> planned area activation failed: {}: {}", dataAreaId, response);
            }
        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
        }
    }

    @Override
    public void dataConfigUnassociated(String dataAreaId, Token token, PlannedServBodySettings bodySettings) {
        try {
            HttpRequest httpRequest = dataConfigUnsRequest(dataAreaId, token, bodySettings);
            HttpResponse<String> httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String response = httpResponse.body();
            if (response.contains("\"code\":0")) {
                log.info(" >> configuration data successfully modified in planned area: {}: {}", dataAreaId, response);
            } else {
                log.info(" >> configuration data modifying failed: {}: {}", dataAreaId, response);
            }
        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
        }
    }

    private HttpRequest dataConfigUnsRequest(String dataAreaId, Token token, PlannedServBodySettings bodySettings) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(new Gson().toJson(List.of(bodySettings))))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_PLANNEDSERV + "/" + dataAreaId + GlobalUtil.PLANNEDSERV_MO_CONFIG_UNS))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }


    private HttpRequest activateAreaRequest(String dataAreaId, Token token) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString("{}"))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_PLANNEDSERV + "/" + dataAreaId + GlobalUtil.PLANNEDSERV_ACTIVATE))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }


}
