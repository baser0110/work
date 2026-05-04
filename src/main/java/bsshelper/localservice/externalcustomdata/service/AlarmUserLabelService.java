package bsshelper.localservice.externalcustomdata.service;

import bsshelper.localservice.externalcustomdata.entity.AlarmUserLabel;
import bsshelper.localservice.externalcustomdata.repository.AlarmUserLabelRepository;
import bsshelper.localservice.externalcustomdata.service.aop.CachePopulator;
import bsshelper.localservice.externalcustomdata.service.aop.RefreshCache;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class AlarmUserLabelService implements CachePopulator {
    private final AlarmUserLabelRepository alarmUserLabelRepository;

    @Override
    @PostConstruct
    public void populate() {
        Map<String, AlarmUserLabel> alarmUserLabelToAlarmUserLabelMap = new ConcurrentHashMap<>();
        Map<String, AlarmUserLabel> alarmCodeToAlarmUserLabelMap = new ConcurrentHashMap<>();

        List<AlarmUserLabel> list = getAllAlarmUserLabel();
        for (AlarmUserLabel alm: list) {
            alarmUserLabelToAlarmUserLabelMap.put(alm.getUserLabel(), alm);
            alarmCodeToAlarmUserLabelMap.put(alm.getCode(), alm);
        }
        CustomDataService.alarmUserLabelToAlarmUserLabelMap.keySet().retainAll(alarmUserLabelToAlarmUserLabelMap.keySet());
        CustomDataService.alarmUserLabelToAlarmUserLabelMap.putAll(alarmUserLabelToAlarmUserLabelMap);
        CustomDataService.alarmCodeToAlarmUserLabelMap.keySet().retainAll(alarmCodeToAlarmUserLabelMap.keySet());
        CustomDataService.alarmCodeToAlarmUserLabelMap.putAll(alarmCodeToAlarmUserLabelMap);
    }

    public List<AlarmUserLabel> getAllAlarmUserLabel() {
        return alarmUserLabelRepository.findAll();
    }

    public Optional<AlarmUserLabel> findByUserLabel(String userLabel) {
        return alarmUserLabelRepository.findByUserLabel(userLabel);
    }

    public Optional<AlarmUserLabel> getAlarmUserLabelById(String id) {
        return alarmUserLabelRepository.findById(id);
    }

    @Transactional
    @RefreshCache
    public AlarmUserLabel createAlarmUserLabel(AlarmUserLabel alarmUserLabel) {
        return alarmUserLabelRepository.save(alarmUserLabel);
    }

    @Transactional
    @RefreshCache
    public void deleteAlarmUserLabel(String id) {
        alarmUserLabelRepository.deleteById(id);
    }

    @Transactional
    @RefreshCache
    public AlarmUserLabel updateAlarmUserLabel(AlarmUserLabel alarmUserLabel) {
        AlarmUserLabel existingAlarmUserLabel = alarmUserLabelRepository.findById(alarmUserLabel.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        existingAlarmUserLabel.setUserLabel(alarmUserLabel.getUserLabel());
        return alarmUserLabelRepository.save(alarmUserLabel);
    }

    public Optional<AlarmUserLabel> findByCode(String code) {
        return alarmUserLabelRepository.findByCode(code);
    }
}
