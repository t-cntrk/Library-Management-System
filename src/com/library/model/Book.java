package com.library.model;

import java.time.LocalDate;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private boolean isAvailable;
    private LocalDate addedDate;

    // Default constructor
    public Book() {
        this.addedDate = LocalDate.now();
        this.isAvailable = true;
    }

    // Constructor with parameters
    public Book(String title, String author, String isbn, String category) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.isAvailable = true;
        this.addedDate = LocalDate.now();
    }

    // Full constructor
    public Book(int id, String title, String author, String isbn, String category,
                boolean isAvailable, LocalDate addedDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.isAvailable = isAvailable;
        this.addedDate = addedDate;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return isAvailable; }
    public LocalDate getAddedDate() { return addedDate; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setCategory(String category) { this.category = category; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public void setAddedDate(LocalDate addedDate) { this.addedDate = addedDate; }

    @Override
    public String toString() {
        return String.format("Book{id=%d, title='%s', author='%s', isbn='%s', category='%s', available=%s}",
                id, title, author, isbn, category, isAvailable);
    }
}