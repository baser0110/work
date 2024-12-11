package bsshelper.externalapi.alarmmng.activealarm.util;

import bsshelper.externalapi.configurationmng.currentmng.util.CurrentMngBodySettings;
import com.google.gson.Gson;
import lombok.Builder;

import java.util.List;

@Builder
public class ActiveAlarmBodySettings {
    private final Condition condition;

    public static class Condition {
        private final List<String> mes;

        public Condition(List<String> mes) {
            this.mes = mes;
        }
    }

    public String getBodySettings() {
        return new Gson().toJson(this);
    }

}
