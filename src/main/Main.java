package main;

import db.DatabaseConnection;
import service.MemberService;
import java.sql.Connection;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = DatabaseConnection.connect();
        MemberService memberService = new MemberService(connection);

        boolean isRunning = true;
        while (isRunning) {
            // 회원 관리 메뉴 출력
            System.out.println("회원 관리 시스템");
            System.out.println("1. 회원 추가");
            System.out.println("2. 회원 삭제");
            System.out.println("3. 회원 정보 수정");
            System.out.println("4. 회원 조회");
            System.out.println("5. 회원 목록 조회");
            System.out.println("6. 종료");
            System.out.print("선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // 버퍼 비우기

            switch (choice) {
                case 1:
                    memberService.addMember(scanner); // 회원 추가
                    break;
                case 2:
                    memberService.deleteMember(scanner); // 회원 삭제
                    break;
                case 3:
                    memberService.updateMember(scanner); // 회원 정보 수정
                    break;
                case 4:
                    memberService.viewMember(scanner); // 회원 조회
                    break;
                case 5:
                    memberService.listMembers(); // 회원 목록 조회
                    break;
                case 6:
                    System.out.println("프로그램을 종료합니다.");
                    isRunning = false;
                    break;
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }

        DatabaseConnection.closeConnection(connection); // 종료 시 연결 종료
    }
}
