package com.example.albaease.store.repository;

import com.example.albaease.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
    Optional<Store> findByStoreCode(String storeCode);

    @Query("SELECT s FROM Store s JOIN UserStoreRelation usr ON s = usr.store WHERE usr.user.userId = :userId AND usr.role = :role")
    List<Store> findByPartTimerId(@Param("userId") Long userId);

    @Query("SELECT s FROM Store s JOIN UserStoreRelation usr ON s = usr.store WHERE usr.user.userId = :userId AND usr.role = 'OWNER'")
    List<Store> findByOwnerId(@Param("userId") Long userId);

}
