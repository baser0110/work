package bsshelper.localservice.externalcustomdata.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name="alarm_user_label")
public class AlarmUserLabel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String userLabel;

    @PrePersist
    private void prePersist() {
        if (this.id != null && this.id.trim().isEmpty()) {
            this.id = null;
        }
    }
}