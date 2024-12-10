CREATE DATABASE IF NOT EXISTS club;

USE club;

CREATE TABLE IF NOT EXISTS Member (
    memberID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    studentID VARCHAR(20),
    contact VARCHAR(15)
    );

CREATE TABLE IF NOT EXISTS Club (
    clubID INT AUTO_INCREMENT PRIMARY KEY,
    clubName VARCHAR(100)
    );

CREATE TABLE IF NOT EXISTS ClubPresident (
    clubID INT,
    presidentID INT,
    FOREIGN KEY (clubID) REFERENCES Club(clubID),
    FOREIGN KEY (presidentID) REFERENCES Member(memberID),
    PRIMARY KEY (clubID, presidentID)
    );

CREATE TABLE IF NOT EXISTS ClubProfessor (
    clubID INT,
    professorID INT,
    FOREIGN KEY (clubID) REFERENCES Club(clubID),
    FOREIGN KEY (professorID) REFERENCES Professor(professorID),
    PRIMARY KEY (clubID, professorID)
    );

CREATE TABLE IF NOT EXISTS Professor (
    professorID INT AUTO_INCREMENT PRIMARY KEY,
    affiliation VARCHAR(100),
    contact VARCHAR(15)
    );

CREATE TABLE IF NOT EXISTS ClubMember (
    clubID INT,
    memberID INT,
    joinDate DATE,
    FOREIGN KEY (clubID) REFERENCES Club(clubID),
    FOREIGN KEY (memberID) REFERENCES Member(memberID),
    PRIMARY KEY (clubID, memberID)
    );

CREATE TABLE IF NOT EXISTS Post (
    postID INT AUTO_INCREMENT PRIMARY KEY,
    memberID INT,
    title VARCHAR(255),
    content TEXT,
    createdDate DATE,
    modifiedDate DATE,
    FOREIGN KEY (memberID) REFERENCES Member(memberID)
    );

CREATE TABLE IF NOT EXISTS ActivitySchedule (
    scheduleID INT AUTO_INCREMENT PRIMARY KEY,
    clubID INT,
    scheduleName VARCHAR(255),
    date DATE,
    time TIME,
    location VARCHAR(100),
    FOREIGN KEY (clubID) REFERENCES Club(clubID)
    );

CREATE TABLE IF NOT EXISTS Comment (
    commentID INT AUTO_INCREMENT PRIMARY KEY,
    postID INT,
    memberID INT,
    content TEXT,
    createdDate DATE,
    modifiedDate DATE,
    FOREIGN KEY (postID) REFERENCES Post(postID),
    FOREIGN KEY (memberID) REFERENCES Member(memberID)
    );

CREATE TABLE IF NOT EXISTS Reply (
    replyID INT AUTO_INCREMENT PRIMARY KEY,
    commentID INT,
    memberID INT,
    content TEXT,
    modifiedDate DATE,
    FOREIGN KEY (commentID) REFERENCES Comment(commentID),
    FOREIGN KEY (memberID) REFERENCES Member(memberID)
    );
