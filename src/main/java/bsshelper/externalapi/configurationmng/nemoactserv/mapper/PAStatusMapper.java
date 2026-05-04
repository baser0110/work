package bsshelper.externalapi.configurationmng.nemoactserv.mapper;

import bsshelper.externalapi.configurationmng.currentmng.entity.itbbu.TxChannelMoc;
import bsshelper.externalapi.configurationmng.nemoactserv.entity.PAStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PAStatusMapper {

    public static PAStatus toPAStatus(bsshelper.externalapi.configurationmng.currentmng.entity.sdr.TxChannelMoc sdr) {
        return PAStatus.builder()
                .selected(false)
                .userLabel(ldnToUserLabelForSDR(sdr.getLdn()))
                .ldn(sdr.getLdn())
                .adminState(adminStateFromNumericToText(sdr.getAdminState()))
                .build();
    }

    public static PAStatus toPAStatus(TxChannelMoc itbbu) {
        return PAStatus.builder()
                .selected(false)
                .userLabel(ldnToUserLabelForITBBU(itbbu.getLdn()))
                .ldn(itbbu.getLdn())
                .adminState(adminStateFromNumericToText(itbbu.getAdminState()))
                .build();
    }

    public static List<PAStatus> toPAStatusSDR(List<bsshelper.externalapi.configurationmng.currentmng.entity.sdr.TxChannelMoc> sdrList) {
        List<PAStatus> result = new ArrayList<>();
        if (sdrList != null) {
            for (bsshelper.externalapi.configurationmng.currentmng.entity.sdr.TxChannelMoc pa : sdrList) {
                result.add(toPAStatus(pa));
            }
            result.sort(Comparator.comparing(PAStatus::getUserLabel));
        }
        return result;
    }

    public static List<PAStatus> toPAStatusITBBU(List<TxChannelMoc> itbbuList) {
        List<PAStatus> result = new ArrayList<>();
        if (itbbuList != null) {
            for (TxChannelMoc pa : itbbuList) {
                result.add(toPAStatus(pa));
            }
            result.sort(Comparator.comparing(PAStatus::getUserLabel));
        }
        return result;
    }

    private static String ldnToUserLabelForSDR(String ldn) {
        return ldn.replace("Equipment=1,Rack=","RU")
                .replace(",SubRack=1,Slot=1,PlugInUnit=1,SdrDeviceGroup=1,RfDeviceSet=1,RfDevice=", ":ANT")
                .replace(",TxChannel=1","");
    }

    private static String ldnToUserLabelForITBBU(String ldn) {
        return ldn.substring(0,ldn.length() - 1).replace("Equipment=1,ReplaceableUnit=","RU")
                .replace(",RfPort=", ":")
                .replace(",TxChannel=","");
    }

    private static String adminStateFromNumericToText(String adminState) {
        if (adminState.isEmpty() || adminState.equals("0")) return "Unblocked";
        if (adminState.equals("1")) return "Blocked";
        return adminState;
    }
}