package com.crs.dao;

import com.crs.model.Course;
import com.crs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CourseDAO {
    // Create
    public void saveCourse(Course course) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error saving course: " + e.getMessage());
        }
    }

    // Read
    public Course getCourseById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Course.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving course: " + e.getMessage());
        }
    }

    public List<Course> getAllCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Course", Course.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving courses: " + e.getMessage());
        }
    }

    // Update
    public void updateCourse(Course course) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error updating course: " + e.getMessage());
        }
    }

    // Delete
    public void deleteCourse(Course course) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error deleting course: " + e.getMessage());
        }
    }

    // Additional query methods
    public List<Course> findCoursesByDepartment(String department) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Course where department = :dept", Course.class)
                    .setParameter("dept", department)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding courses by department: " + e.getMessage());
        }
    }

    public List<Course> findCoursesByCredits(int credits) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Course where creditHours = :credits", Course.class)
                    .setParameter("credits", credits)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding courses by credits: " + e.getMessage());
        }
    }

    public List<Course> findAvailableCourses(int minCapacity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Course where maxCapacity >= :minCapacity", Course.class)
                    .setParameter("minCapacity", minCapacity)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Error finding available courses: " + e.getMessage());
        }
    }
}