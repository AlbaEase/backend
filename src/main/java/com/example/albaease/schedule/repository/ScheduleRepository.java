package com.example.albaease.schedule.repository;

import com.example.albaease.schedule.domain.Schedule;
import com.example.albaease.schedule.dto.ScheduleResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 스토어 ID로 스케줄 조회
    List<Schedule> findByStoreId(Long storeId);

    // 특정 사용자의 매장별 스케줄 목록 조회
    List<Schedule> findByStoreIdAndUserId(Long storeId, Long userId);

    // 특정 사용자의 스케줄 목록 조회
    List<Schedule> findByUserId(Long userId);

    // 특정 날짜의 스케줄 조회. 필요한가..?
    List<Schedule> findByWorkDate(LocalDate workDate);

    @Query("SELECT new com.example.albaease.schedule.dto.ScheduleResponse(s.scheduleId, s.userId, s.storeId, s.workDate, s.startTime, s.endTime, s.breakTime, s.repeatDays, s.repeatEndDate) FROM Schedule s WHERE s.storeId = :storeId")
    List<ScheduleResponse> findResponsesByStoreId(@Param("storeId") Long storeId);
}

