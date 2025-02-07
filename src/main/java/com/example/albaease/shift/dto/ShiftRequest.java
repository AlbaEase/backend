package com.example.albaease.shift.dto;

import com.example.albaease.shift.domain.enums.ShiftRequestType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShiftRequest {
    private Long fromUserId;  // 테스트용 임시 field
    private Long toUserId;    // 테스트용 임시 field
    private Long scheduleId;  // 테스트용 임시 field
    private ShiftRequestType requestType;
}
