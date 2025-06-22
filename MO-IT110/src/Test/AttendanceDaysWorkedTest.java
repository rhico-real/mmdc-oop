package Test;

import GUI.employee.EmployeeDashboard;
import DAO.AttendanceDAO;
import java.time.LocalDate;

/**
 * Test class to demonstrate the attendance days worked calculation functionality
 * This class shows how to use the new database-based attendance calculation methods
 */
public class AttendanceDaysWorkedTest {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Attendance Days Worked Calculation ===");
        
        // Test the static utility method
        testStaticDaysWorkedCalculation();
        
        // Test getting all attendance records for verification
        testAttendanceRecordsRetrieval();
        
        // Test database connection
        testDatabaseConnection();
    }
    
    /**
     * Test the static method to calculate days worked for different employees and months
     */
    private static void testStaticDaysWorkedCalculation() {
        System.out.println("\n1. Testing Static Days Worked Calculation:");
        System.out.println("==========================================");
        
        // Test different employee numbers and months
        String[] testEmployees = {"10001", "10002", "10003", "20001", "20002"};
        String[] testMonths = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE"};
        
        int currentYear = LocalDate.now().getYear();
        
        for (String employeeNum : testEmployees) {
            System.out.println("\n--- Employee " + employeeNum + " ---");
            for (String month : testMonths) {
                try {
                    int daysWorked = EmployeeDashboard.calculateDaysWorkedForEmployee(employeeNum, month);
                    System.out.printf("  %s %d: %d days worked%n", month, currentYear, daysWorked);
                } catch (Exception e) {
                    System.err.println("  Error calculating for " + month + ": " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Test getting attendance records to verify the data exists
     */
    private static void testAttendanceRecordsRetrieval() {
        System.out.println("\n\n2. Testing Attendance Records Retrieval:");
        System.out.println("=========================================");
        
        try {
            // Get all attendance records
            var allRecords = AttendanceDAO.getAllAttendanceRecords();
            System.out.println("Total attendance records in database: " + allRecords.size());
            
            if (!allRecords.isEmpty()) {
                System.out.println("\nSample attendance records:");
                System.out.println("Employee | Date       | Time In | Time Out | Hours");
                System.out.println("---------|------------|---------|----------|------");
                
                // Show first 10 records as samples
                int count = 0;
                for (AttendanceDAO.AttendanceRecord record : allRecords) {
                    if (count >= 10) break;
                    
                    String timeIn = record.getTimeIn() != null ? record.getTimeIn() : "N/A";
                    String timeOut = record.getTimeOut() != null ? record.getTimeOut() : "N/A";
                    
                    // Calculate hours if both times are available
                    String hours = "N/A";
                    if (!timeIn.equals("N/A") && !timeOut.equals("N/A")) {
                        try {
                            java.time.format.DateTimeFormatter timeFormatter = 
                                java.time.format.DateTimeFormatter.ofPattern("HH:mm");
                            java.time.LocalTime timeInParsed = java.time.LocalTime.parse(timeIn, timeFormatter);
                            java.time.LocalTime timeOutParsed = java.time.LocalTime.parse(timeOut, timeFormatter);
                            long minutes = java.time.Duration.between(timeInParsed, timeOutParsed).toMinutes();
                            double hoursWorked = minutes / 60.0;
                            hours = String.format("%.1f", hoursWorked);
                        } catch (Exception e) {
                            hours = "Error";
                        }
                    }
                    
                    System.out.printf("%8d | %10s | %7s | %8s | %5s%n",
                        record.getEmployeeNum(),
                        record.getDate(),
                        timeIn,
                        timeOut,
                        hours
                    );
                    count++;
                }
            } else {
                System.out.println("No attendance records found in database.");
                System.out.println("Please ensure your PostgreSQL database is running and contains attendance data.");
            }
            
        } catch (Exception e) {
            System.err.println("Error retrieving attendance records: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test method to verify database connection and attendance table structure
     */
    public static void testDatabaseConnection() {
        System.out.println("\n3. Testing Database Connection:");
        System.out.println("===============================");
        
        try {
            // Test getting attendance for a specific employee
            String testEmployeeNum = "10001";
            var employeeRecords = AttendanceDAO.getAttendanceByEmployee(testEmployeeNum);
            
            System.out.println("Employee " + testEmployeeNum + " has " + employeeRecords.size() + " attendance records");
            
            if (!employeeRecords.isEmpty()) {
                AttendanceDAO.AttendanceRecord firstRecord = employeeRecords.get(0);
                System.out.println("\nSample record for employee " + testEmployeeNum + ":");
                System.out.println("  Date: " + firstRecord.getDate());
                System.out.println("  Time In: " + firstRecord.getTimeIn());
                System.out.println("  Time Out: " + firstRecord.getTimeOut());
                System.out.println("  Employee Name: " + firstRecord.getFirstName() + " " + firstRecord.getLastName());
            }
            
        } catch (Exception e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            System.err.println("Please check:");
            System.err.println("1. PostgreSQL is running");
            System.err.println("2. Database 'motorph_payroll' exists");
            System.err.println("3. Attendance table exists with correct structure");
            System.err.println("4. Database credentials are correct");
        }
    }
}
