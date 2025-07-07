package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Database.DatabaseConnection;

public class AttendanceDAO {
    
    /**
     * Attendance record class to hold attendance data
     */
    public static class AttendanceRecord {
        private int employeeNum;
        private String lastName;
        private String firstName;
        private String date;
        private String timeIn;
        private String timeOut;
        
        // Constructors
        public AttendanceRecord() {}
        
        public AttendanceRecord(int employeeNum, String lastName, String firstName, 
                              String date, String timeIn, String timeOut) {
            this.employeeNum = employeeNum;
            this.lastName = lastName;
            this.firstName = firstName;
            this.date = date;
            this.timeIn = timeIn;
            this.timeOut = timeOut;
        }
        
        // Getters and Setters
        public int getEmployeeNum() { return employeeNum; }
        public void setEmployeeNum(int employeeNum) { this.employeeNum = employeeNum; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        
        public String getTimeIn() { return timeIn; }
        public void setTimeIn(String timeIn) { this.timeIn = timeIn; }
        
        public String getTimeOut() { return timeOut; }
        public void setTimeOut(String timeOut) { this.timeOut = timeOut; }
    }
    
    /**
     * Get attendance record by employee number and date
     * @param employeeNum Employee number
     * @param date Date in string format
     * @return AttendanceRecord if found, null otherwise
     */
    public static AttendanceRecord getAttendanceRecord(String employeeNum, String date) {
        // Using the stored procedure sp_get_attendance_record
        String sql = "SELECT * FROM sp_get_attendance_record(?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNum);
            pstmt.setString(2, date);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAttendance(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance record: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get all attendance records for an employee
     * @param employeeNum Employee number
     * @return List of attendance records
     */
    public static List<AttendanceRecord> getAttendanceByEmployee(String employeeNum) {
        List<AttendanceRecord> records = new ArrayList<>();
        
        // Using the stored procedure sp_get_attendance_by_employee
        String sql = "SELECT * FROM sp_get_attendance_by_employee(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNum);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToAttendance(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting attendance by employee: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }
    
    /**
     * Get all attendance records for a specific date
     * @param date Date in string format
     * @return List of attendance records for that date
     */
    public static List<AttendanceRecord> getAttendanceByDate(String date) {
        List<AttendanceRecord> records = new ArrayList<>();
        
        // Using the stored procedure sp_get_attendance_by_date
        String sql = "SELECT * FROM sp_get_attendance_by_date(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, date);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToAttendance(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting attendance by date: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }
    
    /**
     * Get all attendance records
     * @return List of all attendance records
     */
    public static List<AttendanceRecord> getAllAttendanceRecords() {
        List<AttendanceRecord> records = new ArrayList<>();
        
        // Using the stored procedure sp_get_all_attendance_records
        String sql = "SELECT * FROM sp_get_all_attendance_records()";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                records.add(mapResultSetToAttendance(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all attendance records: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }
    
    /**
     * Create or update attendance record
     * @param record AttendanceRecord to save
     * @return true if operation successful, false otherwise
     */
    public static boolean saveAttendanceRecord(AttendanceRecord record) {
        // Using the stored procedure sp_save_attendance_record
        String sql = "{CALL sp_save_attendance_record(?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setString(1, String.valueOf(record.getEmployeeNum()));
            cstmt.setString(2, record.getDate());
            cstmt.setString(3, record.getTimeIn());
            cstmt.setString(4, record.getTimeOut());
            
            // Register output parameters
            cstmt.registerOutParameter(5, Types.BOOLEAN); // success
            cstmt.registerOutParameter(6, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(5);
            if (!success) {
                String errorMessage = cstmt.getString(6);
                System.err.println("Error saving attendance record: " + errorMessage);
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Error saving attendance record: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete attendance record
     * @param employeeNum Employee number
     * @param date Date of the record to delete
     * @return true if deletion successful, false otherwise
     */
    public static boolean deleteAttendanceRecord(String employeeNum, String date) {
        // Using the stored procedure sp_delete_attendance_record
        String sql = "{CALL sp_delete_attendance_record(?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setString(1, employeeNum);
            cstmt.setString(2, date);
            
            // Register output parameters
            cstmt.registerOutParameter(3, Types.BOOLEAN); // success
            cstmt.registerOutParameter(4, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(3);
            if (!success) {
                String errorMessage = cstmt.getString(4);
                System.err.println("Error deleting attendance record: " + errorMessage);
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Error deleting attendance record: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get attendance records within date range
     * @param startDate Start date (inclusive) - can be in MM/dd/yyyy or yyyy-MM-dd format
     * @param endDate End date (inclusive) - can be in MM/dd/yyyy or yyyy-MM-dd format
     * @return List of attendance records within the date range
     */
    public static List<AttendanceRecord> getAttendanceByDateRange(String startDate, String endDate) {
        List<AttendanceRecord> records = new ArrayList<>();
        
        // Using the stored procedure sp_get_attendance_by_date_range
        String sql = "SELECT * FROM sp_get_attendance_by_date_range(?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToAttendance(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting attendance by date range: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }
    
    /**
     * Calculate monthly attendance summary for an employee
     * @param employeeNumber Employee number as string
     * @param month Month (1-12)
     * @param year Year
     * @return Map containing daysWorked and overtimeHours, or null if no data found
     */
    public static Map<String, Object> calculateMonthlyAttendance(String employeeNumber, int month, int year) {
        Map<String, Object> result = new HashMap<>();
        
        // Using the stored procedure sp_calculate_monthly_attendance
        String sql = "SELECT * FROM sp_calculate_monthly_attendance(?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNumber);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int daysWorked = rs.getInt("days_worked");
                    double overtimeHours = rs.getDouble("overtime_hours");
                    
                    result.put("daysWorked", daysWorked);
                    result.put("overtimeHours", overtimeHours);
                    return result;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error calculating monthly attendance: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Invalid employee number format: " + employeeNumber);
            e.printStackTrace();
        }
        
        return null; // Return null if no data found or error occurred
    }
    
    /**
     * Helper method to map ResultSet to AttendanceRecord object
     */
    private static AttendanceRecord mapResultSetToAttendance(ResultSet rs) throws SQLException {
        AttendanceRecord record = new AttendanceRecord();
        record.setEmployeeNum(Integer.parseInt(rs.getString("employee_number")));
        record.setLastName(rs.getString("last_name"));
        record.setFirstName(rs.getString("first_name"));
        record.setDate(rs.getString("attendance_date"));
        record.setTimeIn(rs.getString("time_in"));
        record.setTimeOut(rs.getString("time_out"));
        return record;
    }
}
