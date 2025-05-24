package com.example.albaease.shift.service;

import com.example.albaease.notification.dto.NotificationResponse;
import com.example.albaease.notification.handler.WebSocketHandler;
import com.example.albaease.schedule.domain.Schedule;
import com.example.albaease.schedule.repository.ScheduleRepository;
import com.example.albaease.user.entity.User;
import com.example.albaease.user.repository.UserRepository;
import com.example.albaease.notification.domain.enums.NotificationType;
import com.example.albaease.notification.dto.NotificationRequest;
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

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShiftService {
    private final ShiftRepository shiftRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final WebSocketHandler webSocketHandler;

    @Transactional
    public ShiftResponse createShiftRequest(ShiftRequest request) {
        // 엔티티 조회 및 검증
        User fromUser = userRepository.findById(request.getFromUserId())
                .orElseThrow(() -> new EntityNotFoundException("요청자를 찾을 수 없습니다."));

        User toUser = userRepository.findById(request.getToUserId())
                .orElseThrow(() -> new EntityNotFoundException("대상자를 찾을 수 없습니다."));

        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new EntityNotFoundException("스케줄을 찾을 수 없습니다."));

        // 대타 요청 생성
        Shift shift = Shift.builder()
                .fromUser(fromUser)
                .toUser(toUser)
                .schedule(schedule)
                .requestType(request.getRequestType())
                .requestDate(request.getRequestDate()) // 대타 요청 대상 날짜 추가
                .build();

        Shift savedShift = shiftRepository.save(shift);

        // 알림 생성 및 WebSocket 실시간 알림 전송
        String message = "새로운 대타 요청이 도착했습니다.";
        if (request.getRequestDate() != null) {
            message += " 날짜: " + request.getRequestDate().toString();
        }

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(toUser.getUserId())
                .type(NotificationType.SPECIFIC_USER)
                .message(message)
                .scheduleId(schedule.getScheduleId())
                .fromUserId(fromUser.getUserId())
                .toUserId(toUser.getUserId())
                .build();

        NotificationResponse notificationResponse = notificationService.createShiftNotification(notificationRequest, ShiftResponse.from(savedShift));
        webSocketHandler.sendNotification(notificationResponse);

        return ShiftResponse.from(savedShift);
    }

    @Transactional
    public ShiftResponse updateShiftStatus(Long shiftId, ShiftStatus status, Long approvedById) {
        // 엔티티 조회 및 검증
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new EntityNotFoundException("대타 요청을 찾을 수 없습니다."));

        User approveUser = userRepository.findById(approvedById)
                .orElseThrow(() -> new EntityNotFoundException("승인자를 찾을 수 없습니다."));

        // 상태 업데이트
        shift.updateStatus(status, approveUser);

        // 대타 요청이 승인된 경우 스케줄 업데이트
        if (status == ShiftStatus.APPROVED) {
            updateScheduleForApprovedShift(shift);
        }

        String statusMessage = switch (status) {
            case APPROVED -> "대타 요청이 승인되었습니다.";
            case REJECTED -> "대타 요청이 거절되었습니다.";
            default -> "대타 요청 상태가 변경되었습니다.";
        };

        if (shift.getRequestDate() != null) {
            statusMessage += " 날짜: " + shift.getRequestDate().toString();
        }

        sendShiftNotification(shift.getFromUser().getUserId(),
                shift.getSchedule().getScheduleId(), statusMessage);

        // 상태 변경 알림 전송
        webSocketHandler.sendShiftStatusUpdate(
                shift.getFromUser().getUserId(),
                statusMessage
        );

        return ShiftResponse.from(shift);
    }

    /**
     * 승인된 대타 요청에 대해 스케줄 정보를 업데이트합니다.
     * 기존 사용자의 근무 일정을 대타 사용자로 변경합니다.
     */
    private void updateScheduleForApprovedShift(Shift shift) {
        Schedule schedule = shift.getSchedule();
        LocalDate requestDate = shift.getRequestDate();

        log.info("대타 요청 승인: scheduleId={}, fromUser={}, toUser={}, requestDate={}",
                schedule.getScheduleId(), shift.getFromUser().getUserId(),
                shift.getToUser().getUserId(), requestDate);

        try {
            // 대타 요청 특정 날짜가 있는 경우, 해당 날짜에 대한 새 스케줄을 생성
            if (requestDate != null) {
                // 해당 날짜에 이미 대타 사용자의 스케줄이 있는지 확인
                boolean hasExistingSchedule = scheduleRepository.existsByUserAndWorkDate(
                        shift.getToUser(), requestDate);

                if (hasExistingSchedule) {
                    log.warn("대타 사용자가 해당 날짜에 이미 스케줄이 있습니다: userId={}, date={}",
                            shift.getToUser().getUserId(), requestDate);
                    return;
                }

                // 새 스케줄 생성 (기존 스케줄 복제 후 사용자와 날짜 변경)
                Schedule newSchedule = new Schedule();
                newSchedule.setUser(shift.getToUser());
                newSchedule.setStore(schedule.getStore());
                newSchedule.setWorkDate(requestDate);
                newSchedule.setStartTime(schedule.getStartTime());
                newSchedule.setEndTime(schedule.getEndTime());
                newSchedule.setBreakTime(schedule.getBreakTime());

                scheduleRepository.save(newSchedule);
                log.info("대타 요청으로 새 스케줄 생성 완료: newScheduleId={}, userId={}, date={}",
                        newSchedule.getScheduleId(), newSchedule.getUser().getUserId(), newSchedule.getWorkDate());
            } else {
                // 특정 날짜가 없는 경우, 기존 스케줄의 사용자를 대타 사용자로 변경
                schedule.setUser(shift.getToUser());
                scheduleRepository.save(schedule);
                log.info("대타 요청으로 기존 스케줄 사용자 변경 완료: scheduleId={}, newUserId={}",
                        schedule.getScheduleId(), schedule.getUser().getUserId());
            }
        } catch (Exception e) {
            log.error("대타 요청 스케줄 업데이트 중 오류 발생", e);
            throw new RuntimeException("스케줄 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private void sendShiftNotification(Long userId, Long scheduleId, String message) {
        notificationService.createNotification(NotificationRequest.builder()
                .userId(userId)
                .type(NotificationType.SPECIFIC_USER)
                .message(message)
                .scheduleId(scheduleId)
                .build());
    }

    @Transactional
    public NotificationResponse handleShiftRequest(ShiftRequest request) {
        // 대타 요청 저장
        ShiftResponse shiftResponse = createShiftRequest(request);

        // 메시지 생성
        String message = "대타 요청이 도착했습니다.";
        if (request.getRequestDate() != null) {
            message += " 날짜: " + request.getRequestDate().toString();
        }

        // 알림 생성 요청
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(request.getToUserId())
                .type(NotificationType.SPECIFIC_USER)
                .message(message)
                .scheduleId(request.getScheduleId())
                .fromUserId(request.getFromUserId())
                .toUserId(request.getToUserId())
                .build();

        // Shift 전용 알림 생성 및 반환
        return notificationService.createShiftNotification(notificationRequest, shiftResponse);
    }
}
