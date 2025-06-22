package UtilityClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import Database.DatabaseConnection;

/**
 * Utility class to migrate data from the old 4-table structure 
 * to the new normalized 15+ table structure
 */
public class DataMigrationUtility {
    
    /**
     * Main migration method - converts old structure to new normalized structure
     */
    public static void migrateToNormalizedStructure() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            System.out.println("Starting migration from 4-table structure to normalized structure...");
            
            // Step 1: Backup old tables
            backupOldTables(conn);
            
            // Step 2: Migrate users table
            migrateUsers(conn);
            
            // Step 3: Migrate employees table
            migrateEmployees(conn);
            
            // Step 4: Migrate attendance table
            migrateAttendance(conn);
            
            // Step 5: Migrate leave requests table
            migrateLeaveRequests(conn);
            
            // Step 6: Create missing default data
            createDefaultData(conn);
            
            conn.commit();
            System.out.println("Migration completed successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error during migration: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Backup old tables with a suffix
     */
    private static void backupOldTables(Connection conn) throws SQLException {
        System.out.println("Creating backup of old tables...");
        
        String[] tables = {"users", "employees", "attendance", "leave_requests"};
        
        for (String table : tables) {
            try {
                // Check if old table exists
                String checkSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = '" + table + "_old'";
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery(checkSql);
                    rs.next();
                    if (rs.getInt(1) == 0) {
                        // Create backup
                        String backupSql = "CREATE TABLE " + table + "_old AS SELECT * FROM " + table;
                        stmt.execute(backupSql);
                        System.out.println("Backed up " + table + " to " + table + "_old");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Could not backup " + table + " (table may not exist): " + e.getMessage());
            }
        }
    }
    
    /**
     * Migrate users from old structure
     */
    private static void migrateUsers(Connection conn) throws SQLException {
        System.out.println("Migrating users...");
        
        // Check if old users table exists
        if (!tableExists(conn, "users_old")) {
            System.out.println("No users_old table found, skipping user migration");
            return;
        }
        
        String selectOldUsers = """
            SELECT employee_num, username, password, is_admin, is_hr
            FROM users_old
        """;
        
        Map<String, Integer> employeeToUserId = new HashMap<>();
        
        try (PreparedStatement selectStmt = conn.prepareStatement(selectOldUsers);
             ResultSet rs = selectStmt.executeQuery()) {
            
            while (rs.next()) {
                String employeeNum = rs.getString("employee_num");
                String username = rs.getString("username");
                String password = rs.getString("password");
                boolean isAdmin = rs.getBoolean("is_admin");
                boolean isHR = rs.getBoolean("is_hr");
                
                // Create new user record
                String insertUser = """
                    INSERT INTO users (username, password, email, is_active) 
                    VALUES (?, ?, ?, TRUE)
                """;
                
                int userId;
                try (PreparedStatement userStmt = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
                    userStmt.setString(1, username);
                    userStmt.setString(2, password);
                    userStmt.setString(3, username + "@motorph.com");
                    userStmt.executeUpdate();
                    
                    try (ResultSet userRs = userStmt.getGeneratedKeys()) {
                        if (userRs.next()) {
                            userId = userRs.getInt(1);
                            employeeToUserId.put(employeeNum, userId);
                        } else {
                            throw new SQLException("Failed to get user ID");
                        }
                    }
                }
                
                // Assign roles
                if (isAdmin) {
                    assignRole(conn, userId, "ADMIN");
                    createAdminRecord(conn, userId);
                }
                if (isHR) {
                    assignRole(conn, userId, "HR");
                    createHRRecord(conn, userId);
                }
                if (!isAdmin && !isHR) {
                    assignRole(conn, userId, "EMPLOYEE");
                }
            }
        }
        
        // Store mapping for later use
        storeUserMapping(conn, employeeToUserId);
        System.out.println("Users migration completed.");
    }
    
    /**
     * Migrate employees from old structure
     */
    private static void migrateEmployees(Connection conn) throws SQLException {
        System.out.println("Migrating employees...");
        
        if (!tableExists(conn, "employees_old")) {
            System.out.println("No employees_old table found, skipping employee migration");
            return;
        }
        
        String selectOldEmployees = """
            SELECT employee_num, last_name, first_name, birthday, address, phone_number,
                   sss, philhealth, tin, pagibig, status, position, immediate_supervisor,
                   basic_salary, rice_subsidy, phone_allowance, clothing_allowance,
                   gross_semi_monthly_rate, hourly_rate
            FROM employees_old
        """;
        
        try (PreparedStatement selectStmt = conn.prepareStatement(selectOldEmployees);
             ResultSet rs = selectStmt.executeQuery()) {
            
            while (rs.next()) {
                String employeeNum = rs.getString("employee_num");
                
                // Get corresponding user ID
                Integer userId = getUserIdForEmployee(conn, employeeNum);
                if (userId == null) {
                    System.err.println("No user found for employee: " + employeeNum);
                    continue;
                }
                
                // Create employee record
                int employeeId = createEmployeeRecord(conn, employeeNum, userId);
                
                // Create personal information
                createPersonalInfo(conn, employeeId, rs);
                
                // Create contact information
                createContactInfo(conn, employeeId, rs);
                
                // Create government IDs
                createGovIds(conn, employeeId, rs);
                
                // Create position assignment
                createPositionAssignment(conn, employeeId, rs.getString("position"), rs.getString("immediate_supervisor"));
                
                // Create compensation
                createCompensationRecord(conn, employeeId, rs);
                
                // Create allowances
                createAllowanceRecords(conn, employeeId, rs);
            }
        }
        
        System.out.println("Employees migration completed.");
    }
    
    /**
     * Migrate attendance records from old structure
     */
    private static void migrateAttendance(Connection conn) throws SQLException {
        System.out.println("Migrating attendance records...");
        
        if (!tableExists(conn, "attendance_old")) {
            System.out.println("No attendance_old table found, skipping attendance migration");
            return;
        }
        
        String selectOldAttendance = """
            SELECT employee_num, last_name, first_name, date, time_in, time_out
            FROM attendance_old
        """;
        
        try (PreparedStatement selectStmt = conn.prepareStatement(selectOldAttendance);
             ResultSet rs = selectStmt.executeQuery()) {
            
            String insertAttendance = """
                INSERT INTO attendance_records (employee_id, attendance_date, time_in, time_out, status)
                SELECT e.employee_id, ?::date, ?::time, ?::time, 
                       CASE 
                           WHEN ? IS NOT NULL AND ? IS NOT NULL THEN 'Present'
                           ELSE 'Absent'
                       END
                FROM employees e 
                WHERE e.employee_number = ?
                ON CONFLICT (employee_id, attendance_date) DO NOTHING
            """;
            
            try (PreparedStatement insertStmt = conn.prepareStatement(insertAttendance)) {
                while (rs.next()) {
                    String employeeNum = rs.getString("employee_num");
                    String date = rs.getString("date");
                    String timeIn = rs.getString("time_in");
                    String timeOut = rs.getString("time_out");
                    
                    // Convert date format from MM/dd/yyyy to yyyy-MM-dd
                    String convertedDate = convertDateFormat(date);
                    
                    if (convertedDate != null) {
                        insertStmt.setString(1, convertedDate);
                        insertStmt.setString(2, timeIn);
                        insertStmt.setString(3, timeOut);
                        insertStmt.setString(4, timeIn);
                        insertStmt.setString(5, timeOut);
                        insertStmt.setString(6, employeeNum);
                        insertStmt.executeUpdate();
                    }
                }
            }
        }
        
        System.out.println("Attendance migration completed.");
    }
    
    /**
     * Migrate leave requests from old structure
     */
    private static void migrateLeaveRequests(Connection conn) throws SQLException {
        System.out.println("Migrating leave requests...");
        
        if (!tableExists(conn, "leave_requests_old")) {
            System.out.println("No leave_requests_old table found, skipping leave requests migration");
            return;
        }
        
        String selectOldLeaveRequests = """
            SELECT id, employee_num, first_name, last_name, start_date, end_date,
                   notes, leave_type, approved
            FROM leave_requests_old
        """;
        
        try (PreparedStatement selectStmt = conn.prepareStatement(selectOldLeaveRequests);
             ResultSet rs = selectStmt.executeQuery()) {
            
            String insertLeaveRequest = """
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
            
            try (PreparedStatement insertStmt = conn.prepareStatement(insertLeaveRequest)) {
                while (rs.next()) {
                    String id = rs.getString("id");
                    String employeeNum = rs.getString("employee_num");
                    String startDate = rs.getString("start_date");
                    String endDate = rs.getString("end_date");
                    String notes = rs.getString("notes");
                    String leaveType = rs.getString("leave_type");
                    String approved = rs.getString("approved");
                    
                    insertStmt.setString(1, "LR-" + id);
                    insertStmt.setString(2, startDate);
                    insertStmt.setString(3, endDate);
                    insertStmt.setString(4, startDate);
                    insertStmt.setString(5, startDate);
                    insertStmt.setString(6, notes);
                    insertStmt.setString(7, approved);
                    insertStmt.setString(8, approved);
                    insertStmt.setString(9, leaveType != null ? leaveType : "Personal");
                    insertStmt.setString(10, employeeNum);
                    insertStmt.executeUpdate();
                }
            }
        }
        
        System.out.println("Leave requests migration completed.");
    }
    
    /**
     * Create default data that might be missing
     */
    private static void createDefaultData(Connection conn) throws SQLException {
        System.out.println("Creating default data...");
        
        // Create default department if positions don't have departments
        String createDefaultDept = """
            INSERT INTO departments (department_name, department_code, description)
            VALUES ('General', 'GEN', 'General department for unassigned positions')
            ON CONFLICT (department_name) DO NOTHING
        """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createDefaultDept);
        }
        
        // Create positions for existing position titles from old data
        if (tableExists(conn, "employees_old")) {
            String createPositions = """
                INSERT INTO positions (position_title, position_code, department_id, job_description)
                SELECT DISTINCT 
                    eo.position,
                    UPPER(SUBSTRING(eo.position, 1, 3)) || '_' || ROW_NUMBER() OVER(ORDER BY eo.position),
                    d.department_id,
                    'Migrated position'
                FROM employees_old eo
                CROSS JOIN departments d 
                WHERE d.department_name = 'General'
                AND eo.position IS NOT NULL 
                AND NOT EXISTS (
                    SELECT 1 FROM positions p WHERE p.position_title = eo.position
                )
                ON CONFLICT (position_code) DO NOTHING
            """;
            
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createPositions);
            }
        }
        
        System.out.println("Default data creation completed.");
    }
    
    // Helper methods
    
    private static boolean tableExists(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tableName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
    
    private static void assignRole(Connection conn, int userId, String roleName) throws SQLException {
        String sql = """
            INSERT INTO user_roles (user_id, role_id)
            SELECT ?, role_id FROM roles WHERE role_name = ?
            ON CONFLICT (user_id, role_id) DO NOTHING
        """;
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, roleName);
            stmt.executeUpdate();
        }
    }
    
    private static void createAdminRecord(Connection conn, int userId) throws SQLException {
        String sql = "INSERT INTO admins (user_id, admin_level, permissions) VALUES (?, 1, 'BASIC')";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
    
    private static void createHRRecord(Connection conn, int userId) throws SQLException {
        String sql = "INSERT INTO hr_personnel (user_id, hr_level) VALUES (?, 'Junior')";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
    
    private static void storeUserMapping(Connection conn, Map<String, Integer> mapping) throws SQLException {
        // Create temporary table to store mapping
        String createTempTable = """
            CREATE TEMP TABLE user_mapping (
                employee_num VARCHAR(20),
                user_id INTEGER
            )
        """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTempTable);
        }
        
        String insertMapping = "INSERT INTO user_mapping (employee_num, user_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertMapping)) {
            for (Map.Entry<String, Integer> entry : mapping.entrySet()) {
                stmt.setString(1, entry.getKey());
                stmt.setInt(2, entry.getValue());
                stmt.executeUpdate();
            }
        }
    }
    
    private static Integer getUserIdForEmployee(Connection conn, String employeeNum) throws SQLException {
        String sql = "SELECT user_id FROM user_mapping WHERE employee_num = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employeeNum);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        }
        return null;
    }
    
    private static int createEmployeeRecord(Connection conn, String employeeNum, int userId) throws SQLException {
        String sql = """
            INSERT INTO employees (employee_number, user_id, hire_date, employment_type)
            VALUES (?, ?, CURRENT_DATE, 'Full-time')
        """;
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, employeeNum);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to create employee record");
    }
    
    private static void createPersonalInfo(Connection conn, int employeeId, ResultSet rs) throws SQLException {
        String sql = """
            INSERT INTO personal_information (employee_id, first_name, last_name, birthday, nationality)
            VALUES (?, ?, ?, ?::date, 'Filipino')
        """;
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            stmt.setString(2, rs.getString("first_name"));
            stmt.setString(3, rs.getString("last_name"));
            stmt.setString(4, rs.getString("birthday"));
            stmt.executeUpdate();
        }
    }
    
    private static void createContactInfo(Connection conn, int employeeId, ResultSet rs) throws SQLException {
        String sql = """
            INSERT INTO contact_information (employee_id, home_address, phone_number, mobile_number)
            VALUES (?, ?, ?, ?)
        """;
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            stmt.setString(2, rs.getString("address"));
            stmt.setString(3, rs.getString("phone_number"));
            stmt.setString(4, rs.getString("phone_number"));
            stmt.executeUpdate();
        }
    }
    
    private static void createGovIds(Connection conn, int employeeId, ResultSet rs) throws SQLException {
        String sql = "INSERT INTO government_ids (employee_id, id_type, id_number) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // SSS
            String sss = rs.getString("sss");
            if (sss != null && !sss.trim().isEmpty()) {
                stmt.setInt(1, employeeId);
                stmt.setString(2, "SSS");
                stmt.setString(3, sss);
                stmt.executeUpdate();
            }
            
            // PhilHealth
            String philhealth = rs.getString("philhealth");
            if (philhealth != null && !philhealth.trim().isEmpty()) {
                stmt.setInt(1, employeeId);
                stmt.setString(2, "PHILHEALTH");
                stmt.setString(3, philhealth);
                stmt.executeUpdate();
            }
            
            // TIN
            String tin = rs.getString("tin");
            if (tin != null && !tin.trim().isEmpty()) {
                stmt.setInt(1, employeeId);
                stmt.setString(2, "TIN");
                stmt.setString(3, tin);
                stmt.executeUpdate();
            }
            
            // Pag-IBIG
            String pagibig = rs.getString("pagibig");
            if (pagibig != null && !pagibig.trim().isEmpty()) {
                stmt.setInt(1, employeeId);
                stmt.setString(2, "PAGIBIG");
                stmt.setString(3, pagibig);
                stmt.executeUpdate();
            }
        }
    }
    
    private static void createPositionAssignment(Connection conn, int employeeId, String positionTitle, String supervisor) throws SQLException {
        if (positionTitle == null || positionTitle.trim().isEmpty()) {
            return;
        }
        
        String sql = """
            INSERT INTO employee_positions (employee_id, position_id, start_date, is_current, status)
            SELECT ?, p.position_id, CURRENT_DATE, TRUE, 'Active'
            FROM positions p
            WHERE p.position_title = ?
        """;
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            stmt.setString(2, positionTitle);
            stmt.executeUpdate();
        }
    }
    
    private static void createCompensationRecord(Connection conn, int employeeId, ResultSet rs) throws SQLException {
        String sql = """
            INSERT INTO employee_compensation (employee_id, basic_salary, hourly_rate, gross_semi_monthly_rate, effective_date, is_current)
            VALUES (?, ?, ?, ?, CURRENT_DATE, TRUE)
        """;
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            stmt.setDouble(2, rs.getDouble("basic_salary"));
            stmt.setDouble(3, rs.getDouble("hourly_rate"));
            stmt.setDouble(4, rs.getDouble("gross_semi_monthly_rate"));
            stmt.executeUpdate();
        }
    }
    
    private static void createAllowanceRecords(Connection conn, int employeeId, ResultSet rs) throws SQLException {
        String sql = """
            INSERT INTO employee_allowances (employee_id, allowance_type_id, amount, effective_date, is_active)
            SELECT ?, at.allowance_type_id, ?, CURRENT_DATE, TRUE
            FROM allowance_types at
            WHERE at.allowance_name = ?
        """;
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Rice Subsidy
            double riceSubsidy = rs.getDouble("rice_subsidy");
            if (riceSubsidy > 0) {
                stmt.setInt(1, employeeId);
                stmt.setDouble(2, riceSubsidy);
                stmt.setString(3, "Rice Subsidy");
                stmt.executeUpdate();
            }
            
            // Phone Allowance
            double phoneAllowance = rs.getDouble("phone_allowance");
            if (phoneAllowance > 0) {
                stmt.setInt(1, employeeId);
                stmt.setDouble(2, phoneAllowance);
                stmt.setString(3, "Phone Allowance");
                stmt.executeUpdate();
            }
            
            // Clothing Allowance
            double clothingAllowance = rs.getDouble("clothing_allowance");
            if (clothingAllowance > 0) {
                stmt.setInt(1, employeeId);
                stmt.setDouble(2, clothingAllowance);
                stmt.setString(3, "Clothing Allowance");
                stmt.executeUpdate();
            }
        }
    }
    
    private static String convertDateFormat(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Convert from MM/dd/yyyy to yyyy-MM-dd
            String[] parts = dateStr.split("/");
            if (parts.length == 3) {
                String month = parts[0].length() == 1 ? "0" + parts[0] : parts[0];
                String day = parts[1].length() == 1 ? "0" + parts[1] : parts[1];
                String year = parts[2];
                return year + "-" + month + "-" + day;
            }
        } catch (Exception e) {
            System.err.println("Error converting date: " + dateStr + " - " + e.getMessage());
        }
        
        return dateStr; // Return as-is if conversion fails
    }
    
    /**
     * Cleanup method to remove old tables after successful migration
     */
    public static void cleanupOldTables() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String[] oldTables = {"users_old", "employees_old", "attendance_old", "leave_requests_old"};
            
            for (String table : oldTables) {
                String sql = "DROP TABLE IF EXISTS " + table;
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(sql);
                    System.out.println("Dropped old table: " + table);
                }
            }
            
            System.out.println("Cleanup completed successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error during cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Verify migration by comparing record counts
     */
    public static void verifyMigration() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Verifying migration...");
            
            // Check users
            verifyCount(conn, "users_old", "users", "Users");
            
            // Check employees
            verifyCount(conn, "employees_old", "employees", "Employees");
            
            // Check attendance (approximate due to structure change)
            verifyCount(conn, "attendance_old", "attendance_records", "Attendance Records");
            
            // Check leave requests
            verifyCount(conn, "leave_requests_old", "leave_requests", "Leave Requests");
            
            System.out.println("Migration verification completed!");
            
        } catch (SQLException e) {
            System.err.println("Error during verification: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void verifyCount(Connection conn, String oldTable, String newTable, String entityName) throws SQLException {
        String countOldSql = "SELECT COUNT(*) FROM " + oldTable;
        String countNewSql = "SELECT COUNT(*) FROM " + newTable;
        
        int oldCount = 0, newCount = 0;
        
        try (Statement stmt = conn.createStatement()) {
            try {
                ResultSet rs = stmt.executeQuery(countOldSql);
                if (rs.next()) {
                    oldCount = rs.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println("Old table " + oldTable + " doesn't exist, skipping verification");
                return;
            }
            
            ResultSet rs = stmt.executeQuery(countNewSql);
            if (rs.next()) {
                newCount = rs.getInt(1);
            }
        }
        
        System.out.println(entityName + ": Old=" + oldCount + ", New=" + newCount + 
                          (oldCount == newCount ? " ✓" : " ⚠"));
    }
}
