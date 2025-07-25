package com.example.albaease.schedule.repository;

import com.example.albaease.schedule.domain.Schedule;
import com.example.albaease.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 스토어별 스케줄 조회 (Store 객체 기반)
    List<Schedule> findByStore_Id(Long storeId);

    // 특정 사용자의 스케줄 목록 조회
    List<Schedule> findByUser(User user);

    // 특정 날짜의 스케줄 조회
    List<Schedule> findByWorkDate(LocalDate workDate);

    // 특정 스토어와 사용자의 스케줄 조회
    List<Schedule> findByStore_IdAndUser_UserId(Long storeId, Long userId);

    // 특정 사용자와 날짜에 스케줄이 존재하는지 확인
    boolean existsByUserAndWorkDate(User user, LocalDate workDate);


    List<Schedule> findByUser_UserIdAndWorkDateBetween(Long userId, LocalDate start, LocalDate end);

    List<Schedule> findByStore_IdAndUser_UserIdAndWorkDateBetween(Long storeId, Long userId, LocalDate start, LocalDate end);

    @Query("SELECT DISTINCT s.user.userId FROM Schedule s " +
            "WHERE s.store.id = :storeId " +
            "AND (" +
            // 1. 단일 날짜 충돌
            " (s.workDate = :requestDate AND s.startTime < :endTime AND s.endTime > :startTime) " +
            // 2. 반복 요일 충돌
            " OR (" +
            "     s.workDate IS NULL AND s.repeatDays IS NOT NULL AND s.repeatEndDate >= :requestDate " +
            "     AND FUNCTION('FIND_IN_SET', :dayOfWeekStr, s.repeatDays) > 0 " +
            "     AND s.startTime < :endTime AND s.endTime > :startTime" +
            "     )" +
            ")")
    List<Long> findBusyUserIdsIncludingRepeats(@Param("storeId") Long storeId,
                                               @Param("requestDate") LocalDate requestDate,
                                               @Param("startTime") LocalTime startTime,
                                               @Param("endTime") LocalTime endTime,
                                               @Param("dayOfWeekStr") String dayOfWeekStr);


}
