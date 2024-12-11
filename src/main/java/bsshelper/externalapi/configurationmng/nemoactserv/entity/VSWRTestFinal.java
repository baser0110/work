package bsshelper.externalapi.configurationmng.nemoactserv.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class VSWRTestFinal {
    private final String ruAntName;
    private final String vswrValue;

    public static List<VSWRTestFinal> toVSWRTestFinal(List<DiagnosisRow> infoList) {
        List<VSWRTestFinal> result = new ArrayList<>();
        DiagnosisRow first = infoList.get(0);
        String pos = first.getPosition();
        String name = "RU" + pos.substring(pos.lastIndexOf("(") + 1, pos.lastIndexOf(")") - 4);
        if (first.getResult().equals("Board communication link is interrupted.") || first.getResult().equals("The link between the UME and the NE is abnormal. Please try again later.")) {
            result.add(new VSWRTestFinal(name, "Link is broken"));
            return result;
        }
        result.addAll(getFinal(name, infoList));
        return result;
    }

    private static List<VSWRTestFinal> getFinal(String name, List<DiagnosisRow> infoList) {
        List<VSWRTestFinal> result = new ArrayList<>();
        for (DiagnosisRow diagnosisRow : infoList) {
            if (diagnosisRow.getElementName().contains("VSWR Value")) {
                result.add(new VSWRTestFinal(name + ":" + diagnosisRow.getElementName().substring(3,7), diagnosisRow.getResult()));
            }
        }
        return result;
    }

    public static List<VSWRTestFinal> toVSWRTestFinalForITBBU(String name, List<VSWRTestITBBU> infoList) {
        List<VSWRTestFinal> result = new ArrayList<>();
        if (infoList.isEmpty()) result.add(new VSWRTestFinal(name, "Link is broken"));
        for (VSWRTestITBBU ant : infoList) {
            result.add(new VSWRTestFinal(name + ":" + ant.getAntNo(), ant.getVswrValue().equals("4294967295") ? "Invalid Value" : ant.getVswrValue() ));
        }
        return result;
    }
}
