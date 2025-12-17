package bsshelper.externalapi.configurationmng.nemoactserv.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.*;


@Data
@RequiredArgsConstructor
public class OpticInfoFinal {
    private final String portName;
    private final String TxPwr;
    private final String RxPwr;

    public static List<OpticInfoFinal> toOpticInfoFinalForITBBU(String name, List<OpticInfoITBBU> opticInfoITBBUList) {
        List<OpticInfoFinal> result = new ArrayList<>();
        if (opticInfoITBBUList.isEmpty()) {
            if (name.contains("RU")) {
                result.add(new OpticInfoFinal(name + ":1", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":2", "n/a", "n/a"));
                return result;
            }
            if (name.contains("VBPc1")) {
                result.add(new OpticInfoFinal(name + ":OF1", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":OF2", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":OF3", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":OF4", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":OF5", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":OF6", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":OF7", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":OF8", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":OF9", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":EOF", "n/a", "n/a"));
                return result;
            }
            if (name.contains("VBPc7") || name.contains("VBPc0")) {
                result.add(new OpticInfoFinal(name + ":OF1", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":OF2", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":OF3", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":OF4", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":OF5", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":OF6", "n/a", "n/a"));
                return result;
            }
            if (name.contains("VSW")) {
                result.add(new OpticInfoFinal(name + ":ETH1", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":ETH2", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":ETH3", "n/a", "n/a"));
                result.add(new OpticInfoFinal(name + ":ETH4", "n/a", "n/a"));
                return result;
            }
        }

        for (OpticInfoITBBU opt : opticInfoITBBUList) {
            result.add(new OpticInfoFinal(name + ":" + opt.getPortId(), opt.getTxPower(), opt.getRxPower()));
        }
        return result;
    }

    public static List<OpticInfoFinal> toOpticInfoFinalForRU(List<DiagnosisRow> infoList) {
        List<OpticInfoFinal> result = new ArrayList<>();
        DiagnosisRow first = infoList.get(0);
        String pos = first.getPosition();
        String name = "RU" + pos.substring(pos.lastIndexOf("(") + 1, pos.lastIndexOf(")") - 4);
        if (infoList.size() == 1) {
            result.add(new OpticInfoFinal(name + ":1", "n/a", "n/a"));
            result.add(new OpticInfoFinal(name + ":2", "n/a", "n/a"));
            return result;
        }
        result.addAll(getFinal(name, infoList));
        return result;
    }

    public static List<OpticInfoFinal> toOpticInfoFinalForFS(List<DiagnosisRow> infoList) {
        List<OpticInfoFinal> result = new ArrayList<>();
        DiagnosisRow first = infoList.get(0);
        String pos = first.getPosition();
        String name = pos.replace("1.1.", "Slot");
        if (infoList.size() == 1) {
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

    public static List<OpticInfoFinal> toOpticInfoFinalForCCC(List<DiagnosisRow> infoList) {
        List<OpticInfoFinal> result = new ArrayList<>();
        DiagnosisRow first = infoList.get(0);
        String pos = first.getPosition();
        String name = pos.replace("1.1.", "Slot");
        if (infoList.size() == 1) {
            result.add(new OpticInfoFinal(name + ":ETH0", "n/a", "n/a"));
            return result;
        }
        result.addAll(getFinal(name, infoList));
        return result;
    }

    public static List<OpticInfoFinal> toOpticInfoFinalForUES(List<DiagnosisRow> infoList) {
        List<OpticInfoFinal> result = new ArrayList<>();
        DiagnosisRow first = infoList.get(0);
        String pos = first.getPosition();
        String name = pos.replace("1.1.", "Slot");
        if (infoList.size() == 1) {
            result.add(new OpticInfoFinal(name + ":X4/UPLINK", "n/a", "n/a"));
            result.add(new OpticInfoFinal(name + ":UPLINK", "n/a", "n/a"));
            return result;
        }
        result.addAll(getFinal(name, infoList));

        return result;
    }

    public static List<OpticInfoFinal> toOpticInfoFinalForBP(List<DiagnosisRow> infoList) {
        List<OpticInfoFinal> result = new ArrayList<>();
        DiagnosisRow first = infoList.get(0);
        String pos = first.getPosition();
        String name = pos.replace("1.1.", "Slot");
        if (infoList.size() == 1) {
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

    private static List<OpticInfoFinal> getFinal(String name, List<DiagnosisRow> infoList) {
        List<OpticInfoFinal> result = new ArrayList<>();

        List<String> of = new ArrayList<>();
        List<String> tx = new ArrayList<>();
        List<String> rx = new ArrayList<>();

        for (DiagnosisRow diagnosisRow : infoList) {
            if (diagnosisRow.getElementName().equals("Optical/Electric Port ID")) {
                if (of.size() > tx.size()) {tx.add("n/a");}
                if (of.size() > rx.size()) {rx.add("n/a");}
                of.add(diagnosisRow.getResult());
                continue;
            }
            if (diagnosisRow.getElementName().equals("Optical/Electric Diagnose Success Flag")) {
                if (!diagnosisRow.getResult().equals("Success")) {
                    tx.add("n/a");
                    rx.add("n/a");
                }
                continue;
            }
            if (diagnosisRow.getElementName().equals("TX Power")) {
                tx.add(diagnosisRow.getResult().replace("dBm", ""));
                continue;
            }
            if (diagnosisRow.getElementName().equals("Rx Power")) {
                rx.add(diagnosisRow.getResult().replace("dBm", ""));
            }
        }

        if (of.size() > tx.size()) {tx.add("n/a");}
        if (of.size() > rx.size()) {rx.add("n/a");}

        for (int i = 0; i < of.size(); i++) {
            result.add(new OpticInfoFinal(name + ":" + of.get(i), tx.get(i), rx.get(i)));
        }

        return result;
    }
}
