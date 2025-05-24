package com.example.albaease.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class TemplateRequest {
    private String templateName;

    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", example = "09:00")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", example = "18:00")
    private LocalTime endTime;

    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", example = "01:00")
    private LocalTime breakTime;
}
