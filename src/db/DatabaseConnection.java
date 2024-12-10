package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://192.168.56.101:4567/club?useSSL=false&serverTimezone=UTC";
    private static final String USER = "kimyejin";
    private static final String PASSWORD = "1234";

    public static Connection connect() {
        Connection connection = null;

        try {
            // MySQL JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 데이터베이스 연결
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("데이터베이스에 연결되었습니다.");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC 드라이버를 로드하는 데 실패했습니다: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("데이터베이스 연결에 실패했습니다: " + e.getMessage());
        }

        return connection;
    }


    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("데이터베이스 연결이 종료되었습니다.");
            } catch (SQLException e) {
                System.err.println("데이터베이스 연결 종료에 실패했습니다: " + e.getMessage());
            }
        }
    }
}