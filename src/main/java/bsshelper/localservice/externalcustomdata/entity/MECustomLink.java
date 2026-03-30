package bsshelper.localservice.externalcustomdata.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MECustomLink {
    private final String UserLabel;
    private final String BSCID;
    private final String RNCID;
    private final String GSMID;
    private final String UMTSID;
}
