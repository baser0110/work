package bsshelper.externalapi.configurationmng.currentmng.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.CurrentMngBodySettings;
import bsshelper.externalapi.configurationmng.currentmng.entity.DryContactCableMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.DryContactDeviceMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.mapper.ManagedElementMapper;
import bsshelper.externalapi.configurationmng.currentmng.to.DryContactCableMocTo;
import bsshelper.externalapi.configurationmng.currentmng.to.DryContactDeviceMocTo;
import bsshelper.externalapi.configurationmng.currentmng.to.ManagedElementMocTo;
import bsshelper.globalutil.GlobalUtil;
import bsshelper.globalutil.ManagedElementType;
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
import java.util.List;

@Service
@Slf4j
public class CurrentMgnServiceImpl implements CurrentMgnService {
    @Override
    public String rawDataQuery(Token token, ManagedElement managedElement, String mocName) {
        HttpResponse<String> httpResponse = null;
        String response = null;
        try {
            HttpRequest httpRequest = dataQueryRequest(token, managedElement, mocName);
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
//            System.out.println(httpResponse.body());
            response = httpResponse.body();
            if (response.contains("\"code\":0")) {
                log.info(" >> {} for {} successfully found", mocName, managedElement.getUserLabel());
            }
        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
        }
        return response;
    }

    @Override
    public ManagedElement getManagedElementByNeName(Token token, String userLabel) {
        ManagedElementMocTo managedElementMocTo = null;
        ErrorEntity error = null;
        HttpRequest httpRequest = null;
        HttpResponse<String> httpResponse = null;
        try {
            httpRequest = getManagedElementByNeNameRequest(token, userLabel, ManagedElementType.SDR);
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String response = httpResponse.body();
//            System.out.println(response);
            if (response.contains("\"code\":0") && !response.equals("{\"code\":0,\"message\":\"Success\",\"result\":[],\"failList\":[]}")) {
                log.info(" >> managedElement with userLabel {} successfully found", userLabel);
            } else {
                httpRequest = getManagedElementByNeNameRequest(token, userLabel, ManagedElementType.ITBBU);
                httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
                response = httpResponse.body();
//                System.out.println(response);
                if (response.contains("\"code\":0") && !response.equals("{\"code\":0,\"message\":\"Success\",\"result\":[],\"failList\":[]}")) {
                    log.info(" >> managedElement with userLabel {} successfully found", userLabel);
                }
            }
            try {
                managedElementMocTo = new Gson().fromJson(response, ManagedElementMocTo.class);
            } catch (JsonSyntaxException e1) {
                log.error(" >> error in managedElementMocTo parsing: {}", e1.toString());
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
        }

//        System.out.println(managedElementMocTo);
        ManagedElement managedElement = ManagedElementMapper.toManagedElement(managedElementMocTo);
//        System.out.println(managedElement);
        log.info(" >> managedElement: {}", managedElement);
        return managedElement;
    }

    @Override
    public List<DryContactDeviceMoc> getDryContactDeviceMoc(Token token, ManagedElement managedElement) {
        String mocName = "DryContactDevice";
        List<DryContactDeviceMoc> dryContactDeviceMocList = null;
        DryContactDeviceMocTo dryContactDeviceMocTo = null;
        ErrorEntity error = null;
        String json = rawDataQuery(token, managedElement, mocName);
        try {
            dryContactDeviceMocTo = new Gson().fromJson(json, DryContactDeviceMocTo.class);
        } catch (JsonSyntaxException e1) {
            log.error(" >> error in dryContactDeviceMocTo parsing: {}", e1.toString());
            try {
                error = new Gson().fromJson(json, ErrorEntity.class);
            } catch (JsonSyntaxException e2) {
                log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
            }
            if (error != null) {
                log.error(" >> error {} code({})", error.getMessage(), error.getCode());
            }
//        System.out.println(dryContactDeviceMocTo);
        }
        if (dryContactDeviceMocTo != null) {
            dryContactDeviceMocList = dryContactDeviceMocTo.getResult().get(0).getMoData();
        }
        log.info(" >> dryContactDeviceMocList: {}", dryContactDeviceMocList);
//        System.out.println(dryContactDeviceMocList);
        return dryContactDeviceMocList;
    }

    @Override
    public List<DryContactCableMoc> getDryContactCableMoc(Token token, ManagedElement managedElement) {
        String mocName = "DryContactCable";
        List<DryContactCableMoc> dryContactCableMocList = null;
        DryContactCableMocTo dryContactCableMocTo = null;
        ErrorEntity error = null;
        String json = rawDataQuery(token, managedElement, mocName);
        try {
            dryContactCableMocTo = new Gson().fromJson(json, DryContactCableMocTo.class);
        } catch (JsonSyntaxException e1) {
            log.error(" >> error in dryContactDeviceMocTo parsing: {}", e1.toString());
            try {
                error = new Gson().fromJson(json, ErrorEntity.class);
            } catch (JsonSyntaxException e2) {
                log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
            }
            if (error != null) {
                log.error(" >> error {} code({})", error.getMessage(), error.getCode());
            }
//        System.out.println(dryContactDeviceMocTo);
        }
        if (dryContactCableMocTo != null) {
            dryContactCableMocList = dryContactCableMocTo.getResult().get(0).getMoData();
        }
        log.info(" >> dryContactCableMocList: {}", dryContactCableMocList);
//        System.out.println(dryContactDeviceMocList);
        return dryContactCableMocList;
    }


    private HttpRequest dataQueryRequest(Token token, ManagedElement managedElement, String mocName) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        CurrentMngBodySettings.builder()
                                .ManagedElementType(managedElement.getManagedElementType().toString())
                                .neList(List.of(managedElement.getNe()))
                                .mocList(List.of(mocName))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_CURRENTAREA + GlobalUtil.CURRENTAREA_QUERY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private HttpRequest getManagedElementByNeNameRequest(Token token, String userLabel, ManagedElementType type) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        CurrentMngBodySettings.builder()
                                .ManagedElementType(type.toString())
                                .mocList(List.of("ManagedElement"))
                                .moFilter(List.of(new CurrentMngBodySettings.MoFilter("ManagedElement","userLabel='" + userLabel.trim().toUpperCase() + "'")))
                                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("ManagedElement", List.of("userLabel"))))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_CURRENTAREA + GlobalUtil.CURRENTAREA_QUERY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }
}
