package com.library.dao;

import com.library.model.Book;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    // Add book
    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, category) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setString(4, book.getCategory());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            return false;
        }
    }

    // Get all books
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY title";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("category"),
                        rs.getBoolean("is_available"),
                        LocalDate.parse(rs.getString("added_date"))
                );
                books.add(book);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving books: " + e.getMessage());
        }

        return books;
    }

    // Get book by ID
    public Book getBookById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("category"),
                        rs.getBoolean("is_available"),
                        LocalDate.parse(rs.getString("added_date"))
                );
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving book: " + e.getMessage());
        }

        return null;
    }

    // Search books (by title or author)
    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? ORDER BY title";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + keyword + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("category"),
                        rs.getBoolean("is_available"),
                        LocalDate.parse(rs.getString("added_date"))
                );
                books.add(book);
            }

        } catch (SQLException e) {
            System.err.println("Error searching books: " + e.getMessage());
        }

        return books;
    }

    // Update book availability status
    public boolean updateBookAvailability(int bookId, boolean isAvailable) {
        String sql = "UPDATE books SET is_available = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, isAvailable);
            pstmt.setInt(2, bookId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating book status: " + e.getMessage());
            return false;
        }
    }

    // Delete book
    public boolean deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            return false;
        }
    }
}