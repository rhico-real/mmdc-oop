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
     * Convert date from MM/dd/yyyy to yyyy-MM-dd format
     * @param dateString Date string in MM/dd/yyyy or yyyy-MM-dd format
     * @return Date string in yyyy-MM-dd format
     */
    private static String convertToSqlDateFormat(String dateString) {
        // If already in yyyy-MM-dd format, return as is
        if (dateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return dateString;
        }
        
        // Convert from MM/dd/yyyy to yyyy-MM-dd
        if (dateString.matches("\\d{2}/\\d{2}/\\d{4}")) {
            try {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(dateString, inputFormatter);
                return date.format(outputFormatter);
            } catch (Exception e) {
                System.err.println("Error converting date format: " + e.getMessage());
                return dateString; // Return original if conversion fails
            }
        }
        
        return dateString; // Return as is if format is unknown
    }
    
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
        
        // Convert date to proper format if needed
        String formattedDate = convertToSqlDateFormat(date);
        
        String sql = """
            SELECT ar.*, e.employee_number, pi.first_name, pi.last_name
            FROM attendance_records ar
            JOIN employees e ON ar.employee_id = e.employee_id
            LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id
            WHERE ar.attendance_date = ?::date
            ORDER BY e.employee_number
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, formattedDate);
            
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
        String sql = """
            SELECT ar.*, e.employee_number, pi.first_name, pi.last_name
            FROM attendance_records ar
            JOIN employees e ON ar.employee_id = e.employee_id
            LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id
            ORDER BY ar.attendance_date DESC, e.employee_number
        """;
        
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
        // First find the employee_id based on employee_number
        String findEmployeeIdSql = "SELECT employee_id FROM employees WHERE employee_number = ?";
        String formattedDate = convertToSqlDateFormat(record.getDate());
        
        String insertSql = """
            INSERT INTO attendance_records (employee_id, attendance_date, time_in, time_out)
            VALUES (?, ?::date, ?, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            // Find employee_id
            int employeeId;
            try (PreparedStatement findStmt = conn.prepareStatement(findEmployeeIdSql)) {
                findStmt.setString(1, String.valueOf(record.getEmployeeNum()));
                try (ResultSet rs = findStmt.executeQuery()) {
                    if (rs.next()) {
                        employeeId = rs.getInt("employee_id");
                    } else {
                        System.err.println("Employee not found with number: " + record.getEmployeeNum());
                        return false;
                    }
                }
            }
            
            // Insert attendance record
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setInt(1, employeeId);
                pstmt.setString(2, formattedDate);
                pstmt.setString(3, record.getTimeIn());
                pstmt.setString(4, record.getTimeOut());
                
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
            
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
        String formattedDate = convertToSqlDateFormat(record.getDate());
        
        String sql = """
            UPDATE attendance_records SET 
                time_in = ?, time_out = ?
            WHERE employee_id = (SELECT employee_id FROM employees WHERE employee_number = ?) 
            AND attendance_date = ?::date
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, record.getTimeIn());
            pstmt.setString(2, record.getTimeOut());
            pstmt.setString(3, String.valueOf(record.getEmployeeNum()));
            pstmt.setString(4, formattedDate);
            
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
        String formattedDate = convertToSqlDateFormat(date);
        
        String sql = """
            DELETE FROM attendance_records 
            WHERE employee_id = (SELECT employee_id FROM employees WHERE employee_number = ?) 
            AND attendance_date = ?::date
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNum);
            pstmt.setString(2, formattedDate);
            
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
     * @param startDate Start date (inclusive) - can be in MM/dd/yyyy or yyyy-MM-dd format
     * @param endDate End date (inclusive) - can be in MM/dd/yyyy or yyyy-MM-dd format
     * @return List of attendance records within the date range
     */
    public static List<AttendanceRecord> getAttendanceByDateRange(String startDate, String endDate) {
        List<AttendanceRecord> records = new ArrayList<>();
        
        // Convert dates to proper format if needed
        String formattedStartDate = convertToSqlDateFormat(startDate);
        String formattedEndDate = convertToSqlDateFormat(endDate);
        
        String sql = """
            SELECT ar.*, e.employee_number, pi.first_name, pi.last_name
            FROM attendance_records ar
            JOIN employees e ON ar.employee_id = e.employee_id
            LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id
            WHERE ar.attendance_date >= ?::date AND ar.attendance_date <= ?::date
            ORDER BY ar.attendance_date, e.employee_number
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, formattedStartDate);
            pstmt.setString(2, formattedEndDate);
            
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
        
        String sql = """
            SELECT ar.*, e.employee_number, pi.first_name, pi.last_name
            FROM attendance_records ar
            JOIN employees e ON ar.employee_id = e.employee_id
            LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id
            WHERE e.employee_number = ? AND ar.attendance_date >= ?::date AND ar.attendance_date <= ?::date
            ORDER BY ar.attendance_date
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNumber);
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
