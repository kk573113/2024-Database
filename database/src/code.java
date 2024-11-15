import java.sql.*;
import java.util.Scanner;

public class DBManager {
    private static final String URL = "jdbc:mysql://localhost:3306/madang";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";
    private static Connection conn = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // 데이터베이스 연결
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(URL, USER, PASSWORD);
        stmt = conn.createStatement();
        
        while (true) {
            System.out.println("\n=== 도서관리 시스템 ===");
            System.out.println("1. 도서 검색");
            System.out.println("2. 도서 추가");
            System.out.println("3. 도서 삭제");
            System.out.println("4. 종료");
            System.out.print("선택하세요: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // 버퍼 비우기
            
            switch (choice) {
                case 1:
                    searchBooks();
                    break;
                case 2:
                    insertBook();
                    break;
                case 3:
                    deleteBook();
                    break;
                case 4:
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }
    }
    
    private static void searchBooks() {
        System.out.println("\n=== 도서 검색 ===");
        System.out.println("1. 전체 도서 보기");
        System.out.println("2. 도서명으로 검색");
        System.out.print("선택하세요: ");
        
        int searchChoice = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기
        
        String query;
        if (searchChoice == 1) {
            query = "SELECT * FROM Book";
            rs = stmt.executeQuery(query);
        } else if (searchChoice == 2) {
            System.out.print("검색할 도서명을 입력하세요: ");
            String bookName = scanner.nextLine();
            query = "SELECT * FROM Book WHERE bookname LIKE '%" + bookName + "%'";
            rs = stmt.executeQuery(query);
        } else {
            System.out.println("잘못된 선택입니다.");
            return;
        }
        
        while (rs.next()) {
            System.out.printf("ID: %d, 도서명: %s, 출판사: %s, 가격: %d원\n",
                rs.getInt("bookid"),
                rs.getString("bookname"),
                rs.getString("publisher"),
                rs.getInt("price")
            );
        }
    }
    
    private static void insertBook() {
        System.out.println("\n=== 도서 추가 ===");
        System.out.print("도서명: ");
        String bookName = scanner.nextLine();
        System.out.print("출판사: ");
        String publisher = scanner.nextLine();
        System.out.print("가격: ");
        int price = scanner.nextInt();
        
        String query = String.format(
            "INSERT INTO Book (bookname, publisher, price) VALUES ('%s', '%s', %d)",
            bookName, publisher, price
        );
        
        int result = stmt.executeUpdate(query);
        if (result > 0) {
            System.out.println("도서가 성공적으로 추가되었습니다.");
        } else {
            System.out.println("도서 추가에 실패했습니다.");
        }
    }
    
    private static void deleteBook() {
        System.out.println("\n=== 도서 삭제 ===");
        System.out.print("삭제할 도서의 ID를 입력하세요: ");
        int bookId = scanner.nextInt();
        
        String query = "DELETE FROM Book WHERE bookid = " + bookId;
        int result = stmt.executeUpdate(query);
        
        if (result > 0) {
            System.out.println("도서가 성공적으로 삭제되었습니다.");
        } else {
            System.out.println("해당 ID의 도서를 찾을 수 없습니다.");
        }
    }
}