Group 2 A2102 Rhico Abueme Lerra Mae Dela Cruz Christian Paul Peñaflor Emersson Aporado

Introduction This document shows the current processes of MotorPH. It aims to calculate payroll, leaves, and CRUD ensuring accurate and timely payment of employees while maintaining compliance with relevant laws and regulations. MotorPH was established in 2020 to provide private transportation options to Filipinos. Our goal is to be the first choice for Filipinos searching for competitive and affordable motorcycles. They offer motorcycles from different brands (Honda, Yamaha, Suzuki, and Kawasaki) at a discounted cash rate and flexible installment terms (from 3 months to 3 years). From a purely online presence, MotorPH is planning to open physical stores. In the next 18 months, we're planning to open branches in different parts of the Philippines. MotorPH's goal is to have an end-to-end inventory and payroll system to manage our products, employee details, and salary.

Getting started Building the program To build the program yourself (For Eclipse IDE users only): Click on the "Code" button in green above this ReadMe, then copy the hyperlink Open Eclipse and go to File > Import in Eclipse A window will appear. Under the Git folder, select the option "Projects from Git (with smart import)." A new window will appear; select the option "Clone URI." Another window will appear where you need to paste the GitHub Repository URL, GitHub User ID, and Password. After that, click on the "Next" button. Select master or main and choose the option "When fetching a commit, also fetch its tags." Choose the folder directory where you want to import the repository and then click on the "Next" button. Select the Import Source. The wizard will analyze the content of the folder to find the project and import them into the IDE. Finally, right-click the project. Choose Run As, then Java Application. You will be shown a list of applications to run, choose Main - (default package).

Using the MotorPH Payroll System Secure login procudures You can use the following credentials:

For admin Username: admin Password: 123

For employee Username: Password:

Salary and Deduction Calculations

Salary Computation The class does not currently contain a direct salary computation method. However, it provides methods to compute attendance (getAttendance()) and determine total working hours (getSumOfAttendance()), which are essential in calculating salary.
Attendance Tracking Uses LocalDateTime to extract time_in and time_out from a JSON array. Calculates total hours worked per day and determines: Presence (presentsNum increments if hours worked are within a full workday range). Lateness (latesNum increments if total minutes worked is less than 530). Absence (absentsNum increments if total minutes worked is zero). Tracks hoursRenderedNum, summing all hours worked for salary computation.
Deductions Calculation PhilHealth (getPhilHealth): Based on salary brackets, maxing out at PHP 1,800 (split between employer & employee). SSS (getSSS): Uses compensation ranges to determine the employee’s contribution. Pag-IBIG (getPagibig): Flat PHP 100 deduction if salary > PHP 1,500. Withholding Tax (getWithholding): Uses progressive tax brackets to calculate deductions.
Leave applications

A straightforward method for applying for leave includes the following steps:

Employee submits a leave request

Employees fill out a leave request form (physical or digital). They specify the leave type (sick, vacation, emergency, etc.), duration, and reason. Manager reviews the request

The request is sent to the immediate supervisor or HR for approval. They check the employee’s leave balance and department schedule. Approval/Rejection

If approved, the leave is recorded in the system, and the employee is notified. If rejected, the employee receives feedback explaining why. Payroll & Attendance Adjustment

Approved leaves update attendance records. If the leave is unpaid, salary calculations reflect the deduction.

Technical Information Payroll System Overview A payroll system automates the process of paying employees, calculating wages, withholding taxes, and delivering payments. It ensures compliance with tax laws and labor regulations while maintaining accurate financial records.

Use Case Diagram The use case diagram illustrates the interactions between users (actors) and the system's functionalities.​

Actors: Employee: Views pay slips and requests leave.​HR Manager: Manages employee information and processes payroll.​System Administrator: Manages system settings and user accounts.​

Use Cases: Manage Employee Information: HR Manager can add, update, or remove employee records.​Process Payroll: HR Manager calculates salaries, applies deductions, and generates pay slips.​View Pay Slip: Employees can access their pay slips.​Request Leave: Employees can submit leave requests for approval.​Manage System Settings: System Administrator configures system parameters and manages user roles.

Class Diagram The class diagram depicts the system's structure by showing its classes, attributes, methods, and the relationships among objects.​

Classes: Employee Attributes: employeeID, name, position, department, salary​Methods: viewPaySlip(), requestLeave()​

HRManager Attributes: managerID, name​Methods: manageEmployeeInfo(), processPayroll()​

Payroll Attributes: payrollID, employeeID, payPeriod, grossPay, deductions, netPay​Methods: calculateGrossPay(), calculateDeductions(), calculateNetPay(), generatePaySlip()​GitHub

LeaveRequest Attributes: requestID, employeeID, startDate, endDate, status​Methods: submitRequest(), approveRequest(), denyRequest()​

SystemAdministrator Attributes: adminID, name​Methods: manageSystemSettings(), manageUserAccounts()
