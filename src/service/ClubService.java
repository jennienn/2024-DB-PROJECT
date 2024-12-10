package service;

import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class ClubService {

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
}
