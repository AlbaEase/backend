package com.example.albaease.store.service;

import com.example.albaease.store.domain.Store;
import com.example.albaease.store.dto.StoreRequestDto;
import com.example.albaease.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final BusinessNumberValidator businessNumberValidator; // 수정: BusinessNumberValidator 주입

    /**
     * 📌 매장 생성 (랜덤 코드 생성 + 사업자번호 검증)
     */
    @Transactional
    public Store createStore(StoreRequestDto requestDto) {
        // ✅ 사업자 등록번호 검증
        if (!businessNumberValidator.validateBusinessNumber(requestDto.getBusinessNumber())) { // 수정: 사업자 번호 검증
            throw new IllegalArgumentException("유효하지 않은 사업자 등록번호입니다.");
        }

        // ✅ 랜덤 매장 코드 생성
        String storeCode = generateRandomStoreCode();

        // ✅ Store 엔티티 생성 및 저장
        Store store = new Store(
                storeCode,
                requestDto.getName(),
                requestDto.getLocation(),
                requestDto.getRequiresApproval()
        );
        return storeRepository.save(store);
    }

    /**
     * 📌 랜덤 매장 코드 생성
     */
    private String generateRandomStoreCode() {
        Random random = new Random();
        char letter1 = (char) ('A' + random.nextInt(26));
        int number1 = random.nextInt(100);
        char letter2 = (char) ('A' + random.nextInt(26));
        char letter3 = (char) ('A' + random.nextInt(26));
        int number2 = random.nextInt(10);

        return String.format("%c%02d%c%c%d", letter1, number1, letter2, letter3, number2);
    }

    /**
     * 📌 모든 매장 조회
     */
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    /**
     * 📌 ID로 매장 조회
     */
    public Store getStoreById(int id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 매장을 찾을 수 없습니다."));
    }

    /**
     * 📌 매장 정보 수정
     */
    @Transactional
    public Store updateStore(int id, StoreRequestDto requestDto) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 매장을 찾을 수 없습니다."));

        store.update(requestDto);
        return storeRepository.save(store);
    }

    /**
     * 📌 매장 삭제
     */
    @Transactional
    public void deleteStore(int id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 매장을 찾을 수 없습니다."));

        storeRepository.delete(store);
    }
}
