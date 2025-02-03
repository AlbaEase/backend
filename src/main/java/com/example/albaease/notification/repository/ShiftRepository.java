package com.example.albaease.notification.repository;

import com.example.albaease.notification.domain.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
}
