package com.example.albaease.schedule.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    //외래키. user 폴더 추가되면 수정
    //@ManyToOne
    //@JoinColumn(name = "from_user_id", nullable = false)
    //private User fromUser;

    //@ManyToOne
    //@JoinColumn(name = "to_user_id", nullable = false)
    //private User toUser;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    //외래키. user 폴더 추가되면 수정
    //@ManyToOne
    //@JoinColumn(name = "approved_by")
    //private User approvedBy; // 사장님 승인자

    @Enumerated(EnumType.STRING)
    private RequestType requestType; // 전체 or 특정 알바생

    @Enumerated(EnumType.STRING)
    private Status status; // 대기, 승인, 거절

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

// 근무 변경 요청 유형
enum RequestType {
    ALL,       // 전체 알바생에게 변경 요청
    SPECIFIC   // 특정 알바생 지정
}

// 상태
enum Status {
    PENDING, // 대기
    APPROVED, // 승인
    REJECTED // 거절
}
