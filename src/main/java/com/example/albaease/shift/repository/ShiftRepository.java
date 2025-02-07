package com.example.albaease.shift.repository;

import com.example.albaease.shift.domain.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByToUserIdOrderByCreatedAtDesc(Long toUserId);
}
