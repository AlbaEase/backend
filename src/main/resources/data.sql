  -- Users 데이터 생성
  INSERT INTO user (last_name, first_name, login_id, password, phone_number, role, business_number, created_at, social_type) VALUES
    ('김', '가윤', 'user1', 'hashedpw123', '01011112222', '사장님', '1234567890', NOW(), 'none'),
    ('조', '유성', 'user2', 'hashedpw124', '01022223333', '사장님', '1234567891', NOW(), 'none'),
    ('김', '지희', 'user3', 'hashedpw125', '01033334444', '알바생', NULL, NOW(), 'none'),
    ('김', '시현', 'user4', 'hashedpw126', '01044445555', '알바생', NULL, NOW(), 'none'),
    ('이', '서영', 'user5', 'hashedpw127', '01055556666', '알바생', NULL, NOW(), 'none'),
    ('조', '정현', 'user6', 'hashedpw128', '01066667777', '알바생', NULL, NOW(), 'none'),
    ('이', '은우', 'user7', 'hashedpw129', '01077778888', '알바생', NULL, NOW(), 'none');

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
