package com.example.albaease.notification.repository;

import com.example.albaease.notification.domain.entity.Notification;
import com.example.albaease.notification.domain.enums.NotificationStatus;
import com.example.albaease.notification.domain.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 특정 사용자의 모든 알림 목록 조회 (최신순)
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 읽지 않은 알림 개수 조회 (메인 페이지에 표시 용도)
    long countByUserIdAndStatus(Long userId, NotificationStatus status);
}
