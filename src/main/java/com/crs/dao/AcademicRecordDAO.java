package com.crs.dao;

import com.crs.model.AcademicRecord;
import com.crs.model.Student;
import com.crs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class AcademicRecordDAO {

    public void saveAcademicRecord(AcademicRecord record) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(record);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error saving academic record: " + e.getMessage());
        }
    }

    public List<AcademicRecord> getStudentAcademicHistory(Student student) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from AcademicRecord ar where ar.enrollment.student = :student " +
                                    "order by ar.enrollment.semester desc, ar.enrollment.course.courseId",
                            AcademicRecord.class)
                    .setParameter("student", student)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving academic history: " + e.getMessage());
        }
    }

    public List<AcademicRecord> getStudentAcademicHistoryBySemester(Student student, String semester) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from AcademicRecord ar where ar.enrollment.student = :student " +
                                    "and ar.enrollment.semester = :semester",
                            AcademicRecord.class)
                    .setParameter("student", student)
                    .setParameter("semester", semester)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving semester records: " + e.getMessage());
        }
    }
}