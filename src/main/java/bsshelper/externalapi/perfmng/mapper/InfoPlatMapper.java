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
                 result.add(new InfoGeneral.InfoPlat(s, "", copyInventoryMap.get(s), List.of(0, 0, 0)));
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
                result.add(new InfoGeneral.InfoPlat(s, "", copyInventoryMap.get(s), List.of(0, 0, 0)));
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
        if (bp.equals("BPK_d")) return List.of(0,12,0);
        if (bp.equals("UBPG3")) return List.of(24,0,0);
        if (bp.equals("BPN2")) {
            switch (Integer.parseInt(mode)) {
                case 512 -> {return List.of(72,0,0);}
                case 256 -> {return List.of(0,12,0);}
                case 16385 -> {return List.of(0,0,12);}
                case 769 -> {return List.of(12,12,0);}
                case 770 -> {return List.of(24,9,0);}
                case 771 -> {return List.of(36,6,0);}
                case 772 -> {return List.of(48,6,0);}
                case 16897 -> {return List.of(36,0,6);}
                case 16641 -> {return List.of(0,6,6);}
                case 17153 -> {return List.of(12,6,6);}
                case 17154 -> {return List.of(12,6,4);}
            }
        }
        if (bp.equals("BPQ2")) {
            switch (Integer.parseInt(mode)) {
                case 512 -> {return List.of(72,0,0);}
                case 256 -> {return List.of(0,24,0);}
                case 16385 -> {return List.of(0,0,24);}
                case 769 -> {return List.of(12,18,0);}
                case 770 -> {return List.of(18,18,0);}
                case 771 -> {return List.of(36,12,0);}
                case 16897 -> {return List.of(36,0,12);}
                case 16898 -> {return List.of(18,0,18);}
                case 16641 -> {return List.of(0,12,12);}
                case 16642 -> {return List.of(0,6,18);}
                case 16643 -> {return List.of(0,18,6);}
                case 17154 -> {return List.of(18,12,6);}
                case 17155 -> {return List.of(12,6,12);}
                case 17156 -> {return List.of(18,6,12);}
            }
        }
        if (bp.equals("VBPc1")) {
            switch (Integer.parseInt(mode)) {
                case 514 -> {return List.of(36,0,0);}
                case 257 -> {return List.of(0,24,0);}
                case 16385 -> {return List.of(0,0,36);}
                case 1049344 -> {return List.of(36,24,0);}
                case 16901 -> {return List.of(18,0,30);}
                case 16900 -> {return List.of(36,0,24);}
                case 16672 -> {return List.of(0,18,18);}
                case 16647 -> {return List.of(0,24,12);}
                case 16646 -> {return List.of(0,12,24);}
                case 17157 -> {return List.of(18,24,6);}
            }
        }
        if (bp.equals("VBPc0")) {
            switch (Integer.parseInt(mode)) {
                case 514 -> {return List.of(72,0,0);}
                case 256 -> {return List.of(0,24,0);}
                case 16385 -> {return List.of(0,0,36);}
                case 771 -> {return List.of(36,12,0);}
                case 1049345 -> {return List.of(12,18,0);}
                case 1049346 -> {return List.of(24,12,0);}
                case 16897 -> {return List.of(36,0,12);}
                case 16898 -> {return List.of(18,0,18);}
                case 16641 -> {return List.of(0,12,12);}
                case 16642 -> {return List.of(0,6,18);}
                case 16643 -> {return List.of(0,18,6);}
                case 17158 -> {return List.of(36,6,6);}
                case 20745 -> {return List.of(0,6,6);} //+6FDL
                case 20747 -> {return List.of(0,12,3);} //+6FDL | 12 × UMTS CSs (768 CEs) + 6 × FDD LTE 20 MHz cells + 3 × NB-IoT carriers
            }
        }
        if (bp.equals("VBPc7")) {
            switch (Integer.parseInt(mode)) {
                case 514 -> {return List.of(36,0,0);}
                case 257 -> {return List.of(0,12,0);}
                case 16385 -> {return List.of(0,0,12);}
                case 775 -> {return List.of(12,6,0);}
                case 768 -> {return List.of(18,6,0);}
                case 147712 -> {return List.of(0,6,6);}
            }
        }

        return List.of(0,0,0);
    }

    private static String capasityToString(List<Integer> capacity) {
        String result = "";
        if (capacity.get(0) != 0) result = result + capacity.get(0) + "G + ";
        if (capacity.get(1) != 0) result = result + capacity.get(1) + "U + ";
        if (capacity.get(2) != 0) result = result + capacity.get(2) + "N + ";
        if (!result.isEmpty()) result = " (" + result.substring(0,result.length() - 3) + ")";
        return result;
    }
}



