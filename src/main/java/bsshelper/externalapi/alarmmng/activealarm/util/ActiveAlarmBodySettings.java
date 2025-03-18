package bsshelper.externalapi.alarmmng.activealarm.util;

import bsshelper.externalapi.configurationmng.currentmng.util.CurrentMngBodySettings;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
public class ActiveAlarmBodySettings {
    private final Condition condition;

    public String getBodySettings() {
        return new Gson().toJson(this);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class Condition {
        List<String> mes;
    }
}
