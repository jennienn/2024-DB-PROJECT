package service;

import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MemberService {

    // 회원 추가
    public static void addMember(Scanner scanner, Connection connection) {
        System.out.print("이름: ");
        String name = scanner.nextLine();
        System.out.print("학번: ");
        String studentId = scanner.nextLine();
        System.out.print("연락처: ");
        String contact = scanner.nextLine();

        String sql = "INSERT INTO Member (name, studentId, contact) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, studentId);
            stmt.setString(3, contact);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("회원이 성공적으로 추가되었습니다.");
            } else {
                System.out.println("회원 추가에 실패했습니다.");
            }
        } catch (SQLException e) {
            System.err.println("회원 추가 중 오류 발생: " + e.getMessage());
        }
    }

    // 회원 삭제
    public static void deleteMember(Scanner scanner, Connection connection) {
        System.out.print("삭제할 회원 ID를 입력하세요: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        String sql = "DELETE FROM Member WHERE memberId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("회원이 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("회원 삭제에 실패했습니다. 해당 회원이 존재하지 않거나 이미 삭제되었습니다.");
            }
        } catch (SQLException e) {
            System.err.println("회원 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    // 회원 정보 수정
    public static void updateMember(Scanner scanner, Connection connection) {
        System.out.print("수정할 회원 ID를 입력하세요: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        System.out.print("새로운 이름: ");
        String name = scanner.nextLine();
        System.out.print("새로운 학번: ");
        String studentId = scanner.nextLine();
        System.out.print("새로운 연락처: ");
        String contact = scanner.nextLine();

        String sql = "UPDATE Member SET name = ?, studentId = ?, contact = ? WHERE memberId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, studentId);
            stmt.setString(3, contact);
            stmt.setInt(4, memberId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("회원 정보가 성공적으로 수정되었습니다.");
            } else {
                System.out.println("회원 정보 수정에 실패했습니다. 해당 회원이 존재하지 않거나 이미 수정되었습니다.");
            }
        } catch (SQLException e) {
            System.err.println("회원 정보 수정 중 오류 발생: " + e.getMessage());
        }
    }

    // 회원 조회
    public static void viewMember(Scanner scanner, Connection connection) {
        System.out.print("조회할 회원 ID를 입력하세요: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        String sql = "SELECT * FROM Member WHERE memberId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("회원 ID: " + rs.getInt("memberId"));
                System.out.println("이름: " + rs.getString("name"));
                System.out.println("학번: " + rs.getString("studentId"));
                System.out.println("연락처: " + rs.getString("contact"));
            } else {
                System.out.println("회원이 존재하지 않습니다.");
            }
        } catch (SQLException e) {
            System.err.println("회원 조회 중 오류 발생: " + e.getMessage());
        }
    }

    // 회원 목록 조회
    public static void listMembers(Connection connection) {
        String sql = "SELECT * FROM Member";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            System.out.println("회원 목록:");
            while (rs.next()) {
                int memberId = rs.getInt("memberId");
                String name = rs.getString("name");
                String studentId = rs.getString("studentId");
                String contact = rs.getString("contact");

                System.out.println("회원 ID: " + memberId + ", 이름: " + name + ", 학번: " + studentId + ", 연락처: " + contact);
            }
        } catch (SQLException e) {
            System.err.println("회원 목록 조회 중 오류 발생: " + e.getMessage());
        }
    }
}