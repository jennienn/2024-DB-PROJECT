package service;

import db.DatabaseConnection;
import java.sql.*;
import java.util.Scanner;

public class PostService {

    // 게시글 추가
    public static void addPost(Scanner scanner, Connection connection, int memberId) {
        System.out.print("제목: ");
        String title = scanner.nextLine();
        System.out.print("내용: ");
        String content = scanner.nextLine();

        String sql = "INSERT INTO Post (memberID, title, content, createdDate) VALUES (?, ?, ?, NOW())";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            stmt.setString(2, title);
            stmt.setString(3, content);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("게시글이 성공적으로 추가되었습니다.");
            } else {
                System.out.println("게시글 추가에 실패했습니다.");
            }
        } catch (SQLException e) {
            System.err.println("게시글 추가 중 오류 발생: " + e.getMessage());
        }
    }

    // 게시글 수정
    public static void updatePost(Scanner scanner, Connection connection) {
        System.out.print("수정할 게시글 ID를 입력하세요: ");
        int postId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        System.out.print("수정할 제목: ");
        String newTitle = scanner.nextLine();
        System.out.print("수정할 내용: ");
        String newContent = scanner.nextLine();

        String sql = "UPDATE Post SET title = ?, content = ?, modifiedDate = NOW() WHERE postID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newTitle);
            stmt.setString(2, newContent);
            stmt.setInt(3, postId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("게시글이 성공적으로 수정되었습니다.");
            } else {
                System.out.println("게시글 수정에 실패했습니다.");
            }
        } catch (SQLException e) {
            System.err.println("게시글 수정 중 오류 발생: " + e.getMessage());
        }
    }

    // 게시글 삭제
    public static void deletePost(Scanner scanner, Connection connection) {
        System.out.print("삭제할 게시글 ID를 입력하세요: ");
        int postId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        String sql = "DELETE FROM Post WHERE postID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("게시글이 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("게시글 삭제에 실패했습니다. 해당 게시글이 존재하지 않거나 이미 삭제되었습니다.");
            }
        } catch (SQLException e) {
            System.err.println("게시글 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    // 게시글 조회
    public static void viewPost(Scanner scanner, Connection connection) {
        System.out.print("조회할 게시글 ID를 입력하세요: ");
        int postId = scanner.nextInt();
        scanner.nextLine();  // 버퍼 비우기

        String sql = "SELECT * FROM Post WHERE postID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("게시글 ID: " + rs.getInt("postID"));
                System.out.println("제목: " + rs.getString("title"));
                System.out.println("내용: " + rs.getString("content"));
                System.out.println("작성일: " + rs.getTimestamp("createdDate"));
                System.out.println("수정일: " + rs.getTimestamp("modifiedDate"));
            } else {
                System.out.println("해당 게시글을 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            System.err.println("게시글 조회 중 오류 발생: " + e.getMessage());
        }
    }

    // 게시글 목록 조회
    public static void listPosts(Connection connection) {
        String sql = "SELECT * FROM Post ORDER BY createdDate DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                System.out.println("게시글 ID: " + rs.getInt("postID"));
                System.out.println("제목: " + rs.getString("title"));
                System.out.println("작성일: " + rs.getTimestamp("createdDate"));
                System.out.println("수정일: " + rs.getTimestamp("modifiedDate"));
                System.out.println("------------");
            }
        } catch (SQLException e) {
            System.err.println("게시글 목록 조회 중 오류 발생: " + e.getMessage());
        }
    }
}
