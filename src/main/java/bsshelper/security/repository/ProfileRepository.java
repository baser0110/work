package bsshelper.security.repository;

import bsshelper.security.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String>, JpaSpecificationExecutor<Profile> {
    Optional<Profile> findByName(String name);
}
