package bsshelper.externalapi.inventorymng.util;

import com.google.gson.Gson;
import lombok.Builder;

import java.util.List;

@Builder
public class InventoryMngBodySettings {
    private final String subnetid;
    private final String neid;
    private final String netype;

    public String getBodySettings() {
        return new Gson().toJson(List.of(this));
    }
}
