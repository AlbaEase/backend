  -- Users 데이터 생성
-- Users 데이터 생성 (BCrypt로 해시된 비밀번호)
  INSERT INTO user (last_name, first_name, login_id, password, phone_number, role, business_number, created_at, social_type) VALUES
   ('김', '가윤', 'user1', '$2a$10$dL4az.borMw9.kRwGBlxvOUJx3vwIPH/z62FmHLJsLpjGdVrZjPeq', '01011112222', 'OWNER', '1234567890', NOW(), 'NONE'),
   ('조', '유성', 'user2', '$2a$10$yMLBwMTuS8GrE/HEZiMY/.r7B1Xr2/EUOVjFzFUgLUmUetzY94Q2a', '01022223333', 'OWNER', '1234567891', NOW(), 'NONE'),
   ('김', '지희', 'user3', '$2a$10$nqgJ.T7EphrUFQqJfmjlqeLIFZ6KvO2VeSW4myUqZKHbXRSU6Qv46', '01033334444', 'WORKER', NULL, NOW(), 'NONE'),
   ('김', '시현', 'user4', '$2a$10$CibQUAm.wzQMvZK0YuKXsO7NGm8q.Z.Eqv4Ol8i5A3OkdRZxu7iQC', '01044445555', 'WORKER', NULL, NOW(), 'NONE'),
   ('이', '서영', 'user5', '$2a$10$bM8DmhDFc8XRRr9PZmKjyOHZQniX8pVyQx0GhFo0zDjZO0nEcJIXG', '01055556666', 'WORKER', NULL, NOW(), 'NONE'),
   ('조', '정현', 'user6', '$2a$10$QJ9Uu6Zq3OwCFQB7Wyh.bOX0jMUSWMJWnQYkZZiZkNPjPMWAuq.rO', '01066667777', 'WORKER', NULL, NOW(), 'NONE'),
   ('이', '은우', 'user7', '$2a$10$Sk5LFbTIh6M1uKzrndYzOecZaOtMdgT2Ona7qz3Q3Bn5hbDX1JSLC', '01077778888', 'WORKER', NULL, NOW(), 'NONE');

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
