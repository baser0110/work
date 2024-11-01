package bsshelper.externalapi.configurationmng.nemoactserv.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Data
@RequiredArgsConstructor
public class OpticInfoFinal {
    private final String portName;
    private final String TxPwr;
    private final String RxPwr;

    public static List<OpticInfoFinal> toOpticInfoFinalForRU(List<OpticInfo> infoList) {
        List<OpticInfoFinal> result = new ArrayList<>();
        OpticInfo first = infoList.get(0);
        String pos = first.getPosition();
        String name = "RU" + pos.substring(pos.lastIndexOf("(") + 1, pos.lastIndexOf(")") - 4);
        if (first.getResult().equals("Board communication link is interrupted.")) {
            result.add(new OpticInfoFinal(name + ":1", "n/a", "n/a"));
            result.add(new OpticInfoFinal(name + ":2", "n/a", "n/a"));
            return result;
        }
        result.addAll(getFinal(name, infoList));
        return result;
    }

    public static List<OpticInfoFinal> toOpticInfoFinalForFS(List<OpticInfo> infoList) {
        List<OpticInfoFinal> result = new ArrayList<>();
        OpticInfo first = infoList.get(0);
        String pos = first.getPosition();
        String name = pos.replace("1.1.", "Slot");
        if (first.getResult().equals("Board communication link is interrupted.")) {
            result.add(new OpticInfoFinal(name + ":OF0", "n/a", "n/a"));
            result.add(new OpticInfoFinal(name + ":OF1", "n/a", "n/a"));
            result.add(new OpticInfoFinal(name + ":OF2", "n/a", "n/a"));
            result.add(new OpticInfoFinal(name + ":OF3", "n/a", "n/a"));
            result.add(new OpticInfoFinal(name + ":OF4", "n/a", "n/a"));
            result.add(new OpticInfoFinal(name + ":OF5", "n/a", "n/a"));
            return result;
        }
        result.addAll(getFinal(name, infoList));
        return result;
    }

    public static List<OpticInfoFinal> toOpticInfoFinalForCCC(List<OpticInfo> infoList) {
        List<OpticInfoFinal> result = new ArrayList<>();
        OpticInfo first = infoList.get(0);
        String pos = first.getPosition();
        String name = pos.replace("1.1.", "Slot");
        if (first.getResult().equals("Board communication link is interrupted.")) {
            result.add(new OpticInfoFinal(name + ":ETH0", "n/a", "n/a"));
            return result;
        }
        result.addAll(getFinal(name, infoList));
        return result;
    }

    public static List<OpticInfoFinal> toOpticInfoFinalForUES(List<OpticInfo> infoList) {
        List<OpticInfoFinal> result = new ArrayList<>();
        OpticInfo first = infoList.get(0);
        String pos = first.getPosition();
        String name = pos.replace("1.1.", "Slot");
        if (first.getResult().equals("Board communication link is interrupted.")) {
            result.add(new OpticInfoFinal(name + ":X4/UPLINK", "n/a", "n/a"));
            result.add(new OpticInfoFinal(name + ":UPLINK", "n/a", "n/a"));
            return result;
        }
        result.addAll(getFinal(name, infoList));

        return result;
    }

    private static List<OpticInfoFinal> getFinal(String name, List<OpticInfo> infoList) {
        List<OpticInfoFinal> result = new ArrayList<>();

        List<String> of = new ArrayList<>();
        List<String> tx = new ArrayList<>();
        List<String> rx = new ArrayList<>();

        for (OpticInfo opticInfo : infoList) {
            if (opticInfo.getElementName().equals("Optical/Electric Port ID")) {
                of.add(opticInfo.getResult());
            }
            if (opticInfo.getElementName().equals("Optical/Electric Diagnose Success Flag")) {
                if (!opticInfo.getResult().equals("Success")) {
                    tx.add("n/a");
                    rx.add("n/a");
                }
            }
            if (opticInfo.getElementName().equals("TX Power")) {
                tx.add(opticInfo.getResult().replace("dBm", ""));
            }
            if (opticInfo.getElementName().equals("Rx Power")) {
                rx.add(opticInfo.getResult().replace("dBm", ""));
            }
        }
        for (int i = 0; i < of.size(); i++) {
            result.add(new OpticInfoFinal(name + ":" + of.get(i), tx.get(i), rx.get(i)));
        }
//        System.out.println(result);
        return result;
    }
}
