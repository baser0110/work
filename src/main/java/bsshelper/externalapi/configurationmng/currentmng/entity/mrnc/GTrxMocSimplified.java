package bsshelper.externalapi.configurationmng.currentmng.entity.mrnc;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GTrxMocSimplified {
    private final String userLabel;
    private final String lastModifiedTime;
    private final String ldn;
    private final int moId;
}
