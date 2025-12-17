package bsshelper.externalapi.perfmng.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.ReplaceableUnitMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.EthernetSwitchDeviceMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.SdrDeviceGroupMoc;
import bsshelper.externalapi.perfmng.entity.InfoGeneral;

import java.util.*;

public class InfoPlatMapper {
    public static InfoGeneral.InfoPlat toInfoPlatForSDR(String position, SdrDeviceGroupMoc device, List<EthernetSwitchDeviceMoc> switchPortList, String inv) {
        if (device == null) {
            return null;
        }

        String conf = device.getProductData_productName();
        List<Integer> capacity = getBpCapacity(conf, String.valueOf(device.getFunctionMode()));

        switch (conf) {
            case "FS" -> conf = getFsBoard(device.getPhysicalBrdType());
            case "UES" -> conf = conf + " (" + getPortsInfo(switchPortList) + ")";
            default -> conf = conf + capasityToString(capacity);
        }

        return new InfoGeneral.InfoPlat(position, conf, (inv != null) ? inv : "", capacity);
    }

     public static List<InfoGeneral.InfoPlat> toInfoPlatForSDR(List<SdrDeviceGroupMoc> deviceList, List<EthernetSwitchDeviceMoc> switchPortList, Map<String,String> inventoryMap) {
         if (deviceList == null) {
             return null;
         }
         Map<String,String> copyInventoryMap = new TreeMap<>(inventoryMap);
         List<InfoGeneral.InfoPlat> result = new ArrayList<>();
         for (SdrDeviceGroupMoc s: deviceList) {
             String position = "";
             if (s.getLdn().contains(",Rack=1,")) {
                 position = s.getLdn()
                        .replace("Equipment=1,Rack=1,SubRack=1,Slot=", "Slot")
                        .replace(",PlugInUnit=1,SdrDeviceGroup=1", "");
             } else {
                 position = s.getLdn()
                        .replace(",SubRack=1,Slot=1,PlugInUnit=1,SdrDeviceGroup=1", "")
                        .replace("Equipment=1,Rack=", "");
             }
             String inv = inventoryMap.get(position);
             copyInventoryMap.remove(position);
             result.add(toInfoPlatForSDR(position, s, switchPortList, inv));
         }
         if (!copyInventoryMap.isEmpty()) {
             for (String s : copyInventoryMap.keySet()) {
                 result.add(new InfoGeneral.InfoPlat(s, "", copyInventoryMap.get(s), List.of(0, 0, 0, 0)));
             }
         }
         result.sort(Comparator.comparing(InfoGeneral.InfoPlat::getPosition));
         return result;
    }

    public static InfoGeneral.InfoPlat toInfoPlatForITBBU(String position, ReplaceableUnitMoc device, String inv) {
        if (device == null) {
            return null;
        }
        String conf = device.getName();
        List<Integer> capacity = getBpCapacity(conf, String.valueOf(device.getFunctionMode()));

        conf = conf + capasityToString(capacity);

        return new InfoGeneral.InfoPlat(position, conf, (inv != null) ? inv : "", capacity);
    }

    public static List<InfoGeneral.InfoPlat> toInfoPlatForITBBU(List<ReplaceableUnitMoc> deviceList, Map<String,String> inventoryMap) {
        if (deviceList == null) {
            return null;
        }
        Map<String,String> copyInventoryMap = new TreeMap<>(inventoryMap);
        List<InfoGeneral.InfoPlat> result = new ArrayList<>();
        for (ReplaceableUnitMoc s: deviceList) {
            String position = "";
            if (s.getRefSubRack().contains("Equipment=1,SubRack=1")) {
                position = "Slot" + s.getSlotNo();
            } else {
                position = s.getMoId();
            }
            String inv = inventoryMap.get(position);
            copyInventoryMap.remove(position);
            result.add(toInfoPlatForITBBU(position, s, inv));
        }
        if (!copyInventoryMap.isEmpty()) {
            for (String s : copyInventoryMap.keySet()) {
                result.add(new InfoGeneral.InfoPlat(s, "", copyInventoryMap.get(s), List.of(0, 0, 0, 0)));
            }
        }
        result.sort(Comparator.comparing(InfoGeneral.InfoPlat::getPosition));
        return result;
    }


    private static String getFsBoard(String type) {
        switch (Integer.parseInt(type)) {
            case 5 -> {return "FS5";}
            case 6 -> {return "FS5A";}
            case 9 -> {return "FS3A";}
        }
        return "-";
    }

    private static String getPortsInfo(List<EthernetSwitchDeviceMoc> switchPortList) {
        if (switchPortList == null) return "";
        String X1 = "";
        String X2 = "";
        String X3 = "";
        String X4 = "";
        for (EthernetSwitchDeviceMoc eth: switchPortList) {
            switch (Integer.parseInt(eth.getPort())) {
                case 0 -> X1 = eth.getIsEnable().equals("0") ? "Close" : "Open";
                case 1 -> X2 = eth.getIsEnable().equals("0") ? "Close" : "Open";
                case 2 -> X3 = eth.getIsEnable().equals("0") ? "Close" : "Open";
                case 4 -> X4 = eth.getIsEnable().equals("0") ? "Close" : "Open";
            }
        }
        return "X1:" + X1 + ", " + "X2:" + X2 + ", " + "X3:" + X3 + ", " + "X4:" + X4;
    }

    private static List<Integer> getBpCapacity(String bp, String mode) {
        if (bp.equals("BPK_d")) return List.of(0,12,0,0);
        if (bp.equals("UBPG3")) return List.of(24,0,0,0);
        if (bp.equals("BPN2")) {
            switch (Integer.parseInt(mode)) {
                case 512 -> {return List.of(72,0,0,0);}
                case 256 -> {return List.of(0,12,0,0);}
                case 16385 -> {return List.of(0,0,12,0);}
                case 769 -> {return List.of(12,12,0,0);}
                case 770 -> {return List.of(24,9,0,0);}
                case 771 -> {return List.of(36,6,0,0);}
                case 772 -> {return List.of(48,6,0,0);}
                case 16897 -> {return List.of(36,0,6,0);}
                case 16641 -> {return List.of(0,6,6,0);}
                case 17153 -> {return List.of(12,6,6,0);}
                case 17154 -> {return List.of(12,6,4,0);}
                case 4096,4097,4104,4105,8392704,8392705,8392712,8392713 -> {return List.of(0,0,0,6);}
                case 4360,4352,8392960,8392968 -> {return List.of(0,6,0,6);}
                case 4361,4353,8392961,8392969 -> {return List.of(0,6,0,3);}
                case 4608,4616,8393216,8393224 -> {return List.of(36,0,0,6);}
                case 4614,4622,8393217,8393225 -> {return List.of(36,0,0,3);}
                case 4864,4872,8393472,8393480 -> {return List.of(12,6,0,6);}
                case 4865,4873,8393473,8393481 -> {return List.of(24,3,0,6);}
                case 4898,4906,8393474,8393482 -> {return List.of(12,6,0,3);}
                case 4899,4907,8393475,8393483 -> {return List.of(24,3,0,3);}
                case 20482,20490 -> {return List.of(0,0,3,3);}
                case 20545,20553,8409098,8409090 -> {return List.of(0,0,3,6);}
                case 20481,20489,8409097,8409089 -> {return List.of(0,0,6,6);}
                case 21028,21001,8409601,8409609 -> {return List.of(12,0,3,4);}
                case 20993,21000,8409600,8409608 -> {return List.of(12,0,6,4);}
            }
        }
        if (bp.equals("BPQ2")) {
            switch (Integer.parseInt(mode)) {
                case 512 -> {return List.of(72,0,0,0);}
                case 256 -> {return List.of(0,24,0,0);}
                case 16385 -> {return List.of(0,0,24,0);}
                case 769 -> {return List.of(12,18,0,0);}
                case 770 -> {return List.of(18,18,0,0);}
                case 771 -> {return List.of(36,12,0,0);}
                case 16897 -> {return List.of(36,0,12,0);}
                case 16898 -> {return List.of(18,0,18,0);}
                case 16641 -> {return List.of(0,12,12,0);}
                case 16642 -> {return List.of(0,6,18,0);}
                case 16643 -> {return List.of(0,18,6,0);}
                case 17154 -> {return List.of(18,12,6,0);}
                case 17155 -> {return List.of(12,6,12,0);}
                case 17156 -> {return List.of(18,6,12,0);}
                case 4096,4104 -> {return List.of(0,0,0,12);}
                case 4360,4352 -> {return List.of(0,12,0,6);}
                case 1052929,1052937 -> {return List.of(0,6,0,9);}
                case 4618,4610 -> {return List.of(12,0,0,9);}
                case 4608,4616 -> {return List.of(36,0,0,6);}
                case 4872,4864 -> {return List.of(12,6,0,6);}
                case 4873,4865 -> {return List.of(18,6,0,6);}
                case 4874,4866 -> {return List.of(12,12,0,3);}
                case 100675585,100675593 -> {return List.of(0,0,0,6);} // + 6LTE-TDD
                case 20481,20489 -> {return List.of(0,0,12,6);}
                case 20486,20494 -> {return List.of(0,0,6,12);}
                case 20995,21002 -> {return List.of(36,0,3,6);}
                case 20994,21001 -> {return List.of(12,0,12,3);}
                case 21027,21034 -> {return List.of(12,0,3,9);}
                case 20993,21000 -> {return List.of(18,0,6,6);}
                case 21026,21033 -> {return List.of(36,0,6,3);}
                case 20773,20781 -> {return List.of(0,6,3,9);}
                case 20772,20780 -> {return List.of(0,12,6,3);}
                case 20739,20747 -> {return List.of(0,12,3,6);}
                case 20738,20746 -> {return List.of(0,6,12,3);}
                case 20737,20745 -> {return List.of(0,6,6,6);}
                case 21259,21251 -> {return List.of(12,6,3,6);}
                case 21257,21249 -> {return List.of(18,6,3,6);}
                case 100692006,100692001 -> {return List.of(0,0,3,6);} // + 6LTE-TDD
                case 100675873,100675881 -> {return List.of(0,6,0,3);} // + 6LTE-TDD
                case 100676137,100676129 -> {return List.of(12,0,0,3);} // + 6LTE-TDD
            }
        }
        if (bp.equals("VBPc1")) {
            switch (Integer.parseInt(mode)) {
                case 514 -> {return List.of(36,0,0,0);}
                case 257 -> {return List.of(0,24,0,0);}
                case 16385 -> {return List.of(0,0,36,0);}
                case 1049344 -> {return List.of(36,24,0,0);}
                case 16901 -> {return List.of(18,0,30,0);}
                case 16900 -> {return List.of(36,0,24,0);}
                case 16672 -> {return List.of(0,18,18,0);}
                case 16647 -> {return List.of(0,24,12,0);}
                case 16646 -> {return List.of(0,12,24,0);}
                case 17157 -> {return List.of(18,24,6,0);}
                case 4113 -> {return List.of(0,0,0,24);}
                case 4104 -> {return List.of(0,0,0,18);}
                case 4105 -> {return List.of(0,0,0,6);}
                case 20520 -> {return List.of(0,0,9,18);}
                case 20526 -> {return List.of(0,0,6,18);}
                case 20525 -> {return List.of(0,0,18,12);}
                case 20524 -> {return List.of(0,0,27,6);}
                case 20523 -> {return List.of(0,0,15,12);}
                case 20522 -> {return List.of(0,0,24,6);}
                case 20521 -> {return List.of(0,0,12,12);}
                case 100675689,101724265 -> {return List.of(0,0,0,12);} // + 6LTE-TDD
                case 100675657,101724233 -> {return List.of(0,0,0,6);} // + 12LTE-TDD
                case 21263 -> {return List.of(24,12,3,6);}
                case 21262 -> {return List.of(18,6,3,12);}
                case 21261 -> {return List.of(12,6,3,12);}
                case 21288 -> {return List.of(18,6,12,6);}
                case 21322 -> {return List.of(30,6,9,6);}
                case 21321 -> {return List.of(36,6,9,6);}
                case 21320 -> {return List.of(36,12,3,6);}
                case 21295 -> {return List.of(12,12,9,6);}
                case 21294 -> {return List.of(18,12,9,6);}
                case 21293 -> {return List.of(12,6,6,12);}
                case 21292 -> {return List.of(18,6,6,12);}
                case 21291 -> {return List.of(12,6,15,6);}
                case 21290 -> {return List.of(18,6,15,6);}
                case 21289 -> {return List.of(12,6,12,6);}
                case 21326 -> {return List.of(12,18,3,6);}
                case 21325 -> {return List.of(18,18,3,6);}
                case 21324 -> {return List.of(12,12,3,9);}
                case 21323 -> {return List.of(36,6,6,6);}
                case 4621 -> {return List.of(12,0,0,15);}
                case 4620 -> {return List.of(36,0,0,12);}
                case 4363 -> {return List.of(0,12,0,12);}
                case 4905 -> {return List.of(12,18,0,6);}
                case 4904 -> {return List.of(18,18,0,6);}
                case 4879 -> {return List.of(36,12,0,6);}
                case 4878 -> {return List.of(24,6,0,9);}
                case 4877 -> {return List.of(30,6,0,9);}
                case 4876 -> {return List.of(12,6,0,12);}
                case 4875 -> {return List.of(18,6,0,12);}
                case 1053448 -> {return List.of(12,12,0,9);}
                case 20779 -> {return List.of(0,6,21,6);}
                case 20778 -> {return List.of(0,6,12,12);}
                case 20777 -> {return List.of(0,12,6,12);}
                case 20776 -> {return List.of(0,12,15,6);}
                case 20751 -> {return List.of(0,12,3,12);}
                case 20750 -> {return List.of(0,12,12,6);}
                case 21032 -> {return List.of(18,0,21,6);}
                case 21007 -> {return List.of(18,0,12,12);}
                case 21006 -> {return List.of(36,0,6,12);}
                case 21005 -> {return List.of(36,0,15,6);}
                case 21004 -> {return List.of(36,0,3,12);}
                case 21003 -> {return List.of(36,0,12,6);}
                case 100692169 -> {return List.of(0,0,6,12);} // + 6LTE-TDD
                case 100692137 -> {return List.of(0,0,6,6);} // + 12LTE-TDD
                case 100692105 -> {return List.of(0,0,15,6);} // + 6LTE-TDD
                case 100692073 -> {return List.of(0,0,3,12);} // + 6LTE-TDD
                case 100692041 -> {return List.of(0,0,12,6);} // + 6LTE-TDD
                case 104870697 -> {return List.of(12,6,0,6);} // + 6LTE-TDD
                case 104870698 -> {return List.of(18,6,0,6);} // + 6LTE-TDD
                case 12808 -> {return List.of(12,0,0,9);} // + 6LTE-TDD
                case 12552 -> {return List.of(0,6,0,9);} // + 6LTE-TDD
                case 29192 -> {return List.of(12,0,3,9);} // + 6LTE-TDD
                case 29193 -> {return List.of(18,0,6,6);} // + 6LTE-TDD
                case 29448 -> {return List.of(18,6,3,6);} // + 6LTE-TDD
            }
        }
        if (bp.equals("VBPc0")) {
            switch (Integer.parseInt(mode)) {
                case 514 -> {return List.of(72,0,0,0);}
                case 256 -> {return List.of(0,24,0,0);}
                case 16385 -> {return List.of(0,0,36,0);}
                case 771 -> {return List.of(36,12,0,0);}
                case 1049345 -> {return List.of(12,18,0,0);}
                case 1049346 -> {return List.of(24,12,0,0);}
                case 16897 -> {return List.of(36,0,12,0);}
                case 16898 -> {return List.of(18,0,18,0);}
                case 16641 -> {return List.of(0,12,12,0);}
                case 16642 -> {return List.of(0,6,18,0);}
                case 16643 -> {return List.of(0,18,6,0);}
                case 17158 -> {return List.of(36,6,6,0);}
                case 4113 -> {return List.of(0,0,0,24);}
                case 4103 -> {return List.of(0,0,0,12);}
                case 20489 -> {return List.of(0,0,12,6);}
                case 20494 -> {return List.of(0,0,6,12);}
                case 4360 -> {return List.of(0,12,0,6);}
                case 1052936 -> {return List.of(0,6,0,9);}
                case 4616 -> {return List.of(36,0,0,6);}
                case 4618 -> {return List.of(12,0,0,9);}
                case 4872 -> {return List.of(12,6,0,6);}
                case 4873 -> {return List.of(18,6,0,6);}
                case 4874 -> {return List.of(12,12,0,3);}
                case 100675593 -> {return List.of(0,0,0,6);} // + 6LTE-TDD
                case 21002 -> {return List.of(36,0,3,6);}
                case 21000 -> {return List.of(18,0,6,6);}
                case 21034 -> {return List.of(12,0,3,9);}
                case 20745 -> {return List.of(0,6,6,6);}
                case 20747 -> {return List.of(0,12,3,6);}
                case 21259 -> {return List.of(12,6,3,6);}
                case 21257 -> {return List.of(18,6,3,6);}
                case 100692009 -> {return List.of(0,0,3,6);} // + 6LTE-TDD
                case 100676137 -> {return List.of(12,0,0,3);} // + 6LTE-TDD
            }
        }
        if (bp.equals("VBPc7")) {
            switch (Integer.parseInt(mode)) {
                case 514 -> {return List.of(36,0,0,0);}
                case 257 -> {return List.of(0,12,0,0);}
                case 16385 -> {return List.of(0,0,12,0);}
                case 775 -> {return List.of(12,6,0,0);}
                case 768 -> {return List.of(18,6,0,0);}
                case 147712 -> {return List.of(0,6,6,0);}
                case 4104 -> {return List.of(0,0,0,6);}
                case 20488 -> {return List.of(0,0,3,6);}
                case 4619 -> {return List.of(12,0,0,3);}
                case 135432 -> {return List.of(0,6,0,3);}
            }
        }

        return List.of(0,0,0,0);
    }

    private static String capasityToString(List<Integer> capacity) {
        String result = "";
        if (capacity.get(0) != 0) result = result + capacity.get(0) + "G + ";
        if (capacity.get(1) != 0) result = result + capacity.get(1) + "U + ";
        if (capacity.get(2) != 0) result = result + capacity.get(2) + "N + ";
        if (capacity.get(3) != 0) result = result + capacity.get(3) + "L + ";
        if (!result.isEmpty()) result = " (" + result.substring(0,result.length() - 3) + ")";
        return result;
    }
}



