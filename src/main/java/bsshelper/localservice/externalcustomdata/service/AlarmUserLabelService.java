package bsshelper.localservice.externalcustomdata.service;

import bsshelper.localservice.externalcustomdata.entity.AlarmUserLabel;
import bsshelper.localservice.externalcustomdata.repository.AlarmUserLabelRepository;
import bsshelper.localservice.externalcustomdata.service.listener.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class AlarmUserLabelService implements CacheUpdater {
    private final AlarmUserLabelRepository alarmUserLabelRepository;
    private final ApplicationEventPublisher eventPublisher;

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

    @Override
    public boolean supports(Class<?> clazz) { return clazz == AlarmUserLabel.class; }

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
    public AlarmUserLabel createAlarmUserLabel(AlarmUserLabel entity) {
        entity.setId(null);
        AlarmUserLabel saved = alarmUserLabelRepository.save(entity);
        eventPublisher.publishEvent(new CacheRefreshEvent<>(saved));
        return saved;
    }

    @Transactional
    public AlarmUserLabel deleteAlarmUserLabel(String id) {
        AlarmUserLabel entity = alarmUserLabelRepository.findById(id).orElse(null);
        if (entity != null) {
            alarmUserLabelRepository.delete(entity);
            eventPublisher.publishEvent(new CacheRefreshEvent<>(entity));
            return entity;
        }
        return null;
    }

    @Transactional
    public AlarmUserLabel updateAlarmUserLabel(AlarmUserLabel entity) {
        AlarmUserLabel existing = alarmUserLabelRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Not found"));
        AlarmUserLabel saved = alarmUserLabelRepository.save(entity);
        eventPublisher.publishEvent(new CacheRefreshEvent<>(saved));
        return saved;
    }

    public Optional<AlarmUserLabel> findByCode(String code) {
        return alarmUserLabelRepository.findByCode(code);

    }
}
