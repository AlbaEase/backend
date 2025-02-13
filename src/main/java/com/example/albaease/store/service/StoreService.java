package com.example.albaease.store.service;

import com.example.albaease.store.domain.Store;
import com.example.albaease.store.dto.StoreRequestDto;
import com.example.albaease.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public Store createStore(StoreRequestDto requestDto) {
        Store store = new Store(
                requestDto.getStoreCode(),
                requestDto.getName(),
                requestDto.getLocation(),
                requestDto.getRequiresApproval()
        );
        return storeRepository.save(store);
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public Store getStoreById(int id) {
        return storeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("매장을 찾을 수 없습니다.")
        );
    }
}
