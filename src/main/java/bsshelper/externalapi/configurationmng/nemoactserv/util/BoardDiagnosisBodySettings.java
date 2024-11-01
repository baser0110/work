package bsshelper.externalapi.configurationmng.nemoactserv.util;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
public class BoardDiagnosisBodySettings {
    private final String ldn;
    private final Input input;

    @RequiredArgsConstructor
    public static class Input {
        private final List<String> functionId;
    }

    public String getBodySettings() {
        return new Gson().toJson(this);
    }
}
