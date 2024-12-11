package bsshelper.externalapi.configurationmng.currentmng.entity.itbbu;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RiCableMoc {
    private String userLabel;
    private String lastModifiedTime;
    private String channelNo;
    private String mocName;
    private String ldn;
    private String refUpRiPort;
    private String refDownRiPort;
    private int moId;
}