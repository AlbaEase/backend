package com.example.albaease.store.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BusinessNumberValidator {

    // 상태조회 API URL로 변경
    private static final String API_URL_STATUS = "https://api.odcloud.kr/api/nts-businessman/v1/status";
    // 디코딩(Decoding) 키로 반영
    private static final String SERVICE_KEY = "iqYfkCCw0J0iQfvkpNj6suohKrrXLOw5g14hlN4zToWUnK0UYyy3cEiapdWBQyOenoSQOx0z9TCaZjSfkLGdDQ==";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean validateBusinessNumber(String businessNumber) {
        try {
            String cleanBusinessNumber = businessNumber.replace("-", "");
            log.info("✅ 사업자번호 요청: {}", cleanBusinessNumber);

            Map<String, List<String>> requestData = new HashMap<>();
            requestData.put("b_no", List.of(cleanBusinessNumber));

            String requestBody = objectMapper.writeValueAsString(requestData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            String requestUrl = API_URL_STATUS + "?serviceKey=" + SERVICE_KEY + "&returnType=JSON";

            ResponseEntity<Map> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, Map.class);
            Map<String, Object> responseBody = responseEntity.getBody();
            log.info("🔁 응답: {}", responseBody);

            if (responseBody != null && responseBody.containsKey("data")) {
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) responseBody.get("data");
                if (!dataList.isEmpty()) {
                    Map<String, Object> data = dataList.get(0);

                    // b_stt_cd가 존재하고 01 또는 02인 경우
                    String statusCd = (String) data.get("b_stt_cd");
                    String status = (String) data.get("b_stt");

                    log.info("✅ 사업자 상태 코드: {}", statusCd);
                    log.info("✅ 사업자 상태: {}", status);

                    // 상황에 따라 문자열 비교 fallback
                    if ("01".equals(statusCd) || "02".equals(statusCd)) return true;
                    if ("계속사업자".equals(status) || "휴업자".equals(status)) return true;
                }
            }
        } catch (Exception e) {
            log.error("❌ 사업자번호 검증 오류: {}", e.getMessage(), e);
        }
        return false;
    }
}
