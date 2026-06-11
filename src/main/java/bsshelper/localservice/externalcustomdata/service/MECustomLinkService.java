package bsshelper.localservice.externalcustomdata.service;

import bsshelper.localservice.externalcustomdata.entity.MECustomLink;
import bsshelper.localservice.externalcustomdata.repository.MECustomLinkRepository;
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
public class MECustomLinkService implements CacheUpdater {
    private final MECustomLinkRepository meCustomLinkRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @PostConstruct
    public void populate() {
        Map<String, MECustomLink> MECustomLinkMap = new ConcurrentHashMap<>();

        List<MECustomLink> list = getAllMECustomLink();
        for (MECustomLink link: list) {
            MECustomLinkMap.put(link.getUserLabel(), link);
        }
        CustomDataService.MECustomLinkMap.keySet().retainAll(MECustomLinkMap.keySet());
        CustomDataService.MECustomLinkMap.putAll(MECustomLinkMap);
    }

    @Override
    public boolean supports(Class<?> clazz) { return clazz == MECustomLink.class; }

    public List<MECustomLink> getAllMECustomLink() {
        return meCustomLinkRepository.findAll();
    }

    public Optional<MECustomLink> findByUserLabel(String userLabel) {
        return meCustomLinkRepository.findByUserLabel(userLabel);
    }

    public Optional<MECustomLink> getMECustomLinkById(String id) {
        return meCustomLinkRepository.findById(id);
    }

    @Transactional
    public MECustomLink createMECustomLink(MECustomLink entity) {
        entity.setId(null);
        MECustomLink saved = meCustomLinkRepository.save(entity);
        eventPublisher.publishEvent(new CacheRefreshEvent<>(saved));
        return meCustomLinkRepository.save(saved);
    }

    @Transactional
    public MECustomLink deleteMECustomLink(String id) {
        MECustomLink entity = meCustomLinkRepository.findById(id).orElse(null);
        if (entity != null) {
            meCustomLinkRepository.delete(entity);
            eventPublisher.publishEvent(new CacheRefreshEvent<>(entity));
            return entity;
        }
        return null;
    }

    @Transactional
    public MECustomLink updateMECustomLink(MECustomLink entity) {
        MECustomLink existing = meCustomLinkRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Not found"));
        MECustomLink saved = meCustomLinkRepository.save(entity);
        eventPublisher.publishEvent(new CacheRefreshEvent<>(saved));
        return saved;

    }
}
