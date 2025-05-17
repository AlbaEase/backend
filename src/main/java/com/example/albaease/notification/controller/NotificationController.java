package com.example.albaease.notification.controller;

import com.example.albaease.notification.dto.NotificationRequest;
import com.example.albaease.notification.dto.NotificationResponse;
import com.example.albaease.notification.service.NotificationService;
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
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@Tag(name = "알림 API", description = "알림 조회, 삭제 등의 기능을 제공하는 API")
public class NotificationController {
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    // 알림 목록 조회
    @Operation(summary = "알림 목록 조회", description = "사용자의 모든 알림 목록을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/me")
    public ResponseEntity<NotificationResponse> getNotifications(
            Principal principal,
            @RequestParam(required = false) Long userId) {

        log.info("알림 목록 조회 시작 - principal: {}, userId 파라미터: {}", principal.getName(), userId);

        Long userIdToUse;
        // 요청 파라미터로 userId가 제공된 경우 사용
        if (userId != null) {
            userIdToUse = userId;
            log.info("요청 파라미터로 제공된 userId 사용: {}", userIdToUse);
        } else {
            // principal에서 ID 추출 시도
            try {
                userIdToUse = Long.parseLong(principal.getName());
                log.info("Principal에서 추출한 userId: {}", userIdToUse);
            } catch (NumberFormatException e) {
                // 이메일인 경우 사용자 ID 조회
                String email = principal.getName();
                log.info("Principal이 숫자가 아닌 것으로 판단됨. 이메일로 간주: {}", email);
                userIdToUse = userRepository.findByEmail(email)
                        .map(user -> user.getUserId()) // 또는 getId()
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + email));
                log.info("이메일 {}에서 조회한 userId: {}", email, userIdToUse);
            }
        }

        NotificationResponse response = notificationService.getUserNotifications(userIdToUse);
        return ResponseEntity.ok(response);
    }

    // 알림 개별 삭제
    @Operation(summary = "알림 개별 삭제", description = "특정 알림을 삭제합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "알림 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "알림을 찾을 수 없음")
    })
    @DeleteMapping("/me/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }

    // 알림 전체 삭제
    @Operation(summary = "알림 전체 삭제", description = "사용자의 모든 알림을 삭제합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "알림 전체 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAllNotifications(
            Principal principal,
            @RequestParam(required = false) Long userId) {

        Long userIdToUse;
        if (userId != null) {
            userIdToUse = userId;
        } else {
            try {
                userIdToUse = Long.parseLong(principal.getName());
            } catch (NumberFormatException e) {
                // 이메일인 경우 사용자 ID 조회
                String email = principal.getName();
                userIdToUse = userRepository.findByEmail(email)
                        .map(user -> user.getUserId())
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + email));
            }
        }

        notificationService.deleteAllNotifications(userIdToUse);
        return ResponseEntity.noContent().build();
    }

    // WebSocket을 통해 알림 전송
    @Operation(summary = "WebSocket 알림 전송", description = "WebSocket을 통해 알림을 전송합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "알림 전송 성공")
    })
    @MessageMapping("/notification")
    @SendToUser("/queue/notifications")
    public NotificationResponse handleNotification(
            NotificationRequest request,
            Principal principal) {

        if (request.getUserId() == null) {
            try {
                Long userId = Long.parseLong(principal.getName());
                request.setUserId(userId);
            } catch (NumberFormatException e) {
                // 이메일인 경우 사용자 ID 조회
                String email = principal.getName();
                Long userId = userRepository.findByEmail(email)
                        .map(user -> user.getUserId())
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + email));
                request.setUserId(userId);
            }
        }

        return notificationService.createNotification(request);
    }

    // WebSocket 구독 요청 처리
    @Operation(summary = "WebSocket 구독", description = "클라이언트의 WebSocket 구독 요청을 처리합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "WebSocket 구독 성공")
    })
    @MessageMapping("/subscribe")
    public void subscribe(StompHeaderAccessor headerAccessor, Principal principal) {
        if (principal != null) {
            String userId = principal.getName();
            headerAccessor.getSessionAttributes().put("userId", userId);
            log.info("WebSocket 구독 등록: {}", userId);
        }
    }
}
