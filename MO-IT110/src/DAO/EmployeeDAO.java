package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Classes.Compensation;
import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
import Database.DatabaseConnection;

public class EmployeeDAO {
    
    /**
     * Get complete employee information by employee number
     * @param employeeNum Employee number
     * @return EmployeeInformation object if found, null otherwise
     */
    public static EmployeeInformation getEmployeeByNumber(String employeeNum) {
        String sql = """
            SELECT e.employee_id, e.employee_number, e.hire_date, e.employment_type, e.is_active,
                   u.username, u.email,
                   pi.first_name, pi.last_name, pi.middle_name, pi.birthday, pi.gender, pi.civil_status, pi.nationality,
                   ci.home_address, ci.city, ci.province, ci.postal_code, ci.phone_number, ci.mobile_number,
                   ci.emergency_contact_name, ci.emergency_contact_number,
                   p.position_title, d.department_name,
                   ep.supervisor_id, ep.status as position_status
            FROM employees e
            JOIN users u ON e.user_id = u.user_id
            LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id
            LEFT JOIN contact_information ci ON e.employee_id = ci.employee_id
            LEFT JOIN employee_positions ep ON e.employee_id = ep.employee_id AND ep.is_current = TRUE
            LEFT JOIN positions p ON ep.position_id = p.position_id
            LEFT JOIN departments d ON p.department_id = d.department_id
            WHERE e.employee_number = ? AND e.is_active = TRUE
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNum);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting employee: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get all active employees with their basic information
     * @return List of all employees
     */
    public static List<EmployeeInformation> getAllEmployees() {
        List<EmployeeInformation> employees = new ArrayList<>();
        String sql = """
            SELECT e.employee_id, e.employee_number, e.hire_date, e.employment_type, e.is_active,
                   u.username, u.email,
                   pi.first_name, pi.last_name, pi.middle_name, pi.birthday, pi.gender, pi.civil_status, pi.nationality,
                   ci.home_address, ci.city, ci.province, ci.postal_code, ci.phone_number, ci.mobile_number,
                   p.position_title, d.department_name,
                   ep.status as position_status
            FROM employees e
            JOIN users u ON e.user_id = u.user_id
            LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id
            LEFT JOIN contact_information ci ON e.employee_id = ci.employee_id
            LEFT JOIN employee_positions ep ON e.employee_id = ep.employee_id AND ep.is_current = TRUE
            LEFT JOIN positions p ON ep.position_id = p.position_id
            LEFT JOIN departments d ON p.department_id = d.department_id
            WHERE e.is_active = TRUE
            ORDER BY e.employee_number
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all employees: " + e.getMessage());
            e.printStackTrace();
        }
        return employees;
    }
    
    /**
     * Get employee compensation information by employee number
     * @param employeeNum Employee number
     * @return Compensation object if found, null otherwise
     */
    public static Compensation getEmployeeCompensation(String employeeNum) {
        String sql = """
            SELECT e.employee_id, e.employee_number,
                   pi.first_name, pi.last_name,
                   p.position_title, ep.status,
                   ec.basic_salary, ec.hourly_rate, ec.gross_semi_monthly_rate,
                   sg.grade_name, sg.grade_level
            FROM employees e
            LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id
            LEFT JOIN employee_positions ep ON e.employee_id = ep.employee_id AND ep.is_current = TRUE
            LEFT JOIN positions p ON ep.position_id = p.position_id
            LEFT JOIN employee_compensation ec ON e.employee_id = ec.employee_id AND ec.is_current = TRUE
            LEFT JOIN salary_grades sg ON ec.salary_grade_id = sg.grade_id
            WHERE e.employee_number = ? AND e.is_active = TRUE
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNum);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Compensation compensation = new Compensation(employeeNum);
                    
                    // Set basic employee info
                    compensation.setFirstName(rs.getString("first_name"));
                    compensation.setLastName(rs.getString("last_name"));
                    compensation.setPosition(rs.getString("position_title"));
                    compensation.setStatus(rs.getString("status"));
                    
                    // Set compensation data
                    compensation.setBasicSalary(rs.getDouble("basic_salary"));
                    compensation.setHourlyRate(rs.getDouble("hourly_rate"));
                    compensation.setGrossSemiMonthlyRate(rs.getDouble("gross_semi_monthly_rate"));
                    
                    // Get allowances for this employee
                    setEmployeeAllowances(compensation, employeeNum);
                    
                    return compensation;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting employee compensation: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get employee government identification by employee number
     * @param employeeNum Employee number
     * @return GovernmentIdentification object if found, null otherwise
     */
    public static GovernmentIdentification getEmployeeGovId(String employeeNum) {
        EmployeeInformation employee = getEmployeeByNumber(employeeNum);
        if (employee == null) {
            return null;
        }
        
        GovernmentIdentification govId = new GovernmentIdentification(employeeNum);
        
        // Copy basic employee information
        govId.setFirstName(employee.getFirstName());
        govId.setLastName(employee.getLastName());
        govId.setBirthday(employee.getBirthday());
        govId.setAddress(employee.getAddress());
        govId.setPhoneNumber(employee.getPhoneNumber());
        govId.setPosition(employee.getPosition());
        govId.setStatus(employee.getStatus());
        govId.setSupervisor(employee.getSupervisor());
        
        // Get government IDs
        String sql = """
            SELECT gi.id_type, gi.id_number
            FROM employees e
            JOIN government_ids gi ON e.employee_id = gi.employee_id
            WHERE e.employee_number = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNum);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String idType = rs.getString("id_type");
                    String idNumber = rs.getString("id_number");
                    
                    switch (idType.toUpperCase()) {
                        case "SSS":
                            govId.setSSSNumber(idNumber);
                            break;
                        case "PHILHEALTH":
                            govId.setPhilHealthNumber(idNumber);
                            break;
                        case "PAGIBIG":
                        case "PAG-IBIG":
                            govId.setPagibigNumber(idNumber);
                            break;
                        case "TIN":
                            govId.setTinNumber(idNumber);
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting employee government IDs: " + e.getMessage());
            e.printStackTrace();
        }
        
        return govId;
    }
    
    /**
     * Create a new employee with complete information
     * @param employee EmployeeInformation object
     * @param govId GovernmentIdentification object
     * @param compensation Compensation object
     * @param username Username for login
     * @param password Password for login
     * @param positionTitle Position title
     * @param departmentName Department name
     * @return true if creation successful, false otherwise
     */
    public static boolean createEmployee(EmployeeInformation employee, GovernmentIdentification govId, 
                                          Compensation compensation, String username, String password,
                                          String positionTitle, String departmentName) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // 1. Create user record
            int userId = createUserRecord(conn, username, password, employee.getFirstName() + "@motorph.com");
            
            // 2. Create employee record
            int employeeId = createEmployeeRecord(conn, employee.getEmployeeNumber(), userId);
            
            // 3. Create personal information
            createPersonalInformation(conn, employeeId, employee);
            
            // 4. Create contact information
            createContactInformation(conn, employeeId, employee);
            
            // 5. Create government IDs
            createGovernmentIds(conn, employeeId, govId);
            
            // 6. Assign position
            assignPosition(conn, employeeId, positionTitle, departmentName);
            
            // 7. Set compensation
            setCompensation(conn, employeeId, compensation);
            
            // 8. Set allowances
            setAllowances(conn, employeeId, compensation);
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error creating employee: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Update employee information
     * @param employee EmployeeInformation object with updated information
     * @param govId GovernmentIdentification object with updated information
     * @param compensation Compensation object with updated information
     * @return true if update successful, false otherwise
     */
    public static boolean updateEmployee(EmployeeInformation employee, GovernmentIdentification govId, 
                                          Compensation compensation) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Get employee ID using the same connection
            int employeeId = getEmployeeIdByNumber(conn, employee.getEmployeeNumber());
            if (employeeId == -1) {
                System.err.println("Employee not found: " + employee.getEmployeeNumber());
                return false;
            }
            
            // Update personal information
            updatePersonalInformation(conn, employeeId, employee);
            
            // Update contact information
            updateContactInformation(conn, employeeId, employee);
            
            // Update government IDs
            updateGovernmentIds(conn, employeeId, govId);
            
            // Update compensation
            updateCompensation(conn, employeeId, compensation);
            
            // Update allowances
            updateAllowances(conn, employeeId, compensation);
            
            conn.commit();
            System.out.println("Employee updated successfully: " + employee.getEmployeeNumber());
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error during rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Search employees by name
     * @param searchTerm Search term for name
     * @return List of matching employees
     */
    public static List<EmployeeInformation> searchEmployeesByName(String searchTerm) {
        List<EmployeeInformation> employees = new ArrayList<>();
        String sql = """
            SELECT e.employee_id, e.employee_number, e.hire_date, e.employment_type, e.is_active,
                   u.username, u.email,
                   pi.first_name, pi.last_name, pi.middle_name, pi.birthday, pi.gender, pi.civil_status, pi.nationality,
                   ci.home_address, ci.city, ci.province, ci.postal_code, ci.phone_number, ci.mobile_number,
                   p.position_title, d.department_name,
                   ep.status as position_status
            FROM employees e
            JOIN users u ON e.user_id = u.user_id
            LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id
            LEFT JOIN contact_information ci ON e.employee_id = ci.employee_id
            LEFT JOIN employee_positions ep ON e.employee_id = ep.employee_id AND ep.is_current = TRUE
            LEFT JOIN positions p ON ep.position_id = p.position_id
            LEFT JOIN departments d ON p.department_id = d.department_id
            WHERE e.is_active = TRUE
            AND (pi.last_name ILIKE ? OR pi.first_name ILIKE ? 
                 OR CONCAT(pi.first_name, ' ', pi.last_name) ILIKE ?
                 OR CONCAT(pi.last_name, ' ', pi.first_name) ILIKE ?)
            ORDER BY pi.last_name, pi.first_name
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching employees by name: " + e.getMessage());
            e.printStackTrace();
        }
        return employees;
    }
    
    /**
     * Delete employee (soft delete)
     * @param employeeNum Employee number
     * @return true if deletion successful, false otherwise
     */
    public static boolean deleteEmployee(String employeeNum) {
        String sql = """
            UPDATE employees SET is_active = FALSE, termination_date = CURRENT_DATE, updated_at = CURRENT_TIMESTAMP 
            WHERE employee_number = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNum);
            
            int rowsAffected = pstmt.executeUpdate();
            
            // Also deactivate the user
            if (rowsAffected > 0) {
                UserDAO.deleteUser(employeeNum);
            }
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Helper methods
    
    private static EmployeeInformation mapResultSetToEmployee(ResultSet rs) throws SQLException {
        EmployeeInformation employee = new EmployeeInformation(rs.getString("employee_number"));
        
        // Basic employee info
        employee.setUserId(rs.getString("username"));
        
        // Personal information
        employee.setFirstName(rs.getString("first_name"));
        employee.setLastName(rs.getString("last_name"));
        employee.setBirthday(rs.getDate("birthday") != null ? rs.getDate("birthday").toString() : null);
        
        // Contact information
        employee.setAddress(rs.getString("home_address"));
        employee.setPhoneNumber(rs.getString("phone_number"));
        
        // Position information
        employee.setPosition(rs.getString("position_title"));
        employee.setStatus(rs.getString("position_status"));
        
        return employee;
    }
    
    private static void setEmployeeAllowances(Compensation compensation, String employeeNum) {
        String sql = """
            SELECT at.allowance_name, ea.amount
            FROM employees e
            JOIN employee_allowances ea ON e.employee_id = ea.employee_id
            JOIN allowance_types at ON ea.allowance_type_id = at.allowance_type_id
            WHERE e.employee_number = ? AND ea.is_active = TRUE
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNum);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String allowanceName = rs.getString("allowance_name");
                    double amount = rs.getDouble("amount");
                    
                    switch (allowanceName.toUpperCase()) {
                        case "RICE SUBSIDY":
                            compensation.setRiceSubsidy(amount);
                            break;
                        case "PHONE ALLOWANCE":
                            compensation.setPhoneAllowance(amount);
                            break;
                        case "CLOTHING ALLOWANCE":
                            compensation.setClothingAllowance(amount);
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting employee allowances: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static int getEmployeeIdByNumber(String employeeNumber) {
        String sql = "SELECT employee_id FROM employees WHERE employee_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("employee_id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting employee ID: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    
    private static int getEmployeeIdByNumber(Connection conn, String employeeNumber) throws SQLException {
        String sql = "SELECT employee_id FROM employees WHERE employee_number = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("employee_id");
                }
            }
        }
        return -1;
    }
    
    private static int createUserRecord(Connection conn, String username, String password, String email) throws SQLException {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int userId = rs.getInt(1);
                    
                    // Assign EMPLOYEE role
                    String roleAssignSql = """
                        INSERT INTO user_roles (user_id, role_id) 
                        SELECT ?, role_id FROM roles WHERE role_name = 'EMPLOYEE'
                    """;
                    try (PreparedStatement roleStmt = conn.prepareStatement(roleAssignSql)) {
                        roleStmt.setInt(1, userId);
                        roleStmt.executeUpdate();
                    }
                    
                    return userId;
                }
            }
        }
        throw new SQLException("Failed to create user record");
    }
    
    private static int createEmployeeRecord(Connection conn, String employeeNumber, int userId) throws SQLException {
        String sql = "INSERT INTO employees (employee_number, user_id, hire_date) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, employeeNumber);
            pstmt.setInt(2, userId);
            pstmt.setDate(3, Date.valueOf(LocalDate.now()));
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to create employee record");
    }
    
    private static void createPersonalInformation(Connection conn, int employeeId, EmployeeInformation employee) throws SQLException {
        String sql = """
            INSERT INTO personal_information (employee_id, first_name, last_name, middle_name, birthday) 
            VALUES (?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, employee.getFirstName());
            pstmt.setString(3, employee.getLastName());
            pstmt.setString(4, ""); // Middle name - add to EmployeeInformation class if needed
            pstmt.setDate(5, employee.getBirthday() != null ? Date.valueOf(employee.getBirthday()) : null);
            pstmt.executeUpdate();
        }
    }
    
    private static void createContactInformation(Connection conn, int employeeId, EmployeeInformation employee) throws SQLException {
        String sql = """
            INSERT INTO contact_information (employee_id, home_address, phone_number, mobile_number) 
            VALUES (?, ?, ?, ?)
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, employee.getAddress());
            pstmt.setString(3, employee.getPhoneNumber());
            pstmt.setString(4, employee.getPhoneNumber()); // Assuming same for mobile
            pstmt.executeUpdate();
        }
    }
    
    private static void createGovernmentIds(Connection conn, int employeeId, GovernmentIdentification govId) throws SQLException {
        String sql = "INSERT INTO government_ids (employee_id, id_type, id_number) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // SSS
            if (govId.getSSSNumber() != null && !govId.getSSSNumber().isEmpty()) {
                pstmt.setInt(1, employeeId);
                pstmt.setString(2, "SSS");
                pstmt.setString(3, govId.getSSSNumber());
                pstmt.executeUpdate();
            }
            
            // PhilHealth
            if (govId.getPhilHealthNumber() != null && !govId.getPhilHealthNumber().isEmpty()) {
                pstmt.setInt(1, employeeId);
                pstmt.setString(2, "PHILHEALTH");
                pstmt.setString(3, govId.getPhilHealthNumber());
                pstmt.executeUpdate();
            }
            
            // TIN
            if (govId.getTinNumber() != null && !govId.getTinNumber().isEmpty()) {
                pstmt.setInt(1, employeeId);
                pstmt.setString(2, "TIN");
                pstmt.setString(3, govId.getTinNumber());
                pstmt.executeUpdate();
            }
            
            // Pag-IBIG
            if (govId.getPagibigNumber() != null && !govId.getPagibigNumber().isEmpty()) {
                pstmt.setInt(1, employeeId);
                pstmt.setString(2, "PAGIBIG");
                pstmt.setString(3, govId.getPagibigNumber());
                pstmt.executeUpdate();
            }
        }
    }
    
    private static void assignPosition(Connection conn, int employeeId, String positionTitle, String departmentName) throws SQLException {
        // First get position ID
        String getPositionSql = """
            SELECT p.position_id FROM positions p 
            JOIN departments d ON p.department_id = d.department_id 
            WHERE p.position_title = ? AND d.department_name = ?
        """;
        
        int positionId = -1;
        try (PreparedStatement pstmt = conn.prepareStatement(getPositionSql)) {
            pstmt.setString(1, positionTitle);
            pstmt.setString(2, departmentName);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    positionId = rs.getInt("position_id");
                }
            }
        }
        
        if (positionId != -1) {
            String assignSql = """
                INSERT INTO employee_positions (employee_id, position_id, start_date, is_current) 
                VALUES (?, ?, CURRENT_DATE, TRUE)
            """;
            
            try (PreparedStatement pstmt = conn.prepareStatement(assignSql)) {
                pstmt.setInt(1, employeeId);
                pstmt.setInt(2, positionId);
                pstmt.executeUpdate();
            }
        }
    }
    
    private static void setCompensation(Connection conn, int employeeId, Compensation compensation) throws SQLException {
        String sql = """
            INSERT INTO employee_compensation (employee_id, basic_salary, hourly_rate, gross_semi_monthly_rate, effective_date, is_current) 
            VALUES (?, ?, ?, ?, CURRENT_DATE, TRUE)
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setDouble(2, compensation.getBasicSalary());
            pstmt.setDouble(3, compensation.getHourlyRate());
            pstmt.setDouble(4, compensation.getGrossSemiMonthlyRate());
            pstmt.executeUpdate();
        }
    }
    
    private static void setAllowances(Connection conn, int employeeId, Compensation compensation) throws SQLException {
        String sql = """
            INSERT INTO employee_allowances (employee_id, allowance_type_id, amount, effective_date, is_active) 
            SELECT ?, allowance_type_id, ?, CURRENT_DATE, TRUE 
            FROM allowance_types WHERE allowance_name = ?
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Rice Subsidy
            if (compensation.getRiceSubsidy() > 0) {
                pstmt.setInt(1, employeeId);
                pstmt.setDouble(2, compensation.getRiceSubsidy());
                pstmt.setString(3, "Rice Subsidy");
                pstmt.executeUpdate();
            }
            
            // Phone Allowance
            if (compensation.getPhoneAllowance() > 0) {
                pstmt.setInt(1, employeeId);
                pstmt.setDouble(2, compensation.getPhoneAllowance());
                pstmt.setString(3, "Phone Allowance");
                pstmt.executeUpdate();
            }
            
            // Clothing Allowance
            if (compensation.getClothingAllowance() > 0) {
                pstmt.setInt(1, employeeId);
                pstmt.setDouble(2, compensation.getClothingAllowance());
                pstmt.setString(3, "Clothing Allowance");
                pstmt.executeUpdate();
            }
        }
    }
    
    private static void updatePersonalInformation(Connection conn, int employeeId, EmployeeInformation employee) throws SQLException {
        String sql = """
            UPDATE personal_information SET 
                first_name = ?, last_name = ?, birthday = ?, updated_at = CURRENT_TIMESTAMP
            WHERE employee_id = ?
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getFirstName());
            pstmt.setString(2, employee.getLastName());
            pstmt.setDate(3, employee.getBirthday() != null ? Date.valueOf(employee.getBirthday()) : null);
            pstmt.setInt(4, employeeId);
            pstmt.executeUpdate();
        }
    }
    
    private static void updateContactInformation(Connection conn, int employeeId, EmployeeInformation employee) throws SQLException {
        String sql = """
            UPDATE contact_information SET 
                home_address = ?, phone_number = ?, mobile_number = ?, updated_at = CURRENT_TIMESTAMP
            WHERE employee_id = ?
        """;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getAddress());
            pstmt.setString(2, employee.getPhoneNumber());
            pstmt.setString(3, employee.getPhoneNumber());
            pstmt.setInt(4, employeeId);
            pstmt.executeUpdate();
        }
    }
    
    private static void updateGovernmentIds(Connection conn, int employeeId, GovernmentIdentification govId) throws SQLException {
        // Delete existing IDs and insert new ones
        String deleteSql = "DELETE FROM government_ids WHERE employee_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
        }
        
        // Create new IDs
        createGovernmentIds(conn, employeeId, govId);
    }
    
    private static void updateCompensation(Connection conn, int employeeId, Compensation compensation) throws SQLException {
        // Set current compensation to false
        String updateCurrentSql = "UPDATE employee_compensation SET is_current = FALSE WHERE employee_id = ? AND is_current = TRUE";
        try (PreparedStatement pstmt = conn.prepareStatement(updateCurrentSql)) {
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
        }
        
        // Insert new compensation
        setCompensation(conn, employeeId, compensation);
    }
    
    private static void updateAllowances(Connection conn, int employeeId, Compensation compensation) throws SQLException {
        // Set current allowances to false
        String updateCurrentSql = "UPDATE employee_allowances SET is_active = FALSE WHERE employee_id = ? AND is_active = TRUE";
        try (PreparedStatement pstmt = conn.prepareStatement(updateCurrentSql)) {
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
        }
        
        // Insert new allowances
        setAllowances(conn, employeeId, compensation);
    }
}