SET FOREIGN_KEY_CHECKS=0;

-- 스토어 더미 데이터
INSERT IGNORE INTO store (name, location, store_code, require_approval, business_number, created_at) VALUE
    ('맥도날드 강남점', '서울 강남구', 'STORE001', false, '123-45-67890', NOW()),
    ('스타벅스 홍대점', '서울 마포구', 'STORE002', false, '234-56-78901', NOW()),
    ('롯데리아 가좌점', '인천 서구', 'STORE003', false, '222-56-78901', NOW());

-- 사용자 더미 데이터
INSERT IGNORE INTO user (
    last_name, first_name, login_id, password, phone_number,
    role, store_id, business_number, created_at, social_type
) VALUES
      ('김', '가윤', 'boss1', '$2a$10$vL7Y7c6oAdqJR5DCLJSgOO0/770kFGAq/vN2rhZAO5rzSZrvYGEou', '010-1111-1111', 'OWNER', 1, '123-45-67890', NOW(), 'NONE'),
      ('조', '유성', 'boss2', '$2a$10$vL7Y7c6oAdqJR5DCLJSgOO0/770kFGAq/vN2rhZAO5rzSZrvYGEou', '010-2222-2222', 'OWNER', 2, '234-56-78901', NOW(), 'NONE'),
      ('김', '시현', 'staff1', '$2a$10$vL7Y7c6oAdqJR5DCLJSgOO0/770kFGAq/vN2rhZAO5rzSZrvYGEou', '010-3333-3333', 'WORKER', 1, NULL, NOW(), 'NONE'),
      ('김', '지희', 'staff2', '$2a$10$vL7Y7c6oAdqJR5DCLJSgOO0/770kFGAq/vN2rhZAO5rzSZrvYGEou', '010-4444-4444', 'WORKER', 1, NULL, NOW(), 'NONE'),
      ('이', '서영', 'staff3', '$2a$10$vL7Y7c6oAdqJR5DCLJSgOO0/770kFGAq/vN2rhZAO5rzSZrvYGEou', '010-5555-5555', 'WORKER', 1, NULL, NOW(), 'NONE'),
      ('조', '정현', 'staff4', '$2a$10$vL7Y7c6oAdqJR5DCLJSgOO0/770kFGAq/vN2rhZAO5rzSZrvYGEou', '010-6666-6666', 'WORKER', 2, NULL, NOW(), 'NONE'),
      ('이', '은우', 'staff5', '$2a$10$vL7Y7c6oAdqJR5DCLJSgOO0/770kFGAq/vN2rhZAO5rzSZrvYGEou', '010-7777-7777', 'WORKER', 2, NULL, NOW(), 'NONE');

-- 유저-스토어 관계 더미 데이터
INSERT IGNORE INTO user_store_relationship (user_id, store_id, work_start_date) VALUES
     (1, 1, NOW()),
     (1, 3, NOW()),
     (2, 2, NOW()),
     (3, 1, NOW()),
     (4, 1, NOW()),
     (5, 1, NOW()),
     (6, 2, NOW()),
     (7, 2, NOW());

-- 스케줄 더미 데이터
INSERT IGNORE INTO schedule (
    store_id, user_id, work_date, start_time, end_time,
    break_time, repeat_days, repeat_end_date
) VALUES
      (1, 3, '2024-02-27', '09:00:00', '18:00:00', '12:00:00', 'MON,WED,FRI', '2024-12-31'),
      (1, 4, '2024-02-27', '10:00:00', '19:00:00', '13:00:00', 'TUE,THU,SAT', '2024-12-31'),
      (1, 5, '2024-02-27', '11:00:00', '20:00:00', '14:00:00', 'MON,WED,FRI', '2024-12-31'),
      (2, 6, '2024-02-27', '09:00:00', '18:00:00', '12:00:00', 'TUE,THU,SAT', '2024-12-31'),
      (2, 7, '2024-02-27', '10:00:00', '19:00:00', '13:00:00', 'MON,WED,FRI', '2024-12-31');

SET FOREIGN_KEY_CHECKS=1;
