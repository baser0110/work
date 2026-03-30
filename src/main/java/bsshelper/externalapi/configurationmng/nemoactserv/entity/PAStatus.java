package bsshelper.externalapi.configurationmng.nemoactserv.entity;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PAStatus {
    private boolean selected;
    private String userLabel;
    private String ldn;
    private String adminState;
}
