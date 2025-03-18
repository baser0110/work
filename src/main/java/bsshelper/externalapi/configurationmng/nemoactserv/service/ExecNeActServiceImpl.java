package bsshelper.externalapi.configurationmng.nemoactserv.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ReplaceableUnitMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.SdrDeviceGroupMoc;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.*;
import bsshelper.externalapi.configurationmng.nemoactserv.to.DiagnosisRowTo;
import bsshelper.externalapi.configurationmng.nemoactserv.to.OpticInfoITBBUTo;
import bsshelper.externalapi.configurationmng.nemoactserv.to.VSWRTestITBBUTo;
import bsshelper.externalapi.configurationmng.nemoactserv.util.BoardDiagnosisBodySettings;
import bsshelper.externalapi.configurationmng.nemoactserv.util.BoardDiagnosisITBBUBodySettings;
import bsshelper.externalapi.configurationmng.nemoactserv.util.DiagnosisAction;
import bsshelper.externalapi.openscriptexecengine.entity.ULocalCellStatus;
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
import java.util.*;

@Service
@Slf4j
public class ExecNeActServiceImpl implements ExecNeActService {

    @Override
    public List<OpticInfoFinal> opticInfoFinalDataQuery(Token token, ManagedElement managedElement, List<SdrDeviceGroupMoc> sdrDeviceGroupMocList) {
        if (sdrDeviceGroupMocList.isEmpty()) {return null;}
        List<DiagnosisRow> diagnosisRowList = null;
        DiagnosisRowTo diagnosisRowTo = null;
        String json = null;
        Map<String, List<String>> ldnMap = getLdnMap(sdrDeviceGroupMocList);
        List<OpticInfoFinal> result = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : ldnMap.entrySet()) {
            diagnosisRowList = null;
            String key = entry.getKey();
            List<String> ldnList = entry.getValue();
            for (String ldn : ldnList) {
                json = diagnosisSDRDataQuery(token, managedElement, ldn, DiagnosisAction.OPTICAL_ELECTRIC_INTERFACE_STATUS_TEST);
                if (json != null) {
                    try {
                        diagnosisRowTo = new Gson().fromJson(json, DiagnosisRowTo.class);
                    } catch (JsonSyntaxException e1) {
                        e1.printStackTrace();
                        log.error(" >> error in DiagnosisRowTo parsing: {}", e1.toString());
                    }
                    diagnosisRowList = diagnosisRowTo.getOutput().getResultInfo();
                }
                if (diagnosisRowList != null) {
                    switch (key) {
                        case "RU" -> result.addAll(OpticInfoFinal.toOpticInfoFinalForRU(diagnosisRowList));
                        case "FS" -> result.addAll(OpticInfoFinal.toOpticInfoFinalForFS(diagnosisRowList));
                        case "CCC" -> result.addAll(OpticInfoFinal.toOpticInfoFinalForCCC(diagnosisRowList));
                        case "UES" -> result.addAll(OpticInfoFinal.toOpticInfoFinalForUES(diagnosisRowList));
                        case "BP" -> result.addAll(OpticInfoFinal.toOpticInfoFinalForBP(diagnosisRowList));
                    }
                }
            }
        }
        log.info(" >> opticInfoFinalList: {}", result);
        return result;
    }

    @Override
    public List<VSWRTestFinal> vswrTestFinalDataQuery(Token token, ManagedElement managedElement, List<SdrDeviceGroupMoc> sdrDeviceGroupMocList) {
        if (sdrDeviceGroupMocList.isEmpty()) {return null;}
        List<DiagnosisRow> diagnosisRowList = null;
        DiagnosisRowTo diagnosisRowTo = null;
        String json = null;
        Map<String, List<String>> ldnMap = getLdnMap(sdrDeviceGroupMocList);
        List<String> ruLdnList = ldnMap.get("RU");
        List<VSWRTestFinal> result = new ArrayList<>();
        if (ruLdnList == null || ruLdnList.isEmpty()) {return null;}
        for (String ldn : ruLdnList) {
            json = diagnosisSDRDataQuery(token, managedElement, ldn, DiagnosisAction.RRU_VSWR_TEST);
            if (json != null) {
                try {
                    diagnosisRowTo = new Gson().fromJson(json, DiagnosisRowTo.class);
                } catch (JsonSyntaxException e1) {
                    e1.printStackTrace();
                    log.error(" >> error in DiagnosisRowTo parsing: {}", e1.toString());
                }
                diagnosisRowList = diagnosisRowTo.getOutput().getResultInfo();
            }
            if (diagnosisRowList != null) {
                result.addAll(VSWRTestFinal.toVSWRTestFinal(diagnosisRowList));
            }
        }
        result.sort(Comparator.comparing(VSWRTestFinal::getRuAntName));
        log.info(" >> vSWRTestFinalList: {}", result);
        return result;
    }

    @Override
    public String diagnosisSDRDataQuery(Token token, ManagedElement managedElement, String ldn, DiagnosisAction action) {
        HttpResponse<String> httpResponse = null;
        String response = null;
        ErrorEntity error = null;
        BoardDiagnosisBodySettings bodySettings = BoardDiagnosisBodySettings.builder().ldn(ldn)
                .input(new BoardDiagnosisBodySettings.Input(List.of(action.getCode()))).build();
        try {
            HttpRequest httpRequest = diagnosisDataRequest(token, bodySettings, managedElement);
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            response = httpResponse.body();
            if (response.contains("\"code\":0")) {
                log.info(" >> {} {} for {} successfully found", ldn, action.getInfo(), managedElement.getUserLabel());
            } else {
                if (response.contains("Board communication link is interrupted.")) {
                    log.info(" >> {} {} for {} couldn't found, because of board communication link is interrupted", ldn, action.getInfo(), managedElement.getUserLabel());
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
            if (e instanceof ConnectException) throw new CustomNetworkConnectionException((e.toString()));
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

    // ITBBU

    @Override
    public List<OpticInfoFinal> opticInfoFinalITBBUDataQuery(Token token, ManagedElement managedElement, List<ReplaceableUnitMoc> replaceableUnitMocList) {
        if (replaceableUnitMocList.isEmpty()) {return null;}
        List<OpticInfoFinal> result = new ArrayList<>();

        for (ReplaceableUnitMoc unit : replaceableUnitMocList) {
            String ldn = unit.getLdn();
            if (unit.getSlotNo().isEmpty()) {
                result.addAll(OpticInfoFinal.toOpticInfoFinalForITBBU("RU" + unit.getMoId(), getOpticInfoITBBUList(token, managedElement, ldn)));
                continue;
            }
            if (unit.getLdn().contains("VBP")) {
                result.addAll(OpticInfoFinal.toOpticInfoFinalForITBBU(unit.getMoId().replace("_1_", "(Slot") + ")",
                        getOpticInfoITBBUList(token, managedElement, ldn)));
                continue;
            }
            if (unit.getLdn().contains("VSW")) {
                result.addAll(OpticInfoFinal.toOpticInfoFinalForITBBU(unit.getMoId().replace("_1_", "(Slot") + ")",
                        getOpticInfoITBBUList(token, managedElement, ldn)));
            }
        }
        log.info(" >> opticInfoFinalList: {}", result);
        return result;
    }

    @Override
    public List<VSWRTestFinal> vswrTestFinalITBBUDataQuery(Token token, ManagedElement managedElement, List<ReplaceableUnitMoc> replaceableUnitMocList) {
        if (replaceableUnitMocList.isEmpty()) {return null;}
        List<VSWRTestFinal> result = new ArrayList<>();

        for (ReplaceableUnitMoc unit : replaceableUnitMocList) {
            String ldn = unit.getLdn();
            if (unit.getSlotNo().isEmpty()) {
                result.addAll(VSWRTestFinal.toVSWRTestFinalForITBBU("RU" + unit.getMoId(), getVSWRTestITBBUList(token, managedElement, ldn)));
            }
        }
        result.sort(Comparator.comparing(VSWRTestFinal::getRuAntName));
        log.info(" >> VSWRTestFinalList: {}", result);
        return result;
    }

    @Override
    public String ITBBUDataQuery(Token token, ManagedElement managedElement, String ldn, String query, String description) {
        HttpResponse<String> httpResponse = null;
        String response = null;
        ErrorEntity error = null;
        BoardDiagnosisITBBUBodySettings bodySettings = BoardDiagnosisITBBUBodySettings.builder().ldn(ldn).build();
        try {
            HttpRequest httpRequest = ITBBUDataRequest(token, bodySettings, managedElement, query);
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            response = httpResponse.body();
            if (response.contains("\"code\":0")) {
                log.info(" >> {} {} for {} successfully found", ldn, description, managedElement.getUserLabel());
            } else {
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

    private HttpRequest ITBBUDataRequest(Token token, BoardDiagnosisITBBUBodySettings bodySettings, ManagedElement managedElement, String query) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(bodySettings.getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_EXEC_NE_SERV +
                        "/ManagedElementType/" + managedElement.getManagedElementType() +
                        "/SubNetwork/" + managedElement.getSubNetworkNum() +
                        "/ManagedElement/" + managedElement.getManagedElementNum() + query))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private List<VSWRTestITBBU> getVSWRTestITBBUList(Token token, ManagedElement managedElement, String ldn) {
        VSWRTestITBBUTo vSWRTestITBBUTo = null;
        String json = null;
        List<VSWRTestITBBU> VSWRTestITBBUList = new ArrayList<>();
        json = ITBBUDataQuery(token, managedElement, ldn,
                GlobalUtil.EXEC_NE_SERV_QUERY_VSWR_ITBBU,
                DiagnosisAction.RRU_VSWR_TEST.getInfo());
        if (json != null) {
            try {
                vSWRTestITBBUTo = new Gson().fromJson(json, VSWRTestITBBUTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in VSWRTestITBBUTo parsing: {}", e1.toString());
            }
            if (vSWRTestITBBUTo != null) {
                VSWRTestITBBUList.addAll(vSWRTestITBBUTo.getOutput().getPaAntList());
            }
        }
        return VSWRTestITBBUList;
    }

    private List<OpticInfoITBBU> getOpticInfoITBBUList(Token token, ManagedElement managedElement, String ldn) {
        OpticInfoITBBUTo opticInfoITBBUTo = null;
        String json = null;
        List<OpticInfoITBBU> opticInfoITBBUList = new ArrayList<>();
        List<OpticInfoITBBU> qsfpResult = null;
        json = ITBBUDataQuery(token, managedElement, ldn,
                GlobalUtil.EXEC_NE_SERV_QUERY_OPT_ITBBU,
                DiagnosisAction.OPTICAL_ELECTRIC_INTERFACE_STATUS_TEST.getInfo());
        if (json != null) {
            try {
                opticInfoITBBUTo = new Gson().fromJson(json, OpticInfoITBBUTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in OpticInfoITBBUTo parsing: {}", e1.toString());
            }
            if (opticInfoITBBUTo != null) {
                opticInfoITBBUList.addAll(opticInfoITBBUTo.getOutput().getSfpInfo());
                qsfpResult = opticInfoITBBUTo.getOutput().getQsfpInfo();
                if (qsfpResult != null) {
                    opticInfoITBBUList.addAll(qsfpResult);
                }
            }
        }
        return opticInfoITBBUList;
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
            if (dev.getProductData_productName().contains("BPN") || dev.getProductData_productName().contains("BPQ")) {
                if (map.containsKey("BP")) map.get("BP").add(dev.getLdn());
                else map.put("BP", new ArrayList<>(List.of(dev.getLdn())));
            }
        }
        return map;
    }
}
