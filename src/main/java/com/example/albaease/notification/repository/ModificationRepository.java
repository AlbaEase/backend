package com.example.albaease.notification.repository;

import com.example.albaease.notification.domain.entity.Modification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModificationRepository extends JpaRepository<Modification, Long> {
}
