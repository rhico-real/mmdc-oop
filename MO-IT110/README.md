# MotorPH Payroll System - JDBC PostgreSQL Version

This is a Java-based HR management system that uses JDBC to connect to a PostgreSQL database. It provides functionality for managing employees, attendance records, leave requests, and more.

## Important Note on JSON Files

**The application no longer uses JSON files for data storage.** All data is now stored in a PostgreSQL database. The JSON files that were originally used for data storage can be safely deleted from the resources/JSON_Files directory as they are no longer needed.

## Prerequisites

- Java Development Kit (JDK) 11 or later
- PostgreSQL 11 or later
- PostgreSQL JDBC Driver (included in the libs folder)

## Setup Instructions

### 1. Database Setup

First, you need to create the PostgreSQL database:

1. Make sure PostgreSQL is installed and running on your system
2. Run the setup script (you may need to make it executable first):
   ```bash
   chmod +x setup_database.sh
   ./setup_database.sh
   ```
   
   If you're on Windows, you can run these SQL commands manually in PostgreSQL:
   ```sql
   CREATE ROLE camulite_admin WITH LOGIN PASSWORD '123';
   CREATE DATABASE motorph_payroll WITH OWNER camulite_admin;
   GRANT ALL PRIVILEGES ON DATABASE motorph_payroll TO camulite_admin;
   ```

### 2. Run the Application

1. Compile and run the Main class
2. On first run, the application will:
   - Create the necessary database tables
   - Create the admin user account
   - Start the application GUI

3. You can login with:
   - Admin: username `admin`, password `123`

## Project Structure

- `src/`: Source code
  - `Classes/`: Model classes
  - `DAO/`: Data Access Objects for database operations
  - `Database/`: Database connection and initialization
  - `GUI/`: User interface components
  - `UtilityClasses/`: Helper utilities

## Database Schema

The application uses the following database tables:

1. `users`: Stores user login credentials
2. `employees`: Stores employee information including personal and compensation details
3. `attendance`: Stores employee attendance records
4. `leave_requests`: Stores employee leave request information

## Features

- User authentication (admin and employee roles)
- Employee information management
- Attendance tracking
- Leave request submission and approval
- Salary and compensation management

## Troubleshooting

- If you encounter connection issues, make sure PostgreSQL is running and the database exists
- Check the database connection settings in `DatabaseConnection.java`
- For any permission issues, make sure the database user has appropriate privileges
