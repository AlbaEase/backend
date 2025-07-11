package com.example.albaease.store.service;

import com.example.albaease.store.dto.StoreResponseDto;
import com.example.albaease.store.domain.Store;
import com.example.albaease.store.domain.UserStoreRelationship;
import com.example.albaease.store.dto.UserSimpleResponseDto;
import com.example.albaease.store.repository.StoreRepository;
import com.example.albaease.store.repository.UserStoreRelationshipRepository;
import com.example.albaease.store.dto.StoreRequestDto;
import com.example.albaease.store.dto.StoreUpdateRequestDto;
import com.example.albaease.user.entity.Role;
import com.example.albaease.user.entity.User;
import com.example.albaease.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final UserStoreRelationshipRepository userStoreRelationshipRepository;
    private final BusinessNumberValidator businessNumberValidator;

    @Transactional
    public StoreResponseDto createStore(StoreRequestDto request, String loginId) {

        // 사업자등록번호 검증
        boolean isValidBusinessNumber = businessNumberValidator.validateBusinessNumber(request.getBusinessNumber());
        System.out.println("✅ 사업자등록번호 유효성 검사 결과: " + isValidBusinessNumber);

        // ✅ 유효하지 않으면 등록 자체를 막음
        if (!isValidBusinessNumber) {
            throw new IllegalArgumentException("유효하지 않은 사업자등록번호입니다.");
        }

        // 사용자 조회
        User user = userRepository.findByEmail(loginId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 랜덤 매장 코드 생성
        String storeCode = generateRandomStoreCode();

        // 매장 생성
        Store store = Store.builder()
                .name(request.getName())
                .location(request.getLocation())
                .storeCode(storeCode)
                .businessNumber(request.getBusinessNumber())
                .defaultHourlyWage(request.getDefaultHourlyWage()) // 시급 반영
                .build();

        Store savedStore = storeRepository.save(store);
        System.out.println("📌 저장된 Store의 require_approval: " + savedStore.getRequire_approval());

        // 매장과 사용자 관계 생성 (방금 로그인한 사용자를 매장 관리자로 설정)
        UserStoreRelationship relationship = UserStoreRelationship.builder()
                .user(user)
                .store(savedStore)
                .build();

        userStoreRelationshipRepository.save(relationship);

        // DTO로 변환 후 반환
        return StoreResponseDto.builder()
                .id(savedStore.getId())                            // ✅ Store 엔티티의 id
                .storeId(savedStore.getId())                      // ✅ 필요하다면 동일하게 사용
                .storeCode(savedStore.getStoreCode())             // ✅ 매장 코드
                .businessNumber(savedStore.getBusinessNumber())   // ✅ null 문제 해결
                .name(savedStore.getName())
                .location(savedStore.getLocation())
                .requireApproval(savedStore.getRequire_approval())// ✅ null 문제 해결
                .createdAt(savedStore.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<StoreResponseDto> getMyStore(String loginId) {
        // 현재 로그인한 사용자 조회
        User user = userRepository.findByEmail(loginId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자의 매장 목록 조회
        List<UserStoreRelationship> relationships = userStoreRelationshipRepository
                .findByUser_UserId(user.getUserId());

        if (relationships.isEmpty()) {
            throw new RuntimeException("관리 중인 매장이 없습니다.");
        }


        // StoreResponseDto 리스트로 변환
        return relationships.stream()
                .map(relationship -> {
                    Store store = relationship.getStore();
                    return StoreResponseDto.builder()
                            .storeId(store.getId())
                            .storeCode(store.getStoreCode())
                            .businessNumber(store.getBusinessNumber())
                            .name(store.getName())
                            .location(store.getLocation())
                            .requireApproval(store.getRequire_approval())
                            .defaultHourlyWage(store.getDefaultHourlyWage())//
                            .createdAt(store.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public StoreResponseDto updateStore(Long storeId, StoreUpdateRequestDto request, String loginId) {
        // 사용자 조회
        User user = userRepository.findByEmail(loginId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 매장 수정 권한 확인 (이 로직은 사용자의 권한에 따라 달라질 수 있음)
        UserStoreRelationship relationship = userStoreRelationshipRepository
                .findByUser_UserIdAndStore_Id(user.getUserId(), storeId)
                .orElseThrow(() -> new RuntimeException("수정 권한이 없습니다."));

        Store store = relationship.getStore();

        // 매장 정보 업데이트
        store.setName(request.getName());
        store.setLocation(request.getLocation());

        Store updatedStore = storeRepository.save(store);

        return StoreResponseDto.builder()
                .storeCode(updatedStore.getStoreCode())
                .name(updatedStore.getName())
                .location(updatedStore.getLocation())
                .createdAt(updatedStore.getCreatedAt())
                .build();
    }

    @Transactional
    public void deleteStore(Long storeId, String loginId) {
        // 사용자 조회
        User user = userRepository.findByEmail(loginId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 매장 삭제 권한 확인 (이 로직은 사용자의 권한에 따라 달라질 수 있음)
        UserStoreRelationship relationship = userStoreRelationshipRepository
                .findByUser_UserIdAndStore_Id(user.getUserId(), storeId)
                .orElseThrow(() -> new RuntimeException("삭제 권한이 없습니다."));

        // 매장 삭제
        storeRepository.deleteById(storeId);
    }

    // 랜덤 매장 코드 생성
    private String generateRandomStoreCode() {
        Random random = new Random();
        char letter1 = (char) ('A' + random.nextInt(26));
        int number1 = random.nextInt(100);
        char letter2 = (char) ('A' + random.nextInt(26));
        char letter3 = (char) ('A' + random.nextInt(26));
        int number2 = random.nextInt(10);

        return String.format("%c%02d%c%c%d", letter1, number1, letter2, letter3, number2);
    }

    @Transactional
    public void joinStore(String storeCode, String loginId) {
        // 사용자 찾기
        User user = userRepository.findByEmail(loginId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 매장 찾기
        Store store = storeRepository.findByStoreCode(storeCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 매장 코드의 매장을 찾을 수 없습니다."));

        // 이미 등록된 관계인지 확인 (중복 등록 방지)
        boolean exists = userStoreRelationshipRepository
                .existsByUser_UserIdAndStore_Id(user.getUserId(), store.getId());

        if (exists) {
            throw new IllegalArgumentException("이미 등록된 매장입니다.");
        }

        // 관계 저장
        UserStoreRelationship relationship = UserStoreRelationship.builder()
                .user(user)
                .store(store)
                .build();

        userStoreRelationshipRepository.save(relationship);
    }

    // 해당 매장 알바생 삭제(관계 삭제)
    @Transactional
    public void removeWorker(Long storeId, Long targetUserId, String requesterLoginId) {
        // 요청자 정보 조회
        User requester = userRepository.findByEmail(requesterLoginId)
                .orElseThrow(() -> new RuntimeException("요청한 사용자를 찾을 수 없습니다."));

        // 요청자가 해당 storeId의 OWNER인지 확인 (store + user 쌍으로 관계가 있어야 함)
      userStoreRelationshipRepository.findByUser_UserIdAndStore_Id(requester.getUserId(), storeId)
                .orElseThrow(() -> new RuntimeException("매장에 대한 권한이 없습니다."));

        if (requester.getRole() != Role.OWNER) {
            throw new RuntimeException("알바생을 삭제할 권한이 없습니다.");
        }


        UserStoreRelationship relationship = userStoreRelationshipRepository
                .findByUser_UserIdAndStore_Id(targetUserId, storeId)
                .orElseThrow(() -> new RuntimeException("알바생 관계를 찾을 수 없습니다."));

        userStoreRelationshipRepository.delete(relationship);
    }

    //해당 매장 알바생 조회
    @Transactional(readOnly = true)
    public List<UserSimpleResponseDto> getWorkersByStore(Long storeId, String requesterLoginId) {
        // 요청자 정보 조회
        User requester = userRepository.findByEmail(requesterLoginId)
                .orElseThrow(() -> new RuntimeException("요청한 사용자를 찾을 수 없습니다."));

        // 요청자가 해당 storeId의 사장인지 검증
        userStoreRelationshipRepository.findByUser_UserIdAndStore_Id(requester.getUserId(), storeId)
                .orElseThrow(() -> new RuntimeException("해당 매장에 대한 접근 권한이 없습니다."));

        if (requester.getRole() != Role.OWNER) {
            throw new RuntimeException("사장님만 알바생 목록을 조회할 수 있습니다.");
        }

        // 해당 매장의 모든 알바생 관계 가져오기
        List<UserStoreRelationship> relationships = userStoreRelationshipRepository.findByStore_Id(storeId);

        return relationships.stream()
                .filter(rel -> rel.getUser().getRole() == Role.WORKER)
                .map(rel -> {
                    User user = rel.getUser();
                    String fullName = user.getLastName() + user.getFirstName();
                    return new UserSimpleResponseDto(user.getUserId(), fullName);
                })
                .collect(Collectors.toList());
    }
}
