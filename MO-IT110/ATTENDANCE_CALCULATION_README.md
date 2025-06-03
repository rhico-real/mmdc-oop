# Attendance Days Worked Calculation - PostgreSQL Implementation

This document explains the new database-based attendance calculation functionality that has been added to your MotorPH Payroll System.

## Overview

The system now calculates days worked for employees directly from your PostgreSQL attendance table instead of using JSON files. This provides more accurate, real-time data for payroll calculations.

## New Features Added

### 1. Database-Based Attendance Calculation
- **File Modified**: `EmployeeDashboard.java`
- **New Method**: `calculateDaysWorkedFromDatabase(String employeeNumber, String selectedMonth)`
- **Purpose**: Calculates total days worked, hours rendered, and attendance statistics from PostgreSQL

### 2. Static Utility Methods
- **Method 1**: `calculateDaysWorkedForEmployee(String employeeNumber, String selectedMonth)`
- **Method 2**: `calculateDaysWorkedForEmployee(String employeeNumber, String selectedMonth, int year)`
- **Purpose**: Can be used by other classes without needing an EmployeeDashboard instance

### 3. Updated Payslip Integration
- **Modified**: `viewPayslipButtonActionPerformed()` method
- **Improvement**: Now automatically calculates days worked from database before showing payslip

## How It Works

### Database Query Process
1. Takes the selected month and employee number
2. Calculates the first and last day of the selected month
3. Queries PostgreSQL attendance table for records in that date range
4. Filters records for the specific employee
5. Counts days where time_in and time_out are valid (minutes > 0)
6. Updates UI with calculated values

### Attendance Calculation Logic
- **Present Day**: Employee has valid time_in and time_out with minutes > 0
- **Absent Day**: Missing time_in, time_out, or both are null/empty
- **Late Day**: Present but worked less than 8.5 hours (530 minutes)
- **Hours Rendered**: Total hours worked with special handling for 8.5+ hour days

## Usage Examples

### 1. Using the Instance Method (within EmployeeDashboard)
```java
// Calculate days worked for currently logged-in employee
int daysWorked = getDaysWorkedForEmployee("JANUARY");
System.out.println("Days worked in January: " + daysWorked);
```

### 2. Using Static Methods (from any class)
```java
// Calculate for any employee and month (current year)
int daysWorked = EmployeeDashboard.calculateDaysWorkedForEmployee("10001", "FEBRUARY");

// Calculate for specific year
int daysWorked2023 = EmployeeDashboard.calculateDaysWorkedForEmployee("10001", "MARCH", 2023);
```

### 3. Integration in Payslip View
The payslip now automatically:
1. Calculates days worked from database
2. Ensures all salary computations are complete
3. Passes accurate days worked to ViewPayslipPage

## Database Requirements

### Expected Attendance Table Structure
```sql
CREATE TABLE attendance (
    employee_num INTEGER,
    last_name VARCHAR(255),
    first_name VARCHAR(255),
    date VARCHAR(20),        -- Format: MM/dd/yyyy
    time_in VARCHAR(10),     -- Format: HH:mm
    time_out VARCHAR(10)     -- Format: HH:mm
);
```

### Sample Data Format
```
employee_num | date       | time_in | time_out
-------------|------------|---------|----------
10001        | 01/15/2025 | 08:00   | 17:00
10001        | 01/16/2025 | 08:15   | 17:30
10002        | 01/15/2025 | 09:00   | 18:00
```

## Testing

### Run the Test Class
```java
// Execute the test to verify functionality
Test.AttendanceDaysWorkedTest.main(null);
```

### What the Test Does
1. **Static Calculation Test**: Tests days worked calculation for multiple employees and months
2. **Database Retrieval Test**: Verifies attendance records exist and can be retrieved
3. **Connection Test**: Validates database connection and table structure

## Error Handling

### Common Issues and Solutions

1. **No attendance records found**
   - Check if PostgreSQL is running
   - Verify database name and credentials in `DatabaseConnection.java`
   - Ensure attendance table exists with correct structure

2. **Date parsing errors**
   - Verify date format in database matches MM/dd/yyyy
   - Check time format matches HH:mm

3. **Database connection errors**
   - Confirm PostgreSQL service is running
   - Check database URL, username, and password
   - Verify JDBC driver is in classpath

## Integration Points

### Modified Files
1. **EmployeeDashboard.java**
   - Added imports for DAO, SQL, and time formatting
   - New database calculation methods
   - Updated compute and payslip view methods

2. **Existing Dependencies**
   - Uses existing `AttendanceDAO.java`
   - Uses existing `DatabaseConnection.java`
   - Compatible with existing compensation and deduction calculations

### Backward Compatibility
- Original JSON-based method (`loadAttendanceRecordsFromJsonFile`) still exists
- Can switch between database and JSON by changing the compute button method
- All existing functionality remains intact

## Benefits

1. **Real-time Data**: Always uses current database information
2. **Accuracy**: Eliminates need to manually sync JSON files
3. **Scalability**: Handles large amounts of attendance data efficiently
4. **Maintainability**: Single source of truth for attendance data
5. **Flexibility**: Static methods allow use throughout the application

## Next Steps

1. **Test with your data**: Run the test class to verify it works with your PostgreSQL setup
2. **Verify calculations**: Compare results with your existing payroll calculations
3. **Add error logging**: Consider adding more detailed logging for production use
4. **Performance optimization**: Add database indexing on employee_num and date columns for better performance

## Support

If you encounter any issues:
1. Check the console output for detailed error messages
2. Verify your PostgreSQL attendance table structure matches the expected format
3. Ensure all attendance records have valid date and time formats
4. Test database connectivity using the provided test class
