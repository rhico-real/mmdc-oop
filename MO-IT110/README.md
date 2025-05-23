# CamuLite HR System - JDBC PostgreSQL Version

This is a Java-based HR management system that uses JDBC to connect to a PostgreSQL database. It provides functionality for managing employees, attendance records, leave requests, and more.

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
   CREATE DATABASE camulite_hr WITH OWNER camulite_admin;
   GRANT ALL PRIVILEGES ON DATABASE camulite_hr TO camulite_admin;
   ```

### 2. Run the Application

1. Compile and run the Main class
2. On first run, the application will:
   - Create the necessary database tables
   - Import data from the JSON files in resources/JSON_Files into the database
   - Start the application GUI

3. You can login with:
   - Admin: username `admin`, password `123`
   - Employee: Use any username/password from the LoginCredentials.json file

## Project Structure

- `src/`: Source code
  - `Classes/`: Model classes
  - `DAO/`: Data Access Objects for database operations
  - `Database/`: Database connection and initialization
  - `GUI/`: User interface components
  - `UtilityClasses/`: Helper utilities
- `resources/`: Resources used by the application
  - `JSON_Files/`: JSON data files for initial import
- `libs/`: External libraries and dependencies

## Database Schema

The application uses the following database tables:

1. `users`: Stores user login credentials
2. `employees`: Stores employee information including personal and compensation details
3. `attendance`: Stores employee attendance records
4. `leave_requests`: Stores employee leave requests

## Features

- User authentication (admin and employee roles)
- Employee information management
- Attendance tracking
- Leave request submission and approval
- Salary and compensation management

## Implementation Details

The application originally used JSON files for data storage, and has been converted to use a PostgreSQL database with JDBC. The main components of the JDBC implementation are:

1. `DatabaseConnection.java`: Manages the connection to the PostgreSQL database
2. `DatabaseInitializer.java`: Creates the database schema (tables)
3. `JsonToDatabaseImporter.java`: Imports data from JSON files into the database
4. DAO classes: Provide methods for database operations
   - `UserDAO.java`: User authentication and management
   - `EmployeeDAO.java`: Employee information management
   - `AttendanceDAO.java`: Attendance record management
   - `LeaveRequestDAO.java`: Leave request management

## Troubleshooting

- If you encounter connection issues, make sure PostgreSQL is running and the database exists
- Check the database connection settings in `DatabaseConnection.java`
- For any permission issues, make sure the database user has appropriate privileges
