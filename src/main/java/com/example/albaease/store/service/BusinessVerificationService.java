package com.example.albaease.store.service;

import com.example.albaease.store.dto.BusinessVerificationRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class BusinessVerificationService {

    private static final String API_URL_VALIDATE = "https://api.odcloud.kr/api/nts-businessman/v1/validate";
    private static final String SERVICE_KEY = "iqYfkCCw0J0iQfvkpNj6suohKrrXLOw5g14hlN4zToWUnK0UYyy3cEiapdWBQyOenoSQOx0z9TCaZjSfkLGdDQ=="; // 실제 서비스 키로 대체

    private final RestTemplate restTemplate = new RestTemplate();


    public boolean validateBusinessNumber(String businessNumber) {
        final String API_URL = "https://api.odcloud.kr/api/nts-businessman/v1/status";
        final String SERVICE_KEY = "YOUR_SERVICE_KEY"; // 공공 API 키 입력

        final RestTemplate restTemplate = new RestTemplate();

        public com.example.albaease.dto.BusinessVerificationResponse verifyBusinessNumber(String businessNumber) {
            // 요청 객체 생성
            BusinessVerificationRequest request = new BusinessVerificationRequest(Collections.singletonList(businessNumber));

            // HTTP 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 엔티티 생성
            HttpEntity<BusinessVerificationRequest> requestEntity = new HttpEntity<>(request, headers);

            // API 호출
            ResponseEntity<com.example.albaease.dto.BusinessVerificationResponse> responseEntity =
                    restTemplate.exchange(API_URL + "?serviceKey=" + SERVICE_KEY,
                            HttpMethod.POST, requestEntity, com.example.albaease.dto.BusinessVerificationResponse.class);

            return responseEntity.getBody();
        }
}
