package bsshelper.externalapi.configurationmng.nemoactserv.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class SyncFinal {
    private final String query;
    private final String element;
    private final String result;

    public static List<SyncFinal> toSyncFinalSDR(List<DiagnosisRow> infoList) {
        List<SyncFinal> result = new ArrayList<>();
        for (DiagnosisRow row : infoList) {
            result.add(new SyncFinal(row.getFunctionName(), row.getElementName(), row.getResult()));
        }
        return result;
    }

    public static List<SyncFinal> toSyncFinalITBBU(
            ClockStatusITBBU staus,
            ClockSyncEInformationITBBU syncE,
            Clock1588InformationITBBU sync1588,
            ClockGNSSInformationITBBU syncGNSS) {
        List<SyncFinal> result = new ArrayList<>();


        if (staus != null) {
            result.add(new SyncFinal("Clock Status Query", "Current active reference source",
                    staus.getCurrentActiveReferenceClock() == null ? "N/A" :
                            syncTypeName(staus.getCurrentActiveReferenceClock())));
            result.add(new SyncFinal("Clock Status Query", "Control status of the current reference source",
                    staus.getControllingStatus() == null ? "N/A" :
                            staus.getControllingStatus().equals("2") ? "Control status of the current reference source" :
                                    staus.getControllingStatus()));
            result.add(new SyncFinal("Clock Status Query", "Active/standby status of the clock module",
                    staus.getClockModuleStatus() == null ? "N/A" :
                            staus.getClockModuleStatus().equals("1") ? "Active Status" :
                                    staus.getClockModuleStatus().equals("0") ? "Standby Status" :
                                            staus.getClockModuleStatus()));
            result.add(new SyncFinal("Clock Status Query", "Lock status of network port PLL",
                    staus.getNetPortpllLockStatus() == null ? "N/A" :
                            staus.getNetPortpllLockStatus().equals("0") ? "Locked" :
                                    staus.getNetPortpllLockStatus()));
            result.add(new SyncFinal("Clock Status Query", "Phase of current active reference clock",
                    staus.getPhasevalue() == null ? "N/A" :
                            staus.getPhasevalue() + "ns"));
            result.add(new SyncFinal("Clock Status Query", "Phase discrimination frequency offset of the current active reference source",
                    staus.getPhaseOffset() == null ? "N/A" :
                            staus.getPhaseOffset() + "ppb"));
        } else {
            result.add(new SyncFinal("Clock Status Query", "---",
                    "Link between the UME and the NE is abnormal"));
        }

        if (syncE != null) {
            result.add(new SyncFinal("SyncE Query", "SyncE clock status",
                    syncE.getSyncEStatus() == null ? "N/A" :
                            syncE.getSyncEStatus().equals("1") ? "Available" :
                                    syncE.getSyncEStatus().equals("0") ? "Not Available" :
                                            syncE.getSyncEStatus()));
            result.add(new SyncFinal("SyncE Query", "SSM packet enable status",
                    syncE.getIsEnableSSM() == null ? "N/A" :
                            syncE.getIsEnableSSM().equals("1") ? "Enabled" :
                                    syncE.getIsEnableSSM().equals("0") ? "Disabled" :
                                            syncE.getIsEnableSSM()));
            result.add(new SyncFinal("SyncE Query", "SyncE clock source level",
                    syncE.getClockGrade() == null ? "N/A" :
                            syncE.getClockGrade().equals("2") ? "PRC" :
                                    syncE.getClockGrade()));
            result.add(new SyncFinal("SyncE Query", "SyncE clock frequency deviation",
                    syncE.getSyncEclockFreqOffset() == null ? "N/A" :
                            syncE.getSyncEclockFreqOffset() + "ppb"));
            result.add(new SyncFinal("SyncE Query", "Blocking status",
                    syncE.getBlockStatus() == null ? "N/A" :
                            syncE.getBlockStatus().equals("0") ? "Unblock" :
                                    syncE.getBlockStatus().equals("1") ? "Block" :
                                            syncE.getBlockStatus()));
        } else {
            result.add(new SyncFinal("SyncE Query", "---",
                    "SyncE not configured or link between the UME and the NE is abnormal"));
        }

        if (sync1588 != null) {
            result.add(new SyncFinal("1588 Query", "Status of the 1588 clock source",
                    sync1588.getClock1588Status() == null ? "N/A" :
                            sync1588.getClock1588Status().equals("1") ? "Available" :
                                    sync1588.getClock1588Status().equals("0") ? "Not Available" :
                                            sync1588.getClock1588Status()));
            result.add(new SyncFinal("1588 Query", "Time of the 1588 clock source",
                    sync1588.getClock1588Time() == null ? "N/A" :
                            sync1588.getClock1588Time()));
            result.add(new SyncFinal("1588 Query", "UTC time",
                    sync1588.getUtcTime() == null ? "N/A" :
                            sync1588.getUtcTime()));
            result.add(new SyncFinal("1588 Query", "Leap second information",
                    sync1588.getLeapSeconds() == null ? "N/A" :
                            sync1588.getLeapSeconds() + "s"));
            result.add(new SyncFinal("1588 Query", "Blocking status",
                    sync1588.getBlockStatus() == null ? "N/A" :
                            sync1588.getBlockStatus().equals("0") ? "Unblock" :
                                    sync1588.getBlockStatus().equals("1") ? "Block" :
                                            sync1588.getBlockStatus()));
            result.add(new SyncFinal("1588 Query", "Link ID",
                    sync1588.getMasterLinkInfo().getFirst().getLinkNo() == null ? "N/A" :
                            sync1588.getMasterLinkInfo().getFirst().getLinkNo()));
            result.add(new SyncFinal("1588 Query", "Working mode",
                    sync1588.getMasterLinkInfo().getFirst().getLinkMode() == null ? "N/A" :
                            sync1588.getMasterLinkInfo().getFirst().getLinkMode().equals("9") ? "Standby" :
                                    sync1588.getMasterLinkInfo().getFirst().getLinkMode()));
            result.add(new SyncFinal("1588 Query", "Link status",
                    sync1588.getMasterLinkInfo().getFirst().getLinkStatus() == null ? "N/A" :
                            sync1588.getMasterLinkInfo().getFirst().getLinkStatus().equals("1") ? "Normal" :
                                    sync1588.getMasterLinkInfo().getFirst().getLinkStatus().equals("0") ? "Abnormal" :
                                            sync1588.getMasterLinkInfo().getFirst().getLinkStatus()));
            result.add(new SyncFinal("1588 Query", "Phase status",
                    sync1588.getMasterLinkInfo().getFirst().getPhaseStatus() == null ? "N/A" :
                            sync1588.getMasterLinkInfo().getFirst().getPhaseStatus().equals("0") ? "Cannot Tracked" :
                                    sync1588.getMasterLinkInfo().getFirst().getPhaseStatus().equals("1") ? "Can Tracked" :
                                            sync1588.getMasterLinkInfo().getFirst().getPhaseStatus()));
            result.add(new SyncFinal("1588 Query", "Frequency status",
                    sync1588.getMasterLinkInfo().getFirst().getFrequencyStatus() == null ? "N/A" :
                            sync1588.getMasterLinkInfo().getFirst().getFrequencyStatus().equals("0") ? "Cannot Tracked" :
                                    sync1588.getMasterLinkInfo().getFirst().getFrequencyStatus().equals("1") ? "Can Tracked" :
                                            sync1588.getMasterLinkInfo().getFirst().getFrequencyStatus()));
            result.add(new SyncFinal("1588 Query", "Clock grade",
                    sync1588.getMasterLinkInfo().getFirst().getClockClass() == null ? "N/A" :
                            sync1588.getMasterLinkInfo().getFirst().getClockClass()));
        } else {
            result.add(new SyncFinal("1588 Query", "---",
                    "1588 not configured or link between the UME and the NE is abnormal"));
        }

        if (syncGNSS != null) {
            result.add(new SyncFinal("GNSS Query", "Serial port status of receiver",
                    syncGNSS.getReceiverUARTStatus() == null ? "N/A" :
                            syncGNSS.getReceiverUARTStatus().equals("0") ? "Normal" :
                                    syncGNSS.getReceiverUARTStatus().equals("1") ? "Abnormal" :
                                            syncGNSS.getReceiverUARTStatus()));
            result.add(new SyncFinal("GNSS Query", "Receiver positioning type",
                    syncGNSS.getReceiverFixType() == null ? "N/A" :
                            syncGNSS.getReceiverFixType().equals("5") ? "Time Only Fix" :
                                    syncGNSS.getReceiverFixType()));
            result.add(new SyncFinal("GNSS Query", "Receiver jamming status",
                    syncGNSS.getReceiverJammingStatus() == null ? "N/A" :
                            syncGNSS.getReceiverJammingStatus().equals("255") ? "Not Support" :
                                    syncGNSS.getReceiverJammingStatus()));
            result.add(new SyncFinal("GNSS Query", "Receiver operation mode",
                    syncGNSS.getReceiverWorkingMode() == null ? "N/A" :
                            syncGNSS.getReceiverWorkingMode().equals("1") ? "GPS" :
                                    syncGNSS.getReceiverWorkingMode()));
            result.add(new SyncFinal("GNSS Query", "Initial status of receiver",
                    syncGNSS.getBlockStatus() == null ? "N/A" :
                            syncGNSS.getBlockStatus().equals("0") ? "Success" :
                                    syncGNSS.getBlockStatus().equals("1") ? "Fail" :
                                            syncGNSS.getBlockStatus()));
            result.add(new SyncFinal("GNSS Query", "1PPS status",
                    syncGNSS.getPpsStatus() == null ? "N/A" :
                            syncGNSS.getPpsStatus().equals("0") ? "Available" :
                                    syncGNSS.getPpsStatus().equals("1") ? "Not Available" :
                                            syncGNSS.getPpsStatus()));
            result.add(new SyncFinal("GNSS Query", "Antenna status",
                    syncGNSS.getAntennaStatus() == null ? "N/A" :
                            syncGNSS.getAntennaStatus().equals("1") ? "Normal" :
                                    syncGNSS.getAntennaStatus().equals("0") ? "Abnormal" :
                                            syncGNSS.getAntennaStatus()));
            result.add(new SyncFinal("GNSS Query", "Blocking status",
                    syncGNSS.getBlockStatus() == null ? "N/A" :
                            syncGNSS.getBlockStatus().equals("0") ? "Unblock" :
                                    syncGNSS.getBlockStatus().equals("1") ? "Block" :
                                            syncGNSS.getBlockStatus()));
            result.add(new SyncFinal("GNSS Query", "Satellite search status of receiver",
                    syncGNSS.getReceiverLockSatelliteStatus() == null ? "N/A" :
                            syncGNSS.getReceiverLockSatelliteStatus().equals("0") ? "Success" :
                                    syncGNSS.getReceiverLockSatelliteStatus().equals("1") ? "Fail" :
                                            syncGNSS.getReceiverLockSatelliteStatus()));
            result.add(new SyncFinal("GNSS Query", "Number of locked GPS-positioned satellites",
                    syncGNSS.getNumberofLockGPSSatellites() == null ? "N/A" :
                            syncGNSS.getNumberofLockGPSSatellites()));
            result.add(new SyncFinal("GNSS Query", "Receiver location (longitude)",
                    syncGNSS.getReceiverLongitude() == null ? "N/A" :
                            syncGNSS.getReceiverLongitude() + "°"));
            result.add(new SyncFinal("GNSS Query", "Receiver location (latitude)",
                    syncGNSS.getReceiverLatitude() == null ? "N/A" :
                            syncGNSS.getReceiverLatitude() + "°"));
            result.add(new SyncFinal("GNSS Query", "Receiver location (altitude)",
                    syncGNSS.getReceiverAltitude() == null ? "N/A" :
                            syncGNSS.getReceiverAltitude() + "m"));
            result.add(new SyncFinal("GNSS Query", "GPS time",
                    syncGNSS.getGpsTime() == null ? "N/A" :
                            syncGNSS.getGpsTime()));
            result.add(new SyncFinal("GNSS Query", "UTC time",
                    syncGNSS.getUtcTime() == null ? "N/A" : 
                            syncGNSS.getUtcTime()));
            result.add(new SyncFinal("GNSS Query", "Leap seconds",
                    syncGNSS.getLeapSeconds() == null ? "N/A" :
                            syncGNSS.getLeapSeconds() + "s"));
        } else {
            result.add(new SyncFinal("GNSS Query", "---",
                    "GNSS not configured or link between the UME and the NE is abnormal"));
        }

        return result;
    }

    private static String syncTypeName(String numberValue) {
        switch (numberValue) {
            case "1" -> {return  "Inner GNSS";}
            case "24" -> {return  "Inner GNSS backup";}
            case "17" -> {return  "RRU GNSS";}
            case "4" -> {return  "1PPS+TOD";}
            case "13" -> {return  "SyncE";}
            case "10" -> {return  "SyncE+1588";}
            case "11" -> {return  "1588 Frequency synchronization";}
            case "12" -> {return  "1588 ATR";}
            case "23" -> {return  "1588 Loose synchronization";}
            default -> {
                return numberValue;
            }
        }
    }

}
