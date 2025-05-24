package com.example.albaease.store.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BusinessVerificationResponse {
    private String status_code;
    private int match_cnt;
    private int request_cnt;
    private List<BusinessData> data;

    @Getter
    @Setter
    public static class BusinessData {
        private String b_no;
        private String b_stt;
        private String b_stt_cd;
    }
}
