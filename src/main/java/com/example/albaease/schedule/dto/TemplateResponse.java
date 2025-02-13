package com.example.albaease.schedule.dto;

import com.example.albaease.schedule.domain.Template;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Getter
public class TemplateResponse {
    private Long templateId;
    private Long storeId;
    private String templateName;
    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakTime;

    public TemplateResponse(Template template) {
        this.templateId = template.getTemplateId();
        this.storeId = template.getStoreId();
        this.templateName = template.getTemplateName();
        this.workDate = template.getWorkDate();
        this.startTime = template.getStartTime();
        this.endTime = template.getEndTime();
        this.breakTime = template.getBreakTime();
    }

    // String -> List<String> 변환
    private List<String> convertRepeatDaysToList(String repeatDays) {
        return (repeatDays == null || repeatDays.isEmpty()) ? null : Arrays.asList(repeatDays.split(","));
    }


}
