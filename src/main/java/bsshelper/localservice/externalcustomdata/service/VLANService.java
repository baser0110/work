package bsshelper.localservice.externalcustomdata.service;

import bsshelper.localservice.externalcustomdata.entity.VLAN;
import bsshelper.localservice.externalcustomdata.repository.VLANRepository;
import bsshelper.localservice.externalcustomdata.service.listener.CacheRefreshEvent;
import bsshelper.localservice.externalcustomdata.service.listener.CacheUpdater;
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
public class VLANService implements CacheUpdater {
    private final VLANRepository vlanRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @PostConstruct
    public void populate() {
        Map<String, String> VLANMap = new ConcurrentHashMap<>();

        List<VLAN> list = getAllVlan();
        for (VLAN vlan: list) {
            VLANMap.put(vlan.getVlan(), vlan.getTechnology());
        }

        CustomDataService.VLANMap.keySet().retainAll(VLANMap.keySet());
        CustomDataService.VLANMap.putAll(VLANMap);
    }

    @Override
    public boolean supports(Class<?> clazz) { return clazz == VLAN.class; }


    public List<VLAN> getAllVlan() {
        return vlanRepository.findAll();
    }

    public Optional<VLAN> findByVlan(String vlan) {
        return vlanRepository.findByVlan(vlan);
    }

    public Optional<VLAN> getVlanById(String id) {
        return vlanRepository.findById(id);
    }

    @Transactional
    public VLAN createVlan(VLAN entity) {
        entity.setId(null);
        VLAN saved = vlanRepository.save(entity);
        eventPublisher.publishEvent(new CacheRefreshEvent<>(saved));
        return vlanRepository.save(saved);
    }

    @Transactional
    public VLAN deleteVlan(String id) {
        VLAN entity = vlanRepository.findById(id).orElse(null);
        if (entity != null) {
            vlanRepository.delete(entity);
            eventPublisher.publishEvent(new CacheRefreshEvent<>(entity));
            return entity;
        }
        return null;
    }

    @Transactional
    public VLAN updateVlan(VLAN entity) {
        VLAN existing = vlanRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Not found"));
        VLAN saved = vlanRepository.save(entity);
        eventPublisher.publishEvent(new CacheRefreshEvent<>(saved));
        return saved;
    }
}
