package service;

import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ClubService {

    // 동아리 추가
    public static void addClub(Scanner scanner, Connection connection) {
        System.out.print("동아리 이름: ");
        String clubName = scanner.nextLine();

        String sql = "INSERT INTO Club (clubName) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, clubName);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("동아리가 성공적으로 추가되었습니다.");
            } else {
                System.out.println("동아리 추가에 실패했습니다.");
            }
        } catch (SQLException e) {
            System.err.println("동아리 추가 중 오류 발생: " + e.getMessage());
        }
    }

    // 동아리 삭제
    public static void deleteClub(Scanner scanner, Connection connection) {
        System.out.print("삭제할 동아리 ID를 입력하세요: ");
        int clubId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        String sql = "DELETE FROM Club WHERE clubID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clubId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("동아리가 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("동아리 삭제에 실패했습니다. 해당 동아리가 존재하지 않거나 이미 삭제되었습니다.");
            }
        } catch (SQLException e) {
            System.err.println("동아리 삭제 중 오류 발생: " + e.getMessage());
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
}
