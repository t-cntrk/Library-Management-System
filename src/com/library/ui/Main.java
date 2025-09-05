package com.library.ui;

import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.User;
import com.library.service.LibraryService;
import com.library.util.DatabaseConnection;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static LibraryService libraryService;
    private static Scanner scanner;

    public static void main(String[] args) {
        System.out.println("=== Library Management System ===\n");

        // Test database connection
        if (!DatabaseConnection.testConnection()) {
            System.err.println("Database connection failed! Program terminating.");
            return;
        }

        libraryService = new LibraryService();
        scanner = new Scanner(System.in);

        mainMenu();

        DatabaseConnection.closeConnection();
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Book Operations");
            System.out.println("2. Member Operations");
            System.out.println("3. Loan Operations");
            System.out.println("4. Reports");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            switch (choice) {
                case 1 -> bookMenu();
                case 2 -> userMenu();
                case 3 -> loanMenu();
                case 4 -> reportsMenu();
                case 0 -> {
                    System.out.println("Program terminating...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void bookMenu() {
        while (true) {
            System.out.println("\n=== BOOK OPERATIONS ===");
            System.out.println("1. Add New Book");
            System.out.println("2. List All Books");
            System.out.println("3. Search Books");
            System.out.println("4. Add Test Data");
            System.out.println("0. Back to Main Menu");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addBook();
                case 2 -> listAllBooks();
                case 3 -> searchBooks();
                case 4 -> addTestData();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void userMenu() {
        while (true) {
            System.out.println("\n=== MEMBER OPERATIONS ===");
            System.out.println("1. Add New Member");
            System.out.println("2. List All Members");
            System.out.println("0. Back to Main Menu");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addUser();
                case 2 -> listAllUsers();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void loanMenu() {
        while (true) {
            System.out.println("\n=== LOAN OPERATIONS ===");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. Active Loans");
            System.out.println("0. Back to Main Menu");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> borrowBook();
                case 2 -> returnBook();
                case 3 -> showActiveLoans();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void reportsMenu() {
        while (true) {
            System.out.println("\n=== REPORTS ===");
            System.out.println("1. All Loan History");
            System.out.println("2. Overdue Books");
            System.out.println("0. Back to Main Menu");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> showAllLoans();
                case 2 -> showOverdueLoans();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // Book operations
    private static void addBook() {
        System.out.println("\n--- ADD NEW BOOK ---");
        System.out.print("Book Title: ");
        String title = scanner.nextLine();

        System.out.print("Author: ");
        String author = scanner.nextLine();

        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        System.out.print("Category: ");
        String category = scanner.nextLine();

        Book book = new Book(title, author, isbn, category);

        if (libraryService.addBook(book)) {
            System.out.println("✅ Book added successfully!");
        } else {
            System.out.println("❌ Error adding book!");
        }
    }

    private static void listAllBooks() {
        System.out.println("\n--- ALL BOOKS ---");
        List<Book> books = libraryService.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("No books added yet.");
            return;
        }

        System.out.printf("%-5s %-30s %-20s %-15s %-10s%n",
                "ID", "Title", "Author", "Category", "Status");
        System.out.println("-".repeat(80));

        for (Book book : books) {
            System.out.printf("%-5d %-30s %-20s %-15s %-10s%n",
                    book.getId(),
                    truncate(book.getTitle(), 30),
                    truncate(book.getAuthor(), 20),
                    truncate(book.getCategory(), 15),
                    book.isAvailable() ? "Available" : "On Loan");
        }
    }

    private static void searchBooks() {
        System.out.println("\n--- SEARCH BOOKS ---");
        System.out.print("Search keyword (title or author): ");
        String keyword = scanner.nextLine();

        List<Book> books = libraryService.searchBooks(keyword);

        if (books.isEmpty()) {
            System.out.println("No books found matching your search criteria.");
            return;
        }

        System.out.println("\n--- SEARCH RESULTS ---");
        System.out.printf("%-5s %-30s %-20s %-15s %-10s%n",
                "ID", "Title", "Author", "Category", "Status");
        System.out.println("-".repeat(80));

        for (Book book : books) {
            System.out.printf("%-5d %-30s %-20s %-15s %-10s%n",
                    book.getId(),
                    truncate(book.getTitle(), 30),
                    truncate(book.getAuthor(), 20),
                    truncate(book.getCategory(), 15),
                    book.isAvailable() ? "Available" : "On Loan");
        }
    }

    // User operations
    private static void addUser() {
        System.out.println("\n--- ADD NEW MEMBER ---");
        System.out.print("Full Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.print("Student Number: ");
        String studentNumber = scanner.nextLine();

        User user = new User(name, email, phone, studentNumber);

        if (libraryService.addUser(user)) {
            System.out.println("✅ Member added successfully!");
        } else {
            System.out.println("❌ Error adding member!");
        }
    }

    private static void listAllUsers() {
        System.out.println("\n--- ALL MEMBERS ---");
        List<User> users = libraryService.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("No members added yet.");
            return;
        }

        System.out.printf("%-5s %-25s %-25s %-15s%n",
                "ID", "Full Name", "Email", "Student No");
        System.out.println("-".repeat(70));

        for (User user : users) {
            System.out.printf("%-5d %-25s %-25s %-15s%n",
                    user.getId(),
                    truncate(user.getName(), 25),
                    truncate(user.getEmail(), 25),
                    user.getStudentNumber());
        }
    }

    // Loan operations
    private static void borrowBook() {
        System.out.println("\n--- BORROW BOOK ---");

        System.out.print("Book ID: ");
        int bookId = scanner.nextInt();

        System.out.print("Member ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        if (libraryService.borrowBook(bookId, userId)) {
            System.out.println("✅ Book borrowed successfully!");
        } else {
            System.out.println("❌ Error borrowing book! Is the book available? Check IDs.");
        }
    }

    private static void returnBook() {
        System.out.println("\n--- RETURN BOOK ---");

        System.out.print("Loan ID: ");
        int loanId = scanner.nextInt();
        scanner.nextLine();

        if (libraryService.returnBook(loanId)) {
            System.out.println("✅ Book returned successfully!");
        } else {
            System.out.println("❌ Error returning book! Check loan ID.");
        }
    }

    private static void showActiveLoans() {
        System.out.println("\n--- ACTIVE LOANS ---");
        List<Loan> loans = libraryService.getActiveLoans();

        if (loans.isEmpty()) {
            System.out.println("No active loans found.");
            return;
        }

        System.out.printf("%-8s %-8s %-8s %-12s %-12s %-10s%n",
                "Loan ID", "Book ID", "User ID", "Loan Date", "Due Date", "Overdue");
        System.out.println("-".repeat(60));

        for (Loan loan : loans) {
            long overdueDays = loan.getOverdueDays();
            System.out.printf("%-8d %-8d %-8d %-12s %-12s %-10s%n",
                    loan.getId(),
                    loan.getBookId(),
                    loan.getUserId(),
                    loan.getLoanDate(),
                    loan.getDueDate(),
                    overdueDays > 0 ? overdueDays + " days" : "None");
        }
    }

    // Reports
    private static void showAllLoans() {
        System.out.println("\n--- ALL LOAN HISTORY ---");
        List<Loan> loans = libraryService.getAllLoans();

        if (loans.isEmpty()) {
            System.out.println("No loan records found.");
            return;
        }

        System.out.printf("%-8s %-8s %-8s %-12s %-12s %-12s %-8s %-8s%n",
                "Loan ID", "Book ID", "User ID", "Borrowed", "Due", "Returned", "Status", "Fine");
        System.out.println("-".repeat(80));

        for (Loan loan : loans) {
            System.out.printf("%-8d %-8d %-8d %-12s %-12s %-12s %-8s %.2f%n",
                    loan.getId(),
                    loan.getBookId(),
                    loan.getUserId(),
                    loan.getLoanDate(),
                    loan.getDueDate(),
                    loan.getReturnDate() != null ? loan.getReturnDate() : "-",
                    loan.isReturned() ? "Returned" : "Active",
                    loan.getFineAmount());
        }
    }

    private static void showOverdueLoans() {
        System.out.println("\n--- OVERDUE BOOKS ---");
        List<Loan> loans = libraryService.getActiveLoans();
        loans.removeIf(loan -> loan.getOverdueDays() <= 0);

        if (loans.isEmpty()) {
            System.out.println("No overdue books found.");
            return;
        }

        System.out.printf("%-8s %-8s %-8s %-12s %-12s %-10s %-8s%n",
                "Loan ID", "Book ID", "User ID", "Loan Date", "Due Date", "Overdue", "Fine");
        System.out.println("-".repeat(70));

        for (Loan loan : loans) {
            long overdueDays = loan.getOverdueDays();
            double fine = loan.calculateFine();
            System.out.printf("%-8d %-8d %-8d %-12s %-12s %-10d %.2f%n",
                    loan.getId(),
                    loan.getBookId(),
                    loan.getUserId(),
                    loan.getLoanDate(),
                    loan.getDueDate(),
                    overdueDays,
                    fine);
        }
    }

    // Test data
    private static void addTestData() {
        System.out.println("\n--- ADDING TEST DATA ---");

        // Test books
        Book[] testBooks = {
                new Book("Java: The Complete Reference", "Herbert Schildt", "978-1260440232", "Programming"),
                new Book("Effective Java", "Joshua Bloch", "978-0134685991", "Programming"),
                new Book("Spring in Action", "Craig Walls", "978-1617294945", "Framework"),
                new Book("Clean Code", "Robert C. Martin", "978-0132350884", "Software Engineering")
        };

        int successCount = 0;
        for (Book book : testBooks) {
            if (libraryService.addBook(book)) {
                successCount++;
            }
        }

        // Test users
        User[] testUsers = {
                new User("John Doe", "john@email.com", "5551234567", "20230001"),
                new User("Jane Smith", "jane@email.com", "5557654321", "20230002")
        };

        for (User user : testUsers) {
            if (libraryService.addUser(user)) {
                successCount++;
            }
        }

        System.out.println("✅ " + successCount + " test records added successfully!");
    }

    // Helper method
    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }
}