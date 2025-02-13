package com.example.albaease.schedule.dto;

import com.example.albaease.schedule.domain.Schedule;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class ScheduleRequest {
    private Long userId;
    private Long storeId;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakTime;
    private List<String> repeatDays;
    private LocalDate repeatEndDate;

    public Schedule toEntity() {
        Schedule schedule = new Schedule();
        schedule.setUserId(userId);
        schedule.setStoreId(storeId);
        schedule.setWorkDate(workDate);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setBreakTime(breakTime);
        schedule.setRepeatDaysFromList(repeatDays);
        schedule.setRepeatEndDate(repeatEndDate);
        return schedule;
    }
}
