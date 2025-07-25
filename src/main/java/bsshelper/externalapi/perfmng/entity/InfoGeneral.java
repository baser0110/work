package bsshelper.externalapi.perfmng.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class InfoGeneral {
    private final List<InfoPlat> platInfoList;
    private final List<InfoIp> ipInfoList;
    private List<Integer> capacityList;
    private List<Integer> cellAmountList;

    @Data
    @RequiredArgsConstructor
    public static class InfoPlat{
        private final String position;
        private final String conf;
        private final String inv;
        private final List<Integer> capacity;
    }

    @Data
    @RequiredArgsConstructor
    public static class InfoIp{
        private final String technology;
        private final String vlan;
        private final String ip;
        private final String mask;
        private final String gateway;
    }

    public void populateCapacityList(List<InfoPlat> platInfoList) {
        int g = 0;
        int u = 0;
        int n = 0;
        for (InfoPlat p: platInfoList) {
            g = g + p.getCapacity().get(0);
            u = u + p.getCapacity().get(1);
            n = n + p.getCapacity().get(2);
        }
        capacityList =  List.of(g,u,n);
    }

    public void populateCellAmountList(int gsm, int umts, int nbiot) {
        cellAmountList = List.of(gsm, umts, nbiot);
    }

}
