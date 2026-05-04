package bsshelper.externalapi.configurationmng.currentmng.entity;

import bsshelper.globalutil.ManagedElementType;
import bsshelper.globalutil.SubnetworkToBSCOrRNC;
import bsshelper.localservice.externalcustomdata.service.CustomDataService;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class ManagedElement {
    private final String userLabel;
    private final ManagedElementType ManagedElementType;
    private final String ne;
//    private final String bsc;
//    private final String rnc;
//    private final String siteID;
//    private final String btsID;


    public String getParsedNe() {
        return ne.replace("=", ": ").replace("ManagedElement:", " NE:");
    }

    public String getManagedElementNum() {
        int index = ne.lastIndexOf("=");
        return ne.substring(index + 1);
    }

    public String getBTSManagedElementNum() {
        int index = ne.lastIndexOf("=");
        if (CustomDataService.MECustomLinkMap.containsKey(userLabel)) {
            return CustomDataService.MECustomLinkMap.get(userLabel).getGSMID();
        }
        if (ManagedElementType.equals(bsshelper.globalutil.ManagedElementType.SDR)) return ne.substring(index + 1);
        else {
            String result = ne.substring(index + 3);
            while (result.charAt(0) == '0') result = result.substring(1);
            return result;
        }
    }

    public String getRNCManagedElementNum() {
        int index = ne.lastIndexOf("=");
        if (CustomDataService.MECustomLinkMap.containsKey(userLabel)) {
            return CustomDataService.MECustomLinkMap.get(userLabel).getUMTSID();
        }
        if (ManagedElementType.equals(bsshelper.globalutil.ManagedElementType.SDR)) return ne.substring(index + 1);
        else {
            String result = ne.substring(index + 3);
            while (result.charAt(0) == '0') result = result.substring(1);
            return result;
        }
    }

    public Integer getSubNetworkNum() {
        int index = ne.indexOf("=");
        return Integer.parseInt(ne.substring(index + 1,index + 4));
    }

    public String getBSC() {
        if (CustomDataService.MECustomLinkMap.containsKey(userLabel)) {
            return CustomDataService.MECustomLinkMap.get(userLabel).getBSCID();
        }
        return String.valueOf(SubnetworkToBSCOrRNC.getBSCbySubnetwork(this.getSubNetworkNum(), this));
    }

    public String getRNC() {
        if (CustomDataService.MECustomLinkMap.containsKey(userLabel)) {
            return CustomDataService.MECustomLinkMap.get(userLabel).getRNCID();
        }
        return String.valueOf(SubnetworkToBSCOrRNC.getRNCbySubnetwork(this.getSubNetworkNum(), this));
    }

}
