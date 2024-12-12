package service;

import db.DatabaseConnection;
import java.sql.*;
import java.util.Scanner;

public class CommentService {

    // 댓글 추가
    public static void addComment(Scanner scanner, Connection connection, int loggedInMemberId) {
        System.out.print("게시글 ID를 입력하세요: ");
        int postId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기
        System.out.print("댓글 내용: ");
        String content = scanner.nextLine();

        // 게시글이 존재하는지 확인
        String postCheckSql = "SELECT COUNT(*) FROM Post WHERE postID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(postCheckSql)) {
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int postCount = rs.getInt(1);

            if (postCount == 0) {
                System.out.println("해당 게시글 ID는 존재하지 않습니다. 댓글을 추가할 수 없습니다.");
                return; // 게시글이 존재하지 않으면 댓글 추가를 중단
            }
        } catch (SQLException e) {
            System.err.println("게시글 조회 중 오류 발생: " + e.getMessage());
            return;
        }

        // 댓글 추가
        String sql = "INSERT INTO Comment (postID, memberID, content, createdDate) VALUES (?, ?, ?, NOW())";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, loggedInMemberId); // 로그인한 회원 ID로 댓글 작성
            stmt.setString(3, content);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("댓글이 성공적으로 추가되었습니다.");
            } else {
                System.out.println("댓글 추가에 실패했습니다.");
            }
        } catch (SQLException e) {
            System.err.println("댓글 추가 중 오류 발생: " + e.getMessage());
        }
    }

    // 댓글 수정
    public static void updateComment(Scanner scanner, Connection connection, int loggedInMemberId) {
        System.out.print("수정할 댓글 ID를 입력하세요: ");
        int commentId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        // 댓글 작성자 확인
        String authorCheckSql = "SELECT memberID FROM Comment WHERE commentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(authorCheckSql)) {
            stmt.setInt(1, commentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int commentAuthorId = rs.getInt("memberID");

                // 로그인한 회원과 댓글 작성자가 일치하는지 확인
                if (loggedInMemberId != commentAuthorId) {
                    System.out.println("본인이 작성한 댓글만 수정할 수 있습니다.");
                    return;
                }
            } else {
                System.out.println("해당 댓글을 찾을 수 없습니다.");
                return;
            }
        } catch (SQLException e) {
            System.err.println("댓글 작성자 확인 중 오류 발생: " + e.getMessage());
            return;
        }

        System.out.print("수정할 내용: ");
        String newContent = scanner.nextLine();

        String sql = "UPDATE Comment SET content = ?, modifiedDate = NOW() WHERE commentID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newContent);
            stmt.setInt(2, commentId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("댓글이 성공적으로 수정되었습니다.");
            } else {
                System.out.println("댓글 수정에 실패했습니다.");
            }
        } catch (SQLException e) {
            System.err.println("댓글 수정 중 오류 발생: " + e.getMessage());
        }
    }

    // 댓글 삭제
    public static void deleteComment(Scanner scanner, Connection connection, int loggedInMemberId) {
        System.out.print("삭제할 댓글 ID를 입력하세요: ");
        int commentId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        // 댓글 작성자 확인
        String authorCheckSql = "SELECT memberID FROM Comment WHERE commentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(authorCheckSql)) {
            stmt.setInt(1, commentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int commentAuthorId = rs.getInt("memberID");

                // 로그인한 회원과 댓글 작성자가 일치하는지 확인
                if (loggedInMemberId != commentAuthorId) {
                    System.out.println("본인이 작성한 댓글만 삭제할 수 있습니다.");
                    return;
                }
            } else {
                System.out.println("해당 댓글을 찾을 수 없습니다.");
                return;
            }
        } catch (SQLException e) {
            System.err.println("댓글 작성자 확인 중 오류 발생: " + e.getMessage());
            return;
        }

        String sql = "DELETE FROM Comment WHERE commentID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commentId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("댓글이 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("댓글 삭제에 실패했습니다. 해당 댓글이 존재하지 않거나 이미 삭제되었습니다.");
            }
        } catch (SQLException e) {
            System.err.println("댓글 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    // 댓글 조회
    public static void viewComment(Scanner scanner, Connection connection) {
        System.out.print("조회할 댓글 ID를 입력하세요: ");
        int commentId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        String sql = "SELECT * FROM Comment WHERE commentID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("댓글 ID: " + rs.getInt("commentID"));
                System.out.println("게시글 ID: " + rs.getInt("postID"));
                System.out.println("회원 ID: " + rs.getInt("memberID"));
                System.out.println("내용: " + rs.getString("content"));
                System.out.println("작성일: " + rs.getDate("createdDate"));
                System.out.println("수정일: " + rs.getDate("modifiedDate"));
            } else {
                System.out.println("해당 댓글을 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            System.err.println("댓글 조회 중 오류 발생: " + e.getMessage());
        }
    }

    // 댓글 목록 조회
    public static void listComments(Connection connection) {
        String sql = "SELECT * FROM Comment ORDER BY createdDate DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("댓글 ID: " + rs.getInt("commentID"));
                System.out.println("게시글 ID: " + rs.getInt("postID"));
                System.out.println("회원 ID: " + rs.getInt("memberID"));
                System.out.println("내용: " + rs.getString("content"));
                System.out.println("작성일: " + rs.getDate("createdDate"));
                System.out.println("수정일: " + rs.getDate("modifiedDate"));
                System.out.println("------------");
            }
        } catch (SQLException e) {
            System.err.println("댓글 목록 조회 중 오류 발생: " + e.getMessage());
        }
    }
}
