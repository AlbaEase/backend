package com.example.albaease.store.service;

import com.example.albaease.store.dto.BusinessVerificationResponse;
import com.example.albaease.store.dto.BusinessVerificationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class BusinessVerificationService {

    private static final String API_URL_VALIDATE = "https://api.odcloud.kr/api/nts-businessman/v1/status";

    @Value("${api.service.key}") // application.properties에서 키를 가져옴
    private String serviceKey;

    private final RestTemplate restTemplate;

    public BusinessVerificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * API를 호출하여 사업자 상태 정보를 조회
     */
    public BusinessVerificationResponse verifyBusinessNumber(String businessNumber) {
        BusinessVerificationRequest request = new BusinessVerificationRequest(List.of(businessNumber));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<BusinessVerificationRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<BusinessVerificationResponse> responseEntity = restTemplate.exchange(
                API_URL_VALIDATE + "?serviceKey=" + serviceKey,
                HttpMethod.POST,
                requestEntity,
                BusinessVerificationResponse.class
        );

        return responseEntity.getBody();
    }

    /**
     * 사업자등록번호 유효성 여부를 boolean으로 반환
     */
    public boolean isBusinessNumberValid(String businessNumber) {
        BusinessVerificationResponse response = verifyBusinessNumber(businessNumber);

        if (response == null || response.getData() == null || response.getData().isEmpty()) {
            return false;
        }

        BusinessVerificationResponse.BusinessData data = response.getData().get(0);

        // "01"은 계속사업자 (유효), 그 외는 폐업 또는 등록되지 않은 사업자
        return "01".equals(data.getB_stt_cd());
    }
}
