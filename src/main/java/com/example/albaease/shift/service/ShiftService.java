package com.example.albaease.shift.service;

import com.example.albaease.notification.domain.enums.NotificationType;
import com.example.albaease.notification.dto.NotificationRequest;
import com.example.albaease.notification.dto.NotificationResponse;
import com.example.albaease.notification.service.NotificationService;
import com.example.albaease.shift.domain.entity.Shift;
import com.example.albaease.shift.domain.enums.ShiftStatus;
import com.example.albaease.shift.dto.ShiftRequest;
import com.example.albaease.shift.dto.ShiftResponse;
import com.example.albaease.shift.repository.ShiftRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final NotificationService notificationService;

    // 대타 요청 생성 및 알림 전송
    @Transactional
    public ShiftResponse createShiftRequest(ShiftRequest request) {
        // 대타 요청 생성
        Shift shift = Shift.builder()
                .fromUserId(request.getFromUserId())  // 병합 후 수정 필요
                .toUserId(request.getToUserId())      // 병합 후 수정 필요
                .scheduleId(request.getScheduleId())  // 병합 후 수정 필요
                .requestType(request.getRequestType())
                .build();

        Shift savedShift = shiftRepository.save(shift);

        // 알림 생성 및 전송
        notificationService.createNotification(NotificationRequest.builder()
                .userId(request.getToUserId())  // 병합 후 수정 필요
                .type(NotificationType.SPECIFIC_USER)  // 특정 사용자 대상 알림
                .message("새로운 대타 요청이 도착했습니다.")
                .scheduleId(request.getScheduleId())
                .build());

        return ShiftResponse.from(savedShift);
    }

    // 대타 요청 상태 업데이트
    @Transactional
    public ShiftResponse updateShiftStatus(Long shiftId, ShiftStatus status, Long approvedById) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new EntityNotFoundException("대타 요청을 찾을 수 없습니다."));

        shift.updateStatus(status, approvedById);

        // 상태 변경 알림 전송
        String statusMessage = switch (status) {
            case APPROVED -> "대타 요청이 승인되었습니다.";
            case REJECTED -> "대타 요청이 거절되었습니다.";
            default -> "대타 요청 상태가 변경되었습니다.";
        };

        notificationService.createNotification(NotificationRequest.builder()
                .userId(shift.getFromUserId())
                .type(NotificationType.SPECIFIC_USER)  // 특정 사용자 대상 알림
                .message(statusMessage)
                .scheduleId(shift.getScheduleId())
                .build());

        return ShiftResponse.from(shift);
    }

    @Transactional
    public NotificationResponse handleShiftRequest(ShiftRequest request) {
        // 1. 기존의 createShiftRequest 호출하여 대타 요청 처리
        ShiftResponse shiftResponse = createShiftRequest(request);

        // 2. 알림 생성 요청
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(request.getToUserId())
                .type(NotificationType.SPECIFIC_USER)
                .message("대타 요청이 도착했습니다.")
                .scheduleId(request.getScheduleId())
                .build();

        // 3. 알림 생성 및 반환
        return notificationService.createNotification(notificationRequest);
    }
}
