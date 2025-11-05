package bsshelper.externalapi.alarmmng.activealarm.service;

import bsshelper.externalapi.alarmmng.activealarm.entity.AlarmEntity;
import bsshelper.externalapi.alarmmng.activealarm.to.AlarmEntityTo;
import bsshelper.externalapi.alarmmng.activealarm.util.ActiveAlarmBodySettings;
import bsshelper.externalapi.alarmmng.activealarm.util.CommentAlarmBodySettings;
import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.openscriptexecengine.entity.CellStatus;
import bsshelper.externalapi.openscriptexecengine.entity.GCellStatus;
import bsshelper.globalutil.GlobalUtil;
import bsshelper.globalutil.SubnetworkToBSCOrRNC;
import bsshelper.globalutil.Verb;
import bsshelper.globalutil.entity.ErrorEntity;
import bsshelper.exception.CustomNetworkConnectionException;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
@Slf4j
public class ActiveAlarmServiceImpl implements ActiveAlarmService {

    @Override
    public List<AlarmEntity> alarmDataExport(Token token, HttpRequest httpRequest, ManagedElement managedElement) {
        HttpResponse<Stream<String>> httpResponse = null;
        String response = null;
        ErrorEntity error = null;
        AlarmEntityTo alarmEntityTo = null;
        try {
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofLines());
            response = httpResponse.body().toList().toString();

            if (!response.contains("\"code\":")) {
                response = "{\"data\":" + response + "}";
                try {
                    alarmEntityTo = new Gson().fromJson(response, AlarmEntityTo.class);
                } catch (JsonSyntaxException e) {
                    log.error(" >> error in AlarmEntityTo parsing: {}", e.toString());
                }
            } else {
                try {
                    error = new Gson().fromJson(response, ErrorEntity.class);
                } catch (JsonSyntaxException e) {
                    log.error(" >> error in ErrorEntity parsing: {}", e.toString());
                }
                if (error != null) {
                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
                }
            }
        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
            if (e instanceof ConnectException) throw new CustomNetworkConnectionException((e.toString()));
        }
        if (alarmEntityTo != null) {
            return alarmEntityTo.getData();
        } else return null;
    }

    public void setAlarmComment(Token token, HttpRequest httpRequest, ManagedElement managedElement) {
        HttpResponse<String> httpResponse = null;
        String response = null;
        ErrorEntity error = null;
        try {
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            response = httpResponse.body();

//            if (!response.contains("\"code\":")) {
//                response = "{\"data\":" + response + "}";
//                try {
//                    alarmEntityTo = new Gson().fromJson(response, AlarmEntityTo.class);
//                } catch (JsonSyntaxException e) {
//                    log.error(" >> error in AlarmEntityTo parsing: {}", e.toString());
//                }
//            } else {
//                try {
//                    error = new Gson().fromJson(response, ErrorEntity.class);
//                } catch (JsonSyntaxException e) {
//                    log.error(" >> error in ErrorEntity parsing: {}", e.toString());
//                }
//                if (error != null) {
//                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
//                }
//            }
        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
            if (e instanceof ConnectException) throw new CustomNetworkConnectionException((e.toString()));
        }
//        if (alarmEntityTo != null) {
//            return alarmEntityTo.getData();
    }

    @Override
    public Set<String> getHasAlarmSetByMEonBSC (Token token, ManagedElement managedElement) {
        Set<String> hasAlarmCells = new HashSet<>();
        List<AlarmEntity> alarmEntityList = alarmDataExport(token, getActiveAlarmByBSC(token, managedElement), managedElement);
        if (alarmEntityList != null) {
            for (AlarmEntity alarm : alarmEntityList) {
                if (alarm.getCodename().equals("Cell interruption alarm")
                        && alarm.getRan_fm_alarm_site_name().getDisplayname().equals(managedElement.getUserLabel())) {
                    hasAlarmCells.add(alarm.getRan_fm_alarm_object_name().getDisplayname());
                }
            }
        }
        return hasAlarmCells;
    }

    @Override
    public HttpRequest getActiveAlarmByBSC(Token token, ManagedElement managedElement) {
        String bsc = String.valueOf(SubnetworkToBSCOrRNC.getBSCbySubnetwork(managedElement.getSubNetworkNum()));
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        ActiveAlarmBodySettings.builder()
                                .condition(new ActiveAlarmBodySettings.Condition(
                                        List.of( bsc + "," + bsc)))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_EXPRT_ACTIVE_ALARM))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    @Override
    public HttpRequest getActiveAlarmByRNC(Token token, ManagedElement managedElement) {
        String rnc = String.valueOf(SubnetworkToBSCOrRNC.getRNCbySubnetwork(managedElement.getSubNetworkNum()));
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        ActiveAlarmBodySettings.builder()
                                .condition(new ActiveAlarmBodySettings.Condition(
                                        List.of( rnc + "," + rnc)))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_EXPRT_ACTIVE_ALARM))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    @Override
    public HttpRequest getActiveAlarmBySDRSite(Token token, ManagedElement managedElement) {
        String network = managedElement.getSubNetworkNum().toString();
        String id = managedElement.getManagedElementNum();
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        ActiveAlarmBodySettings.builder()
                                .condition(new ActiveAlarmBodySettings.Condition(
                                        List.of( network + "," + id)))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_EXPRT_ACTIVE_ALARM))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    @Override
    public HttpRequest setAlarmCommentRequest(Token token, List<String> ids, String comment) {
        return HttpRequest.newBuilder()
                .method(Verb.PUT.toString(), HttpRequest.BodyPublishers.ofString(
                        CommentAlarmBodySettings.builder()
                                .ids(ids)
                                .commenttext(comment)
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_COMMENT_ALARM))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

}
