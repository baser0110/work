package bsshelper.externalapi.configurationmng.nemoactserv.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OpticInfoITBBU {
    private String portId;
    private String diagnosisResultFlag;
    private String optModuleType;
    private String connectorType;
    private String waveLength;
    private String normalBitRate;
    private String rxPower;
    private String rxPowerStatus;
    private String rxPowerLowThold;
    private String txPower;
    private String txPowerStatus;
    private String txPowerLowThold;
    private String txBiasCurrent;
    private String txBiasCurrentStatus;
    private String txBiasCurrentHighThold;
    private String transceiverTemperature;
    private String transceiverTemperatureStatus;
    private String temperatureHighThold;
    private String transceiverVoltage;
    private String transceiverVoltageStatus;
    private String configuredOptPortRate;
    private String optPortRateMatchResult;
    private String vendorName;
    private String vendorPN;
    private String vendorSN;
    private String dateCode;
    private String zteAuthenticationStatus;
    private String upDownPortStatus;
    private String upperLimit;
    private String lowerLimit;
    private String diagnosisOfType;
    private String lengthFor9umKmSingleOpt;
    private String lengthFor9umHundredmSingleOpt;
}

