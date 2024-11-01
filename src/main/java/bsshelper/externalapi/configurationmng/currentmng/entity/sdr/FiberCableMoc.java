package bsshelper.externalapi.configurationmng.currentmng.entity.sdr;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FiberCableMoc {
    private final String ref1FiberDevice;
    private final String ref2FiberDevice;
    private final String userLabel;
    private final String lastModifiedTime;
    private final String ldn;
    private final String mocName;
    private final int moId;
}
