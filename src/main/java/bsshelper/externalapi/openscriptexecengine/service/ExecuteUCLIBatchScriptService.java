package bsshelper.externalapi.openscriptexecengine.service;

import bsshelper.externalapi.auth.entity.Token;
import bsshelper.externalapi.openscriptexecengine.util.StringFileEntity;

public interface ExecuteUCLIBatchScriptService {
    String uploadParamFile(StringFileEntity file, Token token);
    String executeBatch(String filePath, Token token);
    int queryExecStatus(String execId, Token token);
}
