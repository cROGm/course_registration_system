USE crs_db;

-- Insert updated sample data into Student Table
INSERT INTO students (studentId, contactInfo, dob, name, program, year) VALUES
(1, 'john.new@sample.com', '2000-06-15', 'John Newman', 'Electrical Engineering', 4),
(2, 'jane.smith@sample.com', '2001-08-20', 'Jane Smith', 'Civil Engineering', 3),
(3, 'alice.brown@sample.com', '1999-11-05', 'Alice Brown', 'Chemical Engineering', 5),
(4, 'bob.jones@sample.com', '2002-04-20', 'Bob Jones', 'Environmental Studies', 2),
(5, 'charlie.white@sample.com', '2000-10-11', 'Charlie White', 'Philosophy', 4),
(6, 'eva.green@sample.com', '2001-05-22', 'Eva Green', 'Business Administration', 3),
(7, 'daniel.miller@sample.com', '2000-07-31', 'Daniel Miller', 'Economics', 4),
(8, 'olivia.davis@sample.com', '1999-09-27', 'Olivia Davis', 'Psychology', 5),
(9, 'michael.wilson@sample.com', '2002-01-12', 'Michael Wilson', 'Architecture', 2),
(10, 'sophia.moore@sample.com', '2001-03-16', 'Sophia Moore', 'Nursing', 3),
(11, 'lucas.taylor@sample.com', '2000-12-18', 'Lucas Taylor', 'Mathematics', 4),
(12, 'emma.anderson@sample.com', '2002-06-11', 'Emma Anderson', 'Statistics', 2),
(13, 'benjamin.thomas@sample.com', '2001-10-07', 'Benjamin Thomas', 'Physics', 3),
(14, 'lily.jackson@sample.com', '1999-03-24', 'Lily Jackson', 'History', 5),
(15, 'samuel.martin@sample.com', '2000-09-30', 'Samuel Martin', 'Political Science', 4),
(16, 'harper.lee@sample.com', '2002-02-09', 'Harper Lee', 'Sociology', 2),
(17, 'jackson.walker@sample.com', '2001-11-03', 'Jackson Walker', 'Anthropology', 3),
(18, 'amelia.hall@sample.com', '1999-07-19', 'Amelia Hall', 'Literature', 5),
(19, 'henry.allen@sample.com', '2000-03-15', 'Henry Allen', 'Accounting', 4),
(20, 'grace.young@sample.com', '2002-08-26', 'Grace Young', 'Marketing', 2);

-- Insert updated sample data into Course Table
INSERT INTO courses (courseId, creditHours, department, maxCapacity, title) VALUES
(1, 3, 'Electrical Engineering', 40, 'Circuit Analysis'),
(2, 4, 'Civil Engineering', 35, 'Structural Mechanics'),
(3, 3, 'Chemical Engineering', 45, 'Process Design'),
(4, 3, 'Environmental Studies', 30, 'Sustainable Development'),
(5, 4, 'Philosophy', 25, 'Ethics and Society'),
(6, 3, 'Business Administration', 50, 'Organizational Behavior'),
(7, 4, 'Economics', 40, 'Microeconomic Theory'),
(8, 3, 'Psychology', 45, 'Cognitive Psychology');

-- Insert updated sample data into Enrollment Table
INSERT INTO enrollments (id, enrollmentDate, semester, status, courseId, studentId) VALUES
(1, '2025-04-15', 'Spring2024', 'ENROLLED', 1, 1),
(2, '2025-04-15', 'Spring2024', 'COMPLETED', 2, 2),
(3, '2025-04-15', 'Spring2024', 'DROPPED', 3, 3),
(4, '2025-04-15', 'Spring2024', 'ENROLLED', 4, 4),
(5, '2025-04-15', 'Spring2024', 'ENROLLED', 5, 5),
(6, '2025-04-15', 'Spring2024', 'COMPLETED', 6, 6),
(7, '2025-04-15', 'Spring2024', 'DROPPED', 7, 7),
(8, '2025-04-15', 'Spring2024', 'ENROLLED', 8, 8),
(9, '2025-04-15', 'Spring2024', 'ENROLLED', 1, 9),
(10, '2025-04-15', 'Spring2024', 'DROPPED', 2, 10),
(11, '2025-04-15', 'Spring2024', 'COMPLETED', 3, 11),
(12, '2025-04-15', 'Spring2024', 'ENROLLED', 4, 12),
(13, '2025-04-15', 'Spring2024', 'ENROLLED', 5, 13),
(14, '2025-04-15', 'Spring2024', 'COMPLETED', 6, 14),
(15, '2025-04-15', 'Spring2024', 'DROPPED', 7, 15),
(16, '2025-04-15', 'Spring2024', 'ENROLLED', 8, 16),
(17, '2025-04-15', 'Spring2024', 'ENROLLED', 1, 17),
(18, '2025-04-15', 'Spring2024', 'COMPLETED', 2, 18),
(19, '2025-04-15', 'Spring2024', 'DROPPED', 3, 19),
(20, '2025-04-15', 'Spring2024', 'ENROLLED', 4, 20);

-- Insert updated sample data into AcademicRecord Table
INSERT INTO academic_records (id, completionDate, grade, remarks, status, enrollmentId) VALUES
(1, '2025-07-20', 'A-', 'Very Good performance', 'COMPLETED', 1),
(2, '2025-07-20', 'B', 'Satisfactory progress', 'COMPLETED', 4),
(3, '2025-07-20', 'A', 'Outstanding performance', 'COMPLETED', 5),
(4, '2025-07-20', 'C', 'Needs Improvement', 'COMPLETED', 8),
(5, '2025-07-20', 'B+', 'Good work', 'COMPLETED', 9),
(6, '2025-07-20', 'A', 'Excellent', 'COMPLETED', 10),
(7, '2025-07-20', 'B-', 'Above Average', 'COMPLETED', 13),
(8, '2025-07-20', 'A-', 'Very Good', 'COMPLETED', 14),
(9, '2025-07-20', 'C+', 'Satisfactory', 'COMPLETED', 15),
(10, '2025-07-20', 'B', 'Good', 'COMPLETED', 17),
(11, '2025-07-20', 'B-', 'Above Average', 'COMPLETED', 19),
(12, '2025-07-20', 'A', 'Excellent', 'COMPLETED', 3),
(13, '2025-07-20', 'C-', 'Needs Improvement', 'COMPLETED', 6),
(14, '2025-07-20', 'A-', 'Very Good', 'COMPLETED', 7),
(15, '2025-07-20', 'B+', 'Good performance', 'COMPLETED', 11),
(16, '2025-07-20', 'A', 'Outstanding', 'COMPLETED', 12),
(17, '2025-07-20', 'B-', 'Above Average', 'COMPLETED', 16),
(18, '2025-07-20', 'C', 'Average', 'COMPLETED', 18),
(19, '2025-07-20', 'A-', 'Very Good', 'COMPLETED', 2),
(20, '2025-07-20', 'B+', 'Good', 'COMPLETED', 20);