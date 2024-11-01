package bsshelper.externalapi.configurationmng.nemoactserv.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.SdrDeviceGroupMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.SdrDeviceGroupMocSimpl;
import bsshelper.externalapi.configurationmng.currentmng.to.itbbu.ITBBUULocalCellMocSimplifiedTo;
import bsshelper.externalapi.configurationmng.currentmng.util.CurrentMngBodySettings;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.OpticInfo;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.OpticInfoFinal;
import bsshelper.externalapi.configurationmng.nemoactserv.to.OpticInfoTo;
import bsshelper.externalapi.configurationmng.nemoactserv.util.BoardDiagnosisBodySettings;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExecNeActSDRServiceImpl implements ExecNeActSDRService {

    @Override
    public List<OpticInfoFinal> opticInfoFinalDataQuery(Token token, ManagedElement managedElement, List<SdrDeviceGroupMoc> sdrDeviceGroupMocList) {
        if (sdrDeviceGroupMocList.isEmpty()) {return null;}
        List<OpticInfo> opticInfoList = null;
        OpticInfoTo opticInfoTo = null;
        ErrorEntity error = null;
        String json = null;
        Map<String, List<String>> ldnMap = getLdnMap(sdrDeviceGroupMocList);
        List<OpticInfoFinal> result = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : ldnMap.entrySet()) {
            opticInfoList = null;
            String key = entry.getKey();
            List<String> ldnList = entry.getValue();
            for (String ldn : ldnList) {
                json = diagnosisOpticInfoDataQuery(token, managedElement, ldn);
                if (json != null) {
                    try {
                        opticInfoTo = new Gson().fromJson(json, OpticInfoTo.class);
                    } catch (JsonSyntaxException e1) {
                        e1.printStackTrace();
                        log.error(" >> error in opticInfoTo parsing: {}", e1.toString());
                    }
                    opticInfoList = opticInfoTo.getOutput().getResultInfo();
                }
                if (opticInfoList != null) {
                    switch (key) {
                        case "RU" -> result.addAll(OpticInfoFinal.toOpticInfoFinalForRU(opticInfoList));
                        case "FS" -> result.addAll(OpticInfoFinal.toOpticInfoFinalForFS(opticInfoList));
                        case "CCC" -> result.addAll(OpticInfoFinal.toOpticInfoFinalForCCC(opticInfoList));
                        case "UES" -> result.addAll(OpticInfoFinal.toOpticInfoFinalForUES(opticInfoList));
                    }
                }
            }
        }
        log.info(" >> opticInfoFinalList: {}", result);
        return result;
    }

    @Override
    public String diagnosisOpticInfoDataQuery(Token token, ManagedElement managedElement, String ldn) {
        HttpResponse<String> httpResponse = null;
        String response = null;
        ErrorEntity error = null;
        BoardDiagnosisBodySettings bodySettings = BoardDiagnosisBodySettings.builder().ldn(ldn)
                .input(new BoardDiagnosisBodySettings.Input(List.of("16777243"))).build();
        try {
            HttpRequest httpRequest = diagnosisDataRequest(token, bodySettings, managedElement);
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            response = httpResponse.body();
            if (response.contains("\"code\":0")) {
                log.info(" >> {} optic statistic for {} successfully found", ldn, managedElement.getUserLabel());
            } else {
                if (response.contains("Board communication link is interrupted.")) {
                    log.info(" >> {} optic statistic for {} couldn't found, because of board communication link is interrupted", ldn, managedElement.getUserLabel());
                    return response;
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
        }
        return response;
    }

    private HttpRequest diagnosisDataRequest(Token token, BoardDiagnosisBodySettings bodySettings, ManagedElement managedElement) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(bodySettings.getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_EXEC_NE_SERV +
                        "/ManagedElementType/" + managedElement.getManagedElementType() +
                        "/SubNetwork/" + managedElement.getSubNetworkNum() +
                        "/ManagedElement/" + managedElement.getManagedElementNum() + GlobalUtil.EXEC_NE_SERV_BOARD_DIAGNOSIS))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private Map<String, List<String>> getLdnMap(List<SdrDeviceGroupMoc> sdrDeviceGroupMocList) {
        Map<String, List<String>> map = new HashMap<>();
        for (SdrDeviceGroupMoc dev : sdrDeviceGroupMocList) {
            if (!dev.getLdn().contains(",Rack=1,")) {
                if (map.containsKey("RU")) map.get("RU").add(dev.getLdn());
                else map.put("RU", new ArrayList<>(List.of(dev.getLdn())));
                continue;
            }
            if (dev.getProductData_productName().contains("FS")) {
                if (map.containsKey("FS")) map.get("FS").add(dev.getLdn());
                else map.put("FS", new ArrayList<>(List.of(dev.getLdn())));
                continue;
            }
            if (dev.getProductData_productName().contains("CCC")) {
                if (map.containsKey("CCC")) map.get("CCC").add(dev.getLdn());
                else map.put("CCC", new ArrayList<>(List.of(dev.getLdn())));
                continue;
            }
            if (dev.getProductData_productName().contains("UES")) {
                if (map.containsKey("UES")) map.get("UES").add(dev.getLdn());
                else map.put("UES", new ArrayList<>(List.of(dev.getLdn())));
            }
        }
//        System.out.println(map);
        return map;
    }
}
