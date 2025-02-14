package com.example.albaease.shift.service;

import com.example.albaease.user.domain.User;
import com.example.albaease.user.repository.UserRepository;
import com.example.albaease.schedule.domain.Schedule;
import com.example.albaease.schedule.repository.ScheduleRepository;
import com.example.albaease.notification.domain.enums.NotificationType;
import com.example.albaease.notification.dto.NotificationRequest;
import com.example.albaease.notification.service.NotificationService;
import com.example.albaease.shift.domain.entity.Shift;
import com.example.albaease.shift.domain.enums.ShiftStatus;
import com.example.albaease.shift.dto.ShiftRequest;
import com.example.albaease.shift.dto.ShiftResponse;
import com.example.albaease.shift.repository.ShiftRepository;
import com.example.albaease.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public ShiftResponse createShiftRequest(ShiftRequest request) {
        // 1. 엔티티 조회 및 검증
        User fromUser = userRepository.findById(request.getFromUserId())
                .orElseThrow(() -> new EntityNotFoundException("요청자를 찾을 수 없습니다."));

        User toUser = userRepository.findById(request.getToUserId())
                .orElseThrow(() -> new EntityNotFoundException("대상자를 찾을 수 없습니다."));

        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new EntityNotFoundException("스케줄을 찾을 수 없습니다."));

        // 2. 요청자가 해당 스케줄의 담당자인지 검증
        validateScheduleOwnership(fromUser, schedule);

        // 3. 대타 요청 생성
        Shift shift = Shift.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .schedule(schedule)
                .requestType(request.getRequestType())
                .build();

        Shift savedShift = shiftRepository.save(shift);

        // 4. 알림 생성
        sendShiftNotification(toUser.getId(), schedule.getId(),
                "새로운 대타 요청이 도착했습니다.");

        return ShiftResponse.from(savedShift);
    }

    @Transactional
    public ShiftResponse updateShiftStatus(Long shiftId, ShiftStatus status, Long approvedById) {
        // 1. 엔티티 조회 및 검증
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new EntityNotFoundException("대타 요청을 찾을 수 없습니다."));

        User approver = userRepository.findById(approvedById)
                .orElseThrow(() -> new EntityNotFoundException("승인자를 찾을 수 없습니다."));

        // 2. 승인자 권한 검증
        validateApproverAuthority(approver, shift.getSchedule());

        // 3. 상태 업데이트
        shift.updateStatus(status, approver);

        // 4. 알림 전송
        String statusMessage = generateStatusMessage(status);
        sendShiftNotification(shift.getFromUser().getId(),
                shift.getSchedule().getId(), statusMessage);

        return ShiftResponse.from(shift);
    }

    // 검증 메서드들
    private void validateScheduleOwnership(User user, Schedule schedule) {
        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("해당 스케줄의 담당자가 아닙니다.");
        }
    }

    private void validateApproverAuthority(User approver, Schedule schedule) {
        // TODO: 승인자의 권한 검증 로직 구현 (role이 manager인지 등)
        // 다른 팀원의 User 엔티티 구현 확인 후 수정
    }

    // 유틸리티 메서드들
    private String generateStatusMessage(ShiftStatus status) {
        return switch (status) {
            case APPROVED -> "대타 요청이 승인되었습니다.";
            case REJECTED -> "대타 요청이 거절되었습니다.";
            default -> "대타 요청 상태가 변경되었습니다.";
        };
    }

    private void sendShiftNotification(Long userId, Long scheduleId, String message) {
        notificationService.createNotification(NotificationRequest.builder()
                .userId(userId)
                .type(NotificationType.SPECIFIC_USER)
                .message(message)
                .scheduleId(scheduleId)
                .build());
    }
}
