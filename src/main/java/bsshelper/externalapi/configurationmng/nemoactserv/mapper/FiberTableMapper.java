package bsshelper.externalapi.configurationmng.nemoactserv.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.FiberCableMoc;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.OpticInfoFinal;

import java.util.*;

public class FiberTableMapper {

    public static Map<String, List<String>> getFiberTableMap(List<FiberCableMoc> fiberCableMocList, List<OpticInfoFinal> opticInfoFinalList) {
        if (fiberCableMocList == null) { return null; }
        Map<String, String> connectinMap = getConnectionMap(fiberCableMocList);
        Map<String, OpticInfoFinal> opticMap = getOpticMap(opticInfoFinalList);
        Map<String, List<String>> fiberTableMap = new TreeMap<>();
        for (Map.Entry<String, OpticInfoFinal> opt: opticMap.entrySet()) {
            String name = opt.getValue().getPortName();
            if (name.contains("FS")) {
                String fsName = name;
                name = name.substring(2);
                fiberTableMap.put(fsName, new ArrayList<>(List.of(fsName, "Tx: " + opt.getValue().getTxPwr() + " / Rx: " + opt.getValue().getRxPwr())));
                while (name != null) {
                    if (connectinMap.containsKey(name)) {
                        String nextName = connectinMap.get(name);
                        OpticInfoFinal nextValue = opticMap.get(nextName);
                        fiberTableMap.get(fsName).addAll(new ArrayList<>(List.of(":", " Rx: " + nextValue.getRxPwr() + " / Tx: " + nextValue.getTxPwr(), nextName)));
                        nextName = nextName.replace(":1", ":2");
                        nextValue = opticMap.get(nextName);
                        fiberTableMap.get(fsName).addAll(new ArrayList<>(List.of(nextName, "Tx: " + nextValue.getTxPwr() + " / Rx: " + nextValue.getRxPwr())));
                        if (connectinMap.containsKey(nextName)) {
                            name = nextName;
                        } else name = null;
                    } else name = null;
                }
            }
            else if (name.contains("BP")) {
                String bpName = name;
                name = name.substring(4);
                fiberTableMap.put(bpName, new ArrayList<>(List.of(bpName, "Tx: " + opt.getValue().getTxPwr() + " / Rx: " + opt.getValue().getRxPwr())));
                while (name != null) {
                    if (connectinMap.containsKey(name)) {
                        String nextName = connectinMap.get(name);
                        OpticInfoFinal nextValue = opticMap.get(nextName);
                        fiberTableMap.get(bpName).addAll(new ArrayList<>(List.of(":", " Rx: " + nextValue.getRxPwr() + " / Tx: " + nextValue.getTxPwr(), nextName)));
                        nextName = nextName.replace(":1", ":2");
                        nextValue = opticMap.get(nextName);
                        fiberTableMap.get(bpName).addAll(new ArrayList<>(List.of(nextName, "Tx: " + nextValue.getTxPwr() + " / Rx: " + nextValue.getRxPwr())));
                        if (connectinMap.containsKey(nextName)) {
                            name = nextName;
                        } else name = null;
                    } else name = null;
                }
            }
        }
        return fiberTableMap;
    }

    public static Map<String, List<String>> getLinkTableMap(List<OpticInfoFinal> opticInfoFinalList) {
        Map<String, OpticInfoFinal> opticMap = getOpticMap(opticInfoFinalList);
        Map<String, List<String>> linkTableMap = new TreeMap<>();
        for (Map.Entry<String, OpticInfoFinal> opt: opticMap.entrySet()) {
            String name = opt.getValue().getPortName();
            if (name.contains("UES") || name.contains("CCC")) {
                linkTableMap.put(name, new ArrayList<>(List.of(name, "Tx: " + opt.getValue().getTxPwr() + " / Rx: " + opt.getValue().getRxPwr())));
            }
        }
        return linkTableMap;
    }

    private static Map<String, String> getConnectionMap(List<FiberCableMoc> fiberCableMocList) {
        Map<String, String> connectinMap = new TreeMap<>();
        for (FiberCableMoc f : fiberCableMocList) {
            String ref1;
            String ref2;
            if (f.getRef1FiberDevice().contains(",Rack=1,")) {
                int index1 = f.getRef1FiberDevice().indexOf("Slot=");
                int index2 = index1 + 6;
                int index3 = f.getRef1FiberDevice().lastIndexOf("=");
                ref1 = "(" + f.getRef1FiberDevice().substring(index1, index2) + "):OF"
                        + (Integer.parseInt(f.getRef1FiberDevice().substring(index3 + 1)) - 1) ;
                ref1 = ref1.replace("=","");
            }
            else {
                int index1 = f.getRef1FiberDevice().indexOf("Rack=") + 5;
                int index2 = index1 + 2;
                int index3 = f.getRef1FiberDevice().lastIndexOf("=");
                ref1 = "RU" + f.getRef1FiberDevice().substring(index1, index2) + ":"
                        + f.getRef1FiberDevice().substring(index3 + 1);
            }
            int index1 = f.getRef2FiberDevice().indexOf("Rack=") + 5;
            int index2 = index1 + 2;
            int index3 = f.getRef2FiberDevice().lastIndexOf("=");
            ref2 = "RU" + f.getRef2FiberDevice().substring(index1, index2) + ":"
                    + f.getRef2FiberDevice().substring(index3 + 1);
            connectinMap.put(ref1, ref2);
        }
        return connectinMap;
    }

    private static Map<String, OpticInfoFinal> getOpticMap(List<OpticInfoFinal> opticInfoFinalList) {
        Map<String, OpticInfoFinal> opticInfoFinalMap = new TreeMap<>();
        for (OpticInfoFinal opt : opticInfoFinalList) {
            opticInfoFinalMap.put(opt.getPortName(), opt);
        }
        return opticInfoFinalMap;
    }
}
