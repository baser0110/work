package bsshelper.externalapi.alarmmng.activealarm.util;

import com.google.gson.Gson;
import lombok.Builder;

import java.util.List;

@Builder
public class CommentAlarmBodySettings {
    private final List<String> ids;
    private final String commenttext;

    public String getBodySettings() {
        return new Gson().toJson(this);
    }
}
