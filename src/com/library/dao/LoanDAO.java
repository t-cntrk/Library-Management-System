package com.library.dao;

import com.library.model.Loan;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    // Add new loan record
    public boolean addLoan(Loan loan) {
        String sql = "INSERT INTO loans (book_id, user_id, loan_date, due_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, loan.getBookId());
            pstmt.setInt(2, loan.getUserId());
            pstmt.setString(3, loan.getLoanDate().toString());
            pstmt.setString(4, loan.getDueDate().toString());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error adding loan: " + e.getMessage());
            return false;
        }
    }

    // Mark book as returned
    public boolean returnBook(int loanId) {
        String sql = "UPDATE loans SET return_date = CURRENT_DATE, is_returned = 1, fine_amount = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Calculate fine before updating
            Loan loan = getLoanById(loanId);
            if (loan != null) {
                double fine = loan.calculateFine();
                pstmt.setDouble(1, fine);
                pstmt.setInt(2, loanId);

                return pstmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error returning book: " + e.getMessage());
        }

        return false;
    }

    // Get all loan records
    public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans ORDER BY loan_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                loans.add(createLoanFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving loans: " + e.getMessage());
        }

        return loans;
    }

    // Get loan by ID
    public Loan getLoanById(int id) {
        String sql = "SELECT * FROM loans WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return createLoanFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving loan: " + e.getMessage());
        }

        return null;
    }

    // Get all active (not returned) loans
    public List<Loan> getActiveLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE is_returned = 0 ORDER BY due_date";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                loans.add(createLoanFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving active loans: " + e.getMessage());
        }

        return loans;
    }

    // Helper method to create Loan object from ResultSet
    private Loan createLoanFromResultSet(ResultSet rs) throws SQLException {
        return new Loan(
                rs.getInt("id"),
                rs.getInt("book_id"),
                rs.getInt("user_id"),
                LocalDate.parse(rs.getString("loan_date")),
                LocalDate.parse(rs.getString("due_date")),
                rs.getString("return_date") != null ? LocalDate.parse(rs.getString("return_date")) : null,
                rs.getBoolean("is_returned"),
                rs.getDouble("fine_amount")
        );
    }
}