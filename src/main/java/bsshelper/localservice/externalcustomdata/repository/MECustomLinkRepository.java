package bsshelper.localservice.externalcustomdata.repository;

import bsshelper.localservice.externalcustomdata.entity.MECustomLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MECustomLinkRepository extends JpaRepository<MECustomLink, String>, JpaSpecificationExecutor<MECustomLink> {
    Optional<MECustomLink> findByUserLabel(String userLabel);
}
