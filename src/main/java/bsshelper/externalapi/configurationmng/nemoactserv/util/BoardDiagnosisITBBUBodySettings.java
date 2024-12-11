package bsshelper.externalapi.configurationmng.nemoactserv.util;

import com.google.gson.Gson;
import lombok.Builder;

@Builder
public class BoardDiagnosisITBBUBodySettings {
    private final String ldn;

    public String getBodySettings() {
        return new Gson().toJson(this);
    }
}
