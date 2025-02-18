package com.example.albaease.schedule.repository;

import com.example.albaease.schedule.domain.Schedule;
import com.example.albaease.user.entity.User;
import com.example.albaease.schedule.dto.ScheduleResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 스토어 ID로 스케줄 조회
    List<Schedule> findByStoreId(Long storeId); // 수정 필요

    // 특정 사용자의 매장별 스케줄 목록 조회
    List<Schedule> findByStoreIdAndUser(Long storeId, User user);  // 병합하면서 수정한 부분인데 storeId부분 수정 필요

    // 특정 사용자의 스케줄 목록 조회
    List<Schedule> findByUser(User user);

    // 특정 날짜의 스케줄 조회
    List<Schedule> findByWorkDate(LocalDate workDate);

    List<Schedule> findByStoreIdAndUserId(Long storeId, Long userId);
}


