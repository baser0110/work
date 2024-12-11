//package bsshelper.service.user.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Data
//@Table(uniqueConstraints = {
//        @UniqueConstraint(columnNames = {"user_id","tool_id"})
//})
//public class Role {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private String id;
//
//    @Enumerated(EnumType.STRING)
//    private Permission permission;
//
//    @ManyToOne
//    @JoinColumn(name = "tool_id", nullable = false)
//    private Tool tool;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//}
