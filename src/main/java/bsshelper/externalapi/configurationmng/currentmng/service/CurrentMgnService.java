package bsshelper.externalapi.configurationmng.currentmng.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.configurationmng.currentmng.entity.*;
import bsshelper.externalapi.configurationmng.currentmng.util.CurrentMngBodySettings;
import bsshelper.externalapi.configurationmng.currentmng.util.MocMEType;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;

public interface CurrentMgnService {
    String rawDataQuery (Token token, String userLabel, String mocName, CurrentMngBodySettings bodySettings);
    ManagedElement getManagedElementByNeName(Token token, String userLabel);
    <T> List<T> getMocList(Token token,  String userLabel, MocMEType mocMEType, CurrentMngBodySettings bodySettings);
    <T extends AbstractCellMoc> Map<String, CurrentMgnServiceImpl.CellInfo> getCacheCellsForBatch(Token token, MocMEType moc, String logName);
    Map<String,String> getCacheManagedElementForBatch(Token token);
}
