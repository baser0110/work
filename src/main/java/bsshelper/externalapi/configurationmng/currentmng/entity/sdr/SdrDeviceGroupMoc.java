package bsshelper.externalapi.configurationmng.currentmng.entity.sdr;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SdrDeviceGroupMoc {
    private final String lastModifiedTime;
    private final String ldn;
    private final String productData_productName;
    private final int functionMode;
    private final String physicalBrdType;
    private final String userLabel;
    private final int adminStateGSM;
    private final int adminStateUMTS;
    private final int adminStateLTEFDD;
    private final String productData_productNumber;
    private final int moduleGroupId;
    private final String energySavingStatus;
    private final int sharedSwitch;
    private final String sharedUniqueId;
    private final String rruShareModeId;
    private final String mocName;
    private final String hardInfoType;
    private final String boardWorkState;
    private final String boardDetailInfo;
    private final String radioMode;
    private final int moId;
    private final String adminState;
    private final String minStarNum;
    private final String boardClass;
    private final String adminStateLTETDD;
    private final String boardPower;
    private final String availStatus;
    private final String isHighPriority;
    private final String boardName;
    private final String associSiteBrdId;
    private final String adminStateNBIoT;
    private final String operState;
    private final String rruGnssCableLength;
    private final int adminStateCDMA;
    private final int adminStateTDS;
}
