package com.crs.security;

import com.crs.model.User;

public class SecurityContext {
    private static SecurityContext instance;
    private User currentUser;

    private SecurityContext() {}

    public static SecurityContext getInstance() {
        if (instance == null) {
            instance = new SecurityContext();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public boolean hasRole(User.Role role) {
        return currentUser != null && currentUser.getRole() == role;
    }

    public void clearContext() {
        currentUser = null;
    }
}