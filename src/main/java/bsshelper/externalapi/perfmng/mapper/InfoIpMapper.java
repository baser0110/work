package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.IpMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.IpLayerConfigMoc;
import bsshelper.externalapi.perfmng.entity.InfoGeneral;
import bsshelper.localservice.externalcustomdata.service.CustomDataService;

import java.util.ArrayList;
import java.util.List;

public class InfoIpMapper {
    static public InfoGeneral.InfoIp toInfoIpForSDR(IpLayerConfigMoc ipInfo) {
        if (ipInfo == null) {return null;}
        String tech = CustomDataService.VLANMap.getOrDefault(ipInfo.getVid(), "UNK");
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
        String vlan = ipInfo.getRefInterface().replace("TransportNetwork=1,Interface=", "");
        String tech = CustomDataService.VLANMap.getOrDefault(vlan, "UNK");
        return new InfoGeneral.InfoIp(
                tech,
                vlan,
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
