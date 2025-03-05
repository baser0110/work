package bsshelper.externalapi.perfmng.util;

import com.google.gson.Gson;
import lombok.Builder;

import java.util.List;

@Builder
public class HistoryQueryBodySettings {
    private final String nfctid;
    private final String motid;
    private final int gr;
    private final String me;
    private final List<String> mois;
    private final List<String> items;
    private final String starttime;
    private final String endtime;
    private final String grouplayer;
    private final String filterlayer;
    private final boolean showobjectname;

    public String getBodySettings() {
        return new Gson().toJson(this);
    }
}