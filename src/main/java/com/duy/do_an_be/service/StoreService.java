package com.duy.do_an_be.service;

import com.duy.do_an_be.entity.Store;
import com.duy.do_an_be.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public Page<Store> findAll(String search, int page, int limit) {
        if (search != null && !search.isEmpty()) {
            return storeRepository.findByStoreNameContainingIgnoreCase(search, PageRequest.of(page - 1, limit));
        }
        return storeRepository.findAll(PageRequest.of(page - 1, limit));
    }

    public Store findOne(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found"));
    }

    @Transactional
    public Store create(Store store) {
        return storeRepository.save(store);
    }

    @Transactional
    public Store update(Long id, Store storeDto) {
        Store store = findOne(id);
        store.setStoreName(storeDto.getStoreName());
        store.setAddress(storeDto.getAddress());
        store.setLatitude(storeDto.getLatitude());
        store.setLongitude(storeDto.getLongitude());
        store.setActive(storeDto.isActive());
        return storeRepository.save(store);
    }

    @Transactional
    public void remove(Long id) {
        storeRepository.deleteById(id);
    }

    @Transactional
    public Store updateStatus(Long id, boolean isActive) {
        Store store = findOne(id);
        store.setActive(isActive);
        return storeRepository.save(store);
    }

    @Transactional
    public Store toggleStatus(Long id) {
        Store store = findOne(id);
        store.setActive(!store.isActive());
        return storeRepository.save(store);
    }
}
