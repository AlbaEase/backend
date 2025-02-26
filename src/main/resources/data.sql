  -- Users 데이터 생성
  INSERT INTO User (name, phone_number, password, role, business_number, created_at, social_type, kakao_id) VALUES
    ('김가윤', '01011112222', 'hashedpw123', '사장님', '1234567890', NOW(), 'none', NULL),
    ('조유성', '01022223333', 'hashedpw124', '사장님', '1234567891', NOW(), 'none', NULL),
    ('김지희', '01033334444', 'hashedpw125', '알바생', NULL, NOW(), 'none', NULL),
    ('김시현', '01044445555', 'hashedpw126', '알바생', NULL, NOW(), 'none', NULL),
    ('이서영', '01055556666', 'hashedpw127', '알바생', NULL, NOW(), 'none', NULL),
    ('조정현', '01066667777', 'hashedpw128', '알바생', NULL, NOW(), 'none', NULL),
    ('이은우', '01077778888', 'hashedpw129', '알바생', NULL, NOW(), 'none', NULL);

  -- Stores 데이터 생성
 INSERT INTO Store (store_code, name, location, require_approval, created_at) VALUES
   ('STORE001', '알바이즈 카페', '서울시 강남구 역삼동', true, NOW()),
   ('STORE002', '이지알바 편의점', '서울시 서초구 서초동', false, NOW());

 -- User_Store_Relationship 데이터 생성
 INSERT INTO User_Store_Relationship (user_id, store_id, role, work_start_date) VALUES
   (1, 1, '사장님', NOW()),  -- 김가윤-카페
   (2, 2, '사장님', NOW()),  -- 조유성-편의점
   (3, 1, '알바생', NOW()),  -- 김지희-카페
   (4, 1, '알바생', NOW()),  -- 김시현-카페
   (5, 1, '알바생', NOW()),  -- 이서영-카페
   (6, 2, '알바생', NOW()),  -- 조정현-편의점
   (7, 2, '알바생', NOW());  -- 이은우-편의점

 -- Schedule 데이터 생성
 INSERT INTO Schedule (user_id, store_id, work_date, start_time, end_time, break_time, repeat_days, repeat_end_date) VALUES
   (3, 1, '2025-02-24', '09:00:00', '15:00:00', '12:00:00', '월,수,금', '2025-03-24'),
   (4, 1, '2025-02-24', '14:00:00', '21:00:00', '17:00:00', '화,목,토', '2025-03-24'),
   (5, 1, '2025-02-24', '10:00:00', '17:00:00', '13:00:00', '수,금,일', '2025-03-24'),
   (6, 2, '2025-02-24', '06:00:00', '14:00:00', '10:00:00', '월,수,금', '2025-03-24'),
   (7, 2, '2025-02-24', '14:00:00', '22:00:00', '18:00:00', '화,목,토', '2025-03-24');
