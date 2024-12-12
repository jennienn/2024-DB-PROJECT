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

    // 이메일 중복 체크
    public static boolean isEmailTaken(Connection connection, String email) {
        String sql = "SELECT COUNT(*) FROM Member WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;  // 이메일 중복
            }
        } catch (SQLException e) {
            System.err.println("이메일 중복 확인 중 오류 발생: " + e.getMessage());
        }
        return false;  // 이메일 사용 가능
    }

    // 학번 중복 체크
    public static boolean isStudentIdTaken(Connection connection, String studentId) {
        String sql = "SELECT COUNT(*) FROM Member WHERE studentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;  // 학번 중복
            }
        } catch (SQLException e) {
            System.err.println("학번 중복 확인 중 오류 발생: " + e.getMessage());
        }
        return false;  // 학번 사용 가능
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

        // 이메일 중복 체크
        if (isEmailTaken(connection, username)) {
            System.out.println("이미 사용 중인 이메일입니다.");
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

        // 학번 중복 체크
        if (isStudentIdTaken(connection, studentId)) {
            System.out.println("이미 등록된 학번입니다.");
            return;
        }

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
        // 이메일 중복 체크
        if (isEmailTaken(connection, username)) {
            System.out.println("이미 사용 중인 이메일입니다.");
            return false;
        }

        // 학번 중복 체크
        if (isStudentIdTaken(connection, studentId)) {
            System.out.println("이미 등록된 학번입니다.");
            return false;
        }

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
                String currentUsername = rs.getString("username");
                String currentName = rs.getString("name");
                String currentStudentId = rs.getString("studentID");
                String currentContact = rs.getString("contact");

                System.out.println("현재 이메일: " + currentUsername);
                System.out.println("현재 이름: " + currentName);
                System.out.println("현재 학번: " + currentStudentId);
                System.out.println("현재 연락처: " + currentContact);

                // 수정할 정보 입력 받기
                System.out.print("새 이메일 (현재 이메일: " + currentUsername + "): ");
                String newUsername = scanner.nextLine();
                System.out.print("새 이름 (현재 이름: " + currentName + "): ");
                String newName = scanner.nextLine();
                System.out.print("새 학번 (현재 학번: " + currentStudentId + "): ");
                String newStudentId = scanner.nextLine();
                System.out.print("새 연락처 (현재 연락처: " + currentContact + "): ");
                String newContact = scanner.nextLine();

                // 이메일 중복 확인
                if (!newUsername.equals(currentUsername)) {
                    String emailCheckSql = "SELECT COUNT(*) FROM Member WHERE username = ?";
                    try (PreparedStatement emailStmt = connection.prepareStatement(emailCheckSql)) {
                        emailStmt.setString(1, newUsername);
                        ResultSet emailRs = emailStmt.executeQuery();
                        emailRs.next();
                        if (emailRs.getInt(1) > 0) {
                            System.out.println("입력한 이메일은 이미 다른 회원에 의해 사용 중입니다.");
                            return;
                        }
                    }
                }

                // 학번 중복 확인
                if (!newStudentId.equals(currentStudentId)) {
                    String studentIdCheckSql = "SELECT COUNT(*) FROM Member WHERE studentID = ?";
                    try (PreparedStatement studentIdStmt = connection.prepareStatement(studentIdCheckSql)) {
                        studentIdStmt.setString(1, newStudentId);
                        ResultSet studentIdRs = studentIdStmt.executeQuery();
                        studentIdRs.next();
                        if (studentIdRs.getInt(1) > 0) {
                            System.out.println("입력한 학번은 이미 다른 회원에 의해 사용 중입니다.");
                            return;
                        }
                    }
                }

                // 유효성 검사 (이메일 형식 확인)
                if (!isValidEmail(newUsername)) {
                    System.out.println("이메일 형식이 올바르지 않습니다.");
                    return;
                }

                String updateSql = "UPDATE Member SET username = ?, name = ?, studentID = ?, contact = ? WHERE memberId = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setString(1, newUsername);
                    updateStmt.setString(2, newName);
                    updateStmt.setString(3, newStudentId);
                    updateStmt.setString(4, newContact);
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
    public static void deleteMember(Integer memberId, Connection connection) {
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

    // 가입한 동아리 조회
    public static void listJoinedClubs(Connection connection, Integer memberId) {
        String sql = "SELECT Club.clubName FROM ClubMember " +
                "JOIN Club ON Club.clubID = ClubMember.clubID " +
                "WHERE ClubMember.memberID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, memberId);  // 로그인한 회원의 ID를 사용
            ResultSet rs = stmt.executeQuery();

            System.out.println("가입한 동아리:");
            boolean hasClubs = false;  // 가입한 동아리가 있는지 확인하는 변수

            while (rs.next()) {
                String clubName = rs.getString("clubName");
                System.out.println(clubName);  // 동아리 이름 출력
                hasClubs = true;
            }

            if (!hasClubs) {
                System.out.println("가입한 동아리가 없습니다.");
            }
        } catch (SQLException e) {
            System.err.println("가입한 동아리 조회 중 오류 발생: " + e.getMessage());
        }
    }


}
