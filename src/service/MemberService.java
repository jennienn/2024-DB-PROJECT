package service;

import db.DatabaseConnection;
import java.sql.*;
import java.util.Scanner;

public class MemberService {

    private Connection connection;

    public MemberService(Connection connection) {
        this.connection = connection;
    }

    // 회원 추가
    public void addMember(Scanner scanner) {
        System.out.print("이름: ");
        String name = scanner.nextLine();
        System.out.print("학번: ");
        String studentId = scanner.nextLine();
        System.out.print("연락처: ");
        String contact = scanner.nextLine();

        String sql = "INSERT INTO Member (name, studentID, contact) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, studentId);
            statement.setString(3, contact);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("회원이 성공적으로 추가되었습니다.");
            }
        } catch (SQLException e) {
            System.err.println("회원 추가에 실패했습니다: " + e.getMessage());
        }
    }

    // 회원 삭제
    public void deleteMember(Scanner scanner) {
        System.out.print("삭제할 회원ID: ");
        int memberId = scanner.nextInt();

        String sql = "DELETE FROM Member WHERE memberID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("회원이 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("해당 ID의 회원이 없습니다.");
            }
        } catch (SQLException e) {
            System.err.println("회원 삭제에 실패했습니다: " + e.getMessage());
        }
    }

    // 회원 정보 수정
    public void updateMember(Scanner scanner) {
        System.out.print("수정할 회원ID: ");
        int memberId = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기

        System.out.print("새 이름: ");
        String name = scanner.nextLine();
        System.out.print("새 학번: ");
        String studentId = scanner.nextLine();
        System.out.print("새 연락처: ");
        String contact = scanner.nextLine();

        String sql = "UPDATE Member SET name = ?, studentID = ?, contact = ? WHERE memberID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, studentId);
            statement.setString(3, contact);
            statement.setInt(4, memberId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("회원 정보가 성공적으로 수정되었습니다.");
            } else {
                System.out.println("해당 ID의 회원이 없습니다.");
            }
        } catch (SQLException e) {
            System.err.println("회원 정보 수정에 실패했습니다: " + e.getMessage());
        }
    }

    // 회원 조회
    public void viewMember(Scanner scanner) {
        System.out.print("조회할 회원ID: ");
        int memberId = scanner.nextInt();

        String sql = "SELECT * FROM Member WHERE memberID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String studentId = resultSet.getString("studentID");
                String contact = resultSet.getString("contact");
                System.out.println("회원ID: " + memberId);
                System.out.println("이름: " + name);
                System.out.println("학번: " + studentId);
                System.out.println("연락처: " + contact);
            } else {
                System.out.println("해당 회원ID에 해당하는 회원이 없습니다.");
            }
        } catch (SQLException e) {
            System.err.println("회원 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 회원 목록 조회
    public void listMembers() {
        String sql = "SELECT * FROM Member";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("회원 목록:");
            while (resultSet.next()) {
                int memberId = resultSet.getInt("memberID");
                String name = resultSet.getString("name");
                String studentId = resultSet.getString("studentID");
                String contact = resultSet.getString("contact");
                System.out.println("회원ID: " + memberId + ", 이름: " + name + ", 학번: " + studentId + ", 연락처: " + contact);
            }
        } catch (SQLException e) {
            System.err.println("회원 목록 조회에 실패했습니다: " + e.getMessage());
        }
    }
}
