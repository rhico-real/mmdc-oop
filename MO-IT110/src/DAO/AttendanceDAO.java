package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String sql = """
            SELECT ar.*, e.employee_number, pi.first_name, pi.last_name
            FROM attendance_records ar
            JOIN employees e ON ar.employee_id = e.employee_id
            LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id
            WHERE e.employee_number = ? AND ar.attendance_date = ?
        """;
        
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
        String sql = """
            SELECT ar.*, e.employee_number, pi.first_name, pi.last_name
            FROM attendance_records ar
            JOIN employees e ON ar.employee_id = e.employee_id
            LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id
            WHERE e.employee_number = ?
            ORDER BY ar.attendance_date DESC
        """;
        
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
        String sql = "SELECT * FROM attendance WHERE date = ? ORDER BY employee_num";
        
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
        String sql = "SELECT * FROM attendance ORDER BY date DESC, employee_num";
        
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
        // First check if record exists
        AttendanceRecord existing = getAttendanceRecord(
            String.valueOf(record.getEmployeeNum()), record.getDate());
        
        if (existing != null) {
            return updateAttendanceRecord(record);
        } else {
            return createAttendanceRecord(record);
        }
    }
    
    /**
     * Create new attendance record
     * @param record AttendanceRecord to create
     * @return true if creation successful, false otherwise
     */
    public static boolean createAttendanceRecord(AttendanceRecord record) {
        String sql = """
            INSERT INTO attendance (employee_num, last_name, first_name, date, time_in, time_out)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, record.getEmployeeNum());
            pstmt.setString(2, record.getLastName());
            pstmt.setString(3, record.getFirstName());
            pstmt.setString(4, record.getDate());
            pstmt.setString(5, record.getTimeIn());
            pstmt.setString(6, record.getTimeOut());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating attendance record: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update existing attendance record
     * @param record AttendanceRecord with updated information
     * @return true if update successful, false otherwise
     */
    public static boolean updateAttendanceRecord(AttendanceRecord record) {
        String sql = """
            UPDATE attendance SET 
                last_name = ?, first_name = ?, time_in = ?, time_out = ?
            WHERE employee_num = ? AND date = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, record.getLastName());
            pstmt.setString(2, record.getFirstName());
            pstmt.setString(3, record.getTimeIn());
            pstmt.setString(4, record.getTimeOut());
            pstmt.setInt(5, record.getEmployeeNum());
            pstmt.setString(6, record.getDate());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating attendance record: " + e.getMessage());
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
        String sql = "DELETE FROM attendance WHERE employee_num = ? AND date = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(employeeNum));
            pstmt.setString(2, date);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting attendance record: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get attendance records within date range
     * @param startDate Start date (inclusive)
     * @param endDate End date (inclusive)
     * @return List of attendance records within the date range
     */
    public static List<AttendanceRecord> getAttendanceByDateRange(String startDate, String endDate) {
        List<AttendanceRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE date >= ? AND date <= ? ORDER BY date, employee_num";
        
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
        int daysWorked = 0;
        double totalOvertimeHours = 0.0;
        
        // Create date range for the month
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        
        String sql = "SELECT * FROM attendance WHERE employee_num = ? AND date >= ? AND date <= ? ORDER BY date";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(employeeNumber));
            pstmt.setString(2, startDate.toString());
            pstmt.setString(3, endDate.toString());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                
                while (rs.next()) {
                    String timeIn = rs.getString("time_in");
                    String timeOut = rs.getString("time_out");
                    
                    // If both time in and time out are present, count as a day worked
                    if (timeIn != null && timeOut != null && !timeIn.trim().isEmpty() && !timeOut.trim().isEmpty()) {
                        daysWorked++;
                        
                        try {
                            // Calculate overtime (assuming 8-hour work day)
                            LocalTime inTime = LocalTime.parse(timeIn, timeFormatter);
                            LocalTime outTime = LocalTime.parse(timeOut, timeFormatter);
                            
                            // Calculate total hours worked
                            long totalMinutes = ChronoUnit.MINUTES.between(inTime, outTime);
                            double totalHours = totalMinutes / 60.0;
                            
                            // Subtract 1 hour for lunch break if worked more than 6 hours
                            if (totalHours > 6) {
                                totalHours -= 1; // lunch break
                            }
                            
                            // Calculate overtime (hours beyond 8)
                            if (totalHours > 8) {
                                totalOvertimeHours += (totalHours - 8);
                            }
                            
                        } catch (Exception e) {
                            System.err.println("Error parsing time for employee " + employeeNumber + ": " + e.getMessage());
                            // Continue processing other records even if time parsing fails
                        }
                    }
                }
            }
            
            // Return results if any attendance data found
            if (daysWorked > 0 || totalOvertimeHours > 0) {
                result.put("daysWorked", daysWorked);
                result.put("overtimeHours", Math.round(totalOvertimeHours * 100.0) / 100.0); // Round to 2 decimal places
                return result;
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
