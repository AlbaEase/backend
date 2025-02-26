package com.example.albaease.store.service;

import com.example.albaease.store.dto.StoreResponseDto;
import com.example.albaease.store.domain.Store;
import com.example.albaease.store.domain.UserStoreRelationship;
import com.example.albaease.store.repository.StoreRepository;
import com.example.albaease.store.repository.UserStoreRelationshipRepository;
import com.example.albaease.store.dto.StoreRequestDto;
import com.example.albaease.store.dto.StoreUpdateRequestDto;
import com.example.albaease.user.entity.User;
import com.example.albaease.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final UserStoreRelationshipRepository userStoreRelationshipRepository;
    private final BusinessNumberValidator businessNumberValidator;

    @Transactional
    public StoreResponseDto createStore(StoreRequestDto request, String loginId) {
        // 사업자등록번호 중복 체크
        if (storeRepository.existsByBusinessNumber(request.getBusinessNumber())) {
            throw new RuntimeException("이미 등록된 사업자등록번호입니다.");
        }

        // 사업자등록번호 검증
        boolean isValidBusinessNumber = businessNumberValidator.validateBusinessNumber(request.getBusinessNumber());

        // 사용자 조회
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 매장 생성
        Store store = Store.builder()
                .businessNumber(request.getBusinessNumber())
                .name(request.getName())
                .location(request.getLocation())
                .ownerName(request.getOwnerName())
                .contactNumber(request.getContactNumber())
                .isVerified(isValidBusinessNumber)
                .build();

        Store savedStore = storeRepository.save(store);

        // 매장과 사용자 관계 생성 (방금 로그인한 사용자를 매장 관리자로 설정)
        UserStoreRelationship relationship = UserStoreRelationship.builder()
                .user(user)
                .store(savedStore)
                .role("ADMIN") // 매장 생성자는 관리자 역할
                .build();

        userStoreRelationshipRepository.save(relationship);

        // DTO로 변환 후 반환
        return StoreResponseDto.builder()
                .id(savedStore.getId())
                .businessNumber(savedStore.getBusinessNumber())
                .name(savedStore.getName())
                .location(savedStore.getLocation())
                .ownerName(savedStore.getOwnerName())
                .contactNumber(savedStore.getContactNumber())
                .isVerified(savedStore.getIsVerified())
                .createdAt(savedStore.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public StoreResponseDto getMyStore(String loginId) {
        // 현재 로그인한 사용자 조회
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자의 매장 조회
        UserStoreRelationship relationship = userStoreRelationshipRepository
                .findByUser_UserIdAndRole(user.getUserId(), "ADMIN")
                .orElseThrow(() -> new RuntimeException("관리 중인 매장이 없습니다."));

        Store store = relationship.getStore();

        return StoreResponseDto.builder()
                .id(store.getId())
                .businessNumber(store.getBusinessNumber())
                .name(store.getName())
                .location(store.getLocation())
                .ownerName(store.getOwnerName())
                .contactNumber(store.getContactNumber())
                .isVerified(store.getIsVerified())
                .createdAt(store.getCreatedAt())
                .build();
    }

    @Transactional
    public StoreResponseDto updateStore(Long storeId, StoreUpdateRequestDto request, String loginId) {
        // 사용자 조회
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 매장 수정 권한 확인
        UserStoreRelationship relationship = userStoreRelationshipRepository
                .findByUser_UserIdAndStore_IdAndRole(user.getUserId(), storeId, "ADMIN")
                .orElseThrow(() -> new RuntimeException("수정 권한이 없습니다."));

        Store store = relationship.getStore();

        // 매장 정보 업데이트
        store.setName(request.getName());
        store.setLocation(request.getLocation());
        store.setContactNumber(request.getContactNumber());

        Store updatedStore = storeRepository.save(store);

        return StoreResponseDto.builder()
                .id(updatedStore.getId())
                .businessNumber(updatedStore.getBusinessNumber())
                .name(updatedStore.getName())
                .location(updatedStore.getLocation())
                .ownerName(updatedStore.getOwnerName())
                .contactNumber(updatedStore.getContactNumber())
                .isVerified(updatedStore.getIsVerified())
                .createdAt(updatedStore.getCreatedAt())
                .build();
    }

    @Transactional
    public void deleteStore(Long storeId, String loginId) {
        // 사용자 조회
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 매장 삭제 권한 확인
        UserStoreRelationship relationship = userStoreRelationshipRepository
                .findByUser_UserIdAndStore_IdAndRole(user.getUserId(), storeId, "ADMIN")
                .orElseThrow(() -> new RuntimeException("삭제 권한이 없습니다."));

        // 매장 삭제
        storeRepository.deleteById(storeId);
    }
}
