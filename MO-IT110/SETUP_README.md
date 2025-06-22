# MotorPH Payroll System - Complete Setup Guide

This comprehensive guide walks you through setting up the MotorPH Payroll System from scratch, including database creation, user setup, data migration, and system initialization.

## System Overview

The MotorPH Payroll System is a Java-based HR management application using PostgreSQL database with the following features:
- Employee management
- Attendance tracking
- Leave request processing
- Payroll calculations
- Multi-role user access (Admin, HR, Finance, Employee)

## Prerequisites

- **Java Development Kit (JDK) 11 or later**
- **PostgreSQL 11 or later** (with psql command-line tool)
- **PostgreSQL JDBC Driver** (included in `libs/postgresql-42.7.5.jar`)

## Step-by-Step Setup Instructions

### 1. Database Setup

#### 1.1 Create Database and User

First, create the PostgreSQL database and user account:

```bash
# Connect to PostgreSQL as superuser
psql -U postgres

# Run the following SQL commands:
```

```sql
-- Create the database user
CREATE ROLE camulite_admin WITH LOGIN PASSWORD '123';

-- Create the database
CREATE DATABASE motorph_payroll WITH OWNER camulite_admin;

-- Grant all privileges
GRANT ALL PRIVILEGES ON DATABASE motorph_payroll TO camulite_admin;

-- Exit PostgreSQL
\q
```

#### 1.2 Verify Database Connection

Test the database connection:

```bash
# Test connection with the new user
psql -U camulite_admin -d motorph_payroll -h localhost

# You should see the PostgreSQL prompt, then exit
\q
```

### 2. Database Schema Initialization

#### 2.1 Run Database Initializer

The system will automatically create all necessary tables when you first run the application. The database schema includes:

**Core Tables:**
- `users` - User authentication data
- `roles` - User role definitions (ADMIN, HR, FINANCE, EMPLOYEE)
- `user_roles` - User-role assignments
- `employees` - Employee records
- `personal_information` - Employee personal details
- `contact_information` - Employee contact details
- `government_ids` - Employee government IDs (SSS, PhilHealth, TIN, Pag-IBIG)

**Organizational Tables:**
- `departments` - Company departments
- `positions` - Job positions
- `employee_positions` - Employee position assignments
- `salary_grades` - Salary grade levels

**Operational Tables:**
- `attendance_records` - Daily attendance records
- `leave_requests` - Employee leave requests
- `employee_compensation` - Salary and compensation details
- `employee_allowances` - Employee allowances
- `employee_update_requests` - Employee information update requests

**Reference Tables:**
- `leave_types` - Types of leave (VL, SL, etc.)
- `allowance_types` - Types of allowances

### 3. Data Migration from JSON Files

#### 3.1 Migrate JSON Data to Database

The system includes JSON files with sample data that need to be migrated to the database:

**Option A: Run Migration During First Application Start**
- The application will automatically detect empty tables and migrate data on first run

**Option B: Run Migration Manually**
```bash
# Compile the project first
javac -cp "libs/*:src" src/Test/JsonMigrationRunner.java

# Run the migration
java -cp "libs/*:bin" Test.JsonMigrationRunner
```

#### 3.2 JSON Files Included

The following JSON files will be migrated:
- `resources/JSON_Files/LoginCredentials.json` - User login credentials
- `resources/JSON_Files/Employees.json` - Employee information
- `resources/JSON_Files/Attendance.json` - Attendance records
- `resources/JSON_Files/LeaveRequest.json` - Leave request records

### 4. User Account Setup

#### 4.1 Default Admin Account

The system automatically creates a default admin account:
- **Username:** `admin`
- **Password:** `123`
- **Role:** ADMIN (full system access)

#### 4.2 Create HR User Account

Run the HR user creation utility:

```bash
# Compile and run HR user creator
javac -cp "libs/*:src" src/Test/CreateHRUserTest.java
java -cp "libs/*:bin" Test.CreateHRUserTest
```

This creates:
- **Username:** `hr_user`
- **Password:** `hr123`
- **Role:** HR

#### 4.3 Create Finance User Account

Run the Finance user creation utility:

```bash
# Compile and run Finance user creator
javac -cp "libs/*:src" src/Test/CreateFinanceUserTest.java
java -cp "libs/*:bin" Test.CreateFinanceUserTest
```

This creates:
- **Username:** `finance_user`
- **Password:** `finance123`
- **Role:** FINANCE

### 5. Create Employee Update Requests Table

Ensure the employee update requests table is created:

```bash
# Connect to the database
psql -U camulite_admin -d motorph_payroll -h localhost

# Run the SQL script
\i database/create_update_requests_table.sql

# Exit
\q
```

Or run the SQL manually:

```sql
-- Create the table for employee update requests
CREATE TABLE IF NOT EXISTS employee_update_requests (
    request_id SERIAL PRIMARY KEY,
    employee_number VARCHAR(50) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    birthday VARCHAR(20),
    address TEXT,
    phone_number VARCHAR(20),
    sss_number VARCHAR(20),
    philhealth_number VARCHAR(20),
    tin_number VARCHAR(20),
    pagibig_number VARCHAR(20),
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDING',
    admin_notes TEXT,
    FOREIGN KEY (employee_number) REFERENCES employees(employee_number)
);

-- Create indexes for faster queries
CREATE INDEX IF NOT EXISTS idx_update_requests_employee_number ON employee_update_requests(employee_number);
CREATE INDEX IF NOT EXISTS idx_update_requests_status ON employee_update_requests(status);
```

### 6. Running the Application

#### 6.1 Compile the Project

```bash
# Navigate to the project directory
cd /path/to/mmdc-oop/MO-IT110

# Compile all source files
javac -cp "libs/*" -d bin src/**/*.java
```

#### 6.2 Run the Application

```bash
# Run the main application
java -cp "libs/*:bin" Main
```

#### 6.3 First Run Setup

On the first run, the application will:
1. Initialize the database schema
2. Create all necessary tables
3. Insert default reference data
4. Create the default admin user
5. Migrate JSON data to database tables
6. Launch the login GUI

### 7. User Access and Testing

#### 7.1 Login Credentials

After setup, you can login with these accounts:

**Admin Account:**
- Username: `admin`
- Password: `123`
- Access: Full system administration

**HR Account:**
- Username: `hr_user`
- Password: `hr123`
- Access: HR functions, employee management

**Finance Account:**
- Username: `finance_user`
- Password: `finance123`
- Access: Payroll and financial functions

**Employee Accounts:**
- Username: `josese.crisostomo` (and others from JSON)
- Password: `password10001` (varies per employee)
- Access: Personal information, leave requests

#### 7.2 Verify System Functions

Test the following features:

**Admin Functions:**
- Employee management (add, update, delete)
- User account management
- Leave request approvals
- System administration

**HR Functions:**
- Employee information management
- Leave request processing
- Payslip generation

**Finance Functions:**
- Payroll calculations
- Employee compensation management
- Financial reporting

**Employee Functions:**
- Personal information updates
- Leave request submissions
- Payslip viewing
- Attendance tracking

### 8. Database Connection Configuration

The database connection settings are configured in:
`src/Database/DatabaseConnection.java`

Current settings:
- **URL:** `jdbc:postgresql://localhost:5432/motorph_payroll`
- **Username:** `camulite_admin`
- **Password:** `123`

To modify these settings, edit the constants in `DatabaseConnection.java`:

```java
private static final String URL = "jdbc:postgresql://localhost:5432/motorph_payroll";
private static final String USERNAME = "camulite_admin";
private static final String PASSWORD = "123";
```

### 9. Troubleshooting

#### 9.1 Database Connection Issues

**Problem:** Application cannot connect to database
**Solution:**
1. Verify PostgreSQL is running: `sudo systemctl status postgresql`
2. Check database exists: `psql -U camulite_admin -d motorph_payroll -h localhost`
3. Verify user permissions: `GRANT ALL PRIVILEGES ON DATABASE motorph_payroll TO camulite_admin;`

#### 9.2 Compilation Issues

**Problem:** Java compilation errors
**Solution:**
1. Verify JDK version: `java -version`
2. Check classpath includes all JAR files in `libs/`
3. Ensure all dependencies are present in `libs/` folder

#### 9.3 Data Migration Issues

**Problem:** JSON data not migrating properly
**Solution:**
1. Run migration manually: `java -cp "libs/*:bin" Test.JsonMigrationRunner`
2. Check JSON file format and location in `resources/JSON_Files/`
3. Verify database tables are created before migration

#### 9.4 Login Issues

**Problem:** Cannot login with default credentials
**Solution:**
1. Verify admin user was created: `SELECT * FROM users WHERE username = 'admin';`
2. Reset admin password if needed
3. Check user roles assignment: `SELECT * FROM user_roles;`

### 10. Project Structure

```
MO-IT110/
├── src/                          # Source code
│   ├── Classes/                  # Model classes
│   ├── DAO/                      # Data Access Objects
│   ├── Database/                 # Database connection and initialization
│   ├── GUI/                      # User interface components
│   │   ├── admin/               # Admin interface
│   │   ├── employee/            # Employee interface
│   │   ├── finance/             # Finance interface
│   │   └── hr/                  # HR interface
│   ├── Test/                    # Test and utility classes
│   └── UtilityClasses/          # Helper utilities
├── libs/                        # JAR dependencies
├── resources/                   # Resource files
│   └── JSON_Files/             # JSON data files
├── database/                   # SQL scripts
├── reports/                    # Report templates and generated reports
└── bin/                        # Compiled classes
```

### 11. Additional Notes

#### 11.1 Security Considerations

- Change default passwords in production
- Use environment variables for database credentials
- Implement proper password hashing (currently using plain text)
- Add session management and timeout features

#### 11.2 Performance Optimization

- Database indexes are created for frequently queried columns
- Connection pooling can be implemented for better performance
- Consider implementing caching for reference data

#### 11.3 Backup and Recovery

Regular database backups are recommended:

```bash
# Create backup
pg_dump -U camulite_admin -h localhost motorph_payroll > backup.sql

# Restore backup
psql -U camulite_admin -h localhost motorph_payroll < backup.sql
```

---

## Quick Setup Summary

For experienced users, here's the quick setup:

1. **Create database:** `CREATE DATABASE motorph_payroll WITH OWNER camulite_admin;`
2. **Compile:** `javac -cp "libs/*" -d bin src/**/*.java`
3. **Run:** `java -cp "libs/*:bin" Main`
4. **Create users:** Run `CreateHRUserTest` and `CreateFinanceUserTest`
5. **Login:** Use `admin/123`, `hr_user/hr123`, or `finance_user/finance123`

The system will automatically handle database initialization and data migration on first run.
