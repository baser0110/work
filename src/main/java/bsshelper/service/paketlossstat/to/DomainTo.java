package bsshelper.service.paketlossstat.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainTo {
    private String region;
    private String siteName;
//    private String id;
//    private String mrnc2g;
//    private String mrnc3g;
    private String cluster;
    private String domain;
}
