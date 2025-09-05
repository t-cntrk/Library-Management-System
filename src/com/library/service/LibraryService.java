package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.LoanDAO;
import com.library.dao.UserDAO;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.User;

import java.util.List;

public class LibraryService {
    private BookDAO bookDAO;
    private UserDAO userDAO;
    private LoanDAO loanDAO;

    public LibraryService() {
        this.bookDAO = new BookDAO();
        this.userDAO = new UserDAO();
        this.loanDAO = new LoanDAO();
    }

    // Book operations
    public boolean addBook(Book book) {
        return bookDAO.addBook(book);
    }

    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    public List<Book> searchBooks(String keyword) {
        return bookDAO.searchBooks(keyword);
    }

    // User operations
    public boolean addUser(User user) {
        return userDAO.addUser(user);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // Loan operations
    public boolean borrowBook(int bookId, int userId) {
        Book book = bookDAO.getBookById(bookId);
        if (book != null && book.isAvailable()) {
            Loan loan = new Loan(bookId, userId);
            if (loanDAO.addLoan(loan)) {
                return bookDAO.updateBookAvailability(bookId, false);
            }
        }
        return false;
    }

    public boolean returnBook(int loanId) {
        Loan loan = loanDAO.getLoanById(loanId);
        if (loan != null && !loan.isReturned()) {
            if (loanDAO.returnBook(loanId)) {
                return bookDAO.updateBookAvailability(loan.getBookId(), true);
            }
        }
        return false;
    }

    public List<Loan> getActiveLoans() {
        return loanDAO.getActiveLoans();
    }

    public List<Loan> getAllLoans() {
        return loanDAO.getAllLoans();
    }
}