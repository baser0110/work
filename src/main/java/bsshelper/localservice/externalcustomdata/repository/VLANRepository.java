package bsshelper.localservice.externalcustomdata.repository;

import bsshelper.localservice.externalcustomdata.entity.MECustomLink;
import bsshelper.localservice.externalcustomdata.entity.VLAN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface VLANRepository extends JpaRepository<VLAN, String>, JpaSpecificationExecutor<VLAN> {
    Optional<VLAN> findByVlan(String vlan);
}
