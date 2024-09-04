package bsshelper.externalapi.configurationmng.plannedserv.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.globalutil.GlobalUtil;
import bsshelper.globalutil.Severity;
import bsshelper.globalutil.Verb;
import bsshelper.externalapi.configurationmng.plannedserv.entity.PlannedServBodySettings;
import bsshelper.globalutil.entity.MessageEntity;
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
    public MessageEntity activateArea(String dataAreaId, Token token) {
        MessageEntity result = null;
        try {
            HttpRequest httpRequest = activateAreaRequest(dataAreaId, token);
            HttpResponse<String> httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String response = httpResponse.body();
            if (response.contains("\"code\":0")) {
                log.info(" >> planned area successfully activated: {}: {}", dataAreaId, response);
                result = new MessageEntity(Severity.SUCCESS, "Planned area successfully activated!");
            } else {
                log.info(" >> planned area activation failed: {}: {}", dataAreaId, response);
                result = new MessageEntity(Severity.ERROR, "Planned area activation failed! " + response);
            }
        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
            result = new MessageEntity(Severity.ERROR, "Network connection error!");
        }
        return result;
    }

    @Override
    public MessageEntity dataConfigUnassociated(String dataAreaId, Token token, PlannedServBodySettings bodySettings) {
        MessageEntity result = null;
        try {
            HttpRequest httpRequest = dataConfigUnsRequest(dataAreaId, token, bodySettings);
            HttpResponse<String> httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String response = httpResponse.body();
            if (response.contains("\"code\":0")) {
                log.info(" >> configuration data successfully modified in planned area: {}: {}", dataAreaId, response);
                result = new MessageEntity(Severity.SUCCESS, "Planned area data successfully modified!");
            } else {
                log.info(" >> configuration data modifying failed: {}: {}", dataAreaId, response);
                result = new MessageEntity(Severity.ERROR, "Planned area data modification failed! " + response);
            }
        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
            result = new MessageEntity(Severity.ERROR, "Network connection error!");
        }
        return result;
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
