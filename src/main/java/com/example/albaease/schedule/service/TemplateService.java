package com.example.albaease.schedule.service;

import com.example.albaease.schedule.domain.Template;
import com.example.albaease.schedule.dto.TemplateRequest;
import com.example.albaease.schedule.dto.TemplateResponse;
import com.example.albaease.schedule.repository.TemplateRepository;
import com.example.albaease.store.domain.Store;
import com.example.albaease.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final StoreRepository storeRepository;

    // 특정 매장의 템플릿 조회
    @Transactional(readOnly = true)
    public List<TemplateResponse> getTemplatesByStoreId(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));

        List<Template> templates = templateRepository.findByStore(store);
        return templates.stream()
                .map(TemplateResponse::new)
                .collect(Collectors.toList());
    }

    // 특정 매장에 템플릿 등록
    @Transactional
    public TemplateResponse createTemplate(Long storeId, TemplateRequest templateRequest) {
        // Store 확인
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with ID: " + storeId));

        Template template = new Template();
        template.setStore(store);
        template.setTemplateName(templateRequest.getTemplateName());
        template.setStartTime(templateRequest.getStartTime());
        template.setEndTime(templateRequest.getEndTime());
        template.setBreakTime(templateRequest.getBreakTime());

        Template savedTemplate = templateRepository.save(template);
        return new TemplateResponse(savedTemplate);
    }


    @Transactional(readOnly = true)
    public Template getTemplateById(Long templateId) {
        return templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found with id: " + templateId));
    }
}
