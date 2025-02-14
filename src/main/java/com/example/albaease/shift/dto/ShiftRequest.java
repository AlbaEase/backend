package com.example.albaease.shift.dto;

import com.example.albaease.shift.domain.enums.ShiftRequestType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShiftRequest {
    private Long fromUserId;
    private Long toUserId;
    private Long scheduleId;
    private ShiftRequestType requestType;

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }
}
