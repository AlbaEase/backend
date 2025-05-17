package com.example.albaease.modification.controller;

import com.example.albaease.modification.domain.enums.ModificationStatus;
import com.example.albaease.modification.dto.ModificationRequest;
import com.example.albaease.modification.dto.ModificationResponse;
import com.example.albaease.modification.service.ModificationService;
import com.example.albaease.notification.dto.NotificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "근무시간 수정 요청 API", description = "근무시간 수정 요청 관련 기능을 제공하는 API")
public class ModificationController {
    private final ModificationService modificationService;

    // 근무시간 수정 요청
    @Operation(summary = "근무시간 수정 요청 생성", description = "근무 일정에 대한 수정 요청을 생성합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 요청 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "사용자 또는 스케줄을 찾을 수 없음")
    })
    @PostMapping("/shift-modification/store/{storeId}")
    public ResponseEntity<ModificationResponse> createModification(
            @RequestBody ModificationRequest request,
            Principal principal) {
        String userId = principal.getName();
        request.setUserId(Long.parseLong(userId));
        return ResponseEntity.ok(modificationService.createModification(request));
    }

    // 수정 요청 상태 업데이트, 사용자가 (승인/거절)버튼 클릭 시
    @Operation(summary = "수정 요청 상태 업데이트", description = "근무시간 수정 요청의 상태를 승인 또는 거절로 변경합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상태 업데이트 성공"),
            @ApiResponse(responseCode = "404", description = "수정 요청을 찾을 수 없음")
    })
    @PatchMapping("/shift-modification/{modificationId}/status")
    public ResponseEntity<ModificationResponse> updateStatus(
            @PathVariable Long modificationId,
            @RequestParam ModificationStatus status) {
        return ResponseEntity.ok(modificationService.updateModificationStatus(modificationId, status));
    }

    // WebSocket을 통해 알림 전송
    @Operation(summary = "WebSocket 수정 요청 알림", description = "WebSocket을 통해 수정 요청 알림을 실시간으로 전송합니다")
    @MessageMapping("/modification")
    @SendToUser("/queue/notifications")
    public NotificationResponse handleModificationRequest(
            ModificationRequest request,
            Principal principal) {
        String userId = principal.getName();
        request.setUserId(Long.parseLong(userId));
        return modificationService.handleModificationRequest(request);
    }
}
