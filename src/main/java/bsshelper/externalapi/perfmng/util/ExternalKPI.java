package bsshelper.externalapi.perfmng.util;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Getter
@RequiredArgsConstructor
public class ExternalKPI implements KPIable {
    private final String code;
    private final String info;
    private final String moType;
    private final String unit;
    private final String format;

    public static List<ExternalKPI> getTest() {
        List<ExternalKPI> list = new ArrayList<>();
        list.add(new ExternalKPI("300520","Success Rate of Transfer from HS-DSCH to DCH Due to UE Mobility (Intra-Frequency)","wm.UtranCell","%",""));
        list.add(new ExternalKPI("900329","3G PS Call Drop Rate(PCH)","wm.UtranCell","","#,##0.00%"));
        list.add(new ExternalKPI("300199","Average time of RAB establishment(CS)","wm.UtranCell","ms","#,##0.####"));
        list.add(new ExternalKPI("300572","Traffic of HSUPA","wm.LogicCell","Erl","#,##0.####"));
        list.add(new ExternalKPI("300593","PS R99 streaming traffic UL Throughput(MAC)in best cell","wm.LogicCell","KByte/s","#,##0.####"));
        list.add(new ExternalKPI("300995","Average time of RAB PS R99 Traffic Volume DL 64K(MAC)(CS)","wm.LogicCell","kBytes","#,##0.####"));
        return list;
    }

    public static Map<String, ExternalKPI> getTestMap(List<ExternalKPI> list) {
        Map<String, ExternalKPI> result = new TreeMap<>();
        for (ExternalKPI kpi: list) {
            result.put(kpi.getCode(), kpi);
        }
        return result;
    }
}


