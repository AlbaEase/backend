package com.example.albaease.store.repository;

import com.example.albaease.store.domain.UserStoreRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStoreRelationshipRepository extends JpaRepository<UserStoreRelationship, Long> {
    Optional<UserStoreRelationship> findByUser_UserIdAndRole(Long userId, String role);

    Optional<UserStoreRelationship> findByUser_UserIdAndStore_IdAndRole(Long userId, Long storeId, String role);
}
