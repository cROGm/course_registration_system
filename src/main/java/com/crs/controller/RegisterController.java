package com.crs.controller;

import com.crs.model.User;
import com.crs.service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<User.Role> roleComboBox;
    @FXML private TextField studentIdField;
    @FXML private Label errorLabel;

    private AuthenticationService authService = new AuthenticationService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        roleComboBox.getItems().addAll(User.Role.values());
        
        // Show/hide student ID field based on role selection
        roleComboBox.setOnAction(e -> {
            boolean isStudent = roleComboBox.getValue() == User.Role.STUDENT;
            studentIdField.setVisible(isStudent);
            studentIdField.setManaged(isStudent);
        });

        // Initially hide student ID field
        studentIdField.setVisible(false);
        studentIdField.setManaged(false);
    }

    @FXML
    private void handleRegister() {
        errorLabel.setText("");

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        User.Role selectedRole = roleComboBox.getValue();
        String studentIdText = studentIdField.getText().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("All fields are required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match");
            return;
        }

        if (selectedRole == null) {
            errorLabel.setText("Please select a role");
            return;
        }

        // Validate student ID for student role
        Integer studentId = null;
        if (selectedRole == User.Role.STUDENT) {
            if (studentIdText.isEmpty()) {
                errorLabel.setText("Student ID is required for student registration");
                return;
            }
            try {
                studentId = Integer.parseInt(studentIdText);
            } catch (NumberFormatException e) {
                errorLabel.setText("Invalid Student ID format");
                return;
            }
        }

        // Attempt registration
        try {
            authService.registerUser(username, password, selectedRole, studentId);
            showLogin();
        } catch (Exception e) {
            errorLabel.setText("Registration failed: " + e.getMessage());
        }
    }

    @FXML
    private void showLogin() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/crs/login-view.fxml")));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            errorLabel.setText("Error returning to login page");
        }
    }
}