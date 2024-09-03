package bsshelper.externalapi.configurationmng.currentmng.entity;

import com.google.gson.Gson;
import lombok.Builder;

import java.util.List;

@Builder
public class CurrentMngBodySettings {
    private final String ManagedElementType;
    private final List<String> neList;
    private final List<String> mocList;
    private final List<MoFilter> moFilter;
    private final List<AttrFilter> attrFilter;

    public static class AttrFilter {
        private final String moc;
        private final List<String> attrNames;

        public AttrFilter(String moc, List<String> attrNames) {
            this.moc = moc;
            this.attrNames = attrNames;
        }
    }

    public static class MoFilter {
        private final String moc;
        private final String filter;

        public MoFilter(String moc, String filter) {
            this.moc = moc;
            this.filter = filter;
        }
    }

    public String getBodySettings() {
        return new Gson().toJson(this);
    }
}
