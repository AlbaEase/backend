package com.example.albaease.schedule.domain;

package com.example.albaease.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModificationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modificationId;

    //외래키. user 폴더 추가되면 수정
    //@ManyToOne
    //@JoinColumn(name = "user_id", nullable = false)
    //private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    private String details; // 수정 요청 내용

    @Enumerated(EnumType.STRING)
    private Status status; // 대기, 승인, 거절

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
