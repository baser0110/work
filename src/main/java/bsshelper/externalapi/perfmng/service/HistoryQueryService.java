package bsshelper.externalapi.perfmng.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMoc;
import bsshelper.externalapi.perfmng.entity.*;
import bsshelper.externalapi.perfmng.util.KPI;
import bsshelper.externalapi.perfmng.util.KPIable;

import java.util.List;
import java.util.Map;

public interface HistoryQueryService {
    List<HistoryForUMTSCell> getUMTSCellHistory(Token token, ManagedElement managedElement, List<UUtranCellFDDMoc> cells, int time, int granularity, KPIable kpi);
    List<HistoryForULocalCell> getHistoryCellWithIgnoreRestrictionOnStringCapacity(Token token, ManagedElement managedElement, int time, int granularity, KPIable kpi);
    List<HistoryMaxOpticError> getHistoryOpticError(Token token, ManagedElement managedElement, int time, int granularity);
    List<HistoryMaxOpticPower> getHistoryOpticTxPower(Token token, ManagedElement managedElement, int time, int granularity);
    List<HistoryMaxOpticPower> getHistoryOpticRxPower(Token token, ManagedElement managedElement, int time, int granularity);

    Map<String, List<HistoryOfficeLink>> getOfficeLinkHistory(Token token, ManagedElement managedElement, int time, int granularity, KPI kpi);
    Map<String, List<HistoryOfficeLink>> getFullMRNCOfficeLinkHistory(Token token, String mrnc, int time, KPI kpi, int granularity);
    Map<String, List<HistoryOfficeLink>> getCustomListOfficeLinkHistory(Token token, Map<String, List<String>> mrncIdMap, int time, KPI kpi, int granularity);

    List<HistoryVSWR> getHistoryVSWR(Token token, ManagedElement managedElement, int time, int granularity);
}
