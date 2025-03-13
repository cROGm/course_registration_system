package com.crs.service;

import com.crs.dao.CourseDAO;
import com.crs.dao.EnrollmentDAO;
import com.crs.model.Course;
import com.crs.model.Enrollment;
import com.crs.model.Student;

import java.time.LocalDateTime;
import java.util.List;

public class EnrollmentService {
    private EnrollmentDAO enrollmentDAO;
    private CourseDAO courseDAO;

    public EnrollmentService() {
        this.enrollmentDAO = new EnrollmentDAO();
        this.courseDAO = new CourseDAO();
    }

    public boolean enrollStudent(Student student, Course course, String semester) {
        // Check if course has available capacity
        int currentEnrollment = enrollmentDAO.getEnrollmentCount(course);
        if (currentEnrollment >= course.getMaxCapacity()) {
            throw new RuntimeException("Course is full");
        }

        // Check if student is already enrolled
        boolean alreadyEnrolled = enrollmentDAO.findEnrollmentsByStudent(student)
                .stream()
                .anyMatch(e -> e.getCourse().getCourseId() == course.getCourseId()
                        && e.getSemester().equals(semester));

        if (alreadyEnrolled) {
            throw new RuntimeException("Student already enrolled in this course");
        }

        // Create new enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setStatus(Enrollment.EnrollmentStatus.ENROLLED);
        enrollment.setSemester(semester);

        enrollmentDAO.saveEnrollment(enrollment);
        return true;
    }

    public boolean dropCourse(Student student, Course course, String semester) {
        List<Enrollment> enrollments = enrollmentDAO.findEnrollmentsByStudent(student);

        Enrollment enrollment = enrollments.stream()
                .filter(e -> e.getCourse().getCourseId() == course.getCourseId()
                        && e.getSemester().equals(semester))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setStatus(Enrollment.EnrollmentStatus.DROPPED);
        enrollmentDAO.saveEnrollment(enrollment);
        return true;
    }

    public int getEnrollmentCount(Course course) {
        return enrollmentDAO.getEnrollmentCount(course);
    }

    public List<Enrollment> findEnrollmentsByStudent(Student student) {
        return enrollmentDAO.findEnrollmentsByStudent(student);
    }
}