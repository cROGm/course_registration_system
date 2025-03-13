package com.crs.dao;

import com.crs.model.Course;
import com.crs.model.Enrollment;
import com.crs.model.Student;
import com.crs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EnrollmentDAO {

    public void saveEnrollment(Enrollment enrollment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(enrollment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error saving enrollment: " + e.getMessage());
        }
    }

    public List<Enrollment> findEnrollmentsByStudent(Student student) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Enrollment e where e.student = :student",
                            Enrollment.class)
                    .setParameter("student", student)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding enrollments: " + e.getMessage());
        }
    }

    public List<Enrollment> findEnrollmentsByCourse(Course course) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Enrollment e where e.course = :course",
                            Enrollment.class)
                    .setParameter("course", course)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding enrollments: " + e.getMessage());
        }
    }

    public int getEnrollmentCount(Course course) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select count(e) from Enrollment e where e.course = :course and e.status = 'ENROLLED'",
                            Long.class)
                    .setParameter("course", course)
                    .uniqueResult()
                    .intValue();
        } catch (Exception e) {
            throw new RuntimeException("Error counting enrollments: " + e.getMessage());
        }
    }
}