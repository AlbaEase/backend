package com.example.albaease.schedule.service;

import com.example.albaease.schedule.domain.Schedule;
import com.example.albaease.schedule.dto.ScheduleRequest;
import com.example.albaease.schedule.dto.ScheduleResponse;
import com.example.albaease.schedule.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // 스토어 ID로 스케줄 조회
    public List<ScheduleResponse> getSchedulesByStoreId(Long storeId) {
        return scheduleRepository.findByStoreId(storeId).stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 스케줄 ID로 스케줄 조회
    public ScheduleResponse getScheduleById(Long scheduleId) {
        Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);

        if (optionalSchedule.isPresent()) {
            Schedule schedule = optionalSchedule.get();
            return ScheduleResponse.fromEntity(schedule); // 그냥 map쓰는게 나을지도
        } else {
            throw new RuntimeException("Schedule not found");
        }
    }


    // 특정 사용자의 매장별 스케줄 목록 조회
    public List<ScheduleResponse> getSchedulesByStoreIdAndUserId(Long storeId, Long userId) {
        return scheduleRepository.findByStoreIdAndUserId(storeId, userId).stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 사용자의 스케줄 목록 조회
    public List<ScheduleResponse> getSchedulesByUserId(Long userId) {
        return scheduleRepository.findByUserId(userId).stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 날짜의 스케줄 조회
    public List<ScheduleResponse> getSchedulesByWorkDate(LocalDate workDate) {
        return scheduleRepository.findByWorkDate(workDate).stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 스케줄 등록
    public ScheduleResponse createSchedule(ScheduleRequest scheduleRequest) {
        Schedule schedule = scheduleRequest.toEntity();
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return ScheduleResponse.fromEntity(savedSchedule);
    }

    // 스케줄 수정
    public ScheduleResponse updateSchedule(Long storeId, Long userId, Long scheduleId, ScheduleRequest scheduleRequest) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        // 업데이트할 필드 설정
        schedule.setWorkDate(scheduleRequest.getWorkDate());
        schedule.setStartTime(scheduleRequest.getStartTime());
        schedule.setEndTime(scheduleRequest.getEndTime());
        schedule.setBreakTime(scheduleRequest.getBreakTime());
        schedule.setRepeatDaysFromList(scheduleRequest.getRepeatDays());
        schedule.setRepeatEndDate(scheduleRequest.getRepeatEndDate());

        Schedule updatedSchedule = scheduleRepository.save(schedule);
        return ScheduleResponse.fromEntity(updatedSchedule);
    }

    // 스케줄 삭제
    public void deleteSchedule(Long storeId, Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        scheduleRepository.delete(schedule);
    }


}
