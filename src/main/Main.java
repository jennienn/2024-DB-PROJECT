package main;

import db.DatabaseConnection;
import service.*;

import java.sql.Connection;
import java.util.Scanner;

import static service.ClubService.*;

public class Main {

    private static Integer loggedInMemberId = null;  // 로그인된 회원의 ID

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = DatabaseConnection.connect();

        while (true) {
            if (loggedInMemberId == null) {
                // 로그인 상태가 아니면 로그인 메뉴 표시
                loginMenu(scanner, connection);
            } else {
                // 로그인 상태일 경우 메인 메뉴 표시
                mainMenu(scanner, connection);
            }
        }
    }

    // 로그인 메뉴
    private static void loginMenu(Scanner scanner, Connection connection) {
        System.out.println("====================================");
        System.out.println("          로그인 / 회원가입");
        System.out.println("====================================");
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.println("3. 종료");
        System.out.print("선택: ");

        int choice = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        switch (choice) {
            case 1:
                login(scanner, connection);
                break;
            case 2:
                register(scanner, connection);
                break;
            case 3:
                System.out.println("프로그램을 종료합니다. 감사합니다!");
                DatabaseConnection.closeConnection(connection);
                System.exit(0);
                break;
            default:
                System.out.println("잘못된 선택입니다. 다시 시도해주세요.");
        }
    }

    // 로그인 처리
    private static void login(Scanner scanner, Connection connection) {
        System.out.println("====================================");
        System.out.println("        로그인 화면");
        System.out.println("====================================");
        System.out.print("이메일: ");
        String username = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        if (MemberService.login(connection, username, password)) {
            loggedInMemberId = MemberService.getMemberIdByUsername(connection, username);
            System.out.println("로그인 성공! 환영합니다.");
        } else {
            System.out.println("잘못된 이메일 또는 비밀번호입니다. 다시 시도해주세요.");
        }
    }

    // 회원가입 처리
    private static void register(Scanner scanner, Connection connection) {
        System.out.println("====================================");
        System.out.println("        회원가입 화면");
        System.out.println("====================================");
        System.out.print("이메일: ");
        String username = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();
        System.out.print("이름: ");
        String name = scanner.nextLine();
        System.out.print("학번: ");
        String studentId = scanner.nextLine();
        System.out.print("연락처: ");
        String contact = scanner.nextLine();

        if (MemberService.register(connection, username, password, name, studentId, contact)) {
            System.out.println("회원가입이 완료되었습니다! 이제 로그인하실 수 있습니다.");
        } else {
            System.out.println("회원가입에 실패했습니다. 다시 시도해주세요.");
        }
    }

    // 메인 메뉴
    private static void mainMenu(Scanner scanner, Connection connection) {
        System.out.println("====================================");
        System.out.println("               메인 메뉴");
        System.out.println("====================================");
        System.out.println("1. 회원 관리");
        System.out.println("2. 동아리 관리");
        System.out.println("3. 게시글 관리");
        System.out.println("4. 댓글 관리");
        System.out.println("5. 답글 관리");
        System.out.println("6. 로그아웃");
        System.out.print("선택: ");

        int choice = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        switch (choice) {
            case 1:
                memberManagementMenu(scanner, connection);
                break;
            case 2:
                clubManagementMenu(scanner, connection, loggedInMemberId);
                break;
            case 3:
                postManagementMenu(scanner, connection);
                break;
            case 4:
                commentManagementMenu(scanner, connection);
                break;
            case 5:
                replyManagementMenu(scanner, connection);
                break;
            case 6:
                logout();
                break;
            default:
                System.out.println("잘못된 선택입니다. 다시 시도해주세요.");
        }
    }

    // 로그아웃
    private static void logout() {
        loggedInMemberId = null;
        System.out.println("로그아웃 되었습니다. 다시 로그인해주세요.");
    }

    // 댓글 관리 메뉴
    private static void commentManagementMenu(Scanner scanner, Connection connection) {
        while (true) {
            System.out.println("\n====================================");
            System.out.println("         댓글 관리 메뉴");
            System.out.println("====================================");
            System.out.println("1. 댓글 추가");
            System.out.println("2. 댓글 수정");
            System.out.println("3. 댓글 삭제");
            System.out.println("4. 댓글 조회");
            System.out.println("5. 댓글 목록 조회");
            System.out.println("6. 이전 메뉴로 돌아가기");
            System.out.print("선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // 버퍼 비우기

            switch (choice) {
                case 1:
                    CommentService.addComment(scanner, connection, loggedInMemberId);
                    break;
                case 2:
                    CommentService.updateComment(scanner, connection, loggedInMemberId);
                    break;
                case 3:
                    CommentService.deleteComment(scanner, connection, loggedInMemberId);
                    break;
                case 4:
                    CommentService.viewComment(scanner, connection);
                    break;
                case 5:
                    CommentService.listComments(connection);
                    break;
                case 6:
                    return;  // 이전 메뉴로 돌아가기
                default:
                    System.out.println("잘못된 선택입니다. 다시 시도해주세요.");
            }
        }
    }

    private static void replyManagementMenu(Scanner scanner, Connection connection) {
        while (true) {
            System.out.println("\n====================================");
            System.out.println("         답글 관리 메뉴");
            System.out.println("====================================");
            System.out.println("1. 답글 추가");
            System.out.println("2. 답글 수정");
            System.out.println("3. 답글 삭제");
            System.out.println("4. 답글 조회");
            System.out.println("5. 이전 메뉴로 돌아가기");
            System.out.print("선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // 버퍼 비우기

            switch (choice) {
                case 1:
                    ReplyService.addReply(scanner, connection, loggedInMemberId);
                    break;
                case 2:
                    ReplyService.updateReply(scanner, connection, loggedInMemberId);
                    break;
                case 3:
                    ReplyService.deleteReply(scanner, connection, loggedInMemberId);
                    break;
                case 4:
                    ReplyService.viewReply(scanner, connection);
                    break;
                case 5:
                    return;  // 이전 메뉴로 돌아가기
                default:
                    System.out.println("잘못된 선택입니다. 다시 시도해주세요.");
            }
        }
    }

    // 회원 관리 메뉴
    private static void memberManagementMenu(Scanner scanner, Connection connection) {
        while (true) {
            System.out.println("\n====================================");
            System.out.println("         회원 관리 메뉴");
            System.out.println("====================================");
            System.out.println("1. 나의 정보 수정");
            System.out.println("2. 회원 탈퇴");
            System.out.println("3. 내가 가입한 동아리 조회");
            System.out.println("4. 이전 메뉴로 돌아가기");
            System.out.print("선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // 버퍼 비우기

            switch (choice) {
                case 1:
                    MemberService.updateMemberInfo(scanner, connection, loggedInMemberId);
                    break;
                case 2:
                    // 회원 탈퇴 처리 후 로그인 메뉴로 돌아가기
                    MemberService.deleteMember(loggedInMemberId, connection); // 수정된 부분
                    loggedInMemberId = null; // 로그아웃 처리
                    System.out.println("회원 탈퇴가 완료되었습니다. 로그인/회원가입 메뉴로 돌아갑니다.");
                    return; // 로그인 메뉴로 돌아가기
                case 3:
                    MemberService.listJoinedClubs(connection, loggedInMemberId);
                    break;
                case 4:
                    return;  // 이전 메뉴로 돌아가기
                default:
                    System.out.println("잘못된 선택입니다. 다시 시도해주세요.");
            }
        }
    }

    // 동아리 메뉴
    private static void clubManagementMenu(Scanner scanner, Connection connection, int memberId) {
        while (true) {
            System.out.println("\n====================================");
            System.out.println("         동아리 관리 메뉴");
            System.out.println("====================================");
            System.out.println("1. 동아리 가입");
            System.out.println("2. 동아리 목록 조회");
            System.out.println("3. 동아리 회장 목록 조회");
            System.out.println("4. 동아리 지도 교수 목록 조회");
            System.out.println("5. 동아리 활동 일정 목록 조회");
            System.out.println("6. 동아리 회원 조회");
            System.out.println("7. 동아리 탈퇴");
            System.out.println("8. 이전 메뉴로 돌아가기");
            System.out.print("선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // 버퍼 비우기

            switch (choice) {
                case 1:
                    ClubService.joinClub(scanner, connection, memberId);  // 로그인한 멤버 아이디를 전달
                    break;
                case 2:
                    ClubService.listClubs(connection);
                    break;
                case 3:
                    ClubService.listClubPresidents(connection);
                    break;
                case 4:
                    ClubService.listClubProfessors(connection);
                    break;
                case 5:
                    ClubService.listClubSchedules(connection);
                    break;
                case 6:
                    ClubService.listClubMembers(connection);
                    break;
                case 7:
                    ClubService.leaveClub(scanner, connection, loggedInMemberId);
                    break;
                case 8:
                    return;  // 이전 메뉴로 돌아가기
                default:
                    System.out.println("잘못된 선택입니다. 다시 시도해주세요.");
            }
        }
    }

    // 게시글 관리 메뉴
    private static void postManagementMenu(Scanner scanner, Connection connection) {
        while (true) {
            System.out.println("\n====================================");
            System.out.println("         게시글 관리 메뉴");
            System.out.println("====================================");
            System.out.println("1. 게시글 추가");
            System.out.println("2. 게시글 수정");
            System.out.println("3. 게시글 삭제");
            System.out.println("4. 게시글 조회");
            System.out.println("5. 게시글 목록 조회");
            System.out.println("6. 이전 메뉴로 돌아가기");
            System.out.print("선택: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // 버퍼 비우기

            switch (choice) {
                case 1:
                    PostService.addPost(scanner, connection, loggedInMemberId);
                    break;
                case 2:
                    PostService.updatePost(scanner, connection, loggedInMemberId);
                    break;
                case 3:
                    PostService.deletePost(scanner, connection, loggedInMemberId);
                    break;
                case 4:
                    PostService.viewPost(scanner, connection);
                    break;
                case 5:
                    PostService.listPosts(connection);
                    break;
                case 6:
                    return;  // 이전 메뉴로 돌아가기
                default:
                    System.out.println("잘못된 선택입니다. 다시 시도해주세요.");
            }
        }
    }
}
