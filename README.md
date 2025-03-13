# Academic Course Management Platform

## Prerequisites

- Oracle JDK/OpenJDK (Version 17+)
- Apache Maven (3.8 or newer)
- MySQL Database Server (8.0+)
- JetBrains IntelliJ IDEA Ultimate Edition

## Getting Started

### 1. Initialize Database

```sql
CREATE DATABASE crs_db;
USE crs_db;
```

### 2. Application Configuration

Edit the database connection settings in `src/main/resources/hibernate.cfg.xml`:
```properties
db.url=jdbc:mysql://localhost:3306/crs_db
db.username=your_username
db.password=your_password
```

### 3. Installation Steps

1. Launch IntelliJ IDEA
2. Navigate to `File` > `Open` and locate the project's root folder
3. Allow dependency resolution and project indexing to complete
4. Locate `src/main/java/com/crs/Main.java`
5. Execute the application by right-clicking and selecting `Run 'Main.main()'`
6. Import initial data by executing `db_dump.sql` in your MySQL instance

Upon completion, the platform will be operational.

## Available Functions

### Student Portal
- Browse and search course catalog
- Access personal enrollment list
- View academic performance metrics
- Access academic records

### Instructor Dashboard
- Browse departmental course offerings
- Monitor class enrollment statistics
- Input and modify student grades

### Administrative Console
- **Course Management**: Add, modify, remove, and list academic offerings
- **Student Administration**: Register, update, remove, and query student profiles
- **Academic Record Management**
- **Enrollment Administration**