package bsshelper.externalapi.configurationmng.currentmng.entity.sdr;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SDRGTrxMoc {
    private final String userLabel;
    private final String lastModifiedTime;
    private final String trxNo;
    private final String operatingMode;
    private final String mocName;
    private final String isExtendTrx;
    private final String branchBand;
    private final String moId;
    private final String enableIRC;
    private final String isBcch;
    private final String mcumTxSel2RUMode;
    private final String mcumTxSelMode;
    private final String ldn;
    private final String mainBand;
    private final String multiCarrierGroupNo;
    private final String mcumRxSelMode;
}