package com.example.albaease.shift.controller;

import com.example.albaease.notification.dto.NotificationResponse;
import com.example.albaease.shift.domain.enums.ShiftStatus;
import com.example.albaease.shift.dto.ShiftRequest;
import com.example.albaease.shift.dto.ShiftResponse;
import com.example.albaease.shift.service.ShiftService;
import com.example.albaease.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "대타 요청 API", description = "대타 요청 생성 및 상태 관리 기능을 제공하는 API")
public class ShiftController {
    private final ShiftService shiftService;
    private final UserRepository userRepository;

    // 대타 요청 생성
    @Operation(summary = "대타 요청 생성", description = "근무 일정에 대한 대타 요청을 생성합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대타 요청 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "사용자 또는 스케줄을 찾을 수 없음")
    })
    @PostMapping("/shift-requests/store/{storeId}")
    public ResponseEntity<ShiftResponse> createShiftRequest(
            @RequestBody ShiftRequest request,
            @PathVariable Long storeId,
            Principal principal) {

        log.info("대타 요청 생성 시작 - 요청 데이터: {}", request);

        try {
            Long fromUserId;

            // 이미 fromUserId가 설정되어 있으면 그대로 사용
            if (request.getFromUserId() != null) {
                fromUserId = request.getFromUserId();
                log.info("기존 fromUserId 사용: {}", fromUserId);
            } else {
                // principal에서 사용자 ID 추출
                try {
                    fromUserId = Long.parseLong(principal.getName());
                    log.info("Principal에서 추출한 fromUserId: {}", fromUserId);
                } catch (NumberFormatException e) {
                    // 이메일인 경우 사용자 ID 조회
                    String email = principal.getName();
                    fromUserId = userRepository.findByEmail(email)
                            .map(user -> user.getUserId()) // 또는 getId()
                            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + email));
                    log.info("이메일 {}에서 조회한 fromUserId: {}", email, fromUserId);
                }
                request.setFromUserId(fromUserId);
            }

            // 유효성 검사
            if (request.getScheduleId() == null) {
                return ResponseEntity.badRequest().body(null);
            }

            // toUserId가 필요한 경우 체크
            if ("SPECIFIC_USER".equals(request.getRequestType()) && request.getToUserId() == null) {
                return ResponseEntity.badRequest().body(null);
            }

            ShiftResponse response = shiftService.createShiftRequest(request);
            log.info("대타 요청 생성 성공: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("대타 요청 생성 중 오류 발생", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 대타 요청 상태 업데이트
    @Operation(summary = "대타 요청 상태 업데이트", description = "대타 요청의 상태를 승인 또는 거절로 변경합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 업데이트 성공"),
            @ApiResponse(responseCode = "404", description = "대타 요청을 찾을 수 없음")
    })
    @PatchMapping("/shift-requests/{shiftId}/status")
    public ResponseEntity<ShiftResponse> updateStatus(
            @PathVariable Long shiftId,
            @RequestParam ShiftStatus status,
            Principal principal) {

        Long approverId;
        try {
            approverId = Long.parseLong(principal.getName());
        } catch (NumberFormatException e) {
            // 이메일인 경우 사용자 ID 조회
            String email = principal.getName();
            approverId = userRepository.findByEmail(email)
                    .map(user -> user.getUserId()) // 또는 getId()
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + email));
        }

        return ResponseEntity.ok(shiftService.updateShiftStatus(
                shiftId,
                status,
                approverId)
        );
    }

    // WebSocket을 통해 알림 전송
    @Operation(summary = "WebSocket 대타 요청 알림", description = "WebSocket을 통해 대타 요청 알림을 실시간으로 전송합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 전송 성공")
    })
    @MessageMapping("/shift-requests")
    @SendToUser("/queue/notifications")
    public NotificationResponse handleShiftRequest(
            ShiftRequest request,
            Principal principal) {

        if (request.getFromUserId() == null) {
            try {
                Long userId = Long.parseLong(principal.getName());
                request.setFromUserId(userId);
            } catch (NumberFormatException e) {
                // 이메일인 경우 사용자 ID 조회
                String email = principal.getName();
                Long userId = userRepository.findByEmail(email)
                        .map(user -> user.getUserId()) // 또는 getId()
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + email));
                request.setFromUserId(userId);
            }
        }

        return shiftService.handleShiftRequest(request);
    }
}
