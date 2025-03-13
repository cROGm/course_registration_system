package com.crs.dao;

import com.crs.model.User;
import com.crs.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDAO {
    public User findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User where username = :username", User.class)
                    .setParameter("username", username)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error finding user: " + e.getMessage());
        }
    }

    public void saveUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Check if user already exists
            User existingUser = findByUsername(user.getUsername());
            if (existingUser != null) {
                throw new RuntimeException("User with username " + user.getUsername() + " already exists.");
            }

            // Validate student ID for student role
            if (user.getRole() == User.Role.STUDENT && user.getStudentId() == null) {
                throw new IllegalArgumentException("Student ID is required for student users");
            }

            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Error saving user: " + e.getMessage());
        }
    }

    public User findByStudentId(Integer studentId) {
        if (studentId == null) return null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User where studentId = :studentId", User.class)
                    .setParameter("studentId", studentId)
                    .uniqueResult();
        } catch (Exception e) {
            throw new RuntimeException("Error finding user by student ID: " + e.getMessage());
        }
    }


}