package service;

import db.DatabaseConnection;
import java.sql.*;
import java.util.Scanner;

public class ReplyService {

    // 답글 추가
    public static void addReply(Scanner scanner, Connection connection) {
        System.out.print("답글을 달 댓글 ID를 입력하세요: ");
        int commentId = scanner.nextInt();
        System.out.print("회원 ID를 입력하세요: ");
        int memberId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기
        System.out.print("답글 내용: ");
        String content = scanner.nextLine();

        // 댓글이 존재하는지 확인
        String commentCheckSql = "SELECT COUNT(*) FROM Comment WHERE commentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(commentCheckSql)) {
            stmt.setInt(1, commentId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int commentCount = rs.getInt(1);

            if (commentCount == 0) {
                System.out.println("해당 댓글 ID는 존재하지 않습니다. 답글을 추가할 수 없습니다.");
                return; // 댓글이 존재하지 않으면 답글 추가를 중단
            }
        } catch (SQLException e) {
            System.err.println("댓글 조회 중 오류 발생: " + e.getMessage());
            return;
        }

        // 회원이 존재하는지 확인
        String memberCheckSql = "SELECT COUNT(*) FROM Member WHERE memberID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(memberCheckSql)) {
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int memberCount = rs.getInt(1);

            if (memberCount == 0) {
                System.out.println("해당 회원 ID는 존재하지 않습니다. 답글을 추가할 수 없습니다.");
                return; // 회원이 존재하지 않으면 답글 추가를 중단
            }
        } catch (SQLException e) {
            System.err.println("회원 조회 중 오류 발생: " + e.getMessage());
            return;
        }

        // 답글 추가
        String sql = "INSERT INTO Reply (commentID, memberID, content, modifiedDate) VALUES (?, ?, ?, NOW())";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commentId);
            stmt.setInt(2, memberId);
            stmt.setString(3, content);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("답글이 성공적으로 추가되었습니다.");
            } else {
                System.out.println("답글 추가에 실패했습니다.");
            }
        } catch (SQLException e) {
            System.err.println("답글 추가 중 오류 발생: " + e.getMessage());
        }
    }

    // 답글 수정
    public static void updateReply(Scanner scanner, Connection connection) {
        System.out.print("수정할 답글 ID를 입력하세요: ");
        int replyId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        System.out.print("수정할 내용: ");
        String newContent = scanner.nextLine();

        String sql = "UPDATE Reply SET content = ?, modifiedDate = NOW() WHERE replyID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newContent);
            stmt.setInt(2, replyId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("답글이 성공적으로 수정되었습니다.");
            } else {
                System.out.println("답글 수정에 실패했습니다.");
            }
        } catch (SQLException e) {
            System.err.println("답글 수정 중 오류 발생: " + e.getMessage());
        }
    }

    // 답글 삭제
    public static void deleteReply(Scanner scanner, Connection connection) {
        System.out.print("삭제할 답글 ID를 입력하세요: ");
        int replyId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        String sql = "DELETE FROM Reply WHERE replyID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, replyId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("답글이 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("답글 삭제에 실패했습니다. 해당 답글이 존재하지 않거나 이미 삭제되었습니다.");
            }
        } catch (SQLException e) {
            System.err.println("답글 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    // 답글 조회
    public static void viewReply(Scanner scanner, Connection connection) {
        System.out.print("조회할 답글 ID를 입력하세요: ");
        int replyId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        String sql = "SELECT * FROM Reply WHERE replyID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, replyId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("답글 ID: " + rs.getInt("replyID"));
                System.out.println("댓글 ID: " + rs.getInt("commentID"));
                System.out.println("회원 ID: " + rs.getInt("memberID"));
                System.out.println("내용: " + rs.getString("content"));
                System.out.println("수정일: " + rs.getTimestamp("modifiedDate"));
            } else {
                System.out.println("해당 답글을 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            System.err.println("답글 조회 중 오류 발생: " + e.getMessage());
        }
    }
}
