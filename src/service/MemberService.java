package service;

import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MemberService {

    // 이메일 형식 확인
    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.matches(emailRegex, email);
    }

    // 비밀번호 8자 이상 확인
    private static boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    // 회원 추가 (회원가입)
    public static void addMember(Scanner scanner, Connection connection) {
        System.out.print("사용자 이름 (이메일): ");
        String username = scanner.nextLine();

        // 이메일 형식 체크
        if (!isValidEmail(username)) {
            System.out.println("이메일 형식이 올바르지 않습니다.");
            return;
        }

        System.out.print("비밀번호 (8자 이상): ");
        String password = scanner.nextLine();

        // 비밀번호 길이 체크
        if (!isValidPassword(password)) {
            System.out.println("비밀번호는 8자 이상이어야 합니다.");
            return;
        }

        System.out.print("이름: ");
        String name = scanner.nextLine();
        System.out.print("학번: ");
        String studentId = scanner.nextLine();
        System.out.print("연락처: ");
        String contact = scanner.nextLine();

        String sql = "INSERT INTO Member (username, password, name, studentID, contact) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);  // 비밀번호는 그대로 저장되므로 보안 처리 필요
            stmt.setString(3, name);
            stmt.setString(4, studentId);
            stmt.setString(5, contact);

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

    // 로그인 처리
    public static boolean login(Connection connection, String username, String password) {
        String sql = "SELECT * FROM Member WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return true;  // 로그인 성공
            } else {
                return false;  // 로그인 실패
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 로그인 시 유저 ID 반환
    public static Integer getMemberIdByUsername(Connection connection, String username) {
        String sql = "SELECT memberId FROM Member WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("memberId");
            }
        } catch (SQLException e) {
            System.err.println("회원 ID 조회 중 오류 발생: " + e.getMessage());
        }
        return null;
    }

    // 회원가입 처리
    public static boolean register(Connection connection, String username, String password, String name, String studentId, String contact) {
        // 이메일 형식과 비밀번호 체크
        if (!isValidEmail(username)) {
            System.out.println("이메일 형식이 올바르지 않습니다.");
            return false;
        }
        if (!isValidPassword(password)) {
            System.out.println("비밀번호는 8자 이상이어야 합니다.");
            return false;
        }

        String sql = "INSERT INTO Member (username, password, name, studentID, contact) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setString(4, studentId);
            stmt.setString(5, contact);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("회원가입 중 오류 발생: " + e.getMessage());
        }
        return false;
    }

    // 나의 정보 수정
    public static void updateMemberInfo(Scanner scanner, Connection connection, Integer memberId) {
        System.out.println("수정할 정보를 입력하세요.");

        // 현재 회원 정보를 조회
        String sql = "SELECT * FROM Member WHERE memberId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // 기존 정보 표시
                System.out.println("현재 이메일: " + rs.getString("username"));
                System.out.println("현재 이름: " + rs.getString("name"));
                System.out.println("현재 학번: " + rs.getString("studentID"));
                System.out.println("현재 연락처: " + rs.getString("contact"));

                // 수정할 정보 입력 받기
                System.out.print("새 이메일 (현재 이메일: " + rs.getString("username") + "): ");
                String username = scanner.nextLine();
                System.out.print("새 이름 (현재 이름: " + rs.getString("name") + "): ");
                String name = scanner.nextLine();
                System.out.print("새 학번 (현재 학번: " + rs.getString("studentID") + "): ");
                String studentId = scanner.nextLine();
                System.out.print("새 연락처 (현재 연락처: " + rs.getString("contact") + "): ");
                String contact = scanner.nextLine();

                // 유효성 검사
                if (!isValidEmail(username)) {
                    System.out.println("이메일 형식이 올바르지 않습니다.");
                    return;
                }

                String updateSql = "UPDATE Member SET username = ?, name = ?, studentID = ?, contact = ? WHERE memberId = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setString(1, username);
                    updateStmt.setString(2, name);
                    updateStmt.setString(3, studentId);
                    updateStmt.setString(4, contact);
                    updateStmt.setInt(5, memberId);

                    int rowsAffected = updateStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("회원 정보가 성공적으로 수정되었습니다.");
                    } else {
                        System.out.println("회원 정보 수정에 실패했습니다.");
                    }
                }

            } else {
                System.out.println("회원 정보가 없습니다.");
            }
        } catch (SQLException e) {
            System.err.println("회원 정보 수정 중 오류 발생: " + e.getMessage());
        }
    }

    // 회원 탈퇴
    public static void deleteMember(Connection connection, Integer memberId) {
        String sql = "DELETE FROM Member WHERE memberId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("회원 탈퇴가 완료되었습니다.");
            } else {
                System.out.println("회원 탈퇴에 실패했습니다.");
            }
        } catch (SQLException e) {
            System.err.println("회원 탈퇴 중 오류 발생: " + e.getMessage());
        }
    }


    // 회원 목록 조회
    public static void listMembers(Connection connection) {
        String sql = "SELECT * FROM Member";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            System.out.println("회원 목록:");
            while (rs.next()) {
                System.out.println("회원 ID: " + rs.getInt("memberId"));
                System.out.println("이메일: " + rs.getString("username"));
                System.out.println("이름: " + rs.getString("name"));
                System.out.println("학번: " + rs.getString("studentID"));
                System.out.println("연락처: " + rs.getString("contact"));
                System.out.println("=====================================");
            }
        } catch (SQLException e) {
            System.err.println("회원 목록 조회 중 오류 발생: " + e.getMessage());
        }
    }
}
