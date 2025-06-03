# HR Functionality Implementation

## Overview
This document describes the HR (Human Resources) functionality that has been added to the MotorPH Payroll System. HR users have specialized access to employee management and payroll generation features.

## Features Implemented

### 1. HR User Management
- **Database Schema**: Added `is_hr` boolean field to the `users` table
- **User Class**: Extended with HR flag support and authentication logic
- **Login Routing**: HR users are automatically redirected to HR Dashboard upon login

### 2. HR Dashboard
**File**: `src/GUI/hr/HRDashboard.java`
- Clean, professional interface with HR-specific navigation
- Two main functions:
  - Search Employee & Create Payslip
  - View All Employees & Payslips
- Logout functionality

### 3. Employee Search & Payslip Creation
**File**: `src/GUI/hr/HRSearchEmployeePage.java`
- Search employees by:
  - Employee number (exact match)
  - Name (partial match, supports first name, last name, or full name)
- Display search results with employee details
- Actions for each employee:
  - **Create Payslip**: Generate payslip for the employee
  - **View Details**: Display comprehensive employee information

### 4. View All Employees
**File**: `src/GUI/hr/HRViewAllEmployeesPage.java`
- Tabular view of all employees in the system
- Columns: Employee #, Name, Position, Status, Basic Salary, Phone
- Filter functionality by name or employee number
- Actions for each employee:
  - **View Details**: Show detailed employee information
  - **Create Payslip**: Generate payslip for the employee

### 5. Payslip Creation
**File**: `src/GUI/hr/HRCreatePayslipPage.java`
- Comprehensive payslip generation interface
- Input fields:
  - Payroll period (month and year)
  - Days worked
  - Overtime hours
- **Auto-Calculate Feature**: Automatically retrieve attendance data from database
- **Generate Payslip**: Creates detailed payslip with:
  - Employee information
  - Earnings breakdown (basic salary, allowances, overtime)
  - Deductions (SSS, PhilHealth, Pag-IBIG, taxes)
  - Net pay calculation
  - Government ID numbers

## Database Changes

### Users Table
```sql
ALTER TABLE users ADD COLUMN is_hr BOOLEAN DEFAULT FALSE;
```

The `users` table now includes:
- `employee_num` (Primary Key)
- `username`
- `password`
- `is_admin` (Boolean)
- `is_hr` (Boolean) - **NEW**
- `created_at`
- `updated_at`

## DAO Enhancements

### EmployeeDAO
- **New Method**: `searchEmployeesByName(String searchTerm)`
  - Searches employees by first name, last name, or full name combinations
  - Uses case-insensitive ILIKE queries for flexible searching

### UserDAO
- Already supports HR flag in authentication and CRUD operations
- `authenticateUser()` method returns HR status
- All user management methods handle `is_hr` field

## Usage Instructions

### Creating HR Users
1. **Via Test Class**: Run `CreateHRUserTest.java` to create a test HR user
   - Username: `hr_user`
   - Password: `hr123`
   - Employee Number: `99999`

2. **Via Database**: Manually insert or update users table:
   ```sql
   UPDATE users SET is_hr = TRUE WHERE employee_num = [employee_number];
   ```

### HR Workflow
1. **Login**: HR users login with their credentials
2. **Dashboard**: Automatically redirected to HR Dashboard
3. **Search Employee**: 
   - Enter employee number or name
   - Select employee from results
   - Click "Create Payslip"
4. **Create Payslip**:
   - Enter payroll period
   - Use "Auto-Calculate" for attendance data or enter manually
   - Click "Generate Payslip"
   - Review detailed payslip

### Navigation Flow
```
LoginPage → HRDashboard → HRSearchEmployeePage → HRCreatePayslipPage
                      ↘ HRViewAllEmployeesPage → HRCreatePayslipPage
```

## File Structure
```
src/GUI/hr/
├── HRDashboard.java              # Main HR dashboard
├── HRSearchEmployeePage.java     # Employee search interface
├── HRViewAllEmployeesPage.java   # All employees table view
└── HRCreatePayslipPage.java      # Payslip generation interface
```

## Integration Points

### With Existing Classes
- **User.java**: Extended with HR flag and authentication logic
- **LoginPage.java**: Added HR routing to login flow
- **EmployeeDAO.java**: Added employee search methods
- **SalaryCalculator.java**: Used for payslip calculations
- **AttendanceDAO.java**: Used for auto-calculating work days

### Security & Access Control
- HR users have restricted access (cannot access admin functions)
- Regular employees cannot access HR functions
- Clear separation of concerns between admin, HR, and employee roles

## Testing
- **CreateHRUserTest.java**: Creates test HR user and validates authentication
- Manual testing instructions:
  1. Run CreateHRUserTest to create HR user
  2. Login with hr_user/hr123
  3. Test employee search functionality
  4. Test payslip generation
  5. Verify all navigation flows work correctly

## Future Enhancements
- Payslip history tracking
- Bulk payslip generation
- Export payslips to PDF
- Employee performance metrics
- Advanced reporting features
