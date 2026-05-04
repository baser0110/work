package bsshelper.localservice.externalcustomdata.repository;

import bsshelper.localservice.externalcustomdata.entity.AlarmUserLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AlarmUserLabelRepository extends JpaRepository<AlarmUserLabel, String>, JpaSpecificationExecutor<AlarmUserLabel> {
    Optional<AlarmUserLabel> findByUserLabel(String userLabel);

    Optional<AlarmUserLabel> findByCode(String code);
}
