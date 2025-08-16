package bsshelper.externalapi.configurationmng.nemoactserv.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ClockGNSSInformationITBBU {
    private String receiverUARTStatus;
    private String receiverFixType;
    private String receiverJammingStatus;
    private String receiverWorkingMode;
    private String receiverInitialStatus;
    private String ppsStatus;
    private String antennaStatus;
    private String blockStatus;
    private String receiverLockSatelliteStatus;
    private String numberofLockGPSSatellites;
    private String receiverLongitude;
    private String receiverLatitude;
    private String receiverAltitude;
    private String gpsTime;
    private String utcTime;
    private String leapSeconds;
    private List<SatelliteInfo> satelliteInfo;

    @Data
    @RequiredArgsConstructor
    public static class SatelliteInfo{
        private String satelliteType;
        private String satlliteID;
        private String azimuth;
        private String elevation;
        private String FP1;
        private String FP1SNR;
        private String state;
    }
}