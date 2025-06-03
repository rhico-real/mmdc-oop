# Leave Request Database Integration

This document describes the implementation of leave request functionality that saves data to PostgreSQL database instead of JSON files.

## Changes Made

### 1. Employee Dashboard (EmployeeDashboard.java)
- **Location**: Line 802 in `submitLeaveRequestButton` action listener
- **Change**: The "Submit Leave Request" button now correctly navigates to the `LeaveRequestPage` where employees can fill out leave request forms
- **Functionality**: Button opens the leave request form interface

### 2. Leave Request Form (LeaveRequestPage.java)
- **Location**: `addLeaveRequest()` method
- **Changes**:
  - Added import for `LeaveRequestDAO` and related database classes
  - Modified the method to save leave requests to PostgreSQL database instead of JSON files
  - Added success/error messages to provide user feedback
  - Removed JSON file handling code

**Before**:
```java
// Read existing LeaveRequest objects from the file
List<LeaveRequest> existingLeaveRequests = JsonFileHandler
    .readLeaveRequestsFromFile(JsonFileHandler.getLeaveRequestJsonPath());

// Add the new LeaveRequest object to the list
existingLeaveRequests.add(leaveRequest);

// Write the updated list back to the file
JsonFileHandler.addToJsonFile(existingLeaveRequests, JsonFileHandler.getLeaveRequestJsonPath());
```

**After**:
```java
// Save the leave request to the database
boolean success = LeaveRequestDAO.createLeaveRequest(leaveRequest);

if (success) {
    JOptionPane.showMessageDialog(this, 
        "Leave request submitted successfully!", 
        "Success", 
        JOptionPane.INFORMATION_MESSAGE);
} else {
    JOptionPane.showMessageDialog(this, 
        "Failed to submit leave request. Please try again.", 
        "Error", 
        JOptionPane.ERROR_MESSAGE);
}
```

### 3. Admin Dashboard (DashboardPage.java)
- **Location**: Line 163 in `leaveRequestButtonActionPerformed()` method
- **Status**: ✅ Already correctly implemented
- **Functionality**: Uses `LeaveRequestDAO.getAllLeaveRequests()` to check for and display leave requests from the database

### 4. Database Schema
- **Table**: `leave_requests` table already exists in `DatabaseInitializer.java`
- **Structure**:
  ```sql
  CREATE TABLE IF NOT EXISTS leave_requests (
      id VARCHAR(50) PRIMARY KEY,
      employee_num INTEGER NOT NULL,
      first_name VARCHAR(100),
      last_name VARCHAR(100),
      start_date VARCHAR(100),
      end_date VARCHAR(100),
      notes TEXT,
      leave_type VARCHAR(50),
      approved VARCHAR(50) DEFAULT 'Not Approved Yet',
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      FOREIGN KEY (employee_num) REFERENCES users(employee_num) ON DELETE CASCADE
  )
  ```

## Data Flow

### Employee Submits Leave Request:
1. Employee clicks "Submit Leave Request" button in `EmployeeDashboard`
2. `LeaveRequestPage` opens with form fields
3. Employee fills out:
   - Leave type (Sick Leave, Vacation Leave, Emergency Leave)
   - Start date
   - End date
   - Notes
4. Employee clicks "Submit"
5. `addLeaveRequest()` method creates `LeaveRequest` object
6. `LeaveRequestDAO.createLeaveRequest()` saves to PostgreSQL database
7. Success/error message displayed to employee

### Admin Views Leave Requests:
1. Admin clicks "Leave Requests" button in `DashboardPage`
2. System calls `LeaveRequestDAO.getAllLeaveRequests()` to fetch from database
3. If requests exist, `LeaveRequestListPage` opens showing all requests
4. Admin can view details, approve/reject, or delete requests
5. All actions update the PostgreSQL database directly

## Database Classes Used

### LeaveRequestDAO.java
- `createLeaveRequest(LeaveRequest)` - Saves new leave request to database
- `getAllLeaveRequests()` - Retrieves all leave requests from database
- `getLeaveRequestById(String)` - Gets specific leave request by ID
- `updateLeaveRequestStatus(String, String)` - Updates approval status
- `deleteLeaveRequest(String)` - Removes leave request from database

### DatabaseConnection.java
- Manages PostgreSQL database connections
- Connection string: `jdbc:postgresql://localhost:5432/motorph_payroll`
- Username: `camulite_admin`
- Password: `123`

## Testing

A test file has been created at `Test/DatabaseLeaveRequestTest.java` to verify:
- Database connection
- Schema initialization
- Leave request creation
- Data retrieval
- Status updates

## Benefits of Database Integration

1. **Data Persistence**: Leave requests are stored in a robust PostgreSQL database
2. **Data Integrity**: Foreign key constraints ensure referential integrity
3. **Concurrent Access**: Multiple users can safely access the system
4. **Backup and Recovery**: Database can be backed up and restored
5. **Scalability**: Can handle large numbers of leave requests efficiently
6. **ACID Compliance**: Ensures data consistency and reliability

## Error Handling

- Database connection failures are caught and displayed to users
- Failed save operations show error messages
- Database initialization is handled gracefully
- All database operations include proper error handling and logging

## Next Steps

The leave request system is now fully integrated with the PostgreSQL database. Future enhancements could include:
- Email notifications for leave request submissions and approvals
- Leave balance tracking
- Reporting and analytics
- Workflow automation
