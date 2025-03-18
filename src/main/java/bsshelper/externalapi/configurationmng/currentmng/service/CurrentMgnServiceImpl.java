package bsshelper.externalapi.configurationmng.currentmng.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.*;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.*;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GGsmCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GTrxMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UIubLinkMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.*;
import bsshelper.externalapi.configurationmng.currentmng.mapper.ManagedElementMapper;
import bsshelper.externalapi.configurationmng.currentmng.to.*;
import bsshelper.externalapi.configurationmng.currentmng.to.itbbu.*;
import bsshelper.externalapi.configurationmng.currentmng.to.mrnc.GGsmCellMocSimplifiedTo;
import bsshelper.externalapi.configurationmng.currentmng.to.mrnc.GTrxMocSimplifiedTo;
import bsshelper.externalapi.configurationmng.currentmng.to.mrnc.UIubLinkMocSimplifiedTo;
import bsshelper.externalapi.configurationmng.currentmng.to.mrnc.UUtranCellFDDMocSimplifiedTo;
import bsshelper.externalapi.configurationmng.currentmng.to.sdr.*;
import bsshelper.externalapi.configurationmng.currentmng.util.CurrentMngBodySettings;
import bsshelper.globalutil.GlobalUtil;
import bsshelper.globalutil.ManagedElementType;
import bsshelper.globalutil.SubnetworkToBSC;
import bsshelper.globalutil.Verb;
import bsshelper.globalutil.entity.ErrorEntity;
import bsshelper.globalutil.exception.CustomNetworkConnectionException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
public class CurrentMgnServiceImpl implements CurrentMgnService {
    @Override
    public String rawDataQuery(Token token, ManagedElement managedElement, String mocName) {
        HttpResponse<String> httpResponse = null;
        String response = null;
        ErrorEntity error = null;
        try {
            HttpRequest httpRequest = dataRawQueryRequest(token, managedElement, mocName);
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
//            System.out.println(httpResponse.body());
            response = httpResponse.body();
            if (response.contains("\"code\":0") && !response.contains("\"result\":[]")) {
                log.info(" >> {} for {} successfully found", mocName, managedElement.getUserLabel());
            } else {
                if (response.contains("\"code\":0")) {
                    log.info(" >> {} for {} successfully found but it's empty", mocName, managedElement.getUserLabel());
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
    public String simplifiedRawDataQuery(Token token, ManagedElement managedElement, String mocName, HttpRequest httpRequest) {
        HttpResponse<String> httpResponse = null;
        String response = null;
        ErrorEntity error = null;
        String userLabel = managedElement == null ? "all" : managedElement.getUserLabel();
        try {
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
//            System.out.println(httpResponse.body());
            response = httpResponse.body();
            if (response.contains("\"code\":0") && !response.contains("\"result\":[]")) {
                if (managedElement == null) {
                    log.info(" >> {} successfully found", mocName);
                } else {
                    log.info(" >> {} for {} successfully found", mocName, userLabel);
                }
            } else {
                if (response.contains("\"code\":0")) {
                    if (managedElement == null) {
                        log.info(" >> {} successfully found", mocName);
                    } else {
                        log.info(" >> {} for {} successfully found but it's empty", mocName, userLabel);
                    }
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
    public ManagedElement getManagedElementByNeName(Token token, String userLabel) {
        ManagedElementMocTo managedElementMocTo = null;
        ErrorEntity error = null;
        HttpRequest httpRequest = null;
        HttpResponse<String> httpResponse = null;
        ManagedElement managedElement = null;
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
            if (response.equals("{\"code\":0,\"message\":\"Success\",\"result\":[],\"failList\":[]}")) {
                log.info(" >> managedElement with userLabel {} couldn't be found", userLabel);
                return null;
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
            if (managedElementMocTo != null) {
                managedElement = ManagedElementMapper.toManagedElement(managedElementMocTo);
                log.info(" >> managedElement: {}", managedElement);
            }
        } catch (IOException | InterruptedException e) {
            log.error(" >> error in sending http request: {}", e.toString());
            if (e instanceof ConnectException) throw new CustomNetworkConnectionException((e.toString()));
        }
        return managedElement;
    }

    @Override
    public List<DryContactDeviceMoc> getDryContactDeviceMoc(Token token, ManagedElement managedElement) {
        String mocName = "DryContactDevice";
        List<DryContactDeviceMoc> dryContactDeviceMocList = null;
        DryContactDeviceMocTo dryContactDeviceMocTo = null;
        String json = rawDataQuery(token, managedElement, mocName);
        try {
            dryContactDeviceMocTo = new Gson().fromJson(json, DryContactDeviceMocTo.class);
        } catch (JsonSyntaxException e1) {
            log.error(" >> error in dryContactDeviceMocTo parsing: {}", e1.toString());
        }
        if (dryContactDeviceMocTo != null) {
            dryContactDeviceMocList = dryContactDeviceMocTo.getResult().get(0).getMoData();
        }
        log.info(" >> dryContactDeviceMocList: {}", dryContactDeviceMocList);
        return dryContactDeviceMocList;
    }

    @Override
    public List<RiCableMoc> getRiCableMoc(Token token, ManagedElement managedElement) {
        String mocName = "RiCable";
        List<RiCableMoc> riCableMocList = null;
        RiCableMocTo riCableMocTo = null;
        String json = rawDataQuery(token, managedElement, mocName);
        try {
            riCableMocTo = new Gson().fromJson(json, RiCableMocTo.class);
        } catch (JsonSyntaxException e1) {
            log.error(" >> error in RiCableMocTo parsing: {}", e1.toString());
        }
        if (riCableMocTo != null) {
            riCableMocList = riCableMocTo.getResult().get(0).getMoData();
        }
        log.info(" >> riCableMocList: {}", riCableMocList);
        return riCableMocList;
    }

    @Override
    public List<ReplaceableUnitMoc> getReplaceableUnitMoc(Token token, ManagedElement managedElement) {
        String mocName = "ReplaceableUnit";
        List<ReplaceableUnitMoc> replaceableUnitMocList = null;
        ReplaceableUnitMocTo replaceableUnitMocTo = null;
        String json = rawDataQuery(token, managedElement, mocName);
        try {
            replaceableUnitMocTo = new Gson().fromJson(json, ReplaceableUnitMocTo.class);
        } catch (JsonSyntaxException e1) {
            log.error(" >> error in ReplaceableUnitMocTo parsing: {}", e1.toString());
        }
        if (replaceableUnitMocTo != null) {
            replaceableUnitMocList = replaceableUnitMocTo.getResult().get(0).getMoData();
        }
        log.info(" >> replaceableUnitMocList: {}", replaceableUnitMocList);
        return replaceableUnitMocList;
    }

    @Override
    public List<FiberCableMoc> getFiberCableMoc(Token token, ManagedElement managedElement) {
        String mocName = "FiberCable";
        List<FiberCableMoc> fiberCableMocList = null;
        FiberCableMocTo fiberCableMocTo = null;
        String json = rawDataQuery(token, managedElement, mocName);
        try {
            fiberCableMocTo = new Gson().fromJson(json, FiberCableMocTo.class);
        } catch (JsonSyntaxException e1) {
            log.error(" >> error in FiberCableMocTo parsing: {}", e1.toString());
        }
        if (fiberCableMocTo != null) {
            fiberCableMocList = fiberCableMocTo.getResult().get(0).getMoData();
        }
        log.info(" >> fiberCableMocList: {}", fiberCableMocList);
        return fiberCableMocList;
    }

    @Override
    public List<DryContactCableMoc> getDryContactCableMoc(Token token, ManagedElement managedElement) {
        String mocName = "DryContactCable";
        List<DryContactCableMoc> dryContactCableMocList = null;
        DryContactCableMocTo dryContactCableMocTo = null;
        String json = rawDataQuery(token, managedElement, mocName);
        try {
            dryContactCableMocTo = new Gson().fromJson(json, DryContactCableMocTo.class);
        } catch (JsonSyntaxException e1) {
            log.error(" >> error in dryContactDeviceMocTo parsing: {}", e1.toString());
        }
        if (dryContactCableMocTo != null) {
            dryContactCableMocList = dryContactCableMocTo.getResult().get(0).getMoData();
        }
        log.info(" >> dryContactCableMocList: {}", dryContactCableMocList);
        return dryContactCableMocList;
    }

    @Override
    public List<SdrDeviceGroupMoc> getSdrDeviceGroupMoc(Token token, ManagedElement managedElement) {
        String mocName = "SdrDeviceGroup";
        List<SdrDeviceGroupMoc> sdrDeviceGroupMocList = null;
        SdrDeviceGroupMocTo sdrDeviceGroupMocTo = null;
        String json = rawDataQuery(token, managedElement, mocName);
        try {
            sdrDeviceGroupMocTo = new Gson().fromJson(json, SdrDeviceGroupMocTo.class);
        } catch (JsonSyntaxException e1) {
            log.error(" >> error in sdrDeviceGroupMocTo parsing: {}", e1.toString());
        }
        if (sdrDeviceGroupMocTo != null) {
            sdrDeviceGroupMocList = sdrDeviceGroupMocTo.getResult().get(0).getMoData();
        }
        log.info(" >> sdrDeviceGroupMocList: {}", sdrDeviceGroupMocList);
        return sdrDeviceGroupMocList;
    }

    @Override
    public List<ULocalCellMoc> getULocalCellMoc(Token token, ManagedElement managedElement) {
        String mocName = "ULocalCell";
        List<ULocalCellMoc> ULocalCellMocList = null;
        ULocalCellMocTo uLocalCellMocTo = null;
        String json = rawDataQuery(token, managedElement, mocName);
        if (json != null) {
            try {
                uLocalCellMocTo = new Gson().fromJson(json, ULocalCellMocTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in ULocalCellMocTo parsing: {}", e1.toString());
            }
        }
        if (uLocalCellMocTo != null) {
            ULocalCellMocList = uLocalCellMocTo.getResult().get(0).getMoData();
        }
        log.info(" >> uLocalCellMocList: {}", ULocalCellMocList);
        return ULocalCellMocList;
    }

    @Override
    public List<ITBBUULocalCellMoc> getITBBUULocalCellMoc(Token token, ManagedElement managedElement) {
        String mocName = "ULocalCell";
        List<ITBBUULocalCellMoc> iTBBUULocalCellMocList = null;
        ITBBUULocalCellMocTo iTBBUULocalCellMocTo = null;
        String json = rawDataQuery(token, managedElement, mocName);
        if (json != null) {
            try {
                iTBBUULocalCellMocTo = new Gson().fromJson(json, ITBBUULocalCellMocTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in ITBBUULocalCellMocTo parsing: {}", e1.toString());
            }
        }
        if (iTBBUULocalCellMocTo != null) {
            iTBBUULocalCellMocList = iTBBUULocalCellMocTo.getResult().get(0).getMoData();
        }
        log.info(" >> uLocalCellMocList: {}", iTBBUULocalCellMocList);
        return iTBBUULocalCellMocList;
    }


    @Override
    public List<ULocalCellMocSimplified> getULocalCellMocSimplified(Token token, ManagedElement managedElement) {
        String mocName = "ULocalCell";
        List<ULocalCellMocSimplified> uLocalCellMocSimplifiedList = null;
        ULocalCellMocSimplifiedTo uLocalCellMocSimplifiedTo = null;
        String json = simplifiedRawDataQuery(token, managedElement, mocName,
                getSimplifyULocalCellByNeNameRequest(token, managedElement));
        if (json != null) {
            try {
                uLocalCellMocSimplifiedTo = new Gson().fromJson(json, ULocalCellMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in ULocalCellMocSimplifiedTo parsing: {}", e1.toString());
            }
        }
        if (uLocalCellMocSimplifiedTo != null) {
            uLocalCellMocSimplifiedList = uLocalCellMocSimplifiedTo.getResult().get(0).getMoData();
//            System.out.println(uLocalCellMocSimplifiedTo.getResult().size());
        }
        log.info(" >> ULocalCellMocSimplifiedList: {}", uLocalCellMocSimplifiedList);
        return uLocalCellMocSimplifiedList;
    }

    @Override
    public List<UCellMocSimplified> getUCellMocSimplified(Token token, ManagedElement managedElement) {
        String mocName = "UCell";
        List<UCellMocSimplified> uCellMocSimplifiedList = null;
        UCellMocSimplifiedTo uCellMocSimplifiedTo = null;
        String json = null;
        if (managedElement.getManagedElementType().equals(ManagedElementType.SDR)) {
            json = simplifiedRawDataQuery(token, managedElement, mocName,
                    getSimplifyUCellByNeNameRequest(token, managedElement));
        } else {
            json = simplifiedRawDataQuery(token, managedElement, mocName,
                    getSimplifyITBBUUCellByNeNameRequest(token, managedElement));
        }
        if (json != null) {
            try {
                uCellMocSimplifiedTo = new Gson().fromJson(json, UCellMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in UCellMocSimplifiedTo parsing: {}", e1.toString());
            }
        }
        if (uCellMocSimplifiedTo != null) {
            uCellMocSimplifiedList = uCellMocSimplifiedTo.getResult().get(0).getMoData();
//            System.out.println(uLocalCellMocSimplifiedTo.getResult().size());
        }
        log.info(" >> UCellMocSimplifiedList: {}", uCellMocSimplifiedList);
        return uCellMocSimplifiedList;
    }

    @Override
    public List<ITBBUULocalCellMocSimplified> getITBBUULocalCellMocSimplified(Token token, ManagedElement managedElement) {
        String mocName = "ULocalCell";
        List<ITBBUULocalCellMocSimplified> iTBBUULocalCellMocSimplifiedList = null;
        ITBBUULocalCellMocSimplifiedTo iTBBUULocalCellMocSimplifiedTo = null;
        String json = simplifiedRawDataQuery(token, managedElement, mocName,
                getSimplifyITBBUULocalCellByNeNameRequest(token, managedElement));
        if (json != null) {
            try {
                iTBBUULocalCellMocSimplifiedTo = new Gson().fromJson(json, ITBBUULocalCellMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in ITBBUULocalCellMocSimplifiedTo parsing: {}", e1.toString());
            }
        }
        if (iTBBUULocalCellMocSimplifiedTo != null) {
            iTBBUULocalCellMocSimplifiedList = iTBBUULocalCellMocSimplifiedTo.getResult().get(0).getMoData();
        }
        log.info(" >> iTBBUULocalCellMocSimplifiedList: {}", iTBBUULocalCellMocSimplifiedList);
        return iTBBUULocalCellMocSimplifiedList;
    }

    @Override
    public List<EUtranCellNBIoTMocSimplified> getEUtranCellNBIoTMocSimplified(Token token, ManagedElement managedElement) {
        String mocName = "EUtranCellNBIoT";
        List<EUtranCellNBIoTMocSimplified> eUtranCellNBIoTMocSimplifiedList = null;
        EUtranCellNBIoTMocSimplifiedTo eUtranCellNBIoTMocSimplifiedTo = null;
        String json = simplifiedRawDataQuery(token, managedElement, mocName,
                getSimplifyEUtranCellNBIoTMocByNeNameRequest(token, managedElement));
        if (json != null) {
            try {
                eUtranCellNBIoTMocSimplifiedTo = new Gson().fromJson(json, EUtranCellNBIoTMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in EUtranCellNBIoTMocSimplifiedTo parsing: {}", e1.toString());
            }
        }
        if (eUtranCellNBIoTMocSimplifiedTo != null) {
            eUtranCellNBIoTMocSimplifiedList = eUtranCellNBIoTMocSimplifiedTo.getResult().get(0).getMoData();
        }
        log.info(" >> eUtranCellNBIoTMocSimplifiedList: {}", eUtranCellNBIoTMocSimplifiedList);
        return eUtranCellNBIoTMocSimplifiedList;
    }

    @Override
    public List<ITBBUCUEUtranCellNBIoTMocSimplified> getITBBUCUEUtranCellNBIoTMocSimplified(Token token, ManagedElement managedElement) {
        String mocName = "CUEUtranCellNBIoT";
        List<ITBBUCUEUtranCellNBIoTMocSimplified> iTBBUCUEUtranCellNBIoTMocSimplifiedList = null;
        ITBBUCUEUtranCellNBIoTMocSimplifiedTo iTBBUCUEUtranCellNBIoTMocSimplifiedTo = null;
        String json = simplifiedRawDataQuery(token, managedElement, mocName,
                getSimplifyCUEUtranCellNBIoTMocByNeNameRequest(token, managedElement));
        if (json != null) {
            try {
                iTBBUCUEUtranCellNBIoTMocSimplifiedTo = new Gson().fromJson(json, ITBBUCUEUtranCellNBIoTMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in ITBBUCUEUtranCellNBIoTMocSimplifiedTo parsing: {}", e1.toString());
            }
        }
        if (iTBBUCUEUtranCellNBIoTMocSimplifiedTo != null) {
            iTBBUCUEUtranCellNBIoTMocSimplifiedList = iTBBUCUEUtranCellNBIoTMocSimplifiedTo.getResult().get(0).getMoData();
        }
        log.info(" >> iTBBUCUEUtranCellNBIoTMocSimplifiedList: {}", iTBBUCUEUtranCellNBIoTMocSimplifiedList);
        return iTBBUCUEUtranCellNBIoTMocSimplifiedList;
    }

    @Override
    public List<GGsmCellMocSimplified> getGGsmCellMocSimplified(Token token, ManagedElement managedElement) {
        String mocName = "GGsmCell";
        List<GGsmCellMocSimplified> gGsmCellMocSimplifiedList = null;
        GGsmCellMocSimplifiedTo gGsmCellMocSimplifiedTo = null;
        CurrentMngBodySettings bodySettings = getGGsmCellMocSimplifiedBodySettings(token, managedElement);
        if (bodySettings == null) {
            log.error(" >> couldn't found GGsmCell data for {}", managedElement.getUserLabel());
            return null;
        }
        String json = simplifiedRawDataQuery(token, managedElement, mocName, dataQueryRequest(token, bodySettings));
        if (json != null) {
            try {
                gGsmCellMocSimplifiedTo = new Gson().fromJson(json, GGsmCellMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in gGsmCellMocSimplifiedTo parsing: {}", e1.toString());
            }
        }
        if (gGsmCellMocSimplifiedTo != null) {
            gGsmCellMocSimplifiedList = gGsmCellMocSimplifiedTo.getResult().get(0).getMoData();
            gGsmCellMocSimplifiedList.sort(Comparator.comparing(GGsmCellMocSimplified::getUserLabel));
//            // temporarily
//            if (!gGsmCellMocSimplifiedList.getFirst().getUserLabel().substring(0,gGsmCellMocSimplifiedList.getFirst().getUserLabel().length() - 1).equals(managedElement.getUserLabel())) {
//                gGsmCellMocSimplifiedList = null;
//            }
        }
        log.info(" >> gGsmCellMocSimplifiedList: {}", gGsmCellMocSimplifiedList);

        return gGsmCellMocSimplifiedList;
    }

    @Override
    public List<GTrxMocSimplified> getGTrxMocSimplified(Token token, ManagedElement managedElement, List<GGsmCellMocSimplified> cells) {
        if (cells == null) return null;
        String mocName = "GTrx";
        List<GTrxMocSimplified> gTrxMocSimplifiedList = null;
        GTrxMocSimplifiedTo gTrxMocSimplifiedTo = null;
        CurrentMngBodySettings bodySettings = getGTrxMocSimplifiedBodySettings(token, managedElement, cells);
        if (bodySettings == null) {
            log.error(" >> couldn't found GTrx data for {}", managedElement.getUserLabel());
            return null;
        }
        String json = simplifiedRawDataQuery(token, managedElement, mocName, dataQueryRequest(token, bodySettings));
        if (json != null) {
            try {
                gTrxMocSimplifiedTo = new Gson().fromJson(json, GTrxMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in gTrxMocSimplifiedTo parsing: {}", e1.toString());
            }
        }
        if (gTrxMocSimplifiedTo != null) {
            gTrxMocSimplifiedList = gTrxMocSimplifiedTo.getResult().get(0).getMoData();
            gTrxMocSimplifiedList.sort(Comparator.comparing(GTrxMocSimplified::getUserLabel));
        }
        log.info(" >> gTrxMocSimplifiedList: {}", gTrxMocSimplifiedList);
        return gTrxMocSimplifiedList;
    }

    @Override
    public List<UUtranCellFDDMocSimplified> getUUtranCellFDDMocSimplified(Token token, ManagedElement managedElement) {
        String mocName = "UUtranCellFDD";
        List<UUtranCellFDDMocSimplified> uUtranCellFDDMocSimplifiedList = null;
        UUtranCellFDDMocSimplifiedTo uUtranCellFDDMocSimplifiedTo = null;
        CurrentMngBodySettings bodySettings = getUUtranCellFDDMocSimplifiedBodySettings(token, managedElement);
        if (bodySettings == null) {
            log.error(" >> couldn't found UUtranCellFDD data for {}", managedElement.getUserLabel());
            return null;
        }
        String json = simplifiedRawDataQuery(token, managedElement, mocName, dataQueryRequest(token, bodySettings));
        if (json != null) {
            try {
                uUtranCellFDDMocSimplifiedTo = new Gson().fromJson(json, UUtranCellFDDMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in UUtranCellFDDMocSimplifiedTo parsing: {}", e1.toString());
            }
        }
        if (uUtranCellFDDMocSimplifiedTo != null) {
            uUtranCellFDDMocSimplifiedList = uUtranCellFDDMocSimplifiedTo.getResult().get(0).getMoData();
            uUtranCellFDDMocSimplifiedList.sort(Comparator.comparing(UUtranCellFDDMocSimplified::getUserLabel));
        }
        log.info(" >> uUtranCellFDDMocSimplifiedList: {}", uUtranCellFDDMocSimplifiedList);
        return uUtranCellFDDMocSimplifiedList;
    }

    @Override
    public List<UIubLinkMocSimplified> getUIubLinkMocSimplified(Token token, ManagedElement managedElement) {
        String mocName = "UIubLink";
        List<UIubLinkMocSimplified> uIubLinkMocSimplifiedList = null;
        UIubLinkMocSimplifiedTo uIubLinkMocSimplifiedTo = null;
        CurrentMngBodySettings bodySettings = getUIubLinkMocSimplifiedBodySettings(token, managedElement);
        if (bodySettings == null) {
            log.error(" >> couldn't found UIubLink data for {}", managedElement.getUserLabel());
            return null;
        }
        String json = simplifiedRawDataQuery(token, managedElement, mocName, dataQueryRequest(token, bodySettings));
        if (json != null) {
            try {
                uIubLinkMocSimplifiedTo = new Gson().fromJson(json, UIubLinkMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in uIubLinkMocSimplifiedTo parsing: {}", e1.toString());
            }
        }
        if (uIubLinkMocSimplifiedTo != null) {
            uIubLinkMocSimplifiedList = uIubLinkMocSimplifiedTo.getResult().get(0).getMoData();
        }
        log.info(" >> uIubLinkMocSimplifiedList: {}", uIubLinkMocSimplifiedList);
        return uIubLinkMocSimplifiedList;
    }

    // GET ALL CELL CACHE

    @Override
    public Map<String,CellInfo> getCacheSDRCellsUMTS(Token token) {
        String mocName = "ULocalCell";
        Map<String,CellInfo> umtsSDRMap = new TreeMap<>();
        List<ULocalCellMocSimplifiedTo.ULocalCellMocSimplifiedResultTo> iterateResultList = null;
        ULocalCellMocSimplifiedTo uLocalCellMocSimplifiedTo = null;
        List<ULocalCellMocSimplified> uLocalCellMocSimplifiedList = null;
        String ne = null;
        String json = simplifiedRawDataQuery(token, null, mocName,
                getAllSimplifyULocalCellRequest(token));
        if (json != null) {
            try {
                uLocalCellMocSimplifiedTo = new Gson().fromJson(json, ULocalCellMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in ULocalCellMocSimplifiedTo parsing: {}", e1.toString());
            }
        }
        if (uLocalCellMocSimplifiedTo != null) {
            iterateResultList = uLocalCellMocSimplifiedTo.getResult();
            for (ULocalCellMocSimplifiedTo.ULocalCellMocSimplifiedResultTo site : iterateResultList) {
                uLocalCellMocSimplifiedList = site.getMoData();
                ne = site.getNe();
                for (ULocalCellMocSimplified cell : uLocalCellMocSimplifiedList) {
                    StringBuilder query = new StringBuilder();
                    query.append("ManagedElementType=")
                            .append(ManagedElementType.SDR)
                            .append(",")
                            .append(ne)
                            .append(",")
                            .append(cell.getLdn());
                    umtsSDRMap.put(cell.getUserLabel(),new CellInfo(query.toString(), ne));
                }
            }
        }
        log.info(" >> umtsSDRMap for cache: {}", umtsSDRMap.size());
        return umtsSDRMap;
    }

    @Override
    public Map<String,CellInfo> getCacheITBBUCellsUMTS(Token token) {
        String mocName = "ULocalCell";
        Map<String,CellInfo> umtsITBBUMap = new TreeMap<>();
        List<ITBBUULocalCellMocSimplifiedTo.ITBBUULocalCellMocSimplifiedResultTo> iterateResultList = null;
        List<ITBBUULocalCellMocSimplified> iTBBUULocalCellMocSimplifiedList = null;
        ITBBUULocalCellMocSimplifiedTo iTBBUULocalCellMocSimplifiedTo = null;
        String ne = null;
        String json = simplifiedRawDataQuery(token, null, mocName,
                getAllSimplifyITBBUULocalCellRequest(token));
        if (json != null) {
            try {
                iTBBUULocalCellMocSimplifiedTo = new Gson().fromJson(json, ITBBUULocalCellMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in ITBBUULocalCellMocSimplifiedTo parsing: {}", e1.toString());
            }
        }
        if (iTBBUULocalCellMocSimplifiedTo != null) {
            iterateResultList = iTBBUULocalCellMocSimplifiedTo.getResult();
            for (ITBBUULocalCellMocSimplifiedTo.ITBBUULocalCellMocSimplifiedResultTo site : iterateResultList) {
                iTBBUULocalCellMocSimplifiedList = site.getMoData();
                ne = site.getNe();
                for (ITBBUULocalCellMocSimplified cell : iTBBUULocalCellMocSimplifiedList) {
                    StringBuilder query = new StringBuilder();
                    query.append("ManagedElementType=")
                            .append(ManagedElementType.ITBBU)
                            .append(",")
                            .append(ne)
                            .append(",")
                            .append(cell.getLdn());
                    umtsITBBUMap.put(cell.getUserLabel(),new CellInfo(query.toString(), ne));
                }
            }
        }
        log.info(" >> umtsITBBUMap for cache: {}", umtsITBBUMap.size());
        return umtsITBBUMap;
    }

    @Override
    public Map<String,CellInfo> getCacheSDRCellsNBIOT(Token token) {
        String mocName = "EUtranCellNBIoT";
        Map<String,CellInfo> nbiotSDRMap = new TreeMap<>();
        List<EUtranCellNBIoTMocSimplifiedTo.EUtranCellNBIoTMocSimplifiedResultTo> iterateResultList = null;
        EUtranCellNBIoTMocSimplifiedTo eUtranCellNBIoTMocSimplifiedTo = null;
        List<EUtranCellNBIoTMocSimplified> eUtranCellNBIoTMocSimplifiedList = null;
        String ne = null;
        String json = simplifiedRawDataQuery(token, null, mocName,
                getAllSimplifyEUtranCellNBIoTMocRequest(token));
        if (json != null) {
            try {
                eUtranCellNBIoTMocSimplifiedTo = new Gson().fromJson(json, EUtranCellNBIoTMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in EUtranCellNBIoTMocSimplifiedTo parsing: {}", e1.toString());
            }
        }
        if (eUtranCellNBIoTMocSimplifiedTo != null) {
            iterateResultList = eUtranCellNBIoTMocSimplifiedTo.getResult();
            for (EUtranCellNBIoTMocSimplifiedTo.EUtranCellNBIoTMocSimplifiedResultTo site : iterateResultList) {
                eUtranCellNBIoTMocSimplifiedList = site.getMoData();
                ne = site.getNe();
                for (EUtranCellNBIoTMocSimplified cell : eUtranCellNBIoTMocSimplifiedList) {
                    StringBuilder query = new StringBuilder();
                    query.append("ManagedElementType=")
                            .append(ManagedElementType.SDR)
                            .append(",")
                            .append(ne)
                            .append(",")
                            .append(cell.getLdn());
                    nbiotSDRMap.put(cell.getUserLabel(),new CellInfo(query.toString(), ne));
                }
            }
        }
        log.info(" >> nbiotSDRMap for cache: {}", nbiotSDRMap.size());
        return nbiotSDRMap;
    }

    @Override
    public Map<String,CellInfo> getCacheITBBUCellsNBIOT(Token token) {
        String mocName = "CUEUtranCellNBIoT";
        Map<String,CellInfo> nbiotITBBUMap = new TreeMap<>();
        List<ITBBUCUEUtranCellNBIoTMocSimplifiedTo.ITBBUCUEUtranCellNBIoTMocSimplifiedResultTo> iterateResultList = null;
        ITBBUCUEUtranCellNBIoTMocSimplifiedTo iTBBUCUEUtranCellNBIoTMocSimplifiedTo = null;
        List<ITBBUCUEUtranCellNBIoTMocSimplified> iTBBUCUEUtranCellNBIoTMocSimplifiedList = null;
        String ne = null;
        String json = simplifiedRawDataQuery(token, null, mocName,
                getAllSimplifyCUEUtranCellNBIoTMocRequest(token));
        if (json != null) {
            try {
                iTBBUCUEUtranCellNBIoTMocSimplifiedTo = new Gson().fromJson(json, ITBBUCUEUtranCellNBIoTMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in ITBBUCUEUtranCellNBIoTMocSimplifiedTo parsing: {}", e1.toString());
            }
        }
        if (iTBBUCUEUtranCellNBIoTMocSimplifiedTo != null) {
            iterateResultList = iTBBUCUEUtranCellNBIoTMocSimplifiedTo.getResult();
            for (ITBBUCUEUtranCellNBIoTMocSimplifiedTo.ITBBUCUEUtranCellNBIoTMocSimplifiedResultTo site : iterateResultList) {
                iTBBUCUEUtranCellNBIoTMocSimplifiedList = site.getMoData();
                ne = site.getNe();
                for (ITBBUCUEUtranCellNBIoTMocSimplified cell : iTBBUCUEUtranCellNBIoTMocSimplifiedList) {
                    StringBuilder query = new StringBuilder();
                    query.append("ManagedElementType=")
                            .append(ManagedElementType.ITBBU)
                            .append(",")
                            .append(ne)
                            .append(",")
                            .append(cell.getLdn());
                    nbiotITBBUMap.put(cell.getUserLabel(),new CellInfo(query.toString(), ne));
                }
            }
        }
        log.info(" >> nbiotITBBUMap for cache: {}", nbiotITBBUMap.size());
        return nbiotITBBUMap;
    }

    @Override
    public Map<String,CellInfo> getCacheMRNCCellsGSM(Token token) {
        String mocName = "GGsmCell";
        Map<String,CellInfo> gsmMRNCMap = new TreeMap<>();
        List<GGsmCellMocSimplified> gGsmCellMocSimplifiedList = null;
        GGsmCellMocSimplifiedTo gGsmCellMocSimplifiedTo = null;
        List<GGsmCellMocSimplifiedTo.GGsmCellMocSimplifiedResultTo> iterateResultList = null;
        String ne = null;
        String meId = null;
        String ldn = null;
        String siteId = null;
        String btsId = null;
        String json = simplifiedRawDataQuery(token, null, mocName, getAllSimplifyGGsmCellMocRequest(token));
        if (json != null) {
            try {
                gGsmCellMocSimplifiedTo = new Gson().fromJson(json, GGsmCellMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in gGsmCellMocSimplifiedTo parsing: {}", e1.toString());
            }
        }
        if (gGsmCellMocSimplifiedTo != null) {
            iterateResultList = gGsmCellMocSimplifiedTo.getResult();
            for (GGsmCellMocSimplifiedTo.GGsmCellMocSimplifiedResultTo controller : iterateResultList) {
                gGsmCellMocSimplifiedList = controller.getMoData();
                ne = controller.getNe();
                meId = ne.substring(ne.indexOf("=") + 1, ne.indexOf("=") + 4);

                for (GGsmCellMocSimplified cell : gGsmCellMocSimplifiedList) {
                    ldn = cell.getLdn();
                    siteId = ldn.substring(ldn.indexOf("r=") + 2, ldn.indexOf(",GGs"));
                    btsId = ldn.substring(ldn.lastIndexOf("=") + 1);
                    StringBuilder query = new StringBuilder();
                    query.append("CELL:MEID=")
                            .append(meId)
                            .append(",SITEID=")
                            .append(siteId)
                            .append(",BTSID=")
                            .append(btsId)
                            .append(";")
                            .append(" --netype MRNC --neid ")
                            .append(meId);
                    gsmMRNCMap.put(cell.getUserLabel(),new CellInfo(query.toString(), cell.getUserLabel().substring(0,cell.getUserLabel().length() - 1)));
                }
            }
        }
        log.info(" >> gsmMRNCMap for cache: {}", gsmMRNCMap.size());
        return gsmMRNCMap;
    }

    // type - byUserLabel or byNe
    public Map<String,String> getCacheManagedElement(Token token, Type type) {
        String mocName = "ManagedElement";
        Map<String,String> resultMapByUserLabel = new TreeMap<>();
        Map<String,String> resultMapByNe = new TreeMap<>();
        List<ManagedElementMocTo.ManagedElementMocResultTo> iterateResultList = null;
        ManagedElementMocTo managedElementMocTo = null;
        List<ManagedElementMoc> managedElementMocList = null;
        String ne = null;
        String jsonSDR = simplifiedRawDataQuery(token, null, mocName,
                getAllManagedElementQueryRequest(token, mocName, ManagedElementType.SDR));
        if (jsonSDR != null) {
            try {
                managedElementMocTo = new Gson().fromJson(jsonSDR, ManagedElementMocTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in ManagedElementMocTo parsing: {}", e1.toString());
            }
        }
        if (managedElementMocTo != null) {
            iterateResultList = managedElementMocTo.getResult();
            for (ManagedElementMocTo.ManagedElementMocResultTo site : iterateResultList) {
                managedElementMocList = site.getMoData();
                ne = site.getNe();
                resultMapByUserLabel.put(managedElementMocList.get(0).getUserLabel(), ne);
                resultMapByNe.put(ne, managedElementMocList.get(0).getUserLabel());
            }
        }
        iterateResultList = null;
        managedElementMocTo = null;
        managedElementMocList = null;
        String jsonITBBU = simplifiedRawDataQuery(token, null, mocName,
                getAllManagedElementQueryRequest(token, mocName, ManagedElementType.ITBBU));
        if (jsonITBBU != null) {
            try {
                managedElementMocTo = new Gson().fromJson(jsonITBBU, ManagedElementMocTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in ManagedElementMocTo parsing: {}", e1.toString());
            }
        }
        if (managedElementMocTo != null) {
            iterateResultList = managedElementMocTo.getResult();
            for (ManagedElementMocTo.ManagedElementMocResultTo site : iterateResultList) {
                managedElementMocList = site.getMoData();
                ne = site.getNe();
                resultMapByUserLabel.put(managedElementMocList.get(0).getUserLabel(), ne);
                resultMapByNe.put(ne, managedElementMocList.get(0).getUserLabel());
            }
        }
        log.info(" >> ManagedElementMap for cache: {}", resultMapByUserLabel.size());
        if (Type.BY_NE.equals(type)) {return resultMapByNe;}
        return resultMapByUserLabel;
    }

    private HttpRequest dataRawQueryRequest(Token token, ManagedElement managedElement, String mocName) {
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

    private HttpRequest getAllManagedElementQueryRequest(Token token, String mocName, ManagedElementType managedElementType) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        CurrentMngBodySettings.builder()
                                .ManagedElementType(managedElementType.toString())
                                .mocList(List.of(mocName))
                                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter(mocName,
                                        List.of("userLabel"))))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_CURRENTAREA + GlobalUtil.CURRENTAREA_QUERY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
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

    private HttpRequest getManagedElementByNeNameRequest(Token token, String userLabel, ManagedElementType type) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        CurrentMngBodySettings.builder()
                                .ManagedElementType(type.toString())
                                .mocList(List.of("ManagedElement"))
                                .moFilter(List.of(new CurrentMngBodySettings.MoFilter("ManagedElement", "userLabel='" + userLabel.trim().toUpperCase() + "'")))
                                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("ManagedElement", List.of("userLabel"))))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_CURRENTAREA + GlobalUtil.CURRENTAREA_QUERY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    // GET SDR/ITBBU CELLS

    private HttpRequest getSimplifyULocalCellByNeNameRequest(Token token, ManagedElement managedElement) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        CurrentMngBodySettings.builder()
                                .ManagedElementType(managedElement.getManagedElementType().toString())
                                .neList(List.of(managedElement.getNe()))
                                .mocList(List.of("ULocalCell"))
                                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("ULocalCell",
                                        List.of("userLabel", "adminState", "operState", "cellRadius", "maxDlPwr", "localCellId", "availStatus"))))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_CURRENTAREA + GlobalUtil.CURRENTAREA_QUERY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private HttpRequest getSimplifyUCellByNeNameRequest(Token token, ManagedElement managedElement) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        CurrentMngBodySettings.builder()
                                .ManagedElementType(managedElement.getManagedElementType().toString())
                                .neList(List.of(managedElement.getNe()))
                                .mocList(List.of("UCell"))
                                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("UCell",
                                        List.of("cellId", "adminState", "operState", "availStatus"))))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_CURRENTAREA + GlobalUtil.CURRENTAREA_QUERY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private HttpRequest getSimplifyITBBUUCellByNeNameRequest(Token token, ManagedElement managedElement) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        CurrentMngBodySettings.builder()
                                .ManagedElementType(managedElement.getManagedElementType().toString())
                                .neList(List.of(managedElement.getNe()))
                                .mocList(List.of("UCell"))
                                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("UCell",
                                        List.of("cellId", "operState", "availStatus"))))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_CURRENTAREA + GlobalUtil.CURRENTAREA_QUERY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private HttpRequest getSimplifyITBBUULocalCellByNeNameRequest(Token token, ManagedElement managedElement) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        CurrentMngBodySettings.builder()
                                .ManagedElementType(managedElement.getManagedElementType().toString())
                                .neList(List.of(managedElement.getNe()))
                                .mocList(List.of("ULocalCell"))
                                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("ULocalCell",
                                        List.of("userLabel", "adminState", "operState", "cellRadius", "maxDlPwr", "localCellId", "availStatus"))))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_CURRENTAREA + GlobalUtil.CURRENTAREA_QUERY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private HttpRequest getSimplifyEUtranCellNBIoTMocByNeNameRequest(Token token, ManagedElement managedElement) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        CurrentMngBodySettings.builder()
                                .ManagedElementType(managedElement.getManagedElementType().toString())
                                .neList(List.of(managedElement.getNe()))
                                .mocList(List.of("EUtranCellNBIoT"))
                                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("EUtranCellNBIoT",
                                        List.of("userLabel", "adminState", "operState"))))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_CURRENTAREA + GlobalUtil.CURRENTAREA_QUERY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private HttpRequest getSimplifyCUEUtranCellNBIoTMocByNeNameRequest(Token token, ManagedElement managedElement) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        CurrentMngBodySettings.builder()
                                .ManagedElementType(managedElement.getManagedElementType().toString())
                                .neList(List.of(managedElement.getNe()))
                                .mocList(List.of("CUEUtranCellNBIoT"))
                                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("CUEUtranCellNBIoT",
                                        List.of("userLabel", "adminState", "operState"))))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_CURRENTAREA + GlobalUtil.CURRENTAREA_QUERY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private HttpRequest getAllSimplifyITBBUULocalCellRequest(Token token) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        CurrentMngBodySettings.builder()
                                .ManagedElementType(ManagedElementType.ITBBU.toString())
                                .mocList(List.of("ULocalCell"))
                                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("ULocalCell",
                                        List.of("userLabel"))))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_CURRENTAREA + GlobalUtil.CURRENTAREA_QUERY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private HttpRequest getAllSimplifyULocalCellRequest(Token token) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        CurrentMngBodySettings.builder()
                                .ManagedElementType(ManagedElementType.SDR.toString())
                                .mocList(List.of("ULocalCell"))
                                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("ULocalCell",
                                        List.of("userLabel"))))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_CURRENTAREA + GlobalUtil.CURRENTAREA_QUERY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private HttpRequest getAllSimplifyEUtranCellNBIoTMocRequest(Token token) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        CurrentMngBodySettings.builder()
                                .ManagedElementType(ManagedElementType.SDR.toString())
                                .mocList(List.of("EUtranCellNBIoT"))
                                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("EUtranCellNBIoT",
                                        List.of("userLabel"))))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_CURRENTAREA + GlobalUtil.CURRENTAREA_QUERY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private HttpRequest getAllSimplifyCUEUtranCellNBIoTMocRequest(Token token) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        CurrentMngBodySettings.builder()
                                .ManagedElementType(ManagedElementType.ITBBU.toString())
                                .mocList(List.of("CUEUtranCellNBIoT"))
                                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("CUEUtranCellNBIoT",
                                        List.of("userLabel"))))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_CURRENTAREA + GlobalUtil.CURRENTAREA_QUERY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    private HttpRequest getAllSimplifyGGsmCellMocRequest(Token token) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        CurrentMngBodySettings.builder()
                                .ManagedElementType(ManagedElementType.MRNC.toString())
                                .mocList(List.of("GGsmCell"))
                                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("GGsmCell",
                                        List.of("userLabel"))))
                                .build().getBodySettings()))
                .uri(URI.create(GlobalUtil.GLOBAL_PATH + GlobalUtil.API_CURRENTAREA + GlobalUtil.CURRENTAREA_QUERY))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Content-Type", "application/json; charset=utf-8")
                .header("accessToken", token.getAccessToken())
                .build();
    }

    // GET MRNC CELLS

    private CurrentMngBodySettings getGGsmCellMocSimplifiedBodySettings(Token token, ManagedElement managedElement) {
        return CurrentMngBodySettings.builder()
                .ManagedElementType(ManagedElementType.MRNC.toString())
                .mocList(List.of("GGsmCell"))
                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("GGsmCell",
                        List.of("userLabel", "bcchFrequency", "cellIdentity", "moId"))))
                .moFilter(List.of(new CurrentMngBodySettings.MoFilter("GGsmCell",
                                getGGsmCellFilter(managedElement))))
//                        "userLabel like '" + managedElement.getUserLabel() + "_'")))
                .build();
    }

    // GET MRNC TRX

    private CurrentMngBodySettings getGTrxMocSimplifiedBodySettings(Token token, ManagedElement managedElement, List<GGsmCellMocSimplified> cells) {
        return CurrentMngBodySettings.builder()
                .ManagedElementType(ManagedElementType.MRNC.toString())
                .mocList(List.of("GTrx"))
                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("GTrx",
                        List.of("userLabel", "moId"))))
                .moFilter(List.of(new CurrentMngBodySettings.MoFilter("GTrx",
                        getGTrxFilter(managedElement, cells))))
//                        "userLabel like '" + managedElement.getUserLabel() + "_'")))
                .build();
    }

    private CurrentMngBodySettings getUUtranCellFDDMocSimplifiedBodySettings(Token token, ManagedElement managedElement) {
        List<UIubLinkMocSimplified> uIubLinkMocSimplifiedList = getUIubLinkMocSimplified(token, managedElement);
        if (uIubLinkMocSimplifiedList == null) return null;

        return CurrentMngBodySettings.builder()
                .ManagedElementType(ManagedElementType.MRNC.toString())
                .mocList(List.of("UUtranCellFDD"))
                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("UUtranCellFDD",
                        List.of("userLabel", "maximumTransmissionPower", "primaryScramblingCode", "moId", "cellRadius"))))
                .moFilter(List.of(new CurrentMngBodySettings.MoFilter("UUtranCellFDD",
                        "refUIubLink='" + uIubLinkMocSimplifiedList.get(0).getLdn() + "'")))
                .build();
    }

    private CurrentMngBodySettings getUIubLinkMocSimplifiedBodySettings(Token token, ManagedElement managedElement) {
        return CurrentMngBodySettings.builder()
                .ManagedElementType(ManagedElementType.MRNC.toString())
                .mocList(List.of("UIubLink"))
                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("UIubLink",
                        List.of("userLabel"))))
                .moFilter(List.of(new CurrentMngBodySettings.MoFilter("UIubLink",
                        "userLabel='" + managedElement.getUserLabel() + "'")))
                .build();
    }

    private String getGGsmCellFilter(ManagedElement managedElement) {
        String result = "";
        String ldn = "ldn='" + "GBssFunction=" + SubnetworkToBSC.getBSCbySubnetwork(managedElement.getSubNetworkNum()) +
                ",GBtsSiteManager=" + managedElement.getBTSManagedElementNum() + ",GGsmCell=";
        for (int i = 1; i < 10; i++) {
            result += ldn + i + "' or ";
        }
        return result.substring(0, result.length() - 4);
    }

    private String getGTrxFilter(ManagedElement managedElement, List<GGsmCellMocSimplified> cells) {
        String result = "";
        String userLabel = "userLabel='";
        if (cells == null) return "";
        for (GGsmCellMocSimplified cell : cells) {
                result += userLabel + cell.getUserLabel() + "' or ";
        }
        return result.substring(0, result.length() - 4);
    }

    public enum Type {
        BY_USERLABEL,
        BY_NE;
    }

    @Data
    @RequiredArgsConstructor
    public static class CellInfo {
        private final String command;
        private final String ne; // userLabel for GSM
    }
}