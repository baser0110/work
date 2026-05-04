package bsshelper.localservice.externalcustomdata.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="vlan")
public class VLAN {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String vlan;

    @Column(nullable = false)
    private String technology;
}
