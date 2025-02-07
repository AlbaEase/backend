package com.example.albaease.shift.controller;

import com.example.albaease.notification.domain.enums.NotificationReadStatus;
import com.example.albaease.notification.domain.enums.NotificationType;
import com.example.albaease.notification.dto.NotificationResponse;
import com.example.albaease.shift.domain.enums.ShiftStatus;
import com.example.albaease.shift.dto.ShiftRequest;
import com.example.albaease.shift.dto.ShiftResponse;
import com.example.albaease.shift.service.ShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/shift-requests")  // 테스트용 임시 URL
@RequiredArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;

    // 대타 요청 생성
    @PostMapping
    public ResponseEntity<ShiftResponse> createShiftRequest(
            @RequestBody ShiftRequest request) {
        return ResponseEntity.ok(shiftService.createShiftRequest(request));
    }

    // 대타 요청 상태 업데이트
    @PatchMapping("/{shiftId}/status")
    public ResponseEntity<ShiftResponse> updateStatus(
            @PathVariable Long shiftId,
            @RequestParam ShiftStatus status,
            @RequestParam Long approvedById) {
        return ResponseEntity.ok(shiftService.updateShiftStatus(shiftId, status, approvedById));
    }

    @MessageMapping("/shift-requests")
    @SendToUser("/queue/notifications")
    public NotificationResponse handleShiftRequest(ShiftRequest request) {
        return shiftService.handleShiftRequest(request);
    }
}
