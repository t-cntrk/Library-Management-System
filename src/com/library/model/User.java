package com.library.model;

import java.time.LocalDate;

public class User {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String studentNumber;
    private LocalDate registrationDate;

    public User() {
        this.registrationDate = LocalDate.now();
    }

    public User(String name, String email, String phone, String studentNumber) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.studentNumber = studentNumber;
        this.registrationDate = LocalDate.now();
    }

    public User(int id, String name, String email, String phone, String studentNumber, LocalDate registrationDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.studentNumber = studentNumber;
        this.registrationDate = registrationDate;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getStudentNumber() { return studentNumber; }
    public LocalDate getRegistrationDate() { return registrationDate; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    @Override
    public String toString() {
        return String.format("User{id=%d, name='%s', email='%s', studentNumber='%s'}",
                id, name, email, studentNumber);
    }
}