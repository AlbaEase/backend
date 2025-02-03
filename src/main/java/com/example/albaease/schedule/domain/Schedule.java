package com.example.albaease.schedule.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "Schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @Column(nullable = false)
    private Long userId;

    //외래키. user 폴더 추가되면 수정
    //@ManyToOne
    //@JoinColumn(name = "user_id", nullable = false)
    //private User user;
    @Column(nullable = false)
    private Long storeId;

    //외래키. store 폴더 추가되면 수정
    //@ManyToOne
    //@JoinColumn(name = "store_id", nullable = false)
    //private Store store;

    @Column(nullable = false)
    private LocalDate shiftDate;

    @Column(nullable = false)
    private LocalTime shiftStartTime;

    @Column(nullable = false)
    private LocalTime shiftEndTime;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public LocalDate getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(LocalDate shiftDate) {
        this.shiftDate = shiftDate;
    }

    public LocalTime getShiftStartTime() {
        return shiftStartTime;
    }

    public void setShiftStartTime(LocalTime shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    public LocalTime getShiftEndTime() {
        return shiftEndTime;
    }

    public void setShiftEndTime(LocalTime shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
