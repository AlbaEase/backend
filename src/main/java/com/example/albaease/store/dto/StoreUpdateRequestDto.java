package com.example.albaease.store.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreUpdateRequestDto {
    @NotBlank(message = "매장명은 필수입니다.")
    private String name;

    @NotBlank(message = "주소는 필수입니다.")
    private String location;

    @NotBlank(message = "연락처는 필수입니다.")
    @Pattern(regexp = "^\\d{10,11}$", message = "연락처는 10-11자리 숫자여야 합니다.")
    private String contactNumber;
}
