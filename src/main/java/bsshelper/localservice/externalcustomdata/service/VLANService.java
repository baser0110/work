package bsshelper.localservice.externalcustomdata.service;

import bsshelper.localservice.externalcustomdata.entity.VLAN;
import bsshelper.localservice.externalcustomdata.repository.VLANRepository;
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
public class VLANService implements CachePopulator {
    private final VLANRepository vlanRepository;

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
    @RefreshCache
    public VLAN createVlan(VLAN vlan) {
        return vlanRepository.save(vlan);
    }

    @Transactional
    @RefreshCache
    public void deleteVlan(String id) {
        vlanRepository.deleteById(id);
    }

    @Transactional
    @RefreshCache
    public VLAN updateVlan(VLAN vlan) {
        VLAN existingVlan = vlanRepository.findById(vlan.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        existingVlan.setTechnology(vlan.getTechnology());
        return vlanRepository.save(vlan);
    }
}
