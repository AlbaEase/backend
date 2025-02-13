package com.example.albaease.schedule.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ScheduleFromTemplateRequest {
    private Long userId;         // 선택된 유저 ID
    private LocalDate workDate;  // 근무 시작 날짜
    private List<String> repeatDays; // 반복 요일
    private LocalDate repeatEndDate; // 반복 종료 날짜
}
