package bsshelper.localservice.externalcustomdata.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

//@Data
//@RequiredArgsConstructor
//public class MECustomLink {
//    private final String userLabel;
//    private final String BSCID;
//    private final String RNCID;
//    private final String GSMID;
//    private final String UMTSID;
//}

@Entity
@Data
@Table(name="me_custom_link")
public class MECustomLink {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String userLabel;

    @Column(nullable = false)
    private String BSCID;

    @Column(nullable = false)
    private String RNCID;

    @Column(nullable = false, unique = true)
    private String GSMID;

    @Column(nullable = false, unique = true)
    private String UMTSID;
}
