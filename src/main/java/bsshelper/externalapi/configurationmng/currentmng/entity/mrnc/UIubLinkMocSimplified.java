package bsshelper.externalapi.configurationmng.currentmng.entity.mrnc;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UIubLinkMocSimplified {
    private final String userLabel;
    private final String lastModifiedTime;
    private final String ldn;
}
