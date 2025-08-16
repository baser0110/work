package bsshelper.externalapi.configurationmng.nemoactserv.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ClockStatusITBBU {
    private String currentActiveReferenceClock;
    private String controllingStatus;
    private String clockModuleStatus;
    private String netPortpllLockStatus;
    private String phasevalue;
    private String phaseOffset;
    private String currentControlVoltage;
    private String factoryControlVoltage;
    private String factoryVk;
    private String crystalTemperature;
}

