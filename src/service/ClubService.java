package service;

import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ClubService {

    // 동아리 가입
    public static void joinClub(Scanner scanner, Connection connection, int memberId) {
        System.out.print("가입할 동아리 ID를 입력하세요: ");
        int clubId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        // 동아리가 존재하는지 확인
        String checkClubSql = "SELECT COUNT(*) FROM Club WHERE clubID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(checkClubSql)) {
            stmt.setInt(1, clubId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int clubCount = rs.getInt(1);

            if (clubCount == 0) {
                System.out.println("해당 동아리 ID는 존재하지 않습니다. 동아리 가입을 할 수 없습니다.");
                return;
            }
        } catch (SQLException e) {
            System.err.println("동아리 조회 중 오류 발생: " + e.getMessage());
            return;
        }

        // 동아리 가입
        String sql = "INSERT INTO ClubMember (clubID, memberID) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clubId);
            stmt.setInt(2, memberId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("동아리에 성공적으로 가입되었습니다.");
            } else {
                System.out.println("동아리 가입에 실패했습니다.");
            }
        } catch (SQLException e) {
            System.err.println("동아리 가입 중 오류 발생: " + e.getMessage());
        }
    }

    // 동아리 목록 조회
    public static void listClubs(Connection connection) {
        String sql = "SELECT * FROM Club";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            System.out.println("동아리 목록:");
            while (rs.next()) {
                int clubId = rs.getInt("clubID");
                String clubName = rs.getString("clubName");

                System.out.println("동아리 ID: " + clubId + ", 동아리 이름: " + clubName);
            }
        } catch (SQLException e) {
            System.err.println("동아리 목록 조회 중 오류 발생: " + e.getMessage());
        }
    }

    // 동아리 회장 목록 조회
    public static void listClubPresidents(Connection connection) {
        String sql = "SELECT Club.clubName, ClubPresident.presidentName FROM ClubPresident " +
                "JOIN Club ON Club.clubID = ClubPresident.clubID";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            System.out.println("동아리 회장 목록:");
            while (rs.next()) {
                String clubName = rs.getString("clubName");
                String presidentName = rs.getString("presidentName");

                System.out.println(clubName + " 동아리 회장: " + presidentName);
            }
        } catch (SQLException e) {
            System.err.println("동아리 회장 목록 조회 중 오류 발생: " + e.getMessage());
        }
    }

    // 동아리 지도 교수 목록 조회
    public static void listClubProfessors(Connection connection) {
        String sql = "SELECT Club.clubName, Professor.name AS professorName, Professor.contact AS professorContact, Professor.affiliation AS professorAffiliation " +
                "FROM ClubProfessor " +
                "JOIN Club ON Club.clubID = ClubProfessor.clubID " +
                "JOIN Professor ON Professor.professorID = ClubProfessor.professorID";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            System.out.println("동아리 지도 교수 목록:");
            while (rs.next()) {
                String clubName = rs.getString("clubName");
                String professorName = rs.getString("professorName");
                String professorContact = rs.getString("professorContact");
                String professorAffiliation = rs.getString("professorAffiliation");

                System.out.println(clubName + " 동아리 지도 교수: " + professorName + " | 연락처: " + professorContact + " | 소속: " + professorAffiliation);
            }
        } catch (SQLException e) {
            System.err.println("동아리 지도 교수 목록 조회 중 오류 발생: " + e.getMessage());
        }
    }


    // 동아리 활동 일정 목록 조회
    public static void listClubSchedules(Connection connection) {
        String sql = "SELECT Club.clubName, ActivitySchedule.scheduleName, ActivitySchedule.date, ActivitySchedule.time, ActivitySchedule.location " +
                "FROM ActivitySchedule " +
                "JOIN Club ON Club.clubID = ActivitySchedule.clubID";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            System.out.println("동아리 활동 일정 목록:");
            while (rs.next()) {
                String clubName = rs.getString("clubName");
                String scheduleName = rs.getString("scheduleName");
                String date = rs.getString("date");
                String time = rs.getString("time");
                String location = rs.getString("location");

                System.out.println(clubName + " 동아리 활동: " + scheduleName + " (" + date + " " + time + " @ " + location + ")");
            }
        } catch (SQLException e) {
            System.err.println("동아리 활동 일정 목록 조회 중 오류 발생: " + e.getMessage());
        }
    }

    // 동아리별 회원 목록 조회
    public static void listClubMembers(Connection connection) {
        String sql = "SELECT Club.clubName, Member.studentID, Member.name, Member.contact " +
                "FROM ClubMember " +
                "JOIN Club ON Club.clubID = ClubMember.clubID " +
                "JOIN Member ON Member.memberID = ClubMember.memberID " +
                "ORDER BY Club.clubName";  // 동아리 이름별로 정렬
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            String currentClub = null;

            while (rs.next()) {
                String clubName = rs.getString("clubName");
                String studentID = rs.getString("studentID");
                String memberName = rs.getString("name");
                String contact = rs.getString("contact");

                // 새로운 동아리가 나올 때마다 동아리 이름을 출력
                if (currentClub == null || !currentClub.equals(clubName)) {
                    if (currentClub != null) {
                        System.out.println();  // 동아리 구분을 위해 줄바꿈
                    }
                    currentClub = clubName;
                    System.out.println(clubName + " 동아리 회원:");
                }

                // 회원 정보 출력
                System.out.println("학번: " + studentID + ", 이름: " + memberName + ", 연락처: " + contact);
            }
        } catch (SQLException e) {
            System.err.println("동아리 회원 목록 조회 중 오류 발생: " + e.getMessage());
        }
    }
}
