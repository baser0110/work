package bsshelper.service.user.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name="profile")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Permission extAlarmMng = Permission.NO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Permission cellStatMngSingle = Permission.NO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Permission cellStatMngBatch = Permission.NO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Permission acceptMeasurement = Permission.NO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Permission userMng = Permission.NO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Permission vasilyTools = Permission.NO;

    public List<GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("EXT_ALARM_MNG_" + extAlarmMng.name()),
                new SimpleGrantedAuthority("CELL_STAT_MNG_SINGLE_" + cellStatMngSingle.name()),
                new SimpleGrantedAuthority("CELL_STAT_MNG_BATCH_" + cellStatMngBatch.name()),
                new SimpleGrantedAuthority("ACCEPT_MEASUREMENT_" + acceptMeasurement.name()),
                new SimpleGrantedAuthority("USER_MNG_" + userMng.name()),
                new SimpleGrantedAuthority("VASILY_TOOLS_" + vasilyTools.name())
        );
    }
}
