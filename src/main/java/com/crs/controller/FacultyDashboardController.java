package com.crs.controller;

import com.crs.dao.CourseDAO;
import com.crs.dao.EnrollmentDAO;
import com.crs.model.*;
import com.crs.service.AcademicRecordService;
import com.crs.service.AuthenticationService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class FacultyDashboardController {
    @FXML private Label facultyNameLabel;
    @FXML private Text welcomeUsernameText;
    @FXML private StackPane contentArea;

    private CourseDAO courseDAO;
    private EnrollmentDAO enrollmentDAO;
    private AcademicRecordService academicRecordService;
    private AuthenticationService authenticationService;
    private User currentUser;

    public FacultyDashboardController() {
        courseDAO = new CourseDAO();
        enrollmentDAO = new EnrollmentDAO();
        academicRecordService = new AcademicRecordService();
        authenticationService = new AuthenticationService();
    }

    public void initData(User user) {
        this.currentUser = user;
        facultyNameLabel.setText("Faculty Dashboard");
        facultyNameLabel.setTextFill(Color.web("#a4bcd5"));
        welcomeUsernameText.setText(user.getUsername());
        showMyCourses();
    }

    @FXML
    private void showMyCourses() {
        VBox coursesView = new VBox(10);

        // Department filter
        ComboBox<String> departmentFilter = new ComboBox<>();
        departmentFilter.setPromptText("Select Department");
        List<Course> allCourses = courseDAO.getAllCourses();
        departmentFilter.getItems().addAll(
                allCourses.stream()
                        .map(Course::getDepartment)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList())
        );

        // Course table
        TableView<Course> courseTable = new TableView<>();

        TableColumn<Course, Integer> idCol = new TableColumn<>("Course ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));

        TableColumn<Course, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);

        TableColumn<Course, String> deptCol = new TableColumn<>("Department");
        deptCol.setCellValueFactory(new PropertyValueFactory<>("department"));

        TableColumn<Course, Integer> creditsCol = new TableColumn<>("Credits");
        creditsCol.setCellValueFactory(new PropertyValueFactory<>("creditHours"));

        TableColumn<Course, Integer> enrolledCol = new TableColumn<>("Enrolled");
        enrolledCol.setCellValueFactory(data ->
                new SimpleIntegerProperty(enrollmentDAO.getEnrollmentCount(data.getValue())).asObject());

        TableColumn<Course, Integer> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));

        courseTable.getColumns().addAll(idCol, titleCol, deptCol, creditsCol, enrolledCol, capacityCol);
        courseTable.setItems(FXCollections.observableArrayList(allCourses));

        departmentFilter.setOnAction(e -> {
            String selectedDept = departmentFilter.getValue();
            if (selectedDept != null) {
                List<Course> filteredCourses = courseDAO.findCoursesByDepartment(selectedDept);
                courseTable.setItems(FXCollections.observableArrayList(filteredCourses));
            } else {
                courseTable.setItems(FXCollections.observableArrayList(allCourses));
            }
        });

        coursesView.getChildren().addAll(
                new Label("Filter by Department:"),
                departmentFilter,
                courseTable
        );
        contentArea.getChildren().setAll(coursesView);
    }

    @FXML
    private void showCourseEnrollments() {
        VBox enrollmentsView = new VBox(10);

        // Department filter
        ComboBox<String> departmentFilter = new ComboBox<>();
        departmentFilter.setPromptText("Select Department");
        departmentFilter.getItems().addAll(
                courseDAO.getAllCourses().stream()
                        .map(Course::getDepartment)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList())
        );

        // Course selection
        ComboBox<Course> courseComboBox = new ComboBox<>();
        courseComboBox.setPromptText("Select Course");
        courseComboBox.setDisable(true);



        // Enrollment table
        TableView<Enrollment> enrollmentTable = new TableView<>();

        TableColumn<Enrollment, String> studentCol = new TableColumn<>("Student");
        studentCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStudent().getName()));

        TableColumn<Enrollment, String> semesterCol = new TableColumn<>("Semester");
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));

        TableColumn<Enrollment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        enrollmentTable.getColumns().addAll(studentCol, semesterCol, statusCol);

        departmentFilter.setOnAction(e -> {
            String selectedDept = departmentFilter.getValue();
            if (selectedDept != null) {
                List<Course> courses = courseDAO.findCoursesByDepartment(selectedDept);
                courseComboBox.setItems(FXCollections.observableArrayList(courses));
                courseComboBox.setDisable(false);
            } else {
                courseComboBox.setDisable(true);
            }
        });

        courseComboBox.setOnAction(e -> {
            Course selectedCourse = courseComboBox.getValue();
            if (selectedCourse != null) {
                List<Enrollment> enrollments = enrollmentDAO.findEnrollmentsByCourse(selectedCourse);
                enrollmentTable.setItems(FXCollections.observableArrayList(enrollments));
            }
        });

        enrollmentsView.getChildren().addAll(
                new Label("Department:"),
                departmentFilter,
                new Label("Course:"),
                courseComboBox,
                enrollmentTable
        );
        contentArea.getChildren().setAll(enrollmentsView);
    }

    @FXML
    private void showGradeManagement() {
        VBox gradeView = new VBox(10);

        // Department and course selection
        ComboBox<String> departmentFilter = new ComboBox<>();
        departmentFilter.setPromptText("Select Department");
        departmentFilter.getItems().addAll(
                courseDAO.getAllCourses().stream()
                        .map(Course::getDepartment)
                        .distinct()
                        .sorted()
                        .collect(Collectors.toList())
        );

        ComboBox<Course> courseComboBox = new ComboBox<>();
        courseComboBox.setPromptText("Select Course");
        courseComboBox.setDisable(true);

        // Enrollment table
        TableView<Enrollment> enrollmentTable = new TableView<>();

        TableColumn<Enrollment, String> studentCol = new TableColumn<>("Student");
        studentCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStudent().getName()));

        TableColumn<Enrollment, String> semesterCol = new TableColumn<>("Semester");
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));

        enrollmentTable.getColumns().addAll(studentCol, semesterCol);

        // Grade input controls
        TextField gradeField = new TextField();
        gradeField.setPromptText("Enter grade (A, B, C, D, F)");
        Button submitGradeBtn = new Button("Submit Grade");
        submitGradeBtn.setDisable(true);

        departmentFilter.setOnAction(e -> {
            String selectedDept = departmentFilter.getValue();
            if (selectedDept != null) {
                List<Course> courses = courseDAO.findCoursesByDepartment(selectedDept);
                courseComboBox.setItems(FXCollections.observableArrayList(courses));
                courseComboBox.setDisable(false);
            } else {
                courseComboBox.setDisable(true);
            }
        });

        courseComboBox.setOnAction(e -> {
            Course selectedCourse = courseComboBox.getValue();
            if (selectedCourse != null) {
                List<Enrollment> enrollments = enrollmentDAO.findEnrollmentsByCourse(selectedCourse);
                enrollmentTable.setItems(FXCollections.observableArrayList(enrollments));
            }
        });

        enrollmentTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    submitGradeBtn.setDisable(newSelection == null || gradeField.getText().isEmpty());
                });

        gradeField.textProperty().addListener((obs, oldText, newText) -> {
            submitGradeBtn.setDisable(
                    enrollmentTable.getSelectionModel().getSelectedItem() == null || newText.isEmpty()
            );
        });

        submitGradeBtn.setOnAction(e -> {
            Enrollment selected = enrollmentTable.getSelectionModel().getSelectedItem();
            String grade = gradeField.getText().toUpperCase();

            if (!grade.matches("[A-F]")) {
                showAlert("Error", "Invalid grade. Please enter A, B, C, D, or F");
                return;
            }

            try {
                academicRecordService.recordGrade(selected, grade);
                showAlert("Success", "Grade recorded successfully");
                gradeField.clear();
                enrollmentTable.refresh();
            } catch (Exception ex) {
                showAlert("Error", "Failed to record grade: " + ex.getMessage());
            }
        });

        gradeView.getChildren().addAll(
                new Label("Department:"),
                departmentFilter,
                new Label("Course:"),
                courseComboBox,
                enrollmentTable,
                new Label("Grade:"),
                gradeField,
                submitGradeBtn
        );

        contentArea.getChildren().setAll(gradeView);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/crs/login-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) facultyNameLabel.getScene().getWindow();
        stage.setScene(scene);
    }
}