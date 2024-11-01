package bsshelper.externalapi.perfmng.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class InfoCodeGSM {
    private final int code;
    private final String sector;
}
