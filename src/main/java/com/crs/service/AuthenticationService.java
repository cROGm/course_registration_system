package com.crs.service;

import com.crs.dao.UserDAO;
import com.crs.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AuthenticationService {
    private UserDAO userDAO;
    private User currentUser;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

    public boolean login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && user.isActive() && verifyPassword(password, user.getPassword())) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean hasPermission(String operation) {
        if (currentUser == null) return false;

        switch (currentUser.getRole()) {
            case ADMIN:
                return true;
            case FACULTY:
                return operation.startsWith("VIEW_") ||
                       operation.equals("MANAGE_GRADES") ||
                       operation.equals("VIEW_COURSE_ROSTER");
            case STUDENT:
                if (currentUser.getStudentId() == null) {
                    return false;
                }
                return operation.equals("VIEW_GRADES") ||
                       operation.equals("VIEW_COURSES") ||
                       operation.equals("ENROLL_COURSE");
            default:
                return false;
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password");
        }
    }

    private boolean verifyPassword(String inputPassword, String storedPassword) {
        String hashedInput = hashPassword(inputPassword);
        return hashedInput.equals(storedPassword);
    }

    public void registerUser(String username, String password, User.Role role, Integer studentId) {
        User existingUser = userDAO.findByUsername(username);
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }

        // Check if student ID is already registered
        if (studentId != null) {
            User existingStudentUser = userDAO.findByStudentId(studentId);
            if (existingStudentUser != null) {
                throw new RuntimeException("Student ID is already registered");
            }
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashPassword(password));
        user.setRole(role);
        user.setStudentId(studentId);
        user.setActive(true);

        userDAO.saveUser(user);
    }
}