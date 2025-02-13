package com.example.albaease.store.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storeId;

    @Column(unique = true, nullable = false)
    private String storeCode;

    @Column(nullable = false)
    private String name;

    private String location;
    private Boolean requiresApproval;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Store(String storeCode, String name, String location, Boolean requiresApproval) {
        this.storeCode = storeCode;
        this.name = name;
        this.location = location;
        this.requiresApproval = requiresApproval;
    }
}
