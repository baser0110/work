package bsshelper.externalapi.perfmng.perfquery;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.perfmng.entity.HistoryRTWP;
import bsshelper.externalapi.perfmng.entity.HistoryVSWR;
import bsshelper.externalapi.perfmng.mapper.HistoryITBBUVSWRMapper;
import bsshelper.externalapi.perfmng.mapper.HistoryRTWPMapper;
import bsshelper.externalapi.perfmng.mapper.HistorySDRVSWRMapper;
import bsshelper.externalapi.perfmng.to.HistoryRTWPTo;
import bsshelper.externalapi.perfmng.to.HistoryVSWRTo;
import bsshelper.externalapi.perfmng.util.HistoryQueryBodySettings;
import bsshelper.externalapi.perfmng.util.HistoryQueryErrorEntity;
import bsshelper.externalapi.perfmng.util.TimeToString;
import bsshelper.globalutil.GlobalUtil;
import bsshelper.globalutil.Verb;
import bsshelper.globalutil.entity.ErrorEntity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HistoryQueryServiceImpl implements HistoryQueryService {

    @Override
    public List<HistoryVSWR> getHistoryVSWR(Token token, ManagedElement managedElement) {
        HistoryQueryErrorEntity error = null;
        String json = null;
        HistoryVSWRTo historyVSWRTo = null;
        List<HistoryVSWR> result = null;
        List<String> rawHistoryVSWRList = null;
        HistoryQueryBodySettings bodySettings = getVSWRBodySettings(managedElement);
        if (bodySettings != null) {
            json = rawDataQuery(token, managedElement, dataQueryRequest(token, bodySettings));
        }
//        System.out.println(json);
        try {
            if (json != null) {
            historyVSWRTo = new Gson().fromJson(json, HistoryVSWRTo.class);
            }
        } catch (JsonSyntaxException e1) {
            log.error(" >> error in historyVSWRTo parsing: {}", e1.toString());
            try {
                error = new Gson().fromJson(json, HistoryQueryErrorEntity.class);
            } catch (JsonSyntaxException e2) {
                log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
            }
            if (error != null) {
                log.error(" >> error {} code({})", error.getFailReason(), error.getResult());
            }
//        System.out.println(historySDRVSWRTo);
        }
        if (historyVSWRTo != null) {
            rawHistoryVSWRList = historyVSWRTo.getData();
            rawHistoryVSWRList.remove(0);
            switch (managedElement.getManagedElementType()) {
                case SDR -> result = HistorySDRVSWRMapper.toFinalEntity(rawHistoryVSWRList);
                case ITBBU -> result = HistoryITBBUVSWRMapper.toFinalEntity(rawHistoryVSWRList);
            }
        }
        log.info(" >> historyVSWRList: {}", result == null ? "null" : result.size() + " points");
//        System.out.println(historySDRVSWRList);
        return result;
    }

    @Override
    public List<HistoryRTWP> getHistoryRTWP(Token token, ManagedElement managedElement, List<UUtranCellFDDMocSimplified> cells) {
        if (cells == null || cells.isEmpty()) { return null; }
        String me = cells.getFirst().getRncNum() + ", " + cells.getLast().getRncNum();
        List<String> cellsQuery = new ArrayList<>();
        for (UUtranCellFDDMocSimplified c : cells) {
            cellsQuery.add(String.valueOf(c.getMoId()));
        }
        HistoryQueryErrorEntity error = null;
        String json = null;
        HistoryRTWPTo historyRTWPTo = null;
        List<HistoryRTWP> result = null;
        List<String> rawHistoryRTWPList = null;
        HistoryQueryBodySettings bodySettings = getRTWPBodySettings(me, cellsQuery);
        if (bodySettings != null) {
            json = rawDataQuery(token, managedElement, dataQueryRequest(token, bodySettings));
        }
//        System.out.println(json);
        try {
            if (json != null) {
                historyRTWPTo = new Gson().fromJson(json, HistoryRTWPTo.class);
            }
        } catch (JsonSyntaxException e1) {
            log.error(" >> error in historyRTWPTo parsing: {}", e1.toString());
            try {
                error = new Gson().fromJson(json, HistoryQueryErrorEntity.class);
            } catch (JsonSyntaxException e2) {
                log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
            }
            if (error != null) {
                log.error(" >> error {} code({})", error.getFailReason(), error.getResult());
            }
//        System.out.println(historyRTWPTo);
        }
        if (historyRTWPTo != null) {
            rawHistoryRTWPList = historyRTWPTo.getData();
            rawHistoryRTWPList.remove(0);
            result = HistoryRTWPMapper.toFinalEntity(rawHistoryRTWPList);

        }

        log.info(" >> historyRTWPList: {}", result == null ? "null" : result.size() + " points");
//        System.out.println(result);
        return result;
    }

    private String rawDataQuery(Token token, ManagedElement managedElement, HttpRequest request) {
        HttpResponse<String> httpResponse = null;
        String response = null;
        ErrorEntity error = null;
        try {
            httpResponse = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println(httpResponse.body());
            response = httpResponse.body();
            if (response.contains("\"result\":0") && !response.contains("\"data\":[]")) {
                log.info(" >> statistic for {} successfully found", managedElement.getUserLabel());
            } else {
                if (response.contains("\"result\":0")) {
                    log.info(" >> statistic for {} successfully found but it's empty", managedElement.getUserLabel());
                    return null;
                }
            }
        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
        }
        return response;
    }

    private HttpRequest dataQueryRequest(Token token, HistoryQueryBodySettings bodySettings) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(bodySettings.getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_PERFMNG))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private HistoryQueryBodySettings getVSWRBodySettings(ManagedElement managedElement) {
        switch (managedElement.getManagedElementType()) {
            case SDR -> {
                return   HistoryQueryBodySettings.builder()
                        .nfctid("SDR")
                        .gr(15)
                        .me(managedElement.getSubNetworkNum() + "," + managedElement.getManagedElementNum())
                        .items(List.of("C370150000"))
                        .showobjectname(true)
                        .starttime(TimeToString.dayAgoTime())
                        .endtime(TimeToString.nowTime())
                        .build();
            }
            case ITBBU -> {
                return   HistoryQueryBodySettings.builder()
                        .nfctid("ITBBU")
                        .gr(15)
                        .me(managedElement.getSubNetworkNum() + "," + managedElement.getManagedElementNum())
                        .items(List.of("C370150000"))
                        .showobjectname(true)
                        .starttime(TimeToString.dayAgoTime())
                        .endtime(TimeToString.nowTime())
                        .build();
            }
        }
        return null;
    }

    private HistoryQueryBodySettings getRTWPBodySettings(String me, List<String> cellsQuery) {
        return   HistoryQueryBodySettings.builder()
                .nfctid("MRNC")
                .gr(15)
                .me(me)
                .mois(cellsQuery)
                .items(List.of("300840"))
                .showobjectname(true)
                .starttime(TimeToString.dayAgoTime())
                .endtime(TimeToString.nowTime())
                .filterlayer("WV4.UtranCell")
                .build();
    }
}
