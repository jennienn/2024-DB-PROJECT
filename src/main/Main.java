package main;

import db.DatabaseConnection;
import service.ClubService;
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
                    MemberService.addMember(scanner, connection);
                    break;
                case 2:
                    MemberService.deleteMember(scanner, connection);
                    break;
                case 3:
                    MemberService.updateMember(scanner, connection);
                    break;
                case 4:
                    MemberService.viewMember(scanner, connection);
                    break;
                case 5:
                    MemberService.listMembers(connection);
                    break;
                case 6:
                    return;  // 이전 메뉴로 돌아가기
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }
    }

    // 동아리 관리 메뉴
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
                    ClubService.addClub(scanner, connection);
                    break;
                case 2:
                    ClubService.deleteClub(scanner, connection);
                    break;
                case 3:
                    ClubService.viewClub(scanner, connection);
                    break;
                case 4:
                    ClubService.listClubs(connection);
                    break;
                case 5:
                    return;  // 이전 메뉴로 돌아가기
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }
    }
}
