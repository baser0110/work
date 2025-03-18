package bsshelper.externalapi.perfmng.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.perfmng.entity.HistoryForULocalCell;
import bsshelper.externalapi.perfmng.entity.HistoryForUMTSCell;
import bsshelper.externalapi.perfmng.entity.HistoryMaxOpticError;
import bsshelper.externalapi.perfmng.entity.HistoryVSWR;
import bsshelper.externalapi.perfmng.mapper.*;
import bsshelper.externalapi.perfmng.to.HistoryTo;
import bsshelper.externalapi.perfmng.util.HistoryQueryBodySettings;
import bsshelper.externalapi.perfmng.util.HistoryQueryErrorEntity;
import bsshelper.externalapi.perfmng.util.KPI;
import bsshelper.externalapi.perfmng.util.TimeToString;
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
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HistoryQueryServiceImpl implements HistoryQueryService {

    @Override
    public List<HistoryForUMTSCell> getUMTSCellHistory(Token token, ManagedElement managedElement, List<UUtranCellFDDMocSimplified> cells, int time, KPI kpi) {
        if (cells == null || cells.isEmpty()) { return null; }
        String me = cells.getFirst().getRncNum() + ", " + cells.getLast().getRncNum();
        List<String> cellsQuery = new ArrayList<>();
        for (UUtranCellFDDMocSimplified c : cells) {
            cellsQuery.add(String.valueOf(c.getMoId()));
        }
        String json = null;
        String json1 = null;
        List<HistoryForUMTSCell> result = null;
        HistoryQueryBodySettings bodySettings = getUMTSCellBodySettings(me, cellsQuery, time, kpi);
//        System.out.println(bodySettings.getBodySettings());
//        HistoryQueryBodySettings bodySettings1 = getPacketCellBodySettings(me, time);
//        System.out.println(bodySettings1.getBodySettings());
//        if (bodySettings1 != null) {
//            json1 = rawDataQuery(token, managedElement, dataQueryRequest(token, bodySettings1));
//        }
        if (bodySettings != null) {
            json = rawDataQuery(token, managedElement, dataQueryRequest(token, bodySettings));
        }
//        System.out.println(json1);
        HistoryTo historyTo = getHistoryTo(json, kpi);
        if (historyTo != null) {
            List<String> rawHistoryList = historyTo.getData();
            rawHistoryList.remove(0);
            result = HistoryUMTSCellMapper.toFinalEntity(rawHistoryList, kpi);
        }
        log.info(" >> {} List: {}", kpi.getInfo(), result == null ? "null" : result.size() + " points");
        return result;
    }

    private HistoryTo getHistoryTo(String json, KPI kpi) {
        HistoryQueryErrorEntity error = null;
        HistoryTo historyTo = null;
        try {
            if (json != null) {
                historyTo = new Gson().fromJson(json, HistoryTo.class);
            }
        } catch (JsonSyntaxException e1) {
            log.error(" >> error in {} parsing: {}", kpi.getInfo(), e1.toString());
            try {
                error = new Gson().fromJson(json, HistoryQueryErrorEntity.class);
            } catch (JsonSyntaxException e2) {
                log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
            }
            if (error != null) {
                log.error(" >> error {} code({})", error.getFailReason(), error.getResult());
            }
        }
        return historyTo;
    }

    private String rawDataQuery(Token token, ManagedElement managedElement, HttpRequest request) {
        HttpResponse<String> httpResponse = null;
        String response = null;
        ErrorEntity error = null;
        try {
            httpResponse = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
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
            if (e instanceof ConnectException) throw new CustomNetworkConnectionException((e.toString()));
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

//    private HistoryQueryBodySettings getVSWRBodySettings(ManagedElement managedElement, int time) {
//        switch (managedElement.getManagedElementType()) {
//            case SDR -> {
//                return   HistoryQueryBodySettings.builder()
//                        .nfctid("SDR")
//                        .gr(15)
//                        .me(managedElement.getSubNetworkNum() + "," + managedElement.getManagedElementNum())
//                        .items(List.of(KPI.VSWR.getCode()))
//                        .showobjectname(true)
//                        .starttime(TimeToString.nHoursAgoTime(time))
//                        .endtime(TimeToString.nowTime())
//                        .build();
//            }
//            case ITBBU -> {
//                return   HistoryQueryBodySettings.builder()
//                        .nfctid("ITBBU")
//                        .gr(15)
//                        .me(managedElement.getSubNetworkNum() + "," + managedElement.getManagedElementNum())
//                        .items(List.of(KPI.VSWR.getCode()))
//                        .showobjectname(true)
//                        .starttime(TimeToString.nHoursAgoTime(time))
//                        .endtime(TimeToString.nowTime())
//                        .build();
//            }
//        }
//        return null;
//    }

    private HistoryQueryBodySettings getSDROrITBBUBodySettings(ManagedElement managedElement, int time, KPI kpi) {
        switch (managedElement.getManagedElementType()) {
            case SDR -> {
                return   HistoryQueryBodySettings.builder()
                        .nfctid("SDR")
                        .gr(15)
                        .me(managedElement.getSubNetworkNum() + "," + managedElement.getManagedElementNum())
                        .items(List.of(kpi.getCode()))
                        .showobjectname(true)
                        .starttime(TimeToString.nHoursAgoTime(time))
                        .endtime(TimeToString.nowTime())
                        .build();
            }
            case ITBBU -> {
                return   HistoryQueryBodySettings.builder()
                        .nfctid("ITBBU")
                        .gr(15)
                        .me(managedElement.getSubNetworkNum() + "," + managedElement.getManagedElementNum())
                        .items(List.of(kpi.getCode()))
                        .showobjectname(true)
                        .starttime(TimeToString.nHoursAgoTime(time))
                        .endtime(TimeToString.nowTime())
                        .build();
            }
        }
        return null;
    }

    private HistoryQueryBodySettings getUMTSCellBodySettings(String me, List<String> cellsQuery, int time, KPI kpi) {
        return   HistoryQueryBodySettings.builder()
                .nfctid("MRNC")
                .gr(15)
                .me(me)
                .mois(cellsQuery)
                .items(List.of(kpi.getCode()))
                .showobjectname(true)
                .starttime(TimeToString.nHoursAgoTime(time))
                .endtime(TimeToString.nowTime())
                .filterlayer("WV4.UtranCell")
                .build();
    }

//    private HistoryQueryBodySettings getPacketCellBodySettings(String me, int time) {
//        return   HistoryQueryBodySettings.builder()
//                .nfctid("MRNC")
//                .motid("pm.IPPD")
//                .gr(15)
//                .me("13, 13")
//                .mois(List.of("4"))
//                .items(List.of("900156"))
//                .showobjectname(true)
//                .starttime(TimeToString.nHoursAgoTime(time))
//                .endtime(TimeToString.nowTime())
//                .grouplayer("me,WV4.IubLink")
//                .filterlayer("WV4.IubLink")
//                .build();
//    }

    @Override
    public List<HistoryVSWR> getHistoryVSWR(Token token, ManagedElement managedElement, int time) {
        String json = null;
        List<HistoryVSWR> result = null;
        HistoryQueryBodySettings bodySettings = getSDROrITBBUBodySettings(managedElement, time, KPI.VSWR);
        if (bodySettings != null) {
            json = rawDataQuery(token, managedElement, dataQueryRequest(token, bodySettings));
        }
//        System.out.println(json);
        HistoryTo historyTo = getHistoryTo(json, KPI.VSWR);
        if (historyTo != null) {
            List<String> rawHistoryVSWRList = historyTo.getData();
            rawHistoryVSWRList.remove(0);
            switch (managedElement.getManagedElementType()) {
                case SDR -> result = HistorySDRVSWRMapper.toFinalEntity(rawHistoryVSWRList);
                case ITBBU -> result = HistoryITBBUVSWRMapper.toFinalEntity(rawHistoryVSWRList);
            }
        }
        log.info(" >> historyVSWRList: {}", result == null ? "null" : result.size() + " points");
        return result;
    }

    @Override
    public List<HistoryForULocalCell> getHistoryCell(Token token, ManagedElement managedElement, int time, KPI kpi) {
        String json = null;
        List<HistoryForULocalCell> result = null;
        HistoryQueryBodySettings bodySettings = getSDROrITBBUBodySettings(managedElement, time, kpi);
        if (bodySettings != null) {
            json = rawDataQuery(token, managedElement, dataQueryRequest(token, bodySettings));
        }
//        System.out.println(json);
        HistoryTo historyTo = getHistoryTo(json, kpi);
        if (historyTo != null) {
            List<String> rawHistoryForULocalCellList = historyTo.getData();
            rawHistoryForULocalCellList.remove(0);
            switch (managedElement.getManagedElementType()) {
                case SDR -> result = HistorySDRForULocalCellMapper.toFinalEntity(rawHistoryForULocalCellList);
                case ITBBU -> result = HistoryITBBUForULocalCellMapper.toFinalEntity(rawHistoryForULocalCellList);
            }
        }
        log.info(" >> HistoryForULocalCell: {}", result == null ? "null" : result.size() + " points");
        return result;
    }

    @Override
    public List<HistoryMaxOpticError> getHistoryOptic(Token token, ManagedElement managedElement, int time) {
        KPI kpi = null;
        switch (managedElement.getManagedElementType()) {
            case SDR -> kpi = KPI.MAX_OPTIC_ERROR_SDR;
            case ITBBU -> kpi = KPI.MAX_OPTIC_ERROR_ITBBU;
        }

        String json = null;
        List<HistoryMaxOpticError> result = null;
        HistoryQueryBodySettings bodySettings = getSDROrITBBUBodySettings(managedElement, time, kpi);
        if (bodySettings != null) {
            json = rawDataQuery(token, managedElement, dataQueryRequest(token, bodySettings));
        }
//        System.out.println(json);
        HistoryTo historyTo = getHistoryTo(json, kpi);
        if (historyTo != null) {
            List<String> rawHistoryMaxOpticErrorList = historyTo.getData();
            rawHistoryMaxOpticErrorList.remove(0);
            switch (managedElement.getManagedElementType()) {
                case SDR -> result = HistorySDRMaxOpticErrorMapper.toFinalEntity(rawHistoryMaxOpticErrorList);
                case ITBBU -> result = HistoryITBBUMaxOpticErrorMapper.toFinalEntity(rawHistoryMaxOpticErrorList);
            }
        }
        log.info(" >> HistoryMaxOpticError: {}", result == null ? "null" : result.size() + " points");
        return result;
    }
}
