package com.library.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    // Use absolute path to avoid directory issues
    private static final String DATABASE_DIR = System.getProperty("user.dir") + File.separator + "database";
    private static final String DATABASE_PATH = DATABASE_DIR + File.separator + "library.db";
    private static final String DATABASE_URL = "jdbc:sqlite:" + DATABASE_PATH;

    // Singleton pattern - use a single connection
    private static Connection connection = null;

    // Get database connection with directory creation
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Create database directory if it doesn't exist
                File dbDir = new File(DATABASE_DIR);
                if (!dbDir.exists()) {
                    boolean dirCreated = dbDir.mkdirs();
                    if (dirCreated) {
                        System.out.println("Database directory created: " + DATABASE_DIR);
                    } else {
                        throw new SQLException("Failed to create database directory: " + DATABASE_DIR);
                    }
                }

                // Load SQLite driver
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DATABASE_URL);

                // Create tables on first run
                createTables();

                System.out.println("Database connection successful: " + DATABASE_PATH);
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQLite JDBC driver not found!", e);
            }
        }
        return connection;
    }

    // Create database tables
    private static void createTables() throws SQLException {
        Statement stmt = connection.createStatement();

        // Books table
        String createBooksTable = """
            CREATE TABLE IF NOT EXISTS books (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                author TEXT NOT NULL,
                isbn TEXT UNIQUE,
                category TEXT,
                is_available BOOLEAN DEFAULT 1,
                added_date DATE DEFAULT CURRENT_DATE
            )
        """;

        // Users table
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT UNIQUE,
                phone TEXT,
                student_number TEXT UNIQUE,
                registration_date DATE DEFAULT CURRENT_DATE
            )
        """;

        // Loans table
        String createLoansTable = """
            CREATE TABLE IF NOT EXISTS loans (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                book_id INTEGER NOT NULL,
                user_id INTEGER NOT NULL,
                loan_date DATE DEFAULT CURRENT_DATE,
                due_date DATE NOT NULL,
                return_date DATE,
                is_returned BOOLEAN DEFAULT 0,
                fine_amount REAL DEFAULT 0.0,
                FOREIGN KEY (book_id) REFERENCES books(id),
                FOREIGN KEY (user_id) REFERENCES users(id)
            )
        """;

        // Execute table creation
        stmt.execute(createBooksTable);
        stmt.execute(createUsersTable);
        stmt.execute(createLoansTable);

        System.out.println("Database tables ready!");

        stmt.close();
    }

    // Close database connection
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    // Test database connection
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}