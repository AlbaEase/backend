package com.example.albaease.store.domain;

import com.example.albaease.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "UserStoreRelation")
public class UserStoreRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private User user;// FK1: User 엔티티와 연결

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false, referencedColumnName = "store_id")
    private Store store;// FK2: Store 엔티티와 연결

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;// '알바생' 또는 '사장님'

    @Column(name = "work_start_date", nullable = false)
    private LocalDateTime workStartDate; // 근무 시작 날짜

    @Column(name = "left_at")
    private LocalDateTime leftAt; // 퇴사 날짜 (NULL이면 근무 중)

    /*public enum Role {
        알바생, 사장님
    }*/
}
