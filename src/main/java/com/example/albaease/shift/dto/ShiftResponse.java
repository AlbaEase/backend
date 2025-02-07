package com.example.albaease.shift.dto;

import com.example.albaease.shift.domain.entity.Shift;
import com.example.albaease.shift.domain.enums.ShiftRequestType;
import com.example.albaease.shift.domain.enums.ShiftStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShiftResponse {
    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private Long scheduleId;
    private Long approvedBy;
    private ShiftRequestType requestType;
    private ShiftStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ShiftResponse from(Shift shift) {
        return ShiftResponse.builder()
                .id(shift.getRequest_id())
                .fromUserId(shift.getFromUserId())
                .toUserId(shift.getToUserId())
                .scheduleId(shift.getScheduleId())
                .approvedBy(shift.getApprovedBy())
                .requestType(shift.getRequestType())
                .status(shift.getStatus())
                .createdAt(shift.getCreatedAt())
                .updatedAt(shift.getUpdatedAt())
                .build();
    }
}
