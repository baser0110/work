package bsshelper.externalapi.configurationmng.plannedserv.entity;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class PlannedServBodySettings {
    private final String ManagedElementType;
    private final String ne;
    private final List<MocData> moData;

    public String getBodySettings() {
        System.out.println(new Gson().toJson(this));
        return new Gson().toJson(this);
    }

}
