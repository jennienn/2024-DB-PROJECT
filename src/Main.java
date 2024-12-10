import db.DatabaseConnection;

import java.sql.Connection;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Connection connection = DatabaseConnection.connect();

        if (connection != null) {
            System.out.println("데이터베이스와 연결되었습니다.");

            Scanner scanner = new Scanner(System.in);
            String input;

            while (true) {
                System.out.println("종료하려면 'exit'을 입력하세요.");
                input = scanner.nextLine();

                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("연결을 종료합니다...");
                    break;
                }
            }

            DatabaseConnection.closeConnection(connection);
        } else {
            System.out.println("데이터베이스 연결에 실패했습니다.");
        }
    }
}
