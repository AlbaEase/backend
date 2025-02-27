package com.example.albaease.schedule.service;

import com.example.albaease.schedule.domain.Schedule;
import com.example.albaease.schedule.domain.Template;
import com.example.albaease.schedule.dto.ScheduleRequest;
import com.example.albaease.schedule.dto.ScheduleResponse;
import com.example.albaease.schedule.dto.TemplateScheduleRequest;
import com.example.albaease.schedule.repository.ScheduleRepository;
import com.example.albaease.store.repository.StoreRepository;
import com.example.albaease.store.service.StoreService;
import com.example.albaease.user.entity.User;
import com.example.albaease.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.albaease.store.domain.Store;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TemplateService templateService;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    // 특정 스토어(storeId)의 전체 스케줄 조회
    public List<ScheduleResponse> getSchedulesByStoreId(Long storeId) {
        List<Schedule> schedules = scheduleRepository.findByStore_Id(storeId);

        return schedules.stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //특정 스토어 내 특정 사용자의 스케줄 조회
    public List<ScheduleResponse> getSchedulesByStoreAndUser(Long storeId, Long userId) {
        List<Schedule> schedules = scheduleRepository.findByStore_IdAndUser_UserId(storeId, userId);
        return schedules.stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 스케줄 ID로 조회
    public ScheduleResponse getScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found with id: " + scheduleId));
        return ScheduleResponse.fromEntity(schedule);
    }

    // 스케줄 생성
    @Transactional
    public List<ScheduleResponse> createSchedules(Long storeId, ScheduleRequest request) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));

        List<User> users = userRepository.findAllById(request.getUserIds());
        if (users.isEmpty()) {
            throw new RuntimeException("No valid users found.");
        }

        List<Schedule> schedules = new ArrayList<>();
        for (User user : users) {
            Schedule schedule = new Schedule();
            schedule.setStore(store);
            schedule.setUser(user);
            schedule.setWorkDate(request.getWorkDate());
            schedule.setStartTime(request.getStartTime());
            schedule.setEndTime(request.getEndTime());
            schedule.setBreakTime(request.getBreakTime());
            schedules.add(schedule);
        }

        // 스케줄 저장
        scheduleRepository.saveAll(schedules);

        // 저장된 데이터를 ScheduleResponse로 변환 후 반환
        return schedules.stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }



    // 특정 스케줄 삭제 (스토어 ID, 사용자 ID 확인 후 삭제)
    public void deleteSchedule(Long storeId, Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        // store와 user 객체 기반으로 검증
        if (!schedule.getStore().getId().equals(storeId) ||
                !schedule.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Store ID or User ID mismatch");
        }

        scheduleRepository.delete(schedule);
    }

    // 템플릿을 기반으로 스케줄 생성
    @Transactional
    public void createScheduleFromTemplate(Long templateId, TemplateScheduleRequest scheduleRequest) {
        Template template = templateService.getTemplateById(templateId);
        if (template == null) throw new RuntimeException("Template not found");

        Long storeId = scheduleRequest.getStoreId();
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        List<Schedule> schedules = scheduleRequest.getUserIds().stream()
                .map(userId -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

                    Schedule schedule = new Schedule();
                    schedule.setUser(user);
                    schedule.setStore(store); // storeId 대신 store 객체 설정
                    schedule.setWorkDate(scheduleRequest.getWorkDate());
                    schedule.setStartTime(template.getStartTime());
                    schedule.setEndTime(template.getEndTime());
                    schedule.setBreakTime(template.getBreakTime());

                    return schedule;
                })
                .collect(Collectors.toList());

        scheduleRepository.saveAll(schedules);
    }

    // 스케줄 수정
    public ScheduleResponse updateSchedule(Long storeId, Long userId, Long scheduleId, ScheduleRequest scheduleRequest) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        // store와 user 객체 기반으로 검증
        if (!schedule.getStore().getId().equals(storeId) ||
                !schedule.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Store ID or User ID mismatch");
        }

        // 스케줄 정보 업데이트
        schedule.setStartTime(scheduleRequest.getStartTime());
        schedule.setEndTime(scheduleRequest.getEndTime());
        schedule.setBreakTime(scheduleRequest.getBreakTime());
        schedule.setWorkDate(scheduleRequest.getWorkDate());

        Schedule updatedSchedule = scheduleRepository.save(schedule);
        return ScheduleResponse.fromEntity(updatedSchedule);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedulesByUserId(Long userId) {
        // userId를 이용해 User 객체 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<Schedule> schedules = scheduleRepository.findByUser(user);

        return schedules.stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
