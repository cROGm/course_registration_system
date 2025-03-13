package com.crs.controller;

import com.crs.model.User;
import com.crs.service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private AuthenticationService authService = new AuthenticationService();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both username and password");
            return;
        }

        try {
            if (authService.login(username, password)) {
                User currentUser = authService.getCurrentUser();
                
                // Additional validation for student role
                if (currentUser.getRole() == User.Role.STUDENT && currentUser.getStudentId() == null) {
                    errorLabel.setText("Invalid student account configuration");
                    return;
                }

                // Navigate based on role
                switch (currentUser.getRole()) {
                    case ADMIN:
                        loadAdminDashboard();
                        break;
                    case FACULTY:
                        loadFacultyDashboard();
                        break;
                    case STUDENT:
                        loadStudentDashboard();
                        break;
                }
            } else {
                errorLabel.setText("Invalid username or password");
            }
        } catch (Exception e) {
            errorLabel.setText("Login error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void showRegister() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/crs/register-view.fxml")));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            errorLabel.setText("Error loading registration page");
            System.out.println("Error loading registration page: " + e.getMessage());
        }
    }

    private void loadAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/crs/admin-dashboard-view.fxml"));
            Scene scene = new Scene(loader.load());

            AdminDashboardController controller = loader.getController();
            controller.initData(authService.getCurrentUser());

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            errorLabel.setText("Error loading admin dashboard");
            e.printStackTrace();
        }
    }

    private void loadFacultyDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/crs/faculty-dashboard-view.fxml"));
            Scene scene = new Scene(loader.load());

            FacultyDashboardController controller = loader.getController();
            controller.initData(authService.getCurrentUser());

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            errorLabel.setText("Error loading faculty dashboard");
            e.printStackTrace();
        }
    }

    private void loadStudentDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/crs/student-dashboard-view.fxml"));
            Scene scene = new Scene(loader.load());

            StudentDashboardController controller = loader.getController();
            controller.initData(authService.getCurrentUser());

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            errorLabel.setText("Error loading student dashboard");
            e.printStackTrace();
        }
    }
}