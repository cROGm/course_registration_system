package com.crs.controller;

import com.crs.dao.*;
import com.crs.model.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class AdminDashboardController {
    @FXML private Label adminNameLabel;
    @FXML private Text welcomeUsernameText;
    @FXML private StackPane contentArea;

    private CourseDAO courseDAO;
    private StudentDAO studentDAO;
    private EnrollmentDAO enrollmentDAO;
    private AcademicRecordDAO academicRecordDAO;
    private User currentUser;

    public AdminDashboardController() {
        courseDAO = new CourseDAO();
        studentDAO = new StudentDAO();
        enrollmentDAO = new EnrollmentDAO();
        academicRecordDAO = new AcademicRecordDAO();
    }

    public void initData(User user) {
        this.currentUser = user;
        adminNameLabel.setText("Admin Dashboard");
        adminNameLabel.setTextFill(Color.web("#a4bcd5"));
        welcomeUsernameText.setText(user.getUsername());
        showCourseManagement(); // Show courses by default
    }

    @FXML
    private void showCourseManagement() {
        VBox courseView = new VBox(10);

        // Course Table
        TableView<Course> courseTable = new TableView<>();

        TableColumn<Course, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("courseId"));

        TableColumn<Course, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Course, String> deptCol = new TableColumn<>("Department");
        deptCol.setCellValueFactory(new PropertyValueFactory<>("department"));

        TableColumn<Course, Integer> creditsCol = new TableColumn<>("Credits");
        creditsCol.setCellValueFactory(new PropertyValueFactory<>("creditHours"));

        TableColumn<Course, Integer> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));

        courseTable.getColumns().addAll(idCol, titleCol, deptCol, creditsCol, capacityCol);
        courseTable.setItems(FXCollections.observableArrayList(courseDAO.getAllCourses()));

        // Course Form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(5);

        TextField titleField = new TextField();
        TextField deptField = new TextField();
        TextField creditsField = new TextField();
        TextField capacityField = new TextField();

        form.addRow(0, new Label("Title:"), titleField);
        form.addRow(1, new Label("Department:"), deptField);
        form.addRow(2, new Label("Credits:"), creditsField);
        form.addRow(3, new Label("Capacity:"), capacityField);

        HBox buttons = new HBox(10);
        Button addBtn = new Button("Add Course");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        buttons.getChildren().addAll(addBtn, updateBtn, deleteBtn);

        addBtn.setOnAction(e -> {
            try {
                Course course = new Course();
                course.setTitle(titleField.getText());
                course.setDepartment(deptField.getText());
                course.setCreditHours(Integer.parseInt(creditsField.getText()));
                course.setMaxCapacity(Integer.parseInt(capacityField.getText()));

                courseDAO.saveCourse(course);
                courseTable.getItems().add(course);
                clearFields(titleField, deptField, creditsField, capacityField);
            } catch (Exception ex) {
                showAlert("Error", "Failed to add course: " + ex.getMessage());
            }
        });

        updateBtn.setOnAction(e -> {
            Course selected = courseTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    selected.setTitle(titleField.getText());
                    selected.setDepartment(deptField.getText());
                    selected.setCreditHours(Integer.parseInt(creditsField.getText()));
                    selected.setMaxCapacity(Integer.parseInt(capacityField.getText()));

                    courseDAO.updateCourse(selected);
                    courseTable.refresh();
                    clearFields(titleField, deptField, creditsField, capacityField);
                } catch (Exception ex) {
                    showAlert("Error", "Failed to update course: " + ex.getMessage());
                }
            }
        });

        deleteBtn.setOnAction(e -> {
            Course selected = courseTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    courseDAO.deleteCourse(selected);
                    courseTable.getItems().remove(selected);
                } catch (Exception ex) {
                    showAlert("Error", "Failed to delete course: " + ex.getMessage());
                }
            }
        });

        courseView.getChildren().addAll(courseTable, form, buttons);
        contentArea.getChildren().setAll(courseView);
    }

    @FXML
    private void showStudentManagement() {
        VBox studentView = new VBox(10);

        TableView<Student> studentTable = new TableView<>();

        TableColumn<Student, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Student, String> programCol = new TableColumn<>("Program");
        programCol.setCellValueFactory(new PropertyValueFactory<>("program"));

        TableColumn<Student, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        studentTable.getColumns().addAll(idCol, nameCol, programCol, yearCol);
        studentTable.setItems(FXCollections.observableArrayList(studentDAO.getAllStudents()));

        // Student Form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(5);

        TextField nameField = new TextField();
        TextField programField = new TextField();
        TextField yearField = new TextField();
        TextField contactField = new TextField();

        form.addRow(0, new Label("Name:"), nameField);
        form.addRow(1, new Label("Program:"), programField);
        form.addRow(2, new Label("Year:"), yearField);
        form.addRow(3, new Label("Contact:"), contactField);

        HBox buttons = new HBox(10);
        Button addBtn = new Button("Add Student");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        buttons.getChildren().addAll(addBtn, updateBtn, deleteBtn);

        addBtn.setOnAction(e -> {
            try {
                Student student = new Student();
                student.setName(nameField.getText());
                student.setProgram(programField.getText());
                student.setYear(Integer.parseInt(yearField.getText()));
                student.setContactInfo(contactField.getText());

                studentDAO.saveStudent(student);
                studentTable.getItems().add(student);
                clearFields(nameField, programField, yearField, contactField);
            } catch (Exception ex) {
                showAlert("Error", "Failed to add student: " + ex.getMessage());
            }
        });

        updateBtn.setOnAction(e -> {
            Student selected = studentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    selected.setName(nameField.getText());
                    selected.setProgram(programField.getText());
                    selected.setYear(Integer.parseInt(yearField.getText()));
                    selected.setContactInfo(contactField.getText());

                    studentDAO.updateStudent(selected);
                    studentTable.refresh();
                    clearFields(nameField, programField, yearField, contactField);
                } catch (Exception ex) {
                    showAlert("Error", "Failed to update student: " + ex.getMessage());
                }
            }
        });

        deleteBtn.setOnAction(e -> {
            Student selected = studentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    studentDAO.deleteStudent(selected);
                    studentTable.getItems().remove(selected);
                } catch (Exception ex) {
                    showAlert("Error", "Failed to delete student: " + ex.getMessage());
                }
            }
        });

        studentView.getChildren().addAll(studentTable, form, buttons);
        contentArea.getChildren().setAll(studentView);
    }

    @FXML
    private void showAcademicRecords() {
        VBox recordsView = new VBox(10);

        ComboBox<Student> studentComboBox = new ComboBox<>();
        studentComboBox.setPromptText("Select Student");
        studentComboBox.setItems(FXCollections.observableArrayList(studentDAO.getAllStudents()));

        // Configure student display in combo box
        studentComboBox.setCellFactory(lv -> new ListCell<Student>() {
            @Override
            protected void updateItem(Student student, boolean empty) {
                super.updateItem(student, empty);
                if (empty || student == null) {
                    setText(null);
                } else {
                    setText(student.getName() + " (ID: " + student.getStudentId() + ")");
                }
            }
        });

        TableView<AcademicRecord> recordsTable = new TableView<>();

        TableColumn<AcademicRecord, String> courseCol = new TableColumn<>("Course");
        courseCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEnrollment().getCourse().getTitle()));

        TableColumn<AcademicRecord, String> semesterCol = new TableColumn<>("Semester");
        semesterCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEnrollment().getSemester()));

        TableColumn<AcademicRecord, String> gradeCol = new TableColumn<>("Grade");
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));

        recordsTable.getColumns().addAll(courseCol, semesterCol, gradeCol);

        studentComboBox.setOnAction(e -> {
            Student selected = studentComboBox.getValue();
            if (selected != null) {
                List<AcademicRecord> records = academicRecordDAO.getStudentAcademicHistory(selected);
                recordsTable.setItems(FXCollections.observableArrayList(records));
            }
        });

        recordsView.getChildren().addAll(studentComboBox, recordsTable);
        contentArea.getChildren().setAll(recordsView);
    }

    @FXML
    private void showEnrollmentManagement() {
        VBox enrollmentView = new VBox(10);

        ComboBox<Course> courseComboBox = new ComboBox<>();
        courseComboBox.setPromptText("Select Course");
        courseComboBox.setItems(FXCollections.observableArrayList(courseDAO.getAllCourses()));

        // Configure course display in combo box
        courseComboBox.setCellFactory(lv -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);
                if (empty || course == null) {
                    setText(null);
                } else {
                    setText(course.getTitle() + " (" + course.getDepartment() + ")");
                }
            }
        });

        TableView<Enrollment> enrollmentTable = new TableView<>();

        TableColumn<Enrollment, String> studentCol = new TableColumn<>("Student");
        studentCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStudent().getName()));

        TableColumn<Enrollment, String> semesterCol = new TableColumn<>("Semester");
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));

        TableColumn<Enrollment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        enrollmentTable.getColumns().addAll(studentCol, semesterCol, statusCol);

        courseComboBox.setOnAction(e -> {
            Course selected = courseComboBox.getValue();
            if (selected != null) {
                List<Enrollment> enrollments = enrollmentDAO.findEnrollmentsByCourse(selected);
                enrollmentTable.setItems(FXCollections.observableArrayList(enrollments));
            }
        });

        enrollmentView.getChildren().addAll(courseComboBox, enrollmentTable);
        contentArea.getChildren().setAll(enrollmentView);
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
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
        Stage stage = (Stage) adminNameLabel.getScene().getWindow();
        stage.setScene(scene);
    }
}