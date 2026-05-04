package bsshelper.localservice.externalcustomdata.service;

import bsshelper.localservice.externalcustomdata.entity.MECustomLink;
import bsshelper.localservice.externalcustomdata.repository.MECustomLinkRepository;
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
public class MECustomLinkService implements CachePopulator {
    private final MECustomLinkRepository meCustomLinkRepository;

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
    @RefreshCache
    public MECustomLink createMECustomLink(MECustomLink MECustomLink) {
        return meCustomLinkRepository.save(MECustomLink);
    }

    @Transactional
    @RefreshCache
    public void deleteMECustomLink(String id) {
        meCustomLinkRepository.deleteById(id);
    }

    @Transactional
    @RefreshCache
    public MECustomLink updateMECustomLink(MECustomLink MECustomLink) {
        MECustomLink existingMECustomLink = meCustomLinkRepository.findById(MECustomLink.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        existingMECustomLink.setBSCID(MECustomLink.getBSCID());
        existingMECustomLink.setRNCID(MECustomLink.getRNCID());
        existingMECustomLink.setGSMID(MECustomLink.getGSMID());
        existingMECustomLink.setUMTSID(MECustomLink.getUMTSID());
        return meCustomLinkRepository.save(MECustomLink);
    }
}
