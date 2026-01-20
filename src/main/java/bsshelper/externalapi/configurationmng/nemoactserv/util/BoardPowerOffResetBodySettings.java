package bsshelper.externalapi.configurationmng.nemoactserv.util;

import com.google.gson.Gson;
import lombok.Builder;

@Builder
public class BoardPowerOffResetBodySettings {
    private final String ldn;

    public String getBodySettings() {
        return new Gson().toJson(this);
    }
}
