package com.example.albaease.schedule.dto;

import com.example.albaease.schedule.domain.Schedule;
import com.example.albaease.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter @Setter
public class ScheduleResponse {
    private Long scheduleId;
    private ScheduleResponseUserInfo user;
    private Long storeId;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakTime;
    private List<String> repeatDays;
    private LocalDate repeatEndDate;

    private boolean isShiftChanged;
    private ScheduleResponseUserInfo originalUser;

    public static ScheduleResponse fromEntity(Schedule schedule) {
        ScheduleResponse response = new ScheduleResponse();

        // 사용자 정보 세팅
        User u = schedule.getUser();
        ScheduleResponseUserInfo userInfo = new ScheduleResponseUserInfo();
        userInfo.setUserId(u.getUserId());
        userInfo.setFullName(u.getLastName() + u.getFirstName());
        response.setUser(userInfo);

        response.setScheduleId(schedule.getScheduleId());
        response.setStoreId(schedule.getStoreId());
        response.setWorkDate(schedule.getWorkDate());
        response.setStartTime(schedule.getStartTime());
        response.setEndTime(schedule.getEndTime());
        response.setBreakTime(schedule.getBreakTime());
        response.setRepeatDays(schedule.getRepeatDaysList());
        response.setRepeatEndDate(schedule.getRepeatEndDate());

        response.setShiftChanged(false);
        response.setOriginalUser(null);  // 기본값

        return response;
    }
}
