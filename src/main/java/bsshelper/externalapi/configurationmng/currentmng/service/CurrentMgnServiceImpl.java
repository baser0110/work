package bsshelper.externalapi.configurationmng.currentmng.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.*;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.DryContactCableMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUCUEUtranCellNBIoTMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ITBBUULocalCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GGsmCellMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UIubLinkMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.*;
import bsshelper.externalapi.configurationmng.currentmng.mapper.ManagedElementMapper;
import bsshelper.externalapi.configurationmng.currentmng.to.*;
import bsshelper.externalapi.configurationmng.currentmng.to.itbbu.DryContactCableMocTo;
import bsshelper.externalapi.configurationmng.currentmng.to.itbbu.ITBBUCUEUtranCellNBIoTMocSimplifiedTo;
import bsshelper.externalapi.configurationmng.currentmng.to.itbbu.ITBBUULocalCellMocSimplifiedTo;
import bsshelper.externalapi.configurationmng.currentmng.to.itbbu.ITBBUULocalCellMocTo;
import bsshelper.externalapi.configurationmng.currentmng.to.mrnc.GGsmCellMocSimplifiedTo;
import bsshelper.externalapi.configurationmng.currentmng.to.mrnc.UIubLinkMocSimplifiedTo;
import bsshelper.externalapi.configurationmng.currentmng.to.mrnc.UUtranCellFDDMocSimplifiedTo;
import bsshelper.externalapi.configurationmng.currentmng.to.sdr.*;
import bsshelper.externalapi.configurationmng.currentmng.util.CurrentMngBodySettings;
import bsshelper.globalutil.GlobalUtil;
import bsshelper.globalutil.ManagedElementType;
import bsshelper.globalutil.SubnetworkToBSC;
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
import java.util.Comparator;
import java.util.List;

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
        }
        return response;
    }

    @Override
    public String simplifiedRawDataQuery(Token token, ManagedElement managedElement, String mocName, HttpRequest httpRequest) {
        HttpResponse<String> httpResponse = null;
        String response = null;
        ErrorEntity error = null;
        try {
            httpResponse = HttpClient.newBuilder().build().send(httpRequest, HttpResponse.BodyHandlers.ofString());
//            System.out.println(httpResponse.body());
            response = httpResponse.body();
            if (response.contains("\"code\":0") && !response.contains("\"result\":[]")) {
                if (managedElement == null) {
                    log.info(" >> {} successfully found", mocName);
                } else {
                    log.info(" >> {} for {} successfully found", mocName, managedElement.getUserLabel());
                }
            } else {
                if (response.contains("\"code\":0")) {
                    if (managedElement == null) {
                        log.info(" >> {} successfully found", mocName);
                    } else {
                        log.info(" >> {} for {} successfully found but it's empty", mocName, managedElement.getUserLabel());
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
//            try {
//                error = new Gson().fromJson(json, ErrorEntity.class);
//            } catch (JsonSyntaxException e2) {
//                log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
//            }
//            if (error != null) {
//                log.error(" >> error {} code({})", error.getMessage(), error.getCode());
//            }
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
    public List<FiberCableMoc> getFiberCableMoc(Token token, ManagedElement managedElement) {
        String mocName = "FiberCable";
        List<FiberCableMoc> fiberCableMocList = null;
        FiberCableMocTo fiberCableMocTo = null;
        ErrorEntity error = null;
        String json = rawDataQuery(token, managedElement, mocName);
        try {
            fiberCableMocTo = new Gson().fromJson(json, FiberCableMocTo.class);
        } catch (JsonSyntaxException e1) {
            log.error(" >> error in FiberCableMocTo parsing: {}", e1.toString());
//            try {
//                error = new Gson().fromJson(json, ErrorEntity.class);
//            } catch (JsonSyntaxException e2) {
//                log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
//            }
//            if (error != null) {
//                log.error(" >> error {} code({})", error.getMessage(), error.getCode());
//            }
//        System.out.println(dryContactDeviceMocTo);
        }
        if (fiberCableMocTo != null) {
            fiberCableMocList = fiberCableMocTo.getResult().get(0).getMoData();
        }
        log.info(" >> fiberCableMocList: {}", fiberCableMocList);
//        System.out.println(dryContactDeviceMocList);
        return fiberCableMocList;
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
//            try {
//                error = new Gson().fromJson(json, ErrorEntity.class);
//            } catch (JsonSyntaxException e2) {
//                log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
//            }
//            if (error != null) {
//                log.error(" >> error {} code({})", error.getMessage(), error.getCode());
//            }
//        System.out.println(dryContactDeviceMocTo);
        }
        if (dryContactCableMocTo != null) {
            dryContactCableMocList = dryContactCableMocTo.getResult().get(0).getMoData();
        }
        log.info(" >> dryContactCableMocList: {}", dryContactCableMocList);
//        System.out.println(dryContactDeviceMocList);
        return dryContactCableMocList;
    }

    @Override
    public List<SdrDeviceGroupMoc> getSdrDeviceGroupMoc(Token token, ManagedElement managedElement) {
        String mocName = "SdrDeviceGroup";
        List<SdrDeviceGroupMoc> sdrDeviceGroupMocList = null;
        SdrDeviceGroupMocTo sdrDeviceGroupMocTo = null;
        ErrorEntity error = null;
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
        ErrorEntity error = null;
        String json = rawDataQuery(token, managedElement, mocName);
//        System.out.println(json);
        if (json != null) {
            try {
                uLocalCellMocTo = new Gson().fromJson(json, ULocalCellMocTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in uLocalCellMocListTo parsing: {}", e1.toString());
//                try {
//                    error = new Gson().fromJson(json, ErrorEntity.class);
//                } catch (JsonSyntaxException e2) {
//                    log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
//                }
//                if (error != null) {
//                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
//                }
//            System.out.println(uLocalCellMocTo);
            }
        }
        if (uLocalCellMocTo != null) {
            ULocalCellMocList = uLocalCellMocTo.getResult().get(0).getMoData();
        }
        log.info(" >> uLocalCellMocList: {}", ULocalCellMocList);
//        System.out.println(uLocalCellMocList);
        return ULocalCellMocList;
    }

    @Override
    public List<ITBBUULocalCellMoc> getITBBUULocalCellMoc(Token token, ManagedElement managedElement) {
        String mocName = "ULocalCell";
        List<ITBBUULocalCellMoc> iTBBUULocalCellMocList = null;
        ITBBUULocalCellMocTo iTBBUULocalCellMocTo = null;
        ErrorEntity error = null;
        String json = rawDataQuery(token, managedElement, mocName);
//        System.out.println(json);
        if (json != null) {
            try {
                iTBBUULocalCellMocTo = new Gson().fromJson(json, ITBBUULocalCellMocTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in iTBBUULocalCellMocListTo parsing: {}", e1.toString());
//                try {
//                    error = new Gson().fromJson(json, ErrorEntity.class);
//                } catch (JsonSyntaxException e2) {
//                    log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
//                }
//                if (error != null) {
//                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
//                }
//            System.out.println(uLocalCellMocTo);
            }
        }
        if (iTBBUULocalCellMocTo != null) {
            iTBBUULocalCellMocList = iTBBUULocalCellMocTo.getResult().get(0).getMoData();
        }
        log.info(" >> uLocalCellMocList: {}", iTBBUULocalCellMocList);
//        System.out.println(uLocalCellMocList);
        return iTBBUULocalCellMocList;
    }

    @Override
    public List<EUtranCellNBIoTMoc> getEUtranCellNBIoTMoc(Token token, ManagedElement managedElement) {
        String mocName = "EUtranCellNBIoT";
        List<EUtranCellNBIoTMoc> eUtranCellNBIoTMocList = null;
        EUtranCellNBIoTMocTo eUtranCellNBIoTMocTo = null;
        ErrorEntity error = null;
        String json = rawDataQuery(token, managedElement, mocName);
//        System.out.println(json);
        if (json != null) {
            try {
                eUtranCellNBIoTMocTo = new Gson().fromJson(json, EUtranCellNBIoTMocTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in eUtranCellNBIoTMocListTo parsing: {}", e1.toString());
//                try {
//                    error = new Gson().fromJson(json, ErrorEntity.class);
//                } catch (JsonSyntaxException e2) {
//                    log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
//                }
//                if (error != null) {
//                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
//                }
//            System.out.println(uLocalCellMocTo);
            }
        }
        if (eUtranCellNBIoTMocTo != null) {
            try {
                eUtranCellNBIoTMocList = eUtranCellNBIoTMocTo.getResult().get(0).getMoData();
            } catch (NullPointerException e1) {
//                log.info(" >> " + managedElement.
            }
        }
        log.info(" >> eUtranCellNBIoTMocList: {}", eUtranCellNBIoTMocList);
//        System.out.println(uLocalCellMocList);
        return eUtranCellNBIoTMocList;
    }

    @Override
    public List<ULocalCellMocSimplified> getULocalCellMocSimplified(Token token, ManagedElement managedElement) {
        String mocName = "ULocalCell";
        List<ULocalCellMocSimplified> uLocalCellMocSimplifiedList = null;
        ULocalCellMocSimplifiedTo uLocalCellMocSimplifiedTo = null;
        ErrorEntity error = null;
        String json = simplifiedRawDataQuery(token, managedElement, mocName,
                getSimplifyULocalCellByNeNameRequest(token, managedElement));
//        System.out.println(json);
        if (json != null) {
            try {
                uLocalCellMocSimplifiedTo = new Gson().fromJson(json, ULocalCellMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
//                log.error(" >> error in ULocalCellMocSimplifiedListTo parsing: {}", e1.toString());
//                try {
//                    error = new Gson().fromJson(json, ErrorEntity.class);
//                } catch (JsonSyntaxException e2) {
//                    log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
//                }
//                if (error != null) {
//                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
//                }
//            System.out.println(uLocalCellMocTo);
            }
        }
        if (uLocalCellMocSimplifiedTo != null) {
            uLocalCellMocSimplifiedList = uLocalCellMocSimplifiedTo.getResult().get(0).getMoData();
        }
        log.info(" >> ULocalCellMocSimplifiedList: {}", uLocalCellMocSimplifiedList);
//        System.out.println(uLocalCellMocList);
        return uLocalCellMocSimplifiedList;
    }

    @Override
    public List<ITBBUULocalCellMocSimplified> getITBBUULocalCellMocSimplified(Token token, ManagedElement managedElement) {
        String mocName = "ULocalCell";
        List<ITBBUULocalCellMocSimplified> iTBBUULocalCellMocSimplifiedList = null;
        ITBBUULocalCellMocSimplifiedTo iTBBUULocalCellMocSimplifiedTo = null;
        ErrorEntity error = null;
        String json = simplifiedRawDataQuery(token, managedElement, mocName,
                getSimplifyITBBUULocalCellByNeNameRequest(token, managedElement));
//        System.out.println(json);
        if (json != null) {
            try {
                iTBBUULocalCellMocSimplifiedTo = new Gson().fromJson(json, ITBBUULocalCellMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in iTBBUULocalCellMocSimplifiedListTo parsing: {}", e1.toString());
//                try {
//                    error = new Gson().fromJson(json, ErrorEntity.class);
//                } catch (JsonSyntaxException e2) {
//                    log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
//                }
//                if (error != null) {
//                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
//                }
//            System.out.println(uLocalCellMocTo);
            }
        }
        if (iTBBUULocalCellMocSimplifiedTo != null) {
            iTBBUULocalCellMocSimplifiedList = iTBBUULocalCellMocSimplifiedTo.getResult().get(0).getMoData();
        }
        log.info(" >> iTBBUULocalCellMocSimplifiedList: {}", iTBBUULocalCellMocSimplifiedList);
//        System.out.println(uLocalCellMocList);
        return iTBBUULocalCellMocSimplifiedList;
    }

    @Override
    public List<EUtranCellNBIoTMocSimplified> getEUtranCellNBIoTMocSimplified(Token token, ManagedElement managedElement) {
        String mocName = "EUtranCellNBIoT";
        List<EUtranCellNBIoTMocSimplified> eUtranCellNBIoTMocSimplifiedList = null;
        EUtranCellNBIoTMocSimplifiedTo eUtranCellNBIoTMocSimplifiedTo = null;
        ErrorEntity error = null;
        String json = simplifiedRawDataQuery(token, managedElement, mocName,
                getSimplifyEUtranCellNBIoTMocByNeNameRequest(token, managedElement));
//        System.out.println(json);
        if (json != null) {
            try {
                eUtranCellNBIoTMocSimplifiedTo = new Gson().fromJson(json, EUtranCellNBIoTMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in eUtranCellNBIoTMocSimplifiedListTo parsing: {}", e1.toString());
//                try {
//                    error = new Gson().fromJson(json, ErrorEntity.class);
//                } catch (JsonSyntaxException e2) {
//                    log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
//                }
//                if (error != null) {
//                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
//                }
//            System.out.println(uLocalCellMocTo);
            }
        }
        if (eUtranCellNBIoTMocSimplifiedTo != null) {
            eUtranCellNBIoTMocSimplifiedList = eUtranCellNBIoTMocSimplifiedTo.getResult().get(0).getMoData();
        }
        log.info(" >> eUtranCellNBIoTMocSimplifiedList: {}", eUtranCellNBIoTMocSimplifiedList);
//        System.out.println(uLocalCellMocList);
        return eUtranCellNBIoTMocSimplifiedList;
    }

    @Override
    public List<ITBBUCUEUtranCellNBIoTMocSimplified> getITBBUCUEUtranCellNBIoTMocSimplified(Token token, ManagedElement managedElement) {
        String mocName = "CUEUtranCellNBIoT";
        List<ITBBUCUEUtranCellNBIoTMocSimplified> iTBBUCUEUtranCellNBIoTMocSimplifiedList = null;
        ITBBUCUEUtranCellNBIoTMocSimplifiedTo iTBBUCUEUtranCellNBIoTMocSimplifiedTo = null;
        ErrorEntity error = null;
        String json = simplifiedRawDataQuery(token, managedElement, mocName,
                getSimplifyCUEUtranCellNBIoTMocByNeNameRequest(token, managedElement));
//        System.out.println(json);
        if (json != null) {
            try {
                iTBBUCUEUtranCellNBIoTMocSimplifiedTo = new Gson().fromJson(json, ITBBUCUEUtranCellNBIoTMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in iTBBUCUEUtranCellNBIoTMocSimplifiedListTo parsing: {}", e1.toString());
//                try {
//                    error = new Gson().fromJson(json, ErrorEntity.class);
//                } catch (JsonSyntaxException e2) {
//                    log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
//                }
//                if (error != null) {
//                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
//                }
//            System.out.println(uLocalCellMocTo);
            }
        }
        if (iTBBUCUEUtranCellNBIoTMocSimplifiedTo != null) {
            iTBBUCUEUtranCellNBIoTMocSimplifiedList = iTBBUCUEUtranCellNBIoTMocSimplifiedTo.getResult().get(0).getMoData();
        }
        log.info(" >> iTBBUCUEUtranCellNBIoTMocSimplifiedList: {}", iTBBUCUEUtranCellNBIoTMocSimplifiedList);
//        System.out.println(uLocalCellMocList);
        return iTBBUCUEUtranCellNBIoTMocSimplifiedList;
    }

    @Override
    public List<GGsmCellMocSimplified> getGGsmCellMocSimplified(Token token, ManagedElement managedElement) {
        String mocName = "GGsmCell";
        List<GGsmCellMocSimplified> gGsmCellMocSimplifiedList = null;
        GGsmCellMocSimplifiedTo gGsmCellMocSimplifiedTo = null;
        ErrorEntity error = null;
        CurrentMngBodySettings bodySettings = getGGsmCellMocSimplifiedBodySettings(token, managedElement);
        if (bodySettings == null) {
            log.error(" >> couldn't found GGsmCell data for {}", managedElement.getUserLabel());
            return null;
        }
        String json = simplifiedRawDataQuery(token, managedElement, mocName, dataQueryRequest(token, bodySettings));
//        System.out.println(json);
        if (json != null) {
            try {
                gGsmCellMocSimplifiedTo = new Gson().fromJson(json, GGsmCellMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in gGsmCellMocSimplifiedTo parsing: {}", e1.toString());
//                try {
//                    error = new Gson().fromJson(json, ErrorEntity.class);
//                } catch (JsonSyntaxException e2) {
//                    log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
//                }
//                if (error != null) {
//                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
//                }
            }
        }
        if (gGsmCellMocSimplifiedTo != null) {
            gGsmCellMocSimplifiedList = gGsmCellMocSimplifiedTo.getResult().get(0).getMoData();
            gGsmCellMocSimplifiedList.sort(Comparator.comparing(GGsmCellMocSimplified::getUserLabel));
        }
        log.info(" >> gGsmCellMocSimplifiedList: {}", gGsmCellMocSimplifiedList);
//        System.out.println(uLocalCellMocList);
        return gGsmCellMocSimplifiedList;
    }

    @Override
    public List<UUtranCellFDDMocSimplified> getUUtranCellFDDMocSimplified(Token token, ManagedElement managedElement) {
        String mocName = "UUtranCellFDD";
        List<UUtranCellFDDMocSimplified> uUtranCellFDDMocSimplifiedList = null;
        UUtranCellFDDMocSimplifiedTo uUtranCellFDDMocSimplifiedTo = null;
        ErrorEntity error = null;
        CurrentMngBodySettings bodySettings = getUUtranCellFDDMocSimplifiedBodySettings(token, managedElement);
        if (bodySettings == null) {
            log.error(" >> couldn't found UUtranCellFDD data for {}", managedElement.getUserLabel());
            return null;
        }
//        System.out.println(bodySettings.getBodySettings());
        String json = simplifiedRawDataQuery(token, managedElement, mocName, dataQueryRequest(token, bodySettings));
//        System.out.println(json);
        if (json != null) {
            try {
                uUtranCellFDDMocSimplifiedTo = new Gson().fromJson(json, UUtranCellFDDMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in UUtranCellFDDMocSimplifiedTo parsing: {}", e1.toString());
//                try {
//                    error = new Gson().fromJson(json, ErrorEntity.class);
//                } catch (JsonSyntaxException e2) {
//                    log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
//                }
//                if (error != null) {
//                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
//                }
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
        ErrorEntity error = null;
        CurrentMngBodySettings bodySettings = getUIubLinkMocSimplifiedBodySettings(token, managedElement);
        if (bodySettings == null) {
            log.error(" >> couldn't found UIubLink data for {}", managedElement.getUserLabel());
            return null;
        }
//        System.out.println(bodySettings.getBodySettings());
        String json = simplifiedRawDataQuery(token, managedElement, mocName, dataQueryRequest(token, bodySettings));
//        System.out.println(json);
        if (json != null) {
            try {
                uIubLinkMocSimplifiedTo = new Gson().fromJson(json, UIubLinkMocSimplifiedTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in uIubLinkMocSimplifiedTo parsing: {}", e1.toString());
//                try {
//                    error = new Gson().fromJson(json, ErrorEntity.class);
//                } catch (JsonSyntaxException e2) {
//                    log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
//                }
//                if (error != null) {
//                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
//                }
            }
        }
        if (uIubLinkMocSimplifiedTo != null) {
            uIubLinkMocSimplifiedList = uIubLinkMocSimplifiedTo.getResult().get(0).getMoData();
        }
        log.info(" >> uIubLinkMocSimplifiedList: {}", uIubLinkMocSimplifiedList);
        return uIubLinkMocSimplifiedList;
    }

    @Override
    public List<SdrDeviceGroupMocSimpl> getSdrDeviceGroupMocSimpl(Token token, ManagedElement managedElement) {
        String mocName = "SdrDeviceGroup";
        List<SdrDeviceGroupMocSimpl> sdrDeviceGroupMocSimplList = null;
        SdrDeviceGroupMocSimplTo sdrDeviceGroupMocSimplTo = null;
        ErrorEntity error = null;
        CurrentMngBodySettings bodySettings = getSdrDeviceGroupMocSimplBodySettings(token, managedElement);
        if (bodySettings == null) {
            log.error(" >> couldn't found SdrDeviceGroup data for {}", managedElement.getUserLabel());
            return null;
        }
//        System.out.println(bodySettings.getBodySettings());
        String json = simplifiedRawDataQuery(token, managedElement, mocName, dataQueryRequest(token, bodySettings));
//        System.out.println(json);
        if (json != null) {
            try {
                sdrDeviceGroupMocSimplTo = new Gson().fromJson(json, SdrDeviceGroupMocSimplTo.class);
            } catch (JsonSyntaxException e1) {
                e1.printStackTrace();
                log.error(" >> error in uIubLinkMocSimplifiedTo parsing: {}", e1.toString());
//                try {
//                    error = new Gson().fromJson(json, ErrorEntity.class);
//                } catch (JsonSyntaxException e2) {
//                    log.error(" >> error in ErrorEntity parsing: {}", e2.toString());
//                }
//                if (error != null) {
//                    log.error(" >> error {} code({})", error.getMessage(), error.getCode());
//                }
            }
        }
        if (sdrDeviceGroupMocSimplTo != null) {
            sdrDeviceGroupMocSimplList = sdrDeviceGroupMocSimplTo.getResult().get(0).getMoData();
        }
        log.info(" >> sdrDeviceGroupMocSimplList: {}", sdrDeviceGroupMocSimplList);
        return sdrDeviceGroupMocSimplList;
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

    private HttpRequest getSimplifyULocalCellByNeNameRequest(Token token, ManagedElement managedElement) {
        return HttpRequest.newBuilder()
                .method(Verb.POST.toString(), HttpRequest.BodyPublishers.ofString(
                        CurrentMngBodySettings.builder()
                                .ManagedElementType(managedElement.getManagedElementType().toString())
                                .neList(List.of(managedElement.getNe()))
                                .mocList(List.of("ULocalCell"))
                                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("ULocalCell",
                                        List.of("userLabel", "adminState", "operState", "cellRadius", "maxDlPwr"))))
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
                                        List.of("userLabel", "adminState", "operState", "smoothlyBlock", "cellRadius", "maxDlPwr"))))
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

    private CurrentMngBodySettings getGGsmCellMocSimplifiedBodySettings(Token token, ManagedElement managedElement) {
        return CurrentMngBodySettings.builder()
                .ManagedElementType(ManagedElementType.MRNC.toString())
                .mocList(List.of("GGsmCell"))
                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("GGsmCell",
                        List.of("userLabel", "bcchFrequency", "cellIdentity"))))
                .moFilter(List.of(new CurrentMngBodySettings.MoFilter("GGsmCell",
                                getGGsmCellFilter(managedElement))))
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

    private CurrentMngBodySettings getSdrDeviceGroupMocSimplBodySettings(Token token, ManagedElement managedElement) {
        return CurrentMngBodySettings.builder()
                .ManagedElementType(ManagedElementType.SDR.toString())
                .mocList(List.of("SdrDeviceGroup"))
                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("SdrDeviceGroup",
                        List.of("productData_productName", "functionMode", "physicalBrdType"))))
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
}