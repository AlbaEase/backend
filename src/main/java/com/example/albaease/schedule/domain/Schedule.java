package com.example.albaease.schedule.domain;

import com.example.albaease.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    private Long storeId; // 매장 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  // User와 관계 설정
    private User user;  // 유저 객체로 변경

    private LocalDate workDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakTime;

    @Column(name = "repeat_days")
    private String repeatDays;  // 반복 요일

    @Column(name = "repeat_end_date")
    private LocalDate repeatEndDate;  // 반복 종료 날짜

    // userId 반환 메서드
    public Long getUserId() {
        return user != null ? user.getUserId() : null;
    }

    public List<String> getRepeatDaysList() {
        return repeatDays != null ? Arrays.asList(repeatDays.split(",")) : Collections.emptyList();
    }

    public void setRepeatDaysFromList(List<String> days) {
        this.repeatDays = days != null ? String.join(",", days) : null;
    }
}


