package com.crs.service;

import com.crs.dao.AcademicRecordDAO;
import com.crs.model.AcademicRecord;
import com.crs.model.Enrollment;
import com.crs.model.Student;

import java.time.LocalDateTime;
import java.util.List;

public class AcademicRecordService {
    private AcademicRecordDAO academicRecordDAO;

    public AcademicRecordService() {
        this.academicRecordDAO = new AcademicRecordDAO();
    }

    public void recordGrade(Enrollment enrollment, String grade) {
        AcademicRecord record = new AcademicRecord();
        record.setEnrollment(enrollment);
        record.setGrade(grade);
        record.setCompletionDate(LocalDateTime.now());
        record.setStatus(determineStatus(grade));

        academicRecordDAO.saveAcademicRecord(record);
    }

    private AcademicRecord.CourseStatus determineStatus(String grade) {
        if (grade == null) return AcademicRecord.CourseStatus.IN_PROGRESS;
        switch (grade.toUpperCase()) {
            case "F": return AcademicRecord.CourseStatus.FAILED;
            case "W": return AcademicRecord.CourseStatus.WITHDRAWN;
            default: return AcademicRecord.CourseStatus.COMPLETED;
        }
    }

    public List<AcademicRecord> getStudentTranscript(Student student) {
        return academicRecordDAO.getStudentAcademicHistory(student);
    }

    public double calculateGPA(Student student, String semester) {
        List<AcademicRecord> records = academicRecordDAO
                .getStudentAcademicHistoryBySemester(student, semester);

        double totalPoints = 0;
        int totalCredits = 0;

        for (AcademicRecord record : records) {
            if (record.getStatus() == AcademicRecord.CourseStatus.COMPLETED) {
                int credits = record.getEnrollment().getCourse().getCreditHours();
                totalPoints += convertGradeToPoints(record.getGrade()) * credits;
                totalCredits += credits;
            }
        }

        return totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
    }

    private double convertGradeToPoints(String grade) {
        switch (grade.toUpperCase()) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            default: return 0.0;
        }
    }
}