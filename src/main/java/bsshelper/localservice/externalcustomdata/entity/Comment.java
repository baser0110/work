package bsshelper.localservice.externalcustomdata.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String comment;
}
