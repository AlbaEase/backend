package com.example.albaease.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class TemplateScheduleRequest {
    private List<Long> userIds;  // 여러 명의 사용자 ID 리스트
    private Long storeId;         // 매장 ID

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(type = "string", example = "2025-04-28")
    private LocalDate workDate;   // 근무 날짜

    @Schema(description = "반복 요일 (예: MONDAY, TUESDAY)")
    private List<String> repeatDays;  // 반복 요일 (월, 화, 수 등)

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(type = "string", example = "2025-05-31")
    private LocalDate repeatEndDate;  // 반복 종료 날짜

    public boolean isRepeating() {
        return repeatDays != null && !repeatDays.isEmpty();
    }
}
