-- INSERT INTO TESTUSER VALUES
-- (1,'PeterM','ABC123abc*','peter@email.com','2020-03-17 07:13:30',NULL,'STUDENT','2020-03-17'),
-- (2,'Mike','password','mike@email.com','2020-03-18 14:59:35',NULL,'EMPLOYEE','2020-03-18'),
-- (3,'KingPeter','password','kingpeter@email.com','2020-03-19 12:19:15',NULL,'EMPLOYEE','2020-03-18');

INSERT INTO authority (authority_name) VALUES
('ROLE_PTSTUDENT'),
('ROLE_PTTEACHER'),
('ROLE_USER');

INSERT INTO user_base (`role`,created_date,modified_date,address_detailed_address,address_street,address_zipcode,email,gender,nickname,password,profile_picture_addr,tel,username,authority) VALUES
('Student','2021-08-05 20:36:28.655474000','2021-08-05 20:36:28.655474000','싸피 사무국','대전광역시 유성구 동서대로 98-39','34153','chuu@ssafy.com',1,'츄','$2a$10$LD3wHUZ28afzrOxJe/kixeV2ZpgTfYOFqK9c7PV7Cxw5ihVr13Qte',NULL,'010-2021-0721','김지우',NULL),
('Student','2021-08-05 20:36:28.766994000','2021-08-05 20:36:28.766994000','싸피 사무국','광주광역시 광산구 하남산단 6번로 107','62218','olivia@ssafy.com',1,'올리비아 혜','$2a$10$I4FslU2TBpser/oY559sIe5oURyIzJL2W5UhBpTdFqqPyTPR7gqoO',NULL,'010-2001-1113','손혜주',NULL),
('Teacher','2021-08-05 20:36:28.858993000','2021-08-05 20:36:28.858993000','싸피 사무국','서울시 강남구 테헤란로 212','06220','eggkim@ssafy.com',0,'김계란','$2a$10$j1Y.sjK3vYQ7QDC2jODnhe6uZqRjMVInaUEU5tyoDH295badx8iWu',NULL,'010-2021-0105','김성식','ROLE_PTTEACHER');

INSERT INTO pt_teacher (description,major,price,star_rating,pt_teacher_id) VALUES
('좋았어. 거짓은 머리털만큼도 없다! 신뢰와 정직으로 모시겠습니다','특공무술/재활/스트레칭/마사지/통증완화',500000,4.0,3);

INSERT INTO pt_student (pt_student_id) VALUES
(1),
(2);

INSERT INTO sns (platform,url,pt_teacher_id) VALUES
('instagram','instagram.com/physical_gallery_egg',3),
('facebook','@egg-kim',3),
('youtube','youtube.com/channel/UCdtRAcd3L_UpV4tMXCw63NQ',3);

INSERT INTO career (company,end_date,`role`,start_date,pt_teacher_id) VALUES
('OGYM','2021-07-22','원장','2018-02-11',3);

INSERT INTO certificate (`date`,cert_name,publisher,pt_teacher_id) VALUES
('2015-11-11','NSCA','NSCA KOREA',3),
('2013-05-08','NASM-CPT','NASM KOREA',3),
('2015-03-21','NASM-PES','NASM KOREA',3),
('2021-01-30','NASM-CES','NASM KOREA',3);

INSERT INTO monthly (height,`month`,weight,pt_student_id) VALUES
(160,1,44,1),
(160,2,43,1),
(160,3,46,1),
(160,4,42,1),
(160,5,43,1),
(161,12,40,1);
INSERT INTO monthly (height,`month`,weight,pt_student_id) VALUES
(164,4,51,2),
(164,5,46,2),
(164,6,45,2),
(164,9,49,2),
(163,10,48,2),
(164,11,47,2);

INSERT INTO pt_student_pt_teacher (reservation_date,pt_student_id,pt_teacher_id) VALUES
('2021-07-28 13:00:00',1,3);
