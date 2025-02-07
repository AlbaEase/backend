-- 기존 테이블이 있다면 삭제
DROP TABLE IF EXISTS shift;
DROP TABLE IF EXISTS modification;

-- 대타 요청 테이블
CREATE TABLE IF NOT EXISTS shift (
    request_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    from_user_id BIGINT,
    to_user_id BIGINT,
    schedule_id BIGINT,
    approved_by BIGINT,
    request_type VARCHAR(50),
    status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
    );

-- 수정 요청 테이블
CREATE TABLE IF NOT EXISTS modification (
    modification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    schedule_id BIGINT,
    details TEXT,
    status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
    );
