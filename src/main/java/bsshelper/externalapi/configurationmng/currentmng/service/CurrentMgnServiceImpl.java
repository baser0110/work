package bsshelper.externalapi.configurationmng.currentmng.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.*;
import bsshelper.externalapi.configurationmng.currentmng.to.*;
import bsshelper.externalapi.configurationmng.currentmng.mapper.ManagedElementMapper;
import bsshelper.externalapi.configurationmng.currentmng.util.*;
import bsshelper.globalutil.GlobalUtil;
import bsshelper.globalutil.ManagedElementType;
import bsshelper.globalutil.Verb;
import bsshelper.globalutil.entity.ErrorEntity;
import bsshelper.exception.CustomNetworkConnectionException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
@Slf4j
public class CurrentMgnServiceImpl implements CurrentMgnService {

    @Override
    public String rawDataQuery(Token token, String userLabel, String mocName, CurrentMngBodySettings bodySettings) {
        HttpResponse<String> httpResponse = null;
        String response = null;
        ErrorEntity error = null;
        try {
            HttpRequest httpRequest = dataQueryRequest(token, bodySettings);
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
//            System.out.println(httpResponse.body());
            response = httpResponse.body();
            if (response.contains("\"code\":0") && !response.contains("\"result\":[]")) {
                log.info(" >> {} for {} successfully found", mocName, userLabel);
            } else {
                if (response.contains("\"code\":0")) {
                    log.info(" >> {} for {} successfully found but it's empty", mocName, userLabel);
                    return null;
                }
                try {
                    error = new Gson().fromJson(response, ErrorEntity.class);
                } catch (JsonSyntaxException e2) {
                    log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
                }
                if (error != null) {
                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
                }
                response = null;
            }
        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
            if (e instanceof ConnectException) throw new CustomNetworkConnectionException((e.toString()));
        }
        return response;
    }

    @Override
    public <T> List<T> getMocList(Token token, String userLabel, MocMEType mocMEType, CurrentMngBodySettings bodySettings) {
        if (bodySettings == null) return Collections.emptyList();
        String mocName = mocMEType.getMocName();
        Class<T> clazz = mocMEType.getEntityClass();
        String json = rawDataQuery(token, userLabel, mocName, bodySettings);
//        System.out.println(json);
        try {
            Type type = TypeToken.getParameterized(MocTo.class, clazz).getType();
            MocTo<T> response = new Gson().fromJson(json, type);

            if (response != null && response.getResult() != null && !response.getResult().isEmpty()) {
                List<T> data = response.getResult().get(0).getMoData();
                log.info(" >> {}: found {} items for {}", mocName, data.size(), userLabel);
                return data;
            }
        } catch (JsonSyntaxException e) {
            log.error(" >> Error parsing {} to class {}: {}", mocName, clazz.getSimpleName(), e.getMessage());
        }

        return Collections.emptyList();
    }

    private HttpRequest dataQueryRequest(Token token, CurrentMngBodySettings bodySettings) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(bodySettings.getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_CURRENTAREA + GlobalUtil.CURRENTAREA_QUERY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    @Override
    public ManagedElement getManagedElementByNeName(Token token, String userLabel) {
        String json = rawDataQuery(token, userLabel, MocSDR.MANAGED_ELEMENT.getMocName(),
                CurrentMngBodySettingsFactory.queryManagedElementByUserLabel(userLabel, ManagedElementType.SDR));
        if (json == null) {
            json = rawDataQuery(token, userLabel, MocITBBU.MANAGED_ELEMENT.getMocName(),
                    CurrentMngBodySettingsFactory.queryManagedElementByUserLabel(userLabel, ManagedElementType.ITBBU));
        }
        if (json == null) {return null;}
        try {
            MocTo<ManagedElementMoc> to = new Gson().fromJson(json, TypeToken.getParameterized(MocTo.class, ManagedElementMoc.class).getType());
                return ManagedElementMapper.toManagedElement(to);
        } catch (JsonSyntaxException e) {
            log.error(" >> Error parsing {} to class {}: {}", "MocTo<>", "ManagedElementMoc", e.getMessage());
        }
        return null;
    }

    public <T extends AbstractCellMoc> Map<String, CellInfo> getCacheCellsForBatch(Token token, MocMEType moc, String logName) {
        Map<String, CellInfo> result = new TreeMap<>();
        String mocName = moc.getMocName();
        ManagedElementType meType = moc.getManaManagedElementType();
        Class<T> clazz = moc.getEntityClass();

        CurrentMngBodySettings settings = CurrentMngBodySettingsFactory.queryMocFull(
                mocName, List.of("userLabel"), meType);

        String json = rawDataQuery(token, logName, mocName, settings);
        if (json == null) return Collections.emptyMap();

        try {
            Type type = TypeToken.getParameterized(MocTo.class, clazz).getType();
            MocTo<T> to = new Gson().fromJson(json, type);

            if (to != null && to.getResult() != null) {
                if (meType.equals(ManagedElementType.SDR) || meType.equals(ManagedElementType.ITBBU)) {
                    for (MocTo.MocResultTo<T> site : to.getResult()) {
                        String ne = site.getNe();
                        for (T cell : site.getMoData()) {
                            String query = String.format("ManagedElementType=%s,%s,%s",
                                    ManagedElementType.SDR, ne, cell.getLdn());

                            result.put(cell.getUserLabel(), new CellInfo(query, ne));
                        }
                    }
                }
                if (meType.equals(ManagedElementType.MRNC)) {
                    for (MocTo.MocResultTo<T> controller : to.getResult()) {
                        String ne = controller.getNe();
                        String meId = ne.substring(ne.indexOf("=") + 1, ne.indexOf("=") + 4);
                        for (T cell : controller.getMoData()) {
                            String ldn = cell.getLdn();
                            String siteId = ldn.substring(ldn.indexOf("r=") + 2, ldn.indexOf(",GGs"));
                            String btsId = ldn.substring(ldn.lastIndexOf("=") + 1);
                            String query = String.format("CELL:MEID=%s,SITEID=%s,BTSID=%s; --netype MRNC --neid %s",
                                    meId, siteId, btsId, meId);
                            String cellUserLabel = cell.getUserLabel();
                            String siteUserLabel = (cellUserLabel != null && !cellUserLabel.isEmpty())
                                    ? cellUserLabel.substring(0, cellUserLabel.length() - 1)
                                    : cellUserLabel;
                            result.put(cell.getUserLabel(), new CellInfo(query, siteUserLabel));
                        }
                    }
                }
                log.info(" >> {} size: {}", logName, result.size());
            }
        } catch (JsonSyntaxException e) {
            log.error(" >> Error parsing {} to class {}: {}", mocName, clazz.getSimpleName(), e.getMessage());
        }

        return result;
    }

    public Map<String,String> getCacheManagedElementForBatch(Token token) {
        Map<String,String> resultMapByNe = new TreeMap<>();

        List<MocTo.MocResultTo<ManagedElementMoc>> meSDR = getFullMocResultToList(token, MocSDR.MANAGED_ELEMENT);
        List<MocTo.MocResultTo<ManagedElementMoc>> meITBBU = getFullMocResultToList(token, MocITBBU.MANAGED_ELEMENT);

        for (MocTo.MocResultTo<ManagedElementMoc> site : meSDR) {
            String ne = site.getNe();
            String userLabel = site.getMoData().get(0).getUserLabel();
            resultMapByNe.put(ne, userLabel);
        }
        for (MocTo.MocResultTo<ManagedElementMoc> site : meITBBU) {
            String ne = site.getNe();
            String userLabel = site.getMoData().get(0).getUserLabel();
            resultMapByNe.put(ne, userLabel);
        }
        return resultMapByNe;
    }

    private <T> List<MocTo.MocResultTo<T>> getFullMocResultToList(Token token, MocMEType moc) {
        List<MocTo.MocResultTo<T>> result = new ArrayList<>();
        String mocName = moc.getMocName();
        ManagedElementType meType = moc.getManaManagedElementType();
        Class<T> clazz = moc.getEntityClass();

        CurrentMngBodySettings settings = CurrentMngBodySettingsFactory.queryMocFull(
                    mocName, List.of("userLabel"), meType);

        String json = rawDataQuery(token, "CacheManagedElementForBatch", mocName, settings);
        if (json == null) return Collections.emptyList();

        try {
            Type type = TypeToken.getParameterized(MocTo.class, clazz).getType();
            MocTo<T> to = new Gson().fromJson(json, type);
            result = to.getResult();
            log.info(" >> {} ResultToList size: {}", mocName, result.size());
        } catch (JsonSyntaxException e) {
            log.error(" >> Error parsing {} to class {}: {}", mocName, clazz.getSimpleName(), e.getMessage());
        }
        return result;
    }

    @Data
    @RequiredArgsConstructor
    public static class CellInfo {
        private final String command;
        private final String ne; // userLabel for GSM
    }
}