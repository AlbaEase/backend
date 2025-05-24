package com.example.albaease.schedule.domain;

import com.example.albaease.store.domain.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "template")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateId; // 템플릿 ID

    // Store 테이블과의 관계 설정 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // 외래키로 Store 연결

    @Column(nullable = false)
    private String templateName; // 템플릿 이름

    @Column(nullable = false)
    private LocalTime startTime; // 근무 시작 시간

    @Column(nullable = false)
    private LocalTime endTime; // 근무 종료 시간

    @Column(nullable = false)
    private LocalTime breakTime; // 휴게 시간
}
