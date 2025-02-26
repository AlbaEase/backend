package com.example.albaease.store.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusinessNumberValidator {

    private static final String API_URL_VALIDATE = "https://api.odcloud.kr/api/nts-businessman/v1/validate";
    private static final String SERVICE_KEY = "iqYfkCCw0J0iQfvkpNj6suohKrrXLOw5g14hlN4zToWUnK0UYyy3cEiapdWBQyOenoSQOx0z9TCaZjSfkLGdDQ=="; // 실제 서비스 키로 대체

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public boolean validateBusinessNumber(String businessNumber) {
        // 요청 데이터 생성 (나머지 항목은 빈 문자열 처리)
        Map<String, Object> requestData = new HashMap<>();
        Map<String, String> businessData = new HashMap<>();

        businessData.put("b_no", businessNumber);
        businessData.put("start_dt", "");  // 필수 필드 추가
        businessData.put("p_nm", "");      // 필수 필드 추가
        businessData.put("p_nm2", "");     // 필수 필드 추가
        businessData.put("b_nm", "");      // 필수 필드 추가
        businessData.put("corp_no", "");   // 필수 필드 추가
        businessData.put("b_sector", "");  // 필수 필드 추가
        businessData.put("b_type", "");    // 필수 필드 추가
        businessData.put("b_adr", "");     // 필수 필드 추가

        requestData.put("businesses", List.of(businessData));

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // 요청 엔티티 설정
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestData, headers);

        try {
            // API 호출 (POST 요청)
            String requestUrl = API_URL_VALIDATE + "?serviceKey=" + SERVICE_KEY + "&returnType=JSON";
            ResponseEntity<Map> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, Map.class);

            // 응답 데이터 파싱
            Map<String, Object> responseBody = responseEntity.getBody();
            if (responseBody != null && responseBody.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) ((List<Object>) responseBody.get("data")).get(0);  // 첫 번째 사업자 정보 추출
                // 유효한 사업자등록번호인지 확인
                return "01".equals(data.get("valid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
