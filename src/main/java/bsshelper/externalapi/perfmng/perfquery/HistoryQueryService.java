package bsshelper.externalapi.perfmng.perfquery;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.ManagedElement;
import bsshelper.externalapi.configurationmng.currentmng.entity.mrnc.UUtranCellFDDMocSimplified;
import bsshelper.externalapi.perfmng.entity.HistoryRTWP;
import bsshelper.externalapi.perfmng.entity.HistoryVSWR;

import java.util.List;

public interface HistoryQueryService {
    List<HistoryVSWR> getHistoryVSWR(Token token, ManagedElement managedElement);
    List<HistoryRTWP> getHistoryRTWP(Token token, ManagedElement managedElement, List<UUtranCellFDDMocSimplified> cells);
}
