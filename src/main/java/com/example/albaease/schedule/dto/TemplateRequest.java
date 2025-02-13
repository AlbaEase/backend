package com.example.albaease.schedule.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class TemplateRequest {
    private Long storeId;
    private String templateName;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakTime;
    private List<String> repeatDays; // JSON에서 List<String> 형태로 받는데 수정필요할수도
    private LocalDate repeatEndDate;

    // String으로 변환하여 Entity에 저장
    public String convertRepeatDaysToString() {
        return repeatDays == null ? null : String.join(",", repeatDays);
    }
}
