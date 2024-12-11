package bsshelper.externalapi.configurationmng.nemoactserv.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.RiCableMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.sdr.FiberCableMoc;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.OpticInfoFinal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FiberTableITBBUMapper {

    public static Map<String, List<String>> getFiberTableMap(List<RiCableMoc> riCableMocList, List<OpticInfoFinal> opticInfoFinalList) {
        if (riCableMocList == null) { return null; }
        Map<String, String> connectinMap = getConnectionMap(riCableMocList);
        Map<String, OpticInfoFinal> opticMap = getOpticMap(opticInfoFinalList);
        Map<String, List<String>> fiberTableMap = new TreeMap<>();
        for (Map.Entry<String, OpticInfoFinal> opt: opticMap.entrySet()) {
            String name = opt.getValue().getPortName();
            if (name.contains("VBP")) {
                String vbpName = name;
                if (name.contains("EOF")) vbpName = name.replace("EOF", "ZZZ");
                fiberTableMap.put(vbpName, new ArrayList<>(List.of(name, "Tx: " + (opt.getValue().getTxPwr() == null ? "n/a" : opt.getValue().getTxPwr())
                        + " / Rx: " + (opt.getValue().getRxPwr() == null ? "n/a" : opt.getValue().getRxPwr()))));
                while (name != null) {
                    if (connectinMap.containsKey(name)) {
                        String nextName = connectinMap.get(name);
                        OpticInfoFinal nextValue = opticMap.get(nextName);
                        fiberTableMap.get(vbpName).addAll(new ArrayList<>(List.of(":", " Rx: " + (nextValue.getRxPwr() == null ? "n/a" : nextValue.getRxPwr())
                                + " / Tx: " + (nextValue.getTxPwr() == null ? "n/a" : nextValue.getTxPwr()), nextName)));
                        nextName = nextName.replace(":1", ":2");
                        nextValue = opticMap.get(nextName);
                        fiberTableMap.get(vbpName).addAll(new ArrayList<>(List.of(nextName, "Tx: " + (nextValue.getTxPwr() == null ? "n/a" : nextValue.getTxPwr())
                                + " / Rx: " + (nextValue.getRxPwr() == null ? "n/a" : nextValue.getRxPwr()))));
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
            if (name.contains("VSW")) {
                linkTableMap.put(name, new ArrayList<>(List.of(name, "Tx: " + (opt.getValue().getTxPwr() == null ? "n/a" : opt.getValue().getTxPwr())
                        + " / Rx: " + (opt.getValue().getRxPwr() == null ? "n/a" : opt.getValue().getRxPwr()))));
            }
        }
        return linkTableMap;
    }

    public static Map<String, String> getConnectionMap(List<RiCableMoc> riCableMocList) {
        Map<String, String> connectinMap = new TreeMap<>();
        for (RiCableMoc f : riCableMocList) {
            String ref1;
            String ref2;
            if (f.getRefUpRiPort().contains("=VBP")) {
                int index1 = f.getRefUpRiPort().indexOf("VBP");
                int index2 = f.getRefUpRiPort().length();
                ref1 = f.getRefUpRiPort().substring(index1, index2);
                ref1 = ref1.replace("_1_","(Slot");
                ref1 = ref1.replace(",RiPort=","):");
            }
            else {
                int index1 = f.getRefUpRiPort().indexOf(",RiPort=") - 2;
                int index2 = f.getRefUpRiPort().length();
                ref1 = f.getRefUpRiPort().substring(index1, index2);
                ref1 = "RU" + ref1.replace(",RiPort=OPT",":");
            }
            int index1 = f.getRefDownRiPort().indexOf(",RiPort=") - 2;
            int index2 = f.getRefDownRiPort().length();
            ref2 = f.getRefDownRiPort().substring(index1, index2);
            ref2 = "RU" + ref2.replace(",RiPort=OPT",":");
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
