package com.example.albaease.store.service;

import com.example.albaease.store.domain.Store;
import com.example.albaease.store.dto.StoreRequestDto;
import com.example.albaease.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    //매장 생성 (랜덤코드 자동 생성)
    @Transactional
    public Store createStore(StoreRequestDto requestDto) {
        Store store = new Store(
                requestDto.getStoreCode(),
                requestDto.getName(),
                requestDto.getLocation(),
                requestDto.getRequiresApproval()
        );
        return storeRepository.save(store);
    }



    //모든 매장 조회
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    // ID로 매장 조회
    public Store getStoreById(int id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 매장을 찾을 수 없습니다."));
    }

    // 매장 정보 수정 (PUT 요청 처리)
    public Store updateStore(int id, StoreRequestDto requestDto) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 매장을 찾을 수 없습니다."));

        // 필드 업데이트
        store.update(requestDto);

        return storeRepository.save(store);
    }

    // 매장 삭제 (DELETE 요청 처리)
    public void deleteStore(int id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 매장을 찾을 수 없습니다."));

        storeRepository.delete(store);
    }
}
