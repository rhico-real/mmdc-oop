package UtilityClasses;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Database.DatabaseConnection;
import Database.DatabaseInitializer;

/**
 * Utility class to migrate JSON files from resources/JSON_Files to PostgreSQL database
 * Handles all four JSON files: Employees.json, LoginCredentials.json, Attendance.json, LeaveRequest.json
 */
public class JsonToPostgresMigrator {
    
    // Date formatters for different date formats in JSON files
    private static final SimpleDateFormat[] DATE_FORMATTERS = {
        new SimpleDateFormat("MM/dd/yyyy"),
        new SimpleDateFormat("yyyy-MM-dd"),
        new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy"), // For LeaveRequest dates
        new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy") // Alternative format
    };
    
    // Cache for employee number to username mapping
    private static Map<String, String> employeeToUsernameMap = new HashMap<>();
    
    /**
     * Main method to migrate all JSON files to PostgreSQL
     */
    public static void migrateAllJsonToPostgres() {
        try {
            System.out.println("Starting JSON to PostgreSQL migration...");
            
            // Initialize database first
            DatabaseInitializer.initializeDatabase();
            
            // Build username mapping first
            buildEmployeeUsernameMapping();
            
            // Migrate in order due to dependencies
            migrateLoginCredentials();
            migrateEmployees();
            migrateAttendanceRecords();
            migrateLeaveRequests();
            
            System.out.println("JSON to PostgreSQL migration completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error during JSON migration: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Build mapping between employee numbers and usernames from JSON files
     */
    private static void buildEmployeeUsernameMapping() throws IOException {
        System.out.println("Building employee-username mapping...");
        
        JsonArray loginCredentials = JsonFileHandler.getLoginCredentialsJSON();
        
        for (JsonElement element : loginCredentials) {
            JsonObject credential = element.getAsJsonObject();
            String employeeNum = credential.get("employeeNum").getAsString();
            String username = credential.get("username").getAsString();
            employeeToUsernameMap.put(employeeNum, username);
        }
        
        System.out.println("Mapped " + employeeToUsernameMap.size() + " employee-username pairs");
    }
    
    /**
     * Migrate LoginCredentials.json to users table
     */
    public static void migrateLoginCredentials() throws IOException, SQLException {
        System.out.println("Migrating login credentials...");
        
        JsonArray loginCredentials = JsonFileHandler.getLoginCredentialsJSON();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Clear existing data except admin
            String clearUsers = "DELETE FROM users WHERE username != 'admin'";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(clearUsers);
            }
            
            String insertUserSql = """
                INSERT INTO users (username, password, email, is_active) 
                VALUES (?, ?, ?, TRUE)
                ON CONFLICT (username) DO UPDATE SET 
                password = EXCLUDED.password, email = EXCLUDED.email
            """;
            
            String insertUserRoleSql = """
                INSERT INTO user_roles (user_id, role_id) 
                SELECT ?, role_id FROM roles WHERE role_name = 'EMPLOYEE'
                ON CONFLICT (user_id, role_id) DO NOTHING
            """;
            
            try (PreparedStatement userStmt = conn.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement roleStmt = conn.prepareStatement(insertUserRoleSql)) {
                
                for (JsonElement element : loginCredentials) {
                    JsonObject credential = element.getAsJsonObject();
                    
                    String employeeNum = credential.get("employeeNum").getAsString();
                    String username = credential.get("username").getAsString();
                    String password = credential.get("password").getAsString();
                    String email = username.replace(" ", ".") + "@motorph.com";
                    
                    // Insert user
                    userStmt.setString(1, username);
                    userStmt.setString(2, password);
                    userStmt.setString(3, email);
                    userStmt.executeUpdate();
                    
                    // Get generated user ID
                    try (ResultSet rs = userStmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            int userId = rs.getInt(1);
                            
                            // Assign EMPLOYEE role
                            roleStmt.setInt(1, userId);
                            roleStmt.executeUpdate();
                        }
                    }
                }
            }
        }
        
        System.out.println("Login credentials migration completed.");
    }
    
    /**
     * Migrate Employees.json to multiple normalized tables
     */
    public static void migrateEmployees() throws IOException, SQLException {
        System.out.println("Migrating employee data...");
        
        JsonArray employees = JsonFileHandler.getEmployeesJSON();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Clear existing employee data
            clearEmployeeData(conn);
            
            for (JsonElement element : employees) {
                JsonObject emp = element.getAsJsonObject();
                
                int employeeNum = emp.get("employeeNum").getAsInt();
                String employeeNumStr = String.valueOf(employeeNum);
                
                // Get username from mapping
                String username = employeeToUsernameMap.get(employeeNumStr);
                if (username == null) {
                    System.err.println("No username found for employee: " + employeeNum);
                    continue;
                }
                
                // Get user ID
                int userId = getUserIdByUsername(conn, username);
                if (userId == -1) {
                    System.err.println("User not found for employee: " + employeeNum + " (username: " + username + ")");
                    continue;
                }
                
                // Create employee record
                int employeeId = createEmployeeRecord(conn, employeeNum, userId, emp);
                
                // Create related records
                createPersonalInformation(conn, employeeId, emp);
                createContactInformation(conn, employeeId, emp);
                createGovernmentIds(conn, employeeId, emp);
                createEmployeePosition(conn, employeeId, emp);
                createEmployeeCompensation(conn, employeeId, emp);
                createEmployeeAllowances(conn, employeeId, emp);
            }
            
            conn.commit();
            conn.setAutoCommit(true);
        }
        
        System.out.println("Employee data migration completed.");
    }
    
    /**
     * Migrate Attendance.json to attendance_records table
     */
    public static void migrateAttendanceRecords() throws IOException, SQLException {
        System.out.println("Migrating attendance records...");
        
        JsonArray attendanceData = JsonFileHandler.getAttendanceJSON();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Clear existing attendance data
            String clearAttendance = "DELETE FROM attendance_records";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(clearAttendance);
            }
            
            String insertAttendanceSql = """
                INSERT INTO attendance_records (employee_id, attendance_date, time_in, time_out, status, notes)
                SELECT e.employee_id, ?::date, ?::time, ?::time, ?, ?
                FROM employees e 
                WHERE e.employee_number = ?
                ON CONFLICT (employee_id, attendance_date) DO UPDATE SET
                time_in = EXCLUDED.time_in,
                time_out = EXCLUDED.time_out,
                status = EXCLUDED.status,
                notes = EXCLUDED.notes
            """;
            
            try (PreparedStatement pstmt = conn.prepareStatement(insertAttendanceSql)) {
                for (JsonElement element : attendanceData) {
                    JsonObject attendance = element.getAsJsonObject();
                    
                    String employeeNum = attendance.get("employeeNum").getAsString();
                    String date = attendance.get("date").getAsString();
                    String timeIn = attendance.get("time_in").getAsString();
                    String timeOut = attendance.get("time_out").getAsString();
                    
                    // Convert date format
                    String formattedDate = convertDateFormat(date, "MM/dd/yyyy", "yyyy-MM-dd");
                    if (formattedDate == null) {
                        System.err.println("Invalid date format: " + date);
                        continue;
                    }
                    
                    // Determine status based on time_in and time_out
                    String status = determineAttendanceStatus(timeIn, timeOut);
                    String notes = generateAttendanceNotes(timeIn, timeOut);
                    
                    pstmt.setString(1, formattedDate);
                    pstmt.setString(2, timeIn.equals("00:00") ? null : timeIn);
                    pstmt.setString(3, timeOut.equals("00:00") ? null : timeOut);
                    pstmt.setString(4, status);
                    pstmt.setString(5, notes);
                    pstmt.setString(6, employeeNum);
                    
                    pstmt.executeUpdate();
                }
            }
        }
        
        System.out.println("Attendance records migration completed.");
    }
    
    /**
     * Migrate LeaveRequest.json to leave_requests table
     */
    public static void migrateLeaveRequests() throws IOException, SQLException {
        System.out.println("Migrating leave requests...");
        
        JsonArray leaveRequests = JsonFileHandler.getLeaveRequestJSON();
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Clear existing leave requests
            String clearLeaveRequests = "DELETE FROM leave_requests";
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(clearLeaveRequests);
            }
            
            String insertLeaveRequestSql = """
                INSERT INTO leave_requests (request_number, employee_id, leave_type_id, start_date, end_date, 
                                          total_days, reason, status, submitted_date)
                SELECT ?, e.employee_id, 
                       COALESCE(lt.leave_type_id, (SELECT leave_type_id FROM leave_types WHERE leave_name = 'Personal Leave')),
                       ?::date, ?::date,
                       (?::date - ?::date + 1),
                       ?, 
                       CASE 
                           WHEN ? = 'Approved' THEN 'Approved'
                           WHEN ? = 'Rejected' THEN 'Rejected'
                           ELSE 'Pending'
                       END,
                       CURRENT_TIMESTAMP
                FROM employees e 
                LEFT JOIN leave_types lt ON UPPER(lt.leave_name) LIKE '%' || UPPER(?) || '%'
                WHERE e.employee_number = ?
            """;
            
            try (PreparedStatement pstmt = conn.prepareStatement(insertLeaveRequestSql)) {
                for (JsonElement element : leaveRequests) {
                    JsonObject leaveRequest = element.getAsJsonObject();
                    
                    String id = leaveRequest.get("id").getAsString();
                    String employeeNum = leaveRequest.get("employeeNum").getAsString();
                    String startDateStr = leaveRequest.get("startDate").getAsString();
                    String endDateStr = leaveRequest.get("endDate").getAsString();
                    String notes = leaveRequest.has("notes") ? leaveRequest.get("notes").getAsString() : "";
                    String approved = leaveRequest.get("approved").getAsString();
                    String leaveType = leaveRequest.has("leaveType") ? leaveRequest.get("leaveType").getAsString() : "Personal";
                    
                    // Parse dates
                    Date startDate = parseFlexibleDate(startDateStr);
                    Date endDate = parseFlexibleDate(endDateStr);
                    
                    if (startDate == null || endDate == null) {
                        System.err.println("Invalid date format in leave request: " + id);
                        continue;
                    }
                    
                    String formattedStartDate = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
                    String formattedEndDate = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
                    
                    pstmt.setString(1, "LR-" + id.substring(0, 8)); // Shorten the UUID
                    pstmt.setString(2, formattedStartDate);
                    pstmt.setString(3, formattedEndDate);
                    pstmt.setString(4, formattedStartDate);
                    pstmt.setString(5, formattedStartDate);
                    pstmt.setString(6, notes);
                    pstmt.setString(7, approved);
                    pstmt.setString(8, approved);
                    pstmt.setString(9, leaveType);
                    pstmt.setString(10, employeeNum);
                    
                    pstmt.executeUpdate();
                }
            }
        }
        
        System.out.println("Leave requests migration completed.");
    }
    
    // Helper Methods
    
    private static void clearEmployeeData(Connection conn) throws SQLException {
        String[] tables = {
            "employee_allowances", "employee_compensation", "employee_positions",
            "government_ids", "contact_information", "personal_information", "employees"
        };
        
        for (String table : tables) {
            String sql = "DELETE FROM " + table;
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql);
            }
        }
    }
    
    private static int getUserIdByUsername(Connection conn, String username) throws SQLException {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        }
        return -1;
    }
    
    private static int createEmployeeRecord(Connection conn, int employeeNum, int userId, JsonObject emp) throws SQLException {
        String sql = """
            INSERT INTO employees (employee_number, user_id, hire_date, employment_type, is_active)
            VALUES (?, ?, CURRENT_DATE, 'Full-time', 
                    CASE WHEN ? = 'Regular' THEN TRUE ELSE TRUE END)
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, String.valueOf(employeeNum));
            pstmt.setInt(2, userId);
            pstmt.setString(3, emp.get("Status").getAsString());
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to create employee record");
    }
    
    private static void createPersonalInformation(Connection conn, int employeeId, JsonObject emp) throws SQLException {
        String sql = """
            INSERT INTO personal_information (employee_id, first_name, last_name, birthday, nationality)
            VALUES (?, ?, ?, ?::date, 'Filipino')
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, emp.get("first_name").getAsString());
            pstmt.setString(3, emp.get("last_name").getAsString());
            
            String birthday = emp.get("birthday").getAsString();
            String formattedBirthday = convertDateFormat(birthday, "MM/dd/yyyy", "yyyy-MM-dd");
            pstmt.setString(4, formattedBirthday);
            
            pstmt.executeUpdate();
        }
    }
    
    private static void createContactInformation(Connection conn, int employeeId, JsonObject emp) throws SQLException {
        String sql = """
            INSERT INTO contact_information (employee_id, home_address, phone_number, mobile_number)
            VALUES (?, ?, ?, ?)
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, emp.get("address").getAsString());
            pstmt.setString(3, emp.get("phone_number").getAsString());
            pstmt.setString(4, emp.get("phone_number").getAsString()); // Use same number for mobile
            pstmt.executeUpdate();
        }
    }
    
    private static void createGovernmentIds(Connection conn, int employeeId, JsonObject emp) throws SQLException {
        String sql = "INSERT INTO government_ids (employee_id, id_type, id_number) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // SSS
            if (emp.has("SSS") && !emp.get("SSS").isJsonNull()) {
                pstmt.setInt(1, employeeId);
                pstmt.setString(2, "SSS");
                pstmt.setString(3, emp.get("SSS").getAsString());
                pstmt.executeUpdate();
            }
            
            // PhilHealth
            if (emp.has("Philhealth") && !emp.get("Philhealth").isJsonNull()) {
                pstmt.setInt(1, employeeId);
                pstmt.setString(2, "PHILHEALTH");
                pstmt.setString(3, emp.get("Philhealth").getAsString());
                pstmt.executeUpdate();
            }
            
            // TIN
            if (emp.has("TIN") && !emp.get("TIN").isJsonNull()) {
                pstmt.setInt(1, employeeId);
                pstmt.setString(2, "TIN");
                pstmt.setString(3, emp.get("TIN").getAsString());
                pstmt.executeUpdate();
            }
            
            // Pag-IBIG
            if (emp.has("Pag-ibig") && !emp.get("Pag-ibig").isJsonNull()) {
                pstmt.setInt(1, employeeId);
                pstmt.setString(2, "PAGIBIG");
                pstmt.setString(3, emp.get("Pag-ibig").getAsString());
                pstmt.executeUpdate();
            }
        }
    }
    
    private static void createEmployeePosition(Connection conn, int employeeId, JsonObject emp) throws SQLException {
        String position = emp.has("Position") ? emp.get("Position").getAsString() : "General Staff";
        
        // First ensure the position exists
        int positionId = getOrCreatePosition(conn, position);
        
        String sql = """
            INSERT INTO employee_positions (employee_id, position_id, start_date, is_current, status)
            VALUES (?, ?, CURRENT_DATE, TRUE, 'Active')
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setInt(2, positionId);
            pstmt.executeUpdate();
        }
    }
    
    private static int getOrCreatePosition(Connection conn, String positionTitle) throws SQLException {
        // Check if position exists
        String checkSql = "SELECT position_id FROM positions WHERE position_title = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            pstmt.setString(1, positionTitle);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("position_id");
                }
            }
        }
        
        // Create position if it doesn't exist
        // Generate unique position code
        String positionCode = generateUniquePositionCode(conn, positionTitle);
        
        String insertSql = """
            INSERT INTO positions (position_title, position_code, department_id, job_description)
            SELECT ?, ?, d.department_id, 'Migrated position'
            FROM departments d 
            WHERE d.department_name = 'Operations'
            LIMIT 1
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, positionTitle);
            pstmt.setString(2, positionCode);
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        
        throw new SQLException("Failed to create position: " + positionTitle);
    }
    
    private static String generateUniquePositionCode(Connection conn, String positionTitle) throws SQLException {
        // Generate base code from position title
        String baseCode = positionTitle.toUpperCase()
            .replaceAll("[^A-Z]", "")  // Remove non-letters
            .substring(0, Math.min(3, positionTitle.replaceAll("[^A-Z]", "").length()));
        
        if (baseCode.length() < 3) {
            baseCode = positionTitle.toUpperCase().substring(0, Math.min(3, positionTitle.length()));
        }
        
        // Check if code exists and make it unique
        String checkSql = "SELECT COUNT(*) FROM positions WHERE position_code = ?";
        
        String uniqueCode = baseCode;
        int counter = 1;
        
        while (true) {
            try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
                pstmt.setString(1, uniqueCode);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        return uniqueCode; // Code is unique
                    }
                }
            }
            
            // Generate new code with counter
            uniqueCode = baseCode + counter;
            counter++;
            
            // Safety check to avoid infinite loop
            if (counter > 100) {
                uniqueCode = baseCode + System.currentTimeMillis() % 1000;
                break;
            }
        }
        
        return uniqueCode;
    }
    
    private static void createEmployeeCompensation(Connection conn, int employeeId, JsonObject emp) throws SQLException {
        String sql = """
            INSERT INTO employee_compensation (employee_id, basic_salary, hourly_rate, gross_semi_monthly_rate, 
                                             effective_date, is_current)
            VALUES (?, ?, ?, ?, CURRENT_DATE, TRUE)
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setDouble(2, emp.get("basic_salary").getAsDouble());
            pstmt.setDouble(3, emp.get("hourly_rate").getAsDouble());
            pstmt.setDouble(4, emp.get("gross_semi-monthly_rate").getAsDouble());
            pstmt.executeUpdate();
        }
    }
    
    private static void createEmployeeAllowances(Connection conn, int employeeId, JsonObject emp) throws SQLException {
        String sql = """
            INSERT INTO employee_allowances (employee_id, allowance_type_id, amount, effective_date, is_active)
            SELECT ?, at.allowance_type_id, ?, CURRENT_DATE, TRUE
            FROM allowance_types at
            WHERE at.allowance_name = ?
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Rice Subsidy
            if (emp.has("rice_subsidy") && emp.get("rice_subsidy").getAsDouble() > 0) {
                pstmt.setInt(1, employeeId);
                pstmt.setDouble(2, emp.get("rice_subsidy").getAsDouble());
                pstmt.setString(3, "Rice Subsidy");
                pstmt.executeUpdate();
            }
            
            // Phone Allowance
            if (emp.has("phone_allowance") && emp.get("phone_allowance").getAsDouble() > 0) {
                pstmt.setInt(1, employeeId);
                pstmt.setDouble(2, emp.get("phone_allowance").getAsDouble());
                pstmt.setString(3, "Phone Allowance");
                pstmt.executeUpdate();
            }
            
            // Clothing Allowance
            if (emp.has("clothing_allowance") && emp.get("clothing_allowance").getAsDouble() > 0) {
                pstmt.setInt(1, employeeId);
                pstmt.setDouble(2, emp.get("clothing_allowance").getAsDouble());
                pstmt.setString(3, "Clothing Allowance");
                pstmt.executeUpdate();
            }
        }
    }
    
    private static String convertDateFormat(String dateStr, String fromFormat, String toFormat) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            SimpleDateFormat from = new SimpleDateFormat(fromFormat);
            SimpleDateFormat to = new SimpleDateFormat(toFormat);
            Date date = from.parse(dateStr);
            return to.format(date);
        } catch (ParseException e) {
            System.err.println("Error converting date: " + dateStr + " - " + e.getMessage());
            return null;
        }
    }
    
    private static Date parseFlexibleDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        
        for (SimpleDateFormat formatter : DATE_FORMATTERS) {
            try {
                return formatter.parse(dateStr);
            } catch (ParseException e) {
                // Try next formatter
            }
        }
        
        System.err.println("Unable to parse date: " + dateStr);
        return null;
    }
    
    private static String determineAttendanceStatus(String timeIn, String timeOut) {
        if ("00:00".equals(timeIn) && "00:00".equals(timeOut)) {
            return "Absent";
        } else if (timeIn != null && timeOut != null && !timeIn.equals("00:00") && !timeOut.equals("00:00")) {
            return "Present";
        } else {
            return "Incomplete";
        }
    }
    
    private static String generateAttendanceNotes(String timeIn, String timeOut) {
        if ("00:00".equals(timeIn) && "00:00".equals(timeOut)) {
            return "Employee was absent";
        } else if (!timeIn.equals("00:00") && timeOut.equals("00:00")) {
            return "Missing time out";
        } else if (timeIn.equals("00:00") && !timeOut.equals("00:00")) {
            return "Missing time in";
        } else {
            // Check if late (assuming 8:00 AM is standard time)
            if (timeIn.compareTo("08:00") > 0) {
                return "Late arrival at " + timeIn;
            }
        }
        return null;
    }
    
    /**
     * Verify the migration by counting records
     */
    public static void verifyMigration() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("\\n=== Migration Verification ===");
            
            // Count records in each table
            String[] tables = {
                "users", "employees", "personal_information", "contact_information",
                "government_ids", "employee_positions", "employee_compensation", 
                "employee_allowances", "attendance_records", "leave_requests"
            };
            
            for (String table : tables) {
                String sql = "SELECT COUNT(*) FROM " + table;
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    if (rs.next()) {
                        System.out.println(table + ": " + rs.getInt(1) + " records");
                    }
                }
            }
            
            System.out.println("=== End Verification ===\\n");
            
        } catch (SQLException e) {
            System.err.println("Error during verification: " + e.getMessage());
        }
    }
}
