package com.crs.controller;

import com.crs.dao.CourseDAO;
import com.crs.dao.StudentDAO;
import com.crs.model.*;
import com.crs.service.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class StudentDashboardController {
    @FXML
    private Label studentNameLabel;
    @FXML
    private Text welcomeUsernameText;
    @FXML
    private StackPane contentArea;

    private CourseDAO courseDAO;
    private StudentDAO studentDAO;
    private EnrollmentService enrollmentService;
    private AcademicRecordService academicRecordService;
    private AuthenticationService authenticationService;

    private User currentUser;
    private Student currentStudent;

    public StudentDashboardController() {
        courseDAO = new CourseDAO();
        studentDAO = new StudentDAO();
        enrollmentService = new EnrollmentService();
        academicRecordService = new AcademicRecordService();
        authenticationService = new AuthenticationService();
    }


    public void initData(User user) {
        this.currentUser = user;
        this.currentStudent = studentDAO.getStudentById(user.getStudentId());

        studentNameLabel.setText("Welcome, " + currentStudent.getName());
        welcomeUsernameText.setText(currentUser.getUsername());

        showAvailableCourses(); // Load initial view
    }


    @FXML
    private void showAvailableCourses() {
        VBox courseView = new VBox(10);

        // Search field with department filter
        ComboBox<String> departmentFilter = new ComboBox<>();
        departmentFilter.setPromptText("Filter by Department");
        departmentFilter.getItems().addAll(courseDAO.getAllCourses().stream()
                .map(Course::getDepartment)
                .distinct()
                .collect(Collectors.toList()));

        TableView<Course> courseTable = new TableView<>();

        // Course ID column
        TableColumn<Course, Integer> idCol = new TableColumn<>("Course ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));

        // Title column
        TableColumn<Course, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);

        // Department column
        TableColumn<Course, String> deptCol = new TableColumn<>("Department");
        deptCol.setCellValueFactory(new PropertyValueFactory<>("department"));

        // Credit Hours column
        TableColumn<Course, Integer> creditCol = new TableColumn<>("Credits");
        creditCol.setCellValueFactory(new PropertyValueFactory<>("creditHours"));

        // Available Seats column
        TableColumn<Course, Integer> availableSeatsCol = new TableColumn<>("Available Seats");
        availableSeatsCol.setCellValueFactory(data -> {
            int enrolled = enrollmentService.getEnrollmentCount(data.getValue());
            int available = data.getValue().getMaxCapacity() - enrolled;
            return new SimpleIntegerProperty(available).asObject();
        });

        courseTable.getColumns().addAll(idCol, titleCol, deptCol, creditCol, availableSeatsCol);
        courseTable.setItems(FXCollections.observableArrayList(courseDAO.getAllCourses()));

        // Add filter functionality
        departmentFilter.setOnAction(e -> {
            String selectedDept = departmentFilter.getValue();
            if (selectedDept != null) {
                List<Course> filteredCourses = courseDAO.findCoursesByDepartment(selectedDept);
                courseTable.setItems(FXCollections.observableArrayList(filteredCourses));
            } else {
                courseTable.setItems(FXCollections.observableArrayList(courseDAO.getAllCourses()));
            }
        });

        Button enrollBtn = new Button("Enroll");
        enrollBtn.setDisable(true);

        // Enable enroll button only when a course is selected
        courseTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> enrollBtn.setDisable(newSelection == null));

        enrollBtn.setOnAction(e -> {
            Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
            if (selectedCourse != null) {
                showEnrollmentDialog(selectedCourse);
            }
        });

        courseView.getChildren().addAll(departmentFilter, courseTable, enrollBtn);
        contentArea.getChildren().setAll(courseView);
    }

    private void showEnrollmentDialog(Course course) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Course Enrollment");

        ComboBox<String> semesterBox = new ComboBox<>();
        semesterBox.getItems().addAll("Fall 2025", "Spring 2026", "Summer 2026");
        semesterBox.setPromptText("Select Semester");

        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Course: " + course.getTitle()),
                new Label("Credits: " + course.getCreditHours()),
                new Label("Semester:"),
                semesterBox
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    enrollmentService.enrollStudent(currentStudent, course, semesterBox.getValue());
                    showAlert("Success", "Successfully enrolled in " + course.getTitle());
                    showAvailableCourses(); // Refresh the course list
                } catch (Exception e) {
                    showAlert("Error", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void showMyEnrollments() {
        VBox enrollmentsView = new VBox(10);
        TableView<Enrollment> enrollmentTable = new TableView<>();

        // Course Title
        TableColumn<Enrollment, String> courseCol = new TableColumn<>("Course");
        courseCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCourse().getTitle()));
        courseCol.setPrefWidth(200);

        // Department
        TableColumn<Enrollment, String> deptCol = new TableColumn<>("Department");
        deptCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCourse().getDepartment()));

        // Credits
        TableColumn<Enrollment, Integer> creditsCol = new TableColumn<>("Credits");
        creditsCol.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getCourse().getCreditHours()).asObject());

        // Semester
        TableColumn<Enrollment, String> semesterCol = new TableColumn<>("Semester");
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));

        // Enrollment Status
        TableColumn<Enrollment, Enrollment.EnrollmentStatus> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Enrollment Date
        TableColumn<Enrollment, String> dateCol = new TableColumn<>("Enrollment Date");
        dateCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEnrollmentDate().toString()));

        enrollmentTable.getColumns().addAll(courseCol, deptCol, creditsCol,
                semesterCol, statusCol, dateCol);

        // Get enrollments for current student
        List<Enrollment> enrollments = enrollmentService.findEnrollmentsByStudent(currentStudent);
        enrollmentTable.setItems(FXCollections.observableArrayList(enrollments));

        Button dropBtn = new Button("Drop Course");
        dropBtn.setDisable(true);

        enrollmentTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    dropBtn.setDisable(newSelection == null ||
                            newSelection.getStatus() != Enrollment.EnrollmentStatus.ENROLLED);
                });

        dropBtn.setOnAction(e -> {
            Enrollment selected = enrollmentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    enrollmentService.dropCourse(currentStudent, selected.getCourse(),
                            selected.getSemester());
                    showAlert("Success", "Course dropped successfully");
                    showMyEnrollments();
                } catch (Exception ex) {
                    showAlert("Error", ex.getMessage());
                }
            }
        });

        enrollmentsView.getChildren().addAll(enrollmentTable, dropBtn);
        contentArea.getChildren().setAll(enrollmentsView);
    }

    @FXML
    private void showTranscript() {
        VBox transcriptView = new VBox(10);

        TableView<AcademicRecord> transcriptTable = new TableView<>();

        TableColumn<AcademicRecord, String> courseCol = new TableColumn<>("Course");
        courseCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEnrollment().getCourse().getTitle()));

        TableColumn<AcademicRecord, String> semesterCol = new TableColumn<>("Semester");
        semesterCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEnrollment().getSemester()));

        TableColumn<AcademicRecord, String> gradeCol = new TableColumn<>("Grade");
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));

        transcriptTable.getColumns().addAll(courseCol, semesterCol, gradeCol);
        transcriptTable.setItems(FXCollections.observableArrayList(
                academicRecordService.getStudentTranscript(currentStudent)));

        transcriptView.getChildren().add(transcriptTable);
        contentArea.getChildren().setAll(transcriptView);
    }

    @FXML
    private void calculateGPA() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Calculate GPA");

        // Get distinct semesters from student's enrollments
        List<String> availableSemesters = enrollmentService.findEnrollmentsByStudent(currentStudent)
                .stream()
                .map(Enrollment::getSemester)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        ComboBox<String> semesterBox = new ComboBox<>();
        semesterBox.setItems(FXCollections.observableArrayList(availableSemesters));
        semesterBox.setPromptText("Select Semester");

        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Select Semester:"),
                semesterBox
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Disable OK button if no semester is selected
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        semesterBox.setOnAction(e -> okButton.setDisable(semesterBox.getValue() == null));

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK && semesterBox.getValue() != null) {
                try {
                    double gpa = academicRecordService.calculateGPA(currentStudent,
                            semesterBox.getValue());
                    showAlert("GPA Calculation",
                            String.format("Your GPA for %s is: %.2f",
                                    semesterBox.getValue(), gpa));
                } catch (Exception e) {
                    showAlert("Error", e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void registerForCourse(Course course) {
        if (authenticationService.hasPermission("ENROLL_COURSE")) {
            showAlert("Error", "You don't have permission to enroll in courses");
            return;
        }

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Course Registration");

        TextField studentNameField = new TextField();
        studentNameField.setPromptText("Enter student name");
        TextField semesterField = new TextField();
        semesterField.setPromptText("Enter semester");

        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Student Name:"), studentNameField,
                new Label("Semester:"), semesterField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    Student student = new Student();
                    student.setName(studentNameField.getText());
                    studentDAO.saveStudent(student);
                    enrollmentService.enrollStudent(student, course, semesterField.getText());
                    showAlert("Success", "Successfully registered for the course!");
                } catch (Exception e) {
                    showAlert("Error", "Failed to register: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() throws IOException {
        authenticationService.logout();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/crs/login-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) studentNameLabel.getScene().getWindow();
        stage.setScene(scene);
    }
}