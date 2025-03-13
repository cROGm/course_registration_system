package com.crs.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Can be null for FACULTY and ADMIN roles
    @Column(name = "studentId")
    private Integer studentId;

    private boolean active = true;

    public enum Role {
        STUDENT, FACULTY, ADMIN
    }

    // Add validation in setter
    public void setStudentId(Integer studentId) {
        if (role == Role.STUDENT && studentId == null) {
            throw new IllegalArgumentException("Student ID is required for student users");
        }
        this.studentId = studentId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}