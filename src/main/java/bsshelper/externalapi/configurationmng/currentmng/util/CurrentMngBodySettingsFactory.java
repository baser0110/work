package bsshelper.externalapi.configurationmng.currentmng.util;

import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.GGsmCellMoc;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UIubLinkMoc;
import bsshelper.globalutil.ManagedElementType;
import bsshelper.globalutil.SubnetworkToBSCOrRNC;


import java.util.List;

public class CurrentMngBodySettingsFactory {

    public static CurrentMngBodySettings queryMocByNE(ManagedElement managedElement, String mocName) {
        return CurrentMngBodySettings.builder()
                .ManagedElementType(managedElement.getManagedElementType().toString())
                .neList(List.of(managedElement.getNe()))
                .mocList(List.of(mocName))
                .build();
    }

    public static CurrentMngBodySettings queryMocFull(String mocName, List<String> filterList, ManagedElementType type) {
        return CurrentMngBodySettings.builder()
                .ManagedElementType(type.toString())
                .mocList(List.of(mocName))
                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter(mocName,
                        filterList)))
                .build();
    }

    public static CurrentMngBodySettings queryManagedElementByUserLabel(String userLabel, ManagedElementType type) {
        return CurrentMngBodySettings.builder()
                .ManagedElementType(type.toString())
                .mocList(List.of("ManagedElement"))
                .moFilter(List.of(
                        new CurrentMngBodySettings.MoFilter("ManagedElement", "userLabel='" + userLabel.trim().toUpperCase() + "'")))
                .build();
    }

    public static CurrentMngBodySettings queryGGsmCellMocByOMMBManagedElement(ManagedElement managedElement) {
        return CurrentMngBodySettings.builder()
                .ManagedElementType(ManagedElementType.MRNC.toString())
                .mocList(List.of("GGsmCell"))
//                .attrFilter(List.of(
//                        new CurrentMngBodySettings.AttrFilter("GGsmCell", List.of("userLabel", "bcchFrequency", "cellIdentity", "moId"))))
                .moFilter(List.of(
                        new CurrentMngBodySettings.MoFilter("GGsmCell", getGGsmCellFilter(managedElement))))
                .build();
    }

    public static CurrentMngBodySettings queryGTrxMocByOMMBManagedElement(ManagedElement managedElement, List<GGsmCellMoc> cells) {
        return CurrentMngBodySettings.builder()
                .ManagedElementType(ManagedElementType.MRNC.toString())
                .mocList(List.of("GTrx"))
//                .attrFilter(List.of(
//                        new CurrentMngBodySettings.AttrFilter("GTrx", List.of("userLabel", "moId"))))
                .moFilter(List.of(
                        new CurrentMngBodySettings.MoFilter("GTrx", getGTrxFilter(managedElement, cells))))
                .build();
    }

    public static CurrentMngBodySettings queryUIubLinkMocByOMMBManagedElement(ManagedElement managedElement) {
        return CurrentMngBodySettings.builder()
                .ManagedElementType(ManagedElementType.MRNC.toString())
                .mocList(List.of("UIubLink"))
//                .attrFilter(List.of(
//                        new CurrentMngBodySettings.AttrFilter("UIubLink", List.of("userLabel"))))
                .moFilter(List.of(
                        new CurrentMngBodySettings.MoFilter("UIubLink", "userLabel='" + managedElement.getUserLabel() + "'")))
                .build();
    }

    public static CurrentMngBodySettings queryUUtranCellFDDMocByUIubLinkMoc(List<UIubLinkMoc> uIubLinkMocList) {
        if (uIubLinkMocList == null || uIubLinkMocList.isEmpty()) return null;

        return CurrentMngBodySettings.builder()
                .ManagedElementType(ManagedElementType.MRNC.toString())
                .mocList(List.of("UUtranCellFDD"))
//                .attrFilter(List.of(new CurrentMngBodySettings.AttrFilter("UUtranCellFDD",
//                        List.of("userLabel", "maximumTransmissionPower", "primaryScramblingCode", "moId", "cellRadius"))))
                .moFilter(List.of(new CurrentMngBodySettings.MoFilter("UUtranCellFDD",
                        "refUIubLink='" + uIubLinkMocList.get(0).getLdn() + "'")))
                .build();
    }

    private static String getGGsmCellFilter(ManagedElement managedElement) {
        String result = "";
        String bsc = managedElement.getBSC();
        String ldn = "ldn='" + "GBssFunction=" + bsc +
                ",GBtsSiteManager=" + managedElement.getBTSManagedElementNum() + ",GGsmCell=";
        for (int i = 1; i < 10; i++) {
            result += ldn + i + "' or ";
        }
        return result.substring(0, result.length() - 4);
    }

    private static String getGTrxFilter(ManagedElement managedElement, List<GGsmCellMoc> cells) {
        String result = "";
        String userLabel = "userLabel='";
        if (cells == null) return "";
        for (GGsmCellMoc cell : cells) {
            result += userLabel + cell.getUserLabel() + "' or ";
        }
        return result.substring(0, result.length() - 4);
    }
}
