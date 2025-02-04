package bsshelper.externalapi.alarmmng.activealarm.service;

import bsshelper.externalapi.alarmmng.activealarm.util.ActiveAlarmBodySettings;
import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.EUtranCellNBIoTMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.to.sdr.EUtranCellNBIoTMocSimplifiedTo;
import bsshelper.externalapi.configurationmng.currentmng.util.CurrentMngBodySettings;
import bsshelper.globalutil.GlobalUtil;
import bsshelper.globalutil.Verb;
import bsshelper.globalutil.entity.ErrorEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ActiveAlarmServiceImpl implements ActiveAlarmService {

    @Override
    public String rawDataQuery(Token token, ManagedElement managedElement) {
        HttpResponse<Stream<String>> httpResponse = null;
        String response = null;

        ErrorEntity error = null;
        try {
            HttpRequest httpRequest = getActiveAlarmByMe(token, managedElement);
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofLines());
//            System.out.println(httpResponse.body());
            System.out.println(ActiveAlarmBodySettings.builder().condition(new ActiveAlarmBodySettings.Condition(
                    List.of(managedElement.getSubNetworkNum() + ",1061"))).build().getBodySettings());
            System.out.println();
//            response = httpResponse.body();
            System.out.println(httpResponse.body().collect(Collectors.joining()));
//            if (response.contains("\"code\":0") && !response.contains("\"result\":[]")) {
//                log.info(" >> {} for {} successfully found", mocName, managedElement.getUserLabel());
//            } else {
//                if (response.contains("\"code\":0")) {
//                    log.info(" >> {} for {} successfully found but it's empty", mocName, managedElement.getUserLabel());
//                    return null;
//                }
//                try {
//                    error = new Gson().fromJson(response, ErrorEntity.class);
//                } catch (JsonSyntaxException e2) {
//                    log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
//                }
//                if (error != null) {
//                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
//                }
//                response = null;
//            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
//            log.error(" >> error in sending http request: {}", e.toString());
        }
        return response;
    }

    private HttpRequest getActiveAlarmByMe(Token token, ManagedElement managedElement) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        ActiveAlarmBodySettings.builder()
                                .condition(new ActiveAlarmBodySettings.Condition(
                                        List.of( "114,114"))).build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_ACTIVE_ALARM))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }
}
