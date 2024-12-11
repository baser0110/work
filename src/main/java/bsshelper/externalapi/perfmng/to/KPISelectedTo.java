package bsshelper.externalapi.perfmng.to;

import bsshelper.externalapi.perfmng.util.KPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KPISelectedTo {

    private boolean selected;
    private String kpiName;

    public static List<KPISelectedTo> getDefaultKpiSelectedList() {
        List<KPISelectedTo> result = new ArrayList<>();
        result.add(new KPISelectedTo(true, KPI.RTWP.getInfo()));
        result.add(new KPISelectedTo(false, KPI.RRC.getInfo()));
        result.add(new KPISelectedTo(false, KPI.RAB.getInfo()));
        result.add(new KPISelectedTo(false, KPI.HSUPA.getInfo()));
        result.add(new KPISelectedTo(false, KPI.HSDPA.getInfo()));
        result.add(new KPISelectedTo(false, KPI.RLC.getInfo()));
        return result;
    }
}