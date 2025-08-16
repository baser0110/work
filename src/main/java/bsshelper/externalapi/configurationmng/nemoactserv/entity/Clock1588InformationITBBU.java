package bsshelper.externalapi.configurationmng.nemoactserv.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Clock1588InformationITBBU {
    private String clock1588Status;
    private String clock1588Time;
    private String utcTime;
    private String leapSeconds;
    private String blockStatus;
    private List<LinkInfo> masterLinkInfo;
    private List<LinkInfo> backupLinkInfo;

    @Data
    @RequiredArgsConstructor
    public static class LinkInfo {
        private String linkNo;
        private String linkMode;
        private String linkStatus;
        private String phaseStatus;
        private String frequencyStatus;
        private String clockClass;
        private String gmClockId;
        private String stepsRemoved;
        private String clkPriority1;
        private String clkPriority2;
        private String slaveClockId;
        private String announceInterval;
        private String syncInterval;
        private String delayReqInterval;
        private String delayRespInterval;
    }
}