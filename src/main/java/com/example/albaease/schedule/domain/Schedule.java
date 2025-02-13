package com.example.albaease.schedule.domain;

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

    // api테스트용, 병합 후 삭제 해야함
    private Long userId;  // 임시 유저 아이디
    private Long storeId; // 임시 스토어 아이디

    // 나중에 바꿀용
    /*@ManyToOne(fetch = FetchType.LAZY) // user_id 외래키
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) // store_id 외래키
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;*/

    private LocalDate workDate;  // 근무 날짜
    private LocalTime startTime; // 시작 시간
    private LocalTime endTime;   // 종료 시간
    private LocalTime breakTime; // 휴게 시간

    // SET은 비트마스크 사용해서 복잡하다길래 일단 string으로 했는데 연동 중 수정해야할 수도,,
    @Column(name = "repeat_days", nullable = true)
    private String repeatDays;

    @Column(name = "repeat_end_date", nullable = true)
    private LocalDate repeatEndDate; // 반복 종료 날짜

    // 임시
    public List<String> getRepeatDaysList() {
        return repeatDays != null ? Arrays.asList(repeatDays.split(",")) : Collections.emptyList();
    }

    public void setRepeatDaysFromList(List<String> days) {
        this.repeatDays = days != null ? String.join(",", days) : null;
    }
}

