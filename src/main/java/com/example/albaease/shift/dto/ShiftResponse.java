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
                .fromUserId(shift.getFromUser().getId())  // 엔티티에서 ID 가져오기
                .toUserId(shift.getToUser().getId())      // 엔티티에서 ID 가져오기
                .scheduleId(shift.getSchedule().getId())  // 엔티티에서 ID 가져오기
                .approvedBy(shift.getApprovedBy() != null ?
                        shift.getApprovedBy().getId() : null) // 엔티티에서 ID 가져오기
                .requestType(shift.getRequestType())
                .status(shift.getStatus())
                .createdAt(shift.getCreatedAt())
                .updatedAt(shift.getUpdatedAt())
                .build();
    }
}
