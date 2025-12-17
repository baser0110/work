package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.IpMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.IpLayerConfigMoc;
import bsshelper.externalapi.perfmng.entity.InfoGeneral;

import java.util.ArrayList;
import java.util.List;

public class InfoIpMapper {
    static public InfoGeneral.InfoIp toInfoIpForSDR(IpLayerConfigMoc ipInfo) {
        if (ipInfo == null) {return null;}
            String tech = "";
            switch (Integer.parseInt(ipInfo.getMoId())) {
                case 1 -> tech = "GSM";
                case 2 -> tech = "UMTS";
                case 3 -> tech = "NBIoT";
                case 4 -> tech = "OAM";
                case 5 -> tech = "LTE";
            }
        return new InfoGeneral.InfoIp(
                tech,
                ipInfo.getVid(),
                ipInfo.getIpAddr(),
                ipInfo.getNetworkMask(),
                ipInfo.getGatewayIp());
    }

    static public List<InfoGeneral.InfoIp> toInfoIpForSDR(List<IpLayerConfigMoc> ipInfoList) {
        if (ipInfoList == null) {
            return null;
        }
        List<InfoGeneral.InfoIp> result = new ArrayList<>();
        for (IpLayerConfigMoc s: ipInfoList) {
            result.add(toInfoIpForSDR(s));
        }
        return result;
    }

    static public InfoGeneral.InfoIp toInfoIpForITBBU(IpMoc ipInfo) {
        if (ipInfo == null) {return null;}
        String tech = "";
        switch (Integer.parseInt(ipInfo.getMoId())) {
            case 1 -> tech = "OAM";
            case 2 -> tech = "GSM";
            case 3 -> tech = "UMTS";
            case 4 -> tech = "NBIoT";
            case 5 -> tech = "LTE";
        }
        return new InfoGeneral.InfoIp(
                tech,
                ipInfo.getRefInterface().replace("TransportNetwork=1,Interface=", ""),
                ipInfo.getIpAddress(),
                "/" + ipInfo.getPrefixLength(),
                ipInfo.getGatewayIp());
    }

    static public List<InfoGeneral.InfoIp> toInfoIpForITBBU(List<IpMoc> ipInfoList) {
        if (ipInfoList == null) {
            return null;
        }
        List<InfoGeneral.InfoIp> result = new ArrayList<>();
        for (IpMoc s: ipInfoList) {
            result.add(toInfoIpForITBBU(s));
        }
        return result;
    }
}
