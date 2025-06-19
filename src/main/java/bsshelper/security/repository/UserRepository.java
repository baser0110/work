package bsshelper.security.repository;

import bsshelper.security.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsernameAndIsDeletedFalse(String username);
    List<User> findByIsDeletedFalse();
    Optional<User> findByUsername(String username);

    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
