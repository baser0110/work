package bsshelper.externalapi.configurationmng.currentmng.entity.itbbu;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ITBBUULocalCellMoc {
    private final String userLabel;
    private final String lastModifiedTime;
    private final int enableEnergySaving;
    private final int enableNBIC;
    private final String isAutoAssignBasebandResource;
    private final int enableVAM;
    private final double maxDlPwr;
    private final String refOperator;
    private final int enableDynCode;
    private final int adminState;
    private final double rxFreq;
    private final int dynMpoSwitch;
    private final String availStatus;
    private final int rcvFilterBandwidth;
    private final double freqOffset;
    private final String mocName;
    private final int txOffset;
    private final int priority;
    private final int cellRadius;
    private final String moId;
    private final int coverageType;
    private final double txFreq;
    private final int enhancedVamEnable;
    private final String operState;
    private final String ldn;
    private final int rxOffset;
    private final int enable2msGain;
    private final int localCellId;
    private final int supportSpeed;
    private final int distanceCom;
    private final int totalAttenuationTime;
    private final int smoothlyBlock;
    private final String refBPChip;
}