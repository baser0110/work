//package bsshelper.service.user.repository;
//
//import bsshelper.service.user.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface UserRepository extends JpaRepository<User, String> {
//    User findByUsernameAndIsDeletedFalse(String username);
//    List<User> findByIsDeletedFalse();
//}
