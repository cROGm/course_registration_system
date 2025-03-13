package com.crs.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "academic_records")
public class AcademicRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "enrollmentId")
    private Enrollment enrollment;

    private String grade;

    @Column(name = "completionDate")
    private LocalDateTime completionDate;

    private String remarks;

    @Enumerated(EnumType.STRING)
    private CourseStatus status;

    public enum CourseStatus {
        IN_PROGRESS, COMPLETED, WITHDRAWN, FAILED
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Enrollment getEnrollment() { return enrollment; }
    public void setEnrollment(Enrollment enrollment) { this.enrollment = enrollment; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public LocalDateTime getCompletionDate() { return completionDate; }
    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public CourseStatus getStatus() { return status; }
    public void setStatus(CourseStatus status) { this.status = status; }
}