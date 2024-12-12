use club;

INSERT INTO Club (clubName) VALUES ('네스트넷');
INSERT INTO Club (clubName) VALUES ('샘마루');
INSERT INTO Club (clubName) VALUES ('큐빅');
INSERT INTO Club (clubName) VALUES ('엠시스');
INSERT INTO Club (clubName) VALUES ('노바');
INSERT INTO Club (clubName) VALUES ('PDA');
INSERT INTO Club (clubName) VALUES ('턱스');

INSERT INTO ClubProfessor (clubID, professorID) VALUES (1, 1);
INSERT INTO ClubProfessor (clubID, professorID) VALUES (2, 2);
INSERT INTO ClubProfessor (clubID, professorID) VALUES (3, 3);
INSERT INTO ClubProfessor (clubID, professorID) VALUES (4, 4);
INSERT INTO ClubProfessor (clubID, professorID) VALUES (5, 5);
INSERT INTO ClubProfessor (clubID, professorID) VALUES (6, 6);
INSERT INTO ClubProfessor (clubID, professorID) VALUES (7, 7);


INSERT INTO ClubPresident (clubID, presidentName) VALUES (1, '정한울');
INSERT INTO ClubPresident (clubID, presidentName) VALUES (2, '오승주');
INSERT INTO ClubPresident (clubID, presidentName) VALUES (3, '박준유');
INSERT INTO ClubPresident (clubID, presidentName) VALUES (4, '황재찬');
INSERT INTO ClubPresident (clubID, presidentName) VALUES (5, '최가은');
INSERT INTO ClubPresident (clubID, presidentName) VALUES (6, '이우영');
INSERT INTO ClubPresident (clubID, presidentName) VALUES (7, '조민우');


INSERT INTO Professor (professorID, affiliation, contact, name) VALUES (1, '소프트웨어학과', '010-1111-1111', '이건명');
INSERT INTO Professor (professorID, affiliation, contact, name) VALUES (2, '소프트웨어학과', '010-2222-2222', '아지즈');
INSERT INTO Professor (professorID, affiliation, contact, name) VALUES (3, '소프트웨어학과', '010-3333-3333', '이재성');
INSERT INTO Professor (professorID, affiliation, contact, name) VALUES (4, '소프트웨어학과', '010-4444-4444', '조오현');
INSERT INTO Professor (professorID, affiliation, contact, name) VALUES (5, '소프트웨어학과', '010-5555-5555', '최경주');
INSERT INTO Professor (professorID, affiliation, contact, name) VALUES (6, '소프트웨어학과', '010-6666-6666', '홍장의');
INSERT INTO Professor (professorID, affiliation, contact, name) VALUES (7, '소프트웨어학과', '010-7777-7777', '노서영');

INSERT INTO ActivitySchedule (scheduleID, clubID, scheduleName, date, time, location) VALUES (1, 1, '알고리즘 스터디', '2024-12-01', '18:00:00', '동아리방');
INSERT INTO ActivitySchedule (scheduleID, clubID, scheduleName, date, time, location) VALUES (2, 2, '야식마차', '2024-12-02', '20:00:00', '동아리방');
INSERT INTO ActivitySchedule (scheduleID, clubID, scheduleName, date, time, location) VALUES (3, 3, '친해지길 바라', '2024-12-03', '17:00:00', '중문');
INSERT INTO ActivitySchedule (scheduleID, clubID, scheduleName, date, time, location) VALUES (4, 4, '개강총회', '2024-12-04', '18:00:00', '중문');
INSERT INTO ActivitySchedule (scheduleID, clubID, scheduleName, date, time, location) VALUES (5, 5, '동아리 체육대회', '2024-12-05', '15:00:00', '운동장');
INSERT INTO ActivitySchedule (scheduleID, clubID, scheduleName, date, time, location) VALUES (6, 6, '친목모임', '2024-12-06', '17:00:00', '동아리방');
INSERT INTO ActivitySchedule (scheduleID, clubID, scheduleName, date, time, location) VALUES (7, 7, '공부모임', '2024-12-07', '19:00:00', '도서관');
