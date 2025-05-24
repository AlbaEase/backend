package com.example.albaease.schedule.controller;

import com.example.albaease.auth.CustomUserDetails;
import com.example.albaease.schedule.dto.ScheduleRequest;
import com.example.albaease.schedule.dto.ScheduleResponse;
import com.example.albaease.schedule.dto.TemplateScheduleRequest;
import com.example.albaease.schedule.service.ScheduleService;
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
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 내 스케줄 조회. 로그인한 사용자의 userId 가져와서 검색
    @GetMapping("/me")
    public ResponseEntity<List<ScheduleResponse>> getMySchedules(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<ScheduleResponse> schedules = scheduleService.getSchedulesByUserId(userId);
        return ResponseEntity.ok(schedules);
    }

    // 스토어 ID로 스케줄 조회. 스토어 아이디 부분 수정 필요
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByStoreId(@PathVariable Long storeId) {
        List<ScheduleResponse> schedules = scheduleService.getSchedulesByStoreId(storeId);
        return ResponseEntity.ok(schedules);
    }

    // 특정 알바생의 스토어별 스케줄 조회.
    @GetMapping("/store/{storeId}/user/{userId}")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByStoreAndUser(
            @PathVariable Long storeId,
            @PathVariable Long userId) {

        List<ScheduleResponse> schedules = scheduleService.getSchedulesByStoreAndUser(storeId, userId);
        return ResponseEntity.ok(schedules);
    }

    // 스케줄 ID로 조회
    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> getScheduleById(@PathVariable Long scheduleId) {
        ScheduleResponse schedule = scheduleService.getScheduleById(scheduleId);
        return ResponseEntity.ok(schedule);
    }

    // 스케줄 등록
    @PostMapping("/store/{storeId}")
    public ResponseEntity<List<ScheduleResponse>> createSchedules(
            @PathVariable Long storeId,
            @RequestBody ScheduleRequest request) {

        List<ScheduleResponse> createdSchedules = scheduleService.createSchedules(storeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSchedules);
    }

    // 스케줄 수정
    @PutMapping("/store/{storeId}/user/{userId}/{scheduleId}")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @PathVariable Long storeId,
            @PathVariable Long userId,
            @PathVariable Long scheduleId,
            @RequestBody ScheduleRequest scheduleRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        try {
            ScheduleResponse updatedSchedule = scheduleService.updateSchedule(storeId, userId, scheduleId, scheduleRequest, userDetails);
            return ResponseEntity.ok(updatedSchedule);
        } catch (AccessDeniedException e) {
            // 권한 없는 사용자의 경우
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (IllegalArgumentException e) {
            // 잘못된 요청 데이터
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (RuntimeException e) {
            // 기타 서버 에러 (예외 메시지 노출)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    // 스케줄 삭제
    @DeleteMapping("/store/{storeId}/user/{userId}/{scheduleId}")
    public ResponseEntity<String> deleteSchedule(
            @PathVariable Long storeId,
            @PathVariable Long userId,
            @PathVariable Long scheduleId) {
        try {
            scheduleService.deleteSchedule(storeId, userId, scheduleId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Schedule deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Schedule not found or invalid store/user");
        }
    }


    // 템플릿으로 스케줄 추가
    @PostMapping("/from-template/{templateId}")
    public ResponseEntity<String> createScheduleFromTemplate(
            @PathVariable Long templateId,
            @RequestBody TemplateScheduleRequest scheduleRequest) {
        try {
            // 매장 있는지 확인
            if (scheduleRequest.getStoreId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: Store ID is required in the request.");
            }

            scheduleService.createScheduleFromTemplate(templateId, scheduleRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("Schedules created from template successfully.");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create schedules from template. Error: " + e.getMessage());
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
