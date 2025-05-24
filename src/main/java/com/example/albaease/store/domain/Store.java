package com.example.albaease.store.domain;

import com.example.albaease.store.dto.StoreRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "store_id")
    private int storeId;

    @Column(unique = true, nullable = false)
    private String storeCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String businessNumber;

    @Column
    private String ownerName;

    @Column
    private String startDate;


    private String location;
    private Boolean requiresApproval;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Store(String storeCode, String name, String location, Boolean requiresApproval,
                 String businessNumber, String ownerName, String startDate) {
        this.storeCode = storeCode;
        this.name = name;
        this.location = location;
        this.requiresApproval = requiresApproval;
        this.businessNumber = businessNumber;
        this.ownerName = ownerName;
        this.startDate = startDate;
    }

    // 매장 정보 업데이트
    public void update(StoreRequestDto requestDto) {
        this.storeCode = requestDto.getStoreCode();
        this.name = requestDto.getName();
        this.location = requestDto.getLocation();
        this.requiresApproval = requestDto.getRequiresApproval();
        this.businessNumber = requestDto.getBusinessNumber();
        this.ownerName = requestDto.getOwnerName();
        this.startDate = requestDto.getStartDate();
    }
}
