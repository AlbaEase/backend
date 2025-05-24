package com.example.albaease.schedule.repository;

import com.example.albaease.schedule.domain.Template;
import com.example.albaease.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    // 특정 스토어 템플릿 조회 (Store 엔티티와 관계로 조회)
    List<Template> findByStore(Store store);

    // 템플릿 이름으로 조회
    List<Template> findByTemplateName(String templateName);
}
