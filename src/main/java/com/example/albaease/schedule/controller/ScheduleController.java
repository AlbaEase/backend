package com.example.albaease.schedule.controller;

import com.example.albaease.auth.CustomUserDetails;
import com.example.albaease.schedule.dto.ScheduleRequest;
import com.example.albaease.schedule.dto.ScheduleResponse;
import com.example.albaease.schedule.dto.TemplateScheduleRequest;
import com.example.albaease.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/schedule")
@Tag(name = "schedule-controller", description = "근무 스케줄 관련 API")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/me")
    @Operation(summary = "내 스케줄 조회", description = "로그인한 사용자(userId)의 개인 스케줄 목록을 조회합니다.")
    public ResponseEntity<List<ScheduleResponse>> getMySchedules(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<ScheduleResponse> schedules = scheduleService.getSchedulesByUserId(userId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "매장 스케줄 조회", description = "해당 storeId의 전체 스케줄 목록을 조회합니다.")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByStoreId(
            @Parameter(description = "매장 ID", example = "1") @PathVariable Long storeId) {
        List<ScheduleResponse> schedules = scheduleService.getSchedulesByStoreId(storeId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/store/{storeId}/user/{userId}")
    @Operation(summary = "특정 알바생 스케줄 조회", description = "매장(storeId) 내 특정 알바생(userId)의 스케줄 목록을 조회합니다.")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByStoreAndUser(
            @Parameter(description = "매장 ID", example = "1") @PathVariable Long storeId,
            @Parameter(description = "사용자 ID", example = "10") @PathVariable Long userId) {
        List<ScheduleResponse> schedules = scheduleService.getSchedulesByStoreAndUser(storeId, userId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{scheduleId}")
    @Operation(summary = "스케줄 단건 조회", description = "스케줄 ID(scheduleId)를 기준으로 하나의 스케줄 상세 정보를 조회합니다.")
    public ResponseEntity<ScheduleResponse> getScheduleById(
            @Parameter(description = "스케줄 ID", example = "100") @PathVariable Long scheduleId) {
        ScheduleResponse schedule = scheduleService.getScheduleById(scheduleId);
        return ResponseEntity.ok(schedule);
    }

    @PostMapping("/store/{storeId}")
    @Operation(summary = "스케줄 등록", description = "해당 매장(storeId)에 하나 이상의 스케줄을 등록합니다.")
    public ResponseEntity<List<ScheduleResponse>> createSchedules(
            @Parameter(description = "매장 ID", example = "1") @PathVariable Long storeId,
            @RequestBody ScheduleRequest request) {
        List<ScheduleResponse> createdSchedules = scheduleService.createSchedules(storeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSchedules);
    }

    @PutMapping("/store/{storeId}/user/{userId}/{scheduleId}")
    @Operation(summary = "스케줄 수정", description = "매장, 사용자, 스케줄 ID에 해당하는 스케줄을 수정합니다. 수정 권한이 있어야 합니다.")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @Parameter(description = "매장 ID", example = "1") @PathVariable Long storeId,
            @Parameter(description = "사용자 ID", example = "10") @PathVariable Long userId,
            @Parameter(description = "스케줄 ID", example = "100") @PathVariable Long scheduleId,
            @RequestBody ScheduleRequest scheduleRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {
            ScheduleResponse updatedSchedule = scheduleService.updateSchedule(storeId, userId, scheduleId, scheduleRequest, userDetails);
            return ResponseEntity.ok(updatedSchedule);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/store/{storeId}/user/{userId}/{scheduleId}")
    @Operation(summary = "스케줄 삭제", description = "매장(storeId), 사용자(userId), 스케줄 ID(scheduleId)를 기준으로 스케줄을 삭제합니다.")
    public ResponseEntity<String> deleteSchedule(
            @Parameter(description = "매장 ID", example = "1") @PathVariable Long storeId,
            @Parameter(description = "사용자 ID", example = "10") @PathVariable Long userId,
            @Parameter(description = "스케줄 ID", example = "100") @PathVariable Long scheduleId) {
        try {
            scheduleService.deleteSchedule(storeId, userId, scheduleId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Schedule deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Schedule not found or invalid store/user");
        }
    }

    // 템플릿으로 스케줄 추가
    @PostMapping("/from-template/{templateId}")
    @Operation(
            summary = "템플릿으로 스케줄 생성",
            description = "템플릿 ID와 매장 ID(storeId)를 기반으로 해당 매장에 반복 스케줄을 생성합니다."
    )
    public ResponseEntity<String> createScheduleFromTemplate(
            @Parameter(description = "템플릿 ID", example = "5") @PathVariable Long templateId,
            @RequestBody TemplateScheduleRequest scheduleRequest) {
        try {
            // 매장 있는지 확인
            if (scheduleRequest.getStoreId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: Store ID is required in the request.");
            }

            scheduleService.createScheduleFromTemplate(templateId, scheduleRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Schedules created from template successfully.");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create schedules from template. Error: " + e.getMessage());
        }
    }



    // 월간 스케줄 조회 API 추가 (대타 요청 반영)
    @GetMapping("/store/{storeId}/monthly")
    public ResponseEntity<Map<String, List<ScheduleResponse>>> getMonthlySchedule(
            @PathVariable Long storeId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        // 년도나 월이 지정되지 않은 경우 현재 년월 사용
        if (year == null || month == null) {
            LocalDate now = LocalDate.now();
            year = now.getYear();
            month = now.getMonthValue();
        }

        Map<String, List<ScheduleResponse>> schedules =
                scheduleService.getMonthlyScheduleWithShifts(storeId, year, month);

        return ResponseEntity.ok(schedules);
    }
}
