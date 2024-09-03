package bsshelper.externalapi.configurationmng.currentmng.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ManagedElementMoc{
    private final String userLabel;
    private final String lastModifiedTime;
    private final String mocName;
    private final String ldn;
}
