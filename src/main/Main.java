package main;

import db.DatabaseConnection;
import service.MemberService;
import java.sql.Connection;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = null;
        boolean isConnected = false;

        // 첫 연결 시에만 연결하고 메시지를 출력
        while (true) {
            if (!isConnected) {
                connection = DatabaseConnection.connect();
                isConnected = true;  // 한 번만 연결하도록 설정
            }

            // 메인 메뉴
            System.out.println("메인 메뉴");
            System.out.println("1. 회원 관리");
            System.out.println("2. 동아리 관리");
            System.out.println("3. 종료");
            System.out.print("선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // 버퍼 비우기

            switch (choice) {
                case 1:
                    memberManagementMenu(scanner, connection);
                    break;
                case 2:
                    clubManagementMenu(scanner, connection);
                    break;
                case 3:
                    // 종료
                    System.out.println("프로그램을 종료합니다.");
                    DatabaseConnection.closeConnection(connection);
                    return;
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }
    }

    // 회원 관리 메뉴
    private static void memberManagementMenu(Scanner scanner, Connection connection) {
        while (true) {
            System.out.println("\n회원 관리 메뉴");
            System.out.println("1. 회원 추가");
            System.out.println("2. 회원 삭제");
            System.out.println("3. 회원 정보 수정");
            System.out.println("4. 회원 조회");
            System.out.println("5. 회원 목록 조회");
            System.out.println("6. 이전 메뉴로 돌아가기");
            System.out.print("선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // 버퍼 비우기

            switch (choice) {
                case 1:
                    addMember(scanner, connection);
                    break;
                case 2:
                    deleteMember(scanner, connection);
                    break;
                case 3:
                    updateMember(scanner, connection);
                    break;
                case 4:
                    viewMember(scanner, connection);
                    break;
                case 5:
                    listMembers(connection);
                    break;
                case 6:
                    return;  // 이전 메뉴로 돌아가기
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }
    }

    private static void clubManagementMenu(Scanner scanner, Connection connection) {
        while (true) {
            System.out.println("\n동아리 관리 메뉴");
            System.out.println("1. 동아리 추가");
            System.out.println("2. 동아리 삭제");
            System.out.println("3. 동아리 조회");
            System.out.println("4. 동아리 목록 조회");
            System.out.println("5. 이전 메뉴로 돌아가기");
            System.out.print("선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // 버퍼 비우기

            switch (choice) {
                case 1:
                    addClub(scanner, connection);
                    break;
                case 2:
                    deleteClub(scanner, connection);
                    break;
                case 3:
                    viewClub(scanner, connection);
                    break;
                case 4:
                    listClubs(connection);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }
    }

    private static void addMember(Scanner scanner, Connection connection) {
        System.out.print("이름: ");
        String name = scanner.nextLine();
        System.out.print("학번: ");
        String studentId = scanner.nextLine();
        System.out.print("연락처: ");
        String contact = scanner.nextLine();

        System.out.println("회원이 성공적으로 추가되었습니다.");
    }

    private static void deleteMember(Scanner scanner, Connection connection) {
        System.out.println("회원 삭제 기능");
    }

    private static void updateMember(Scanner scanner, Connection connection) {
        System.out.println("회원 정보 수정 기능");
    }

    private static void viewMember(Scanner scanner, Connection connection) {
        System.out.println("회원 조회 기능");
    }

    private static void listMembers(Connection connection) {
        System.out.println("회원 목록 조회 기능");
    }

    private static void addClub(Scanner scanner, Connection connection) {
        System.out.print("동아리 이름: ");
        String clubName = scanner.nextLine();

        System.out.println("동아리가 성공적으로 추가되었습니다.");
    }

    private static void deleteClub(Scanner scanner, Connection connection) {
        System.out.println("동아리 삭제 기능");
    }

    private static void viewClub(Scanner scanner, Connection connection) {
        System.out.println("동아리 조회 기능");
    }

    private static void listClubs(Connection connection) {
        System.out.println("동아리 목록 조회 기능");
    }
}
