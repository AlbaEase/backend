package com.example.albaease.store.service;

import com.example.albaease.store.domain.Role;
import com.example.albaease.store.domain.Store;
import com.example.albaease.store.domain.UserStoreRelation;
import com.example.albaease.store.dto.StoreRequestDto;
import com.example.albaease.store.dto.StoreResponseDto;
import com.example.albaease.store.repository.StoreRepository;
import com.example.albaease.store.repository.UserStoreRelationRepository;
import com.example.albaease.user.entity.User;
import com.example.albaease.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final BusinessVerificationService businessVerificationService;
    private final UserRepository userRepository;
    private final UserStoreRelationRepository userStoreRelationRepository;

    /**
     * 매장 생성 (사업자등록번호 진위확인 포함)
     */
    @Transactional
    public Store createStore(StoreRequestDto requestDto) {
        // 사업자등록번호 유효성 확인
        boolean isValid = businessVerificationService.isBusinessNumberValid(requestDto.getBusinessNumber());
        if (!isValid) {
            throw new IllegalArgumentException("유효하지 않은 사업자 등록번호입니다.");
        }

        // 랜덤 매장 코드 생성
        String storeCode = generateRandomStoreCode();

        // Store 객체 생성 및 저장
        Store store = new Store(
                storeCode,
                requestDto.getName(),
                requestDto.getLocation(),
                requestDto.getRequiresApproval(),
                requestDto.getBusinessNumber(),
                requestDto.getOwnerName(),
                requestDto.getStartDate()
        );
        storeRepository.save(store);

        // 사장님과 매장 관계 생성 (OWNER)
        User owner = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사업자(유저)를 찾을 수 없습니다."));


        UserStoreRelation relation = new UserStoreRelation();
        relation.setUser(owner);
        relation.setStore(store);
        relation.setRole(Role.OWNER);
        relation.setWorkStartDate(LocalDateTime.now());

        userStoreRelationRepository.save(relation);

        return store;
    }

    // 매장 코드 생성 (랜덤)
    private String generateRandomStoreCode() {
        Random random = new Random();
        char letter1 = (char) ('A' + random.nextInt(26));
        int number1 = random.nextInt(100);
        char letter2 = (char) ('A' + random.nextInt(26));
        char letter3 = (char) ('A' + random.nextInt(26));
        int number2 = random.nextInt(10);

        return String.format("%c%02d%c%c%d", letter1, number1, letter2, letter3, number2);
    }

    // 전체 매장 조회
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    // 매장 ID로 조회
    public Store getStoreById(int id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 매장을 찾을 수 없습니다."));
    }

    // 매장 수정
    @Transactional
    public Store updateStore(int id, StoreRequestDto requestDto) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 매장을 찾을 수 없습니다."));

        store.update(requestDto);
        return storeRepository.save(store);
    }

    // 매장 삭제
    @Transactional
    public void deleteStore(int id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 매장을 찾을 수 없습니다."));

        storeRepository.delete(store);
    }

    // 알바생 매장 등록
    @Transactional
    public String registerPartTimer(Long userId, String storeCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Store store = storeRepository.findByStoreCode(storeCode)
                .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다."));

        UserStoreRelation relation = new UserStoreRelation();
        relation.setUser(user);
        relation.setStore(store);
        relation.setRole(Role.PARTTIMER);
        relation.setWorkStartDate(LocalDateTime.now());

        userStoreRelationRepository.save(relation);
        return "알바생 등록이 완료되었습니다.";
    }

    // 사장님의 모든 매장 조회
    public List<StoreResponseDto> getStoresByOwner(Long userId) {
        List<Store> stores = storeRepository.findByOwnerId(userId);
        return stores.stream().map(StoreResponseDto::new).collect(Collectors.toList());
    }

    // 알바생의 모든 매장 조회
    public List<StoreResponseDto> getStoresByPartTimer(Long userId) {
        List<Store> stores = storeRepository.findByPartTimerId(userId);
        return stores.stream().map(StoreResponseDto::new).collect(Collectors.toList());
    }

    // 사용자의 모든 매장 조회
    public List<Store> getUserStores(Long userId) {
        List<UserStoreRelation> relations = userStoreRelationRepository.findByUserId(userId);
        return relations.stream().map(UserStoreRelation::getStore).collect(Collectors.toList());
    }
}
