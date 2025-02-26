package com.example.albaease.store.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRequestDto {
    @NotBlank(message = "사업자등록번호는 필수입니다.")
    @Pattern(regexp = "^\\d{10}$", message = "사업자등록번호는 10자리 숫자여야 합니다.")
    private String businessNumber;

    @NotBlank(message = "매장명은 필수입니다.")
    private String name;

    @NotBlank(message = "주소는 필수입니다.")
    private String location;

    @NotBlank(message = "대표자명은 필수입니다.")
    private String ownerName;

    @NotBlank(message = "연락처는 필수입니다.")
    @Pattern(regexp = "^\\d{10,11}$", message = "연락처는 10-11자리 숫자여야 합니다.")
    private String contactNumber;
}
