# Database Views and Stored Procedures

This directory contains SQL scripts to create views and stored procedures for the application's database. These database objects replace the SQL queries that were previously embedded in the DAO classes, providing better separation of concerns and improved maintainability.

## Structure

- `EmployeeViews.sql` - Views for employee data
- `EmployeeProcedures.sql` - Stored procedures for employee operations
- `AttendanceViews.sql` - Views for attendance data
- `AttendanceProcedures.sql` - Stored procedures for attendance operations
- `LeaveRequestViews.sql` - Views for leave request data
- `LeaveRequestProcedures.sql` - Stored procedures for leave request operations
- `UserViews.sql` - Views for user data
- `UserProcedures.sql` - Stored procedures for user operations
- `UpdateRequestViews.sql` - Views for update request data
- `UpdateRequestProcedures.sql` - Stored procedures for update request operations
- `install_database_objects.sql` - Master script to install all objects

## Installation

1. Connect to your PostgreSQL database using psql:
   ```
   psql -U your_username -d your_database_name
   ```

2. Navigate to this directory:
   ```
   \cd /path/to/MO-IT110/src/Database/SQL
   ```

3. Run the installation script:
   ```
   \i install_database_objects.sql
   ```

## Usage in DAO Classes

To use these views and stored procedures, update your DAO classes to call them instead of using embedded SQL. Here are some examples:

### Example: Getting an employee by number

Before:
```java
String sql = """
    SELECT e.employee_id, e.employee_number, e.hire_date, e.employment_type, e.is_active,
           u.username, u.email,
           pi.first_name, pi.last_name, pi.middle_name, pi.birthday, pi.gender, pi.civil_status, pi.nationality,
           ci.home_address, ci.city, ci.province, ci.postal_code, ci.phone_number, ci.mobile_number,
           ci.emergency_contact_name, ci.emergency_contact_number,
           p.position_title, d.department_name,
           ep.supervisor_id, ep.status as position_status
    FROM employees e
    JOIN users u ON e.user_id = u.user_id
    LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id
    LEFT JOIN contact_information ci ON e.employee_id = ci.employee_id
    LEFT JOIN employee_positions ep ON e.employee_id = ep.employee_id AND ep.is_current = TRUE
    LEFT JOIN positions p ON ep.position_id = p.position_id
    LEFT JOIN departments d ON p.department_id = d.department_id
    WHERE e.employee_number = ? AND e.is_active = TRUE
""";

try (Connection conn = DatabaseConnection.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
    pstmt.setString(1, employeeNum);
    
    try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
            return mapResultSetToEmployee(rs);
        }
    }
}
```

After:
```java
try (Connection conn = DatabaseConnection.getConnection();
     CallableStatement cstmt = conn.prepareCall("SELECT * FROM vw_employee_information WHERE employee_number = ? AND is_active = TRUE")) {
    
    cstmt.setString(1, employeeNum);
    
    try (ResultSet rs = cstmt.executeQuery()) {
        if (rs.next()) {
            return mapResultSetToEmployee(rs);
        }
    }
}
```

### Example: Creating a leave request

Before:
```java
String sql = """
    INSERT INTO leave_requests (
        id, employee_num, first_name, last_name, start_date, end_date, 
        notes, leave_type, approved
    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
""";

try (Connection conn = DatabaseConnection.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
    pstmt.setString(1, leaveRequest.getId());
    pstmt.setInt(2, Integer.parseInt(leaveRequest.getEmployeeNum()));
    pstmt.setString(3, leaveRequest.getFirstName());
    // ... set other parameters
    
    int rowsAffected = pstmt.executeUpdate();
    return rowsAffected > 0;
}
```

After:
```java
try (Connection conn = DatabaseConnection.getConnection();
     CallableStatement cstmt = conn.prepareCall("{CALL sp_create_leave_request(?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
    
    cstmt.setString(1, leaveRequest.getEmployeeNum());
    cstmt.setString(2, leaveRequest.getLeaveType());
    cstmt.setDate(3, Date.valueOf(leaveRequest.getStartDate()));
    cstmt.setDate(4, Date.valueOf(leaveRequest.getEndDate()));
    cstmt.setString(5, leaveRequest.getNotes());
    cstmt.registerOutParameter(6, Types.VARCHAR); // requestNumber
    cstmt.registerOutParameter(7, Types.BOOLEAN); // success
    cstmt.registerOutParameter(8, Types.VARCHAR); // errorMessage
    
    cstmt.execute();
    
    boolean success = cstmt.getBoolean(7);
    if (success) {
        leaveRequest.setId(cstmt.getString(6)); // Set the generated request number
    }
    
    return success;
}
```

## Benefits

1. **Separation of Concerns**: SQL logic is separated from Java code.
2. **Maintainability**: SQL queries can be modified without changing Java code.
3. **Performance**: Views and stored procedures can be optimized by the database.
4. **Security**: Stored procedures can be granted specific permissions.
5. **Reusability**: Database objects can be used by multiple applications.

## Naming Conventions

- Views are prefixed with `vw_`
- Stored procedures are prefixed with `sp_`
- Functions are prefixed with `fn_`

## Notes on Implementation

- All functions that return data use the `RETURNS TABLE` syntax for consistency.
- All procedures that modify data include `OUT p_success BOOLEAN` and `OUT p_error_message TEXT` parameters.
- Views abstract the joins between tables to simplify data access.
- The `fn_convert_to_sql_date_format` function is provided to handle different date formats consistently.
