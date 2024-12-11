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

    // 동아리 조회
    public static void viewClub(Scanner scanner, Connection connection) {
        System.out.print("조회할 동아리 ID를 입력하세요: ");
        int clubId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        String sql = "SELECT * FROM Club WHERE clubID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clubId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String clubName = rs.getString("clubName");
                System.out.println("동아리 ID: " + clubId);
                System.out.println("동아리 이름: " + clubName);
            } else {
                System.out.println("동아리가 존재하지 않습니다.");
            }
        } catch (SQLException e) {
            System.err.println("동아리 조회 중 오류 발생: " + e.getMessage());
        }
    }
}