package com.example.albaease.schedule.dto;

import com.example.albaease.schedule.domain.Schedule;
import com.example.albaease.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class ScheduleResponse {
    private Long scheduleId;
    private Long userId;
    private String fullName;
    private Long storeId;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakTime;
    private List<String> repeatDays;
    private LocalDate repeatEndDate;

    public static ScheduleResponse fromEntity(Schedule schedule) {
        ScheduleResponse response = new ScheduleResponse();
        response.setScheduleId(schedule.getScheduleId());
        response.setUserId(schedule.getUser().getUserId());
        String fullName = schedule.getUser().getLastName() + " " + schedule.getUser().getFirstName();
        response.setFullName(fullName.trim());
        response.setStoreId(schedule.getStoreId());
        response.setWorkDate(schedule.getWorkDate());
        response.setStartTime(schedule.getStartTime());
        response.setEndTime(schedule.getEndTime());
        response.setBreakTime(schedule.getBreakTime());
        response.setRepeatDays(schedule.getRepeatDaysList());
        response.setRepeatEndDate(schedule.getRepeatEndDate());
        return response;
    }
}
