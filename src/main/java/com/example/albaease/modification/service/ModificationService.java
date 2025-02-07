package com.example.albaease.modification.service;

import com.example.albaease.modification.domain.entity.Modification;
import com.example.albaease.modification.domain.enums.ModificationStatus;
import com.example.albaease.modification.dto.ModificationRequest;
import com.example.albaease.modification.dto.ModificationResponse;
import com.example.albaease.modification.repository.ModificationRepository;
import com.example.albaease.notification.domain.enums.NotificationType;
import com.example.albaease.notification.service.NotificationService;
import com.example.albaease.notification.dto.NotificationRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ModificationService {
    private final ModificationRepository modificationRepository;
    private final NotificationService notificationService;  // 알림 전송을 위한 생성자

    @Transactional
    public ModificationResponse createModification(ModificationRequest request) {
        // 수정 요청 생성
        Modification modification = Modification.builder()
                .userId(request.getUserId())  // 병합 후 수정 필요
                .scheduleId(request.getScheduleId())  // 병합 후 수정 필요
                .details(request.getDetails())
                .build();

        Modification savedModification = modificationRepository.save(modification);

        // 알림 생성 및 전송
        notificationService.createNotification(NotificationRequest.builder()
                .userId(request.getUserId())  // 병합 후 수정 필요
                .type(NotificationType.SPECIFIC_USER)  // 특정 사용자 대상 알림으로 변경
                .message("근무 시간 수정 요청이 도착했습니다.")  // 알림 메시지 추가
                .build());

        return ModificationResponse.from(savedModification);
    }

    // 수정 요청 상태 업데이트
    @Transactional
    public ModificationResponse updateModificationStatus(Long modificationId, ModificationStatus status) {
        Modification modification = modificationRepository.findById(modificationId)
                .orElseThrow(() -> new EntityNotFoundException("수정 요청을 찾을 수 없습니다."));

        modification.updateStatus(status);
        return ModificationResponse.from(modification);
    }


    // 단일 수정 요청 조회
    public ModificationResponse getModification(Long modificationId) {
        return ModificationResponse.from(modificationRepository.findById(modificationId)
                .orElseThrow(() -> new EntityNotFoundException("수정 요청을 찾을 수 없습니다.")));
    }


     // 사용자별 수정 요청 목록 조회
     // 병합 후 사용자 엔티티 연동 필요
    public List<ModificationResponse> getUserModifications(Long userId) {
        return modificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(ModificationResponse::from)
                .collect(Collectors.toList());
    }


     // 스케줄별 수정 요청 목록 조회
     // 병합 후 스케줄 엔티티 연동 필요
    public List<ModificationResponse> getScheduleModifications(Long scheduleId) {
        return modificationRepository.findByScheduleIdOrderByCreatedAtDesc(scheduleId).stream()
                .map(ModificationResponse::from)
                .collect(Collectors.toList());
    }
}
