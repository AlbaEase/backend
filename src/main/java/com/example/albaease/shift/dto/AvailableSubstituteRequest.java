package com.example.albaease.shift.dto;

import com.example.albaease.shift.domain.enums.ShiftRequestType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AvailableSubstituteRequest {
    private Long scheduleId;
    private Long storeId;
    private LocalDate requestDate;
    private Long fromUserId;
    private ShiftRequestType requestType;
}
