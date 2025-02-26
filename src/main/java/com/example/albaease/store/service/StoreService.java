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
    private final BusinessNumberValidator businessNumberValidator; // 수정: BusinessNumberValidator 주입
    private final UserRepository userRepository;
    private final UserStoreRelationRepository userStoreRelationRepository;
     //매장 생성 (랜덤 코드 생성 + 사업자번호 검증)
    @Transactional
    public Store createStore(StoreRequestDto requestDto) {
        //사업자 등록번호 검증
        /*if (!businessNumberValidator.validateBusinessNumber(requestDto.getBusinessNumber())) { // 수정: 사업자 번호 검증
            throw new IllegalArgumentException("유효하지 않은 사업자 등록번호입니다.");
        }*/

        //랜덤 매장 코드 생성
        String storeCode = generateRandomStoreCode();

        //Store 엔티티 생성 및 저장
        Store store = new Store(
                storeCode,
                requestDto.getName(),
                requestDto.getLocation(),
                requestDto.getRequiresApproval()
        );
        return storeRepository.save(store);
    }

    //랜덤 매장 코드 생성
    private String generateRandomStoreCode() {
        Random random = new Random();
        char letter1 = (char) ('A' + random.nextInt(26));
        int number1 = random.nextInt(100);
        char letter2 = (char) ('A' + random.nextInt(26));
        char letter3 = (char) ('A' + random.nextInt(26));
        int number2 = random.nextInt(10);

        return String.format("%c%02d%c%c%d", letter1, number1, letter2, letter3, number2);
    }

    //모든 매장 조회
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    //ID로 매장 조회
    public Store getStoreById(int id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 매장을 찾을 수 없습니다."));
    }

    //매장 정보 수정
    @Transactional
    public Store updateStore(int id, StoreRequestDto requestDto) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 매장을 찾을 수 없습니다."));

        store.update(requestDto);
        return storeRepository.save(store);
    }

    //매장삭제
    @Transactional
    public void deleteStore(int id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 매장을 찾을 수 없습니다."));

        storeRepository.delete(store);
    }

    // 알바생 매장 코드 등록 기능
    @Transactional
    public String registerPartTimer(Long userId, String storeCode) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 매장 조회
        Store store = storeRepository.findByStoreCode(storeCode)
                .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다."));
//
//        // 이미 등록된 경우 중복 방지
//        boolean alreadyRegistered = userStoreRelationRepository.existsByUserAndStore(user, store);
//        if (alreadyRegistered) {
//            return "이미 등록된 매장입니다.";
//        }

        // 중간 테이블에 저장
        UserStoreRelation userStoreRelation = new UserStoreRelation();
        userStoreRelation.setUser(user);
        userStoreRelation.setStore(store);
        userStoreRelation.setRole(Role.PART_TIMER);
        userStoreRelation.setWorkStartDate(LocalDateTime.now());

        userStoreRelationRepository.save(userStoreRelation);
        return "알바생 등록이 완료되었습니다.";
    }

    // 사장님의 모든 매장 조회 기능
    public List<StoreResponseDto> getStoresByOwner(Long userId) {
        List<Store> stores = storeRepository.findByOwnerId(userId);
        return stores.stream().map(StoreResponseDto::new).collect(Collectors.toList());
    }

    // 알바생의 모든 매장 조회 기능
    public List<StoreResponseDto> getStoresByPartTimer(Long userId) {
        List<Store> stores = storeRepository.findByPartTimerId(userId);
        return stores.stream().map(StoreResponseDto::new).collect(Collectors.toList());
    }

    // 사용자의 모든 매장 조회 기능
    public List<Store> getUserStores(Long userId) {
        List<UserStoreRelation> relationships = userStoreRelationRepository.findByUserId(userId);
        return relationships.stream().map(UserStoreRelation::getStore).collect(Collectors.toList());
    }



}
