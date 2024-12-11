package service;

import java.sql.*;
import java.util.Scanner;

public class UserService {

    // 로그인 메서드
    public static int login(Scanner scanner, Connection connection) {
        System.out.print("아이디: ");
        String username = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        String sql = "SELECT memberID FROM Member WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("로그인 성공!");
                return rs.getInt("memberID");  // 로그인 성공 시 memberID 반환
            } else {
                System.out.println("아이디나 비밀번호가 잘못되었습니다.");
                return -1;  // 로그인 실패
            }
        } catch (SQLException e) {
            System.err.println("로그인 중 오류 발생: " + e.getMessage());
            return -1;
        }
    }

    // 로그아웃 메서드
    public static void logout() {
        System.out.println("로그아웃되었습니다.");
    }
}

