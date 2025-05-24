package com.example.albaease.store.repository;

import com.example.albaease.store.domain.UserStoreRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserStoreRelationRepository extends JpaRepository<UserStoreRelation, Long> {
    List<UserStoreRelation> findByUserId(Long userId);
}

