SET FOREIGN_KEY_CHECKS=0;
-- 스토어 더미 데이터
INSERT INTO store (name, location, store_code, require_approval, business_number, created_at)
SELECT '맥도날드 강남점', '서울 강남구', 'STORE001', false, '123-45-67890', NOW()
    WHERE NOT EXISTS (
    SELECT 1
    FROM store
    WHERE store_code = 'STORE001'
);

INSERT INTO store (name, location, store_code, require_approval, business_number, created_at)
SELECT '스타벅스 홍대점', '서울 마포구', 'STORE002', false, '234-56-78901', NOW()
    WHERE NOT EXISTS (
    SELECT 1
    FROM store
    WHERE store_code = 'STORE002'
);

-- 사용자 더미 데이터
INSERT INTO user (last_name, first_name, login_id, password, phone_number, role, store_id, business_number, created_at, social_type)
SELECT '김', '가윤', 'boss1', '$2a$10$vL7Y7c6oAdqJR5DCLJSgOO0/770kFGAq/vN2rhZAO5rzSZrvYGEou', '010-1111-1111', 'OWNER', 1, '123-45-67890', NOW(), 'NONE'
    WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE login_id = 'boss1'
);

INSERT INTO user (last_name, first_name, login_id, password, phone_number, role, store_id, business_number, created_at, social_type)
SELECT '조', '유성', 'boss2', '$2a$10$vL7Y7c6oAdqJR5DCLJSgOO0/770kFGAq/vN2rhZAO5rzSZrvYGEou', '010-2222-2222', 'OWNER', 2, '234-56-78901', NOW(), 'NONE'
    WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE login_id = 'boss2'
);

INSERT INTO user (last_name, first_name, login_id, password, phone_number, role, store_id, business_number, created_at, social_type)
SELECT '김', '시현', 'staff1', '$2a$10$vL7Y7c6oAdqJR5DCLJSgOO0/770kFGAq/vN2rhZAO5rzSZrvYGEou', '010-3333-3333', 'WORKER', 1, NULL, NOW(), 'NONE'
    WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE login_id = 'staff1'
);

INSERT INTO user (last_name, first_name, login_id, password, phone_number, role, store_id, business_number, created_at, social_type)
SELECT '김', '지희', 'staff2', '$2a$10$vL7Y7c6oAdqJR5DCLJSgOO0/770kFGAq/vN2rhZAO5rzSZrvYGEou', '010-4444-4444', 'WORKER', 1, NULL, NOW(), 'NONE'
    WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE login_id = 'staff2'
);

INSERT INTO user (last_name, first_name, login_id, password, phone_number, role, store_id, business_number, created_at, social_type)
SELECT '이', '서영', 'staff3', '$2a$10$vL7Y7c6oAdqJR5DCLJSgOO0/770kFGAq/vN2rhZAO5rzSZrvYGEou', '010-5555-5555', 'WORKER', 1, NULL, NOW(), 'NONE'
    WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE login_id = 'staff3'
);

INSERT INTO user (last_name, first_name, login_id, password, phone_number, role, store_id, business_number, created_at, social_type)
SELECT '조', '정현', 'staff4', '$2a$10$vL7Y7c6oAdqJR5DCLJSgOO0/770kFGAq/vN2rhZAO5rzSZrvYGEou', '010-6666-6666', 'WORKER', 2, NULL, NOW(), 'NONE'
    WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE login_id = 'staff4'
);

INSERT INTO user (last_name, first_name, login_id, password, phone_number, role, store_id, business_number, created_at, social_type)
SELECT '이', '은우', 'staff5', '$2a$10$vL7Y7c6oAdqJR5DCLJSgOO0/770kFGAq/vN2rhZAO5rzSZrvYGEou', '010-7777-7777', 'WORKER', 2, NULL, NOW(), 'NONE'
    WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE login_id = 'staff5'
);

-- 유저-스토어 관계 더미 데이터
INSERT INTO user_store_relationship (user_id, store_id, work_start_date)
SELECT 1, 1, NOW()
    WHERE NOT EXISTS (
    SELECT 1
    FROM user_store_relationship
    WHERE user_id = 1 AND store_id = 1
);

INSERT INTO user_store_relationship (user_id, store_id, work_start_date)
SELECT 2, 2, NOW()
    WHERE NOT EXISTS (
    SELECT 1
    FROM user_store_relationship
    WHERE user_id = 2 AND store_id = 2
);

INSERT INTO user_store_relationship (user_id, store_id, work_start_date)
SELECT 3, 1, NOW()
    WHERE NOT EXISTS (
    SELECT 1
    FROM user_store_relationship
    WHERE user_id = 3 AND store_id = 1
);

INSERT INTO user_store_relationship (user_id, store_id, work_start_date)
SELECT 4, 1, NOW()
    WHERE NOT EXISTS (
    SELECT 1
    FROM user_store_relationship
    WHERE user_id = 4 AND store_id = 1
);

INSERT INTO user_store_relationship (user_id, store_id, work_start_date)
SELECT 5, 1, NOW()
    WHERE NOT EXISTS (
    SELECT 1
    FROM user_store_relationship
    WHERE user_id = 5 AND store_id = 1
);

INSERT INTO user_store_relationship (user_id, store_id, work_start_date)
SELECT 6, 2, NOW()
    WHERE NOT EXISTS (
    SELECT 1
    FROM user_store_relationship
    WHERE user_id = 6 AND store_id = 2
);

INSERT INTO user_store_relationship (user_id, store_id, work_start_date)
SELECT 7, 2, NOW()
    WHERE NOT EXISTS (
    SELECT 1
    FROM user_store_relationship
    WHERE user_id = 7 AND store_id = 2
);

-- 스케줄 더미 데이터
INSERT INTO schedule (store_id, user_id, work_date, start_time, end_time, break_time, repeat_days, repeat_end_date)
SELECT 1, 3, '2024-02-27', '09:00:00', '18:00:00', '12:00:00', 'MON,WED,FRI', '2024-12-31'
    WHERE NOT EXISTS (
    SELECT 1
    FROM schedule
    WHERE store_id = 1 AND user_id = 3 AND work_date = '2024-02-27'
);

INSERT INTO schedule (store_id, user_id, work_date, start_time, end_time, break_time, repeat_days, repeat_end_date)
SELECT 1, 4, '2024-02-27', '10:00:00', '19:00:00', '13:00:00', 'TUE,THU,SAT', '2024-12-31'
    WHERE NOT EXISTS (
    SELECT 1
    FROM schedule
    WHERE store_id = 1 AND user_id = 4 AND work_date = '2024-02-27'
);

INSERT INTO schedule (store_id, user_id, work_date, start_time, end_time, break_time, repeat_days, repeat_end_date)
SELECT 1, 5, '2024-02-27', '11:00:00', '20:00:00', '14:00:00', 'MON,WED,FRI', '2024-12-31'
    WHERE NOT EXISTS (
    SELECT 1
    FROM schedule
    WHERE store_id = 1 AND user_id = 5 AND work_date = '2024-02-27'
);

INSERT INTO schedule (store_id, user_id, work_date, start_time, end_time, break_time, repeat_days, repeat_end_date)
SELECT 2, 6, '2024-02-27', '09:00:00', '18:00:00', '12:00:00', 'TUE,THU,SAT', '2024-12-31'
    WHERE NOT EXISTS (
    SELECT 1
    FROM schedule
    WHERE store_id = 2 AND user_id = 6 AND work_date = '2024-02-27'
);

INSERT INTO schedule (store_id, user_id, work_date, start_time, end_time, break_time, repeat_days, repeat_end_date)
SELECT 2, 7, '2024-02-27', '10:00:00', '19:00:00', '13:00:00', 'MON,WED,FRI', '2024-12-31'
    WHERE NOT EXISTS (
    SELECT 1
    FROM schedule
    WHERE store_id = 2 AND user_id = 7 AND work_date = '2024-02-27'
);
SET FOREIGN_KEY_CHECKS=1;
