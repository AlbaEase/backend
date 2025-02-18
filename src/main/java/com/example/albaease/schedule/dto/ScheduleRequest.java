package com.example.albaease.schedule.dto;

import com.example.albaease.schedule.domain.Schedule;
import com.example.albaease.user.entity.User; // User 엔티티 추가
import com.example.albaease.user.repository.UserRepository; // UserRepository 추가
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ScheduleRequest {
    private List<Long> userIds; // 여러 명의 사용자 ID를 받음
    private Long storeId;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakTime;
    private List<String> repeatDays;
    private LocalDate repeatEndDate;

    // 여러 사용자에 대한 스케줄을 생성하는 로직
    public List<Schedule> toEntities(UserRepository userRepository) {
        List<Schedule> schedules = new ArrayList<>();

        for (Long userId : userIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            Schedule schedule = new Schedule();
            schedule.setUser(user); // 사용자 설정
            schedule.setStoreId(storeId);
            schedule.setWorkDate(workDate);
            schedule.setStartTime(startTime);
            schedule.setEndTime(endTime);
            schedule.setBreakTime(breakTime);
            schedule.setRepeatDaysFromList(repeatDays);
            schedule.setRepeatEndDate(repeatEndDate);
            schedules.add(schedule);
        }
        return schedules;
    }
}
