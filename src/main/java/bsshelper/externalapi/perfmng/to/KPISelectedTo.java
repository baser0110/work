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
        result.add(new KPISelectedTo(false, KPI.RRC_ATTEMPT.getInfo()));
        result.add(new KPISelectedTo(false, KPI.RRC.getInfo()));
        result.add(new KPISelectedTo(false, KPI.RAB.getInfo()));
        result.add(new KPISelectedTo(false, KPI.HSUPA.getInfo()));
        result.add(new KPISelectedTo(false, KPI.HSDPA.getInfo()));
        result.add(new KPISelectedTo(false, KPI.RLC.getInfo()));
        result.add(new KPISelectedTo(false, KPI.ANT_RSSI_1.getInfo()));
        result.add(new KPISelectedTo(false, KPI.ANT_RSSI_2.getInfo()));
        result.add(new KPISelectedTo(false, KPI.ANT_RSSI_1AND2.getInfo()));
        result.add(new KPISelectedTo(false, KPI.CELL_DIVERSITY.getInfo()));
        result.add(new KPISelectedTo(false, KPI.NUMBER_USER_IN_CELL.getInfo()));
        return result;
    }

    public static List<KPISelectedTo> getDefaultNoCellKpiSelectedList() {
        List<KPISelectedTo> result = new ArrayList<>();
        result.add(new KPISelectedTo(false, KPI.VSWR.getInfo()));
        result.add(new KPISelectedTo(false, KPI.MAX_OPTIC_ERROR.getInfo()));
        result.add(new KPISelectedTo(false, KPI.MAX_OPTIC_TX_POWER.getInfo()));
        result.add(new KPISelectedTo(false, KPI.MAX_OPTIC_RX_POWER.getInfo()));
        result.add(new KPISelectedTo(false, KPI.LOST_PACKET.getInfo()));
        result.add(new KPISelectedTo(false, KPI.MEAN_JITTER.getInfo()));
        return result;
    }
}