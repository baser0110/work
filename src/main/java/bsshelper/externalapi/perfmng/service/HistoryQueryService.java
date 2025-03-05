package bsshelper.externalapi.perfmng.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.perfmng.entity.HistoryForULocalCell;
import bsshelper.externalapi.perfmng.entity.HistoryForUMTSCell;
import bsshelper.externalapi.perfmng.entity.HistoryMaxOpticError;
import bsshelper.externalapi.perfmng.entity.HistoryVSWR;
import bsshelper.externalapi.perfmng.util.KPI;

import java.util.List;

public interface HistoryQueryService {
    List<HistoryForUMTSCell> getUMTSCellHistory(Token token, ManagedElement managedElement, List<UUtranCellFDDMocSimplified> cells, int time, KPI kpi);
    List<HistoryForULocalCell> getHistoryCell(Token token, ManagedElement managedElement, int time, KPI kpi);
    List<HistoryMaxOpticError> getHistoryOptic(Token token, ManagedElement managedElement, int time);
    List<HistoryVSWR> getHistoryVSWR(Token token, ManagedElement managedElement, int time);
}
