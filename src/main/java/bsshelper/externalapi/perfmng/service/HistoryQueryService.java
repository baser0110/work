package bsshelper.externalapi.perfmng.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.perfmng.entity.*;
import bsshelper.externalapi.perfmng.util.KPI;

import java.util.List;
import java.util.Map;

public interface HistoryQueryService {
    List<HistoryForUMTSCell> getUMTSCellHistory(Token token, ManagedElement managedElement, List<UUtranCellFDDMocSimplified> cells, int time, KPI kpi);
    List<HistoryForULocalCell> getHistoryCell(Token token, ManagedElement managedElement, int time, KPI kpi);
    List<HistoryMaxOpticError> getHistoryOpticError(Token token, ManagedElement managedElement, int time);
    List<HistoryMaxOpticPower> getHistoryOpticTxPower(Token token, ManagedElement managedElement, int time);
    List<HistoryMaxOpticPower> getHistoryOpticRxPower(Token token, ManagedElement managedElement, int time);
    Map<String, List<HistoryOfficeLink>> getOfficeLinkHistory(Token token, ManagedElement managedElement, int time, KPI kpi);

    List<HistoryVSWR> getHistoryVSWR(Token token, ManagedElement managedElement, int time);
}
