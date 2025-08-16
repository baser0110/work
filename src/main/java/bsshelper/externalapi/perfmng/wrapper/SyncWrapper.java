package bsshelper.externalapi.perfmng.wrapper;

import bsshelper.externalapi.configurationmng.nemoactserv.entity.SyncFinal;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class SyncWrapper {
    private final List<SyncFinal> dataSync;
}
