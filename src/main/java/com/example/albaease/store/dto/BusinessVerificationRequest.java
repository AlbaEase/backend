package com.example.albaease.store.dto;

import java.util.List;

public class BusinessVerificationRequest {
    private List<String> b_no;  // <-- 필드 이름 반드시 "b_no"로 해야 함

    public BusinessVerificationRequest(List<String> b_no) {
        this.b_no = b_no;
    }

    public List<String> getB_no() {
        return b_no;
    }

    public void setB_no(List<String> b_no) {
        this.b_no = b_no;
    }
}
