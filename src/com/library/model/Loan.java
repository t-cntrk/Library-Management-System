package com.library.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Loan {
    private int id;
    private int bookId;
    private int userId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean isReturned;
    private double fineAmount;

    // Loan period in days
    public static final int LOAN_PERIOD_DAYS = 14;
    // Daily late fee amount
    public static final double DAILY_FINE = 0.50;

    public Loan() {
        this.loanDate = LocalDate.now();
        this.dueDate = this.loanDate.plusDays(LOAN_PERIOD_DAYS);
        this.isReturned = false;
        this.fineAmount = 0.0;
    }

    public Loan(int bookId, int userId) {
        this.bookId = bookId;
        this.userId = userId;
        this.loanDate = LocalDate.now();
        this.dueDate = this.loanDate.plusDays(LOAN_PERIOD_DAYS);
        this.isReturned = false;
        this.fineAmount = 0.0;
    }

    public Loan(int id, int bookId, int userId, LocalDate loanDate,
                LocalDate dueDate, LocalDate returnDate, boolean isReturned, double fineAmount) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.isReturned = isReturned;
        this.fineAmount = fineAmount;
    }

    // Calculate overdue days
    public long getOverdueDays() {
        if (isReturned && returnDate != null) {
            return returnDate.isAfter(dueDate) ? ChronoUnit.DAYS.between(dueDate, returnDate) : 0;
        } else {
            LocalDate today = LocalDate.now();
            return today.isAfter(dueDate) ? ChronoUnit.DAYS.between(dueDate, today) : 0;
        }
    }

    // Calculate late fee
    public double calculateFine() {
        long overdueDays = getOverdueDays();
        return overdueDays > 0 ? overdueDays * DAILY_FINE : 0.0;
    }

    // Return the book
    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.isReturned = true;
        this.fineAmount = calculateFine();
    }

    // Getters
    public int getId() { return id; }
    public int getBookId() { return bookId; }
    public int getUserId() { return userId; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public boolean isReturned() { return isReturned; }
    public double getFineAmount() { return fineAmount; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setReturned(boolean returned) { isReturned = returned; }
    public void setFineAmount(double fineAmount) { this.fineAmount = fineAmount; }

    @Override
    public String toString() {
        return String.format("Loan{id=%d, bookId=%d, userId=%d, loanDate=%s, dueDate=%s, returned=%s, fine=%.2f}",
                id, bookId, userId, loanDate, dueDate, isReturned, fineAmount);
    }
}