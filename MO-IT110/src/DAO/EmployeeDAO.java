package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Classes.Compensation;
import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
import Database.DatabaseConnection;

public class EmployeeDAO {
    
    /**
     * Get employee by employee number
     * @param employeeNum Employee number
     * @return EmployeeInformation object if found, null otherwise
     */
    public static EmployeeInformation getEmployeeByNumber(String employeeNum) {
        String sql = """
            SELECT e.*, u.username
            FROM employees e
            JOIN users u ON e.employee_num = u.employee_num
            WHERE e.employee_num = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(employeeNum));
            
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
     * Get all employees
     * @return List of all employees
     */
    public static List<EmployeeInformation> getAllEmployees() {
        List<EmployeeInformation> employees = new ArrayList<>();
        String sql = """
            SELECT e.*, u.username
            FROM employees e
            JOIN users u ON e.employee_num = u.employee_num
            ORDER BY e.employee_num
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
        EmployeeInformation employee = getEmployeeByNumber(employeeNum);
        if (employee != null) {
            Compensation compensation = new Compensation(employeeNum);
            compensation.setBasicSalary(getDoubleEmployeeField(employeeNum, "basic_salary"));
            compensation.setRiceSubsidy(getDoubleEmployeeField(employeeNum, "rice_subsidy"));
            compensation.setPhoneAllowance(getDoubleEmployeeField(employeeNum, "phone_allowance"));
            compensation.setClothingAllowance(getDoubleEmployeeField(employeeNum, "clothing_allowance"));
            compensation.setGrossSemiMonthlyRate(getDoubleEmployeeField(employeeNum, "gross_semi_monthly_rate"));
            compensation.setHourlyRate(getDoubleEmployeeField(employeeNum, "hourly_rate"));
            
            // Set employee info fields
            compensation.setLastName(employee.getLastName());
            compensation.setFirstName(employee.getFirstName());
            compensation.setPosition(employee.getPosition());
            compensation.setStatus(employee.getStatus());
            
            return compensation;
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
        if (employee != null) {
            GovernmentIdentification govId = new GovernmentIdentification(employeeNum);
            govId.setSSSNumber(getStringEmployeeField(employeeNum, "sss"));
            govId.setPhilHealthNumber(String.valueOf(getLongEmployeeField(employeeNum, "philhealth")));
            govId.setTinNumber(getStringEmployeeField(employeeNum, "tin"));
            govId.setPagibigNumber(String.valueOf(getLongEmployeeField(employeeNum, "pagibig")));
            
            // Set employee info fields
            govId.setLastName(employee.getLastName());
            govId.setFirstName(employee.getFirstName());
            govId.setBirthday(employee.getBirthday());
            govId.setAddress(employee.getAddress());
            govId.setPhoneNumber(employee.getPhoneNumber());
            govId.setStatus(employee.getStatus());
            govId.setPosition(employee.getPosition());
            govId.setSupervisor(employee.getSupervisor());
            
            return govId;
        }
        return null;
    }
    
    /**
     * Create a new employee
     * @param employee EmployeeInformation object
     * @param govId GovernmentIdentification object
     * @param compensation Compensation object
     * @param username Username for login
     * @param password Password for login
     * @return true if creation successful, false otherwise
     */
    public static boolean createEmployee(EmployeeInformation employee, GovernmentIdentification govId, 
                                          Compensation compensation, String username, String password) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // First create user entry
            String userSql = "INSERT INTO users (employee_num, username, password, is_admin) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(userSql)) {
                pstmt.setInt(1, Integer.parseInt(employee.getEmployeeNumber()));
                pstmt.setString(2, username);
                pstmt.setString(3, password);
                pstmt.setBoolean(4, false);
                pstmt.executeUpdate();
            }
            
            // Then create employee entry
            String empSql = """
                INSERT INTO employees (
                    employee_num, last_name, first_name, birthday, address, phone_number, 
                    sss, philhealth, tin, pagibig, status, position, immediate_supervisor,
                    basic_salary, rice_subsidy, phone_allowance, clothing_allowance,
                    gross_semi_monthly_rate, hourly_rate
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
            
            try (PreparedStatement pstmt = conn.prepareStatement(empSql)) {
                pstmt.setInt(1, Integer.parseInt(employee.getEmployeeNumber()));
                pstmt.setString(2, employee.getLastName());
                pstmt.setString(3, employee.getFirstName());
                pstmt.setString(4, employee.getBirthday());
                pstmt.setString(5, employee.getAddress());
                pstmt.setString(6, employee.getPhoneNumber());
                pstmt.setString(7, govId.getSSSNumber());
                pstmt.setLong(8, Long.parseLong(govId.getPhilHealthNumber()));
                pstmt.setString(9, govId.getTinNumber());
                pstmt.setLong(10, Long.parseLong(govId.getPagibigNumber()));
                pstmt.setString(11, employee.getStatus());
                pstmt.setString(12, employee.getPosition());
                pstmt.setString(13, employee.getSupervisor());
                pstmt.setDouble(14, compensation.getBasicSalary());
                pstmt.setDouble(15, compensation.getRiceSubsidy());
                pstmt.setDouble(16, compensation.getPhoneAllowance());
                pstmt.setDouble(17, compensation.getClothingAllowance());
                pstmt.setDouble(18, compensation.getGrossSemiMonthlyRate());
                pstmt.setDouble(19, compensation.getHourlyRate());
                
                pstmt.executeUpdate();
            }
            
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
                } catch (SQLException e) {
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
        String sql = """
            UPDATE employees SET 
                last_name = ?, first_name = ?, birthday = ?, address = ?, 
                phone_number = ?, sss = ?, philhealth = ?, tin = ?, pagibig = ?, 
                status = ?, position = ?, immediate_supervisor = ?,
                basic_salary = ?, rice_subsidy = ?, phone_allowance = ?, 
                clothing_allowance = ?, gross_semi_monthly_rate = ?, hourly_rate = ?,
                updated_at = CURRENT_TIMESTAMP
            WHERE employee_num = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employee.getLastName());
            pstmt.setString(2, employee.getFirstName());
            pstmt.setString(3, employee.getBirthday());
            pstmt.setString(4, employee.getAddress());
            pstmt.setString(5, employee.getPhoneNumber());
            pstmt.setString(6, govId.getSSSNumber());
            pstmt.setLong(7, Long.parseLong(govId.getPhilHealthNumber()));
            pstmt.setString(8, govId.getTinNumber());
            pstmt.setLong(9, Long.parseLong(govId.getPagibigNumber()));
            pstmt.setString(10, employee.getStatus());
            pstmt.setString(11, employee.getPosition());
            pstmt.setString(12, employee.getSupervisor());
            pstmt.setDouble(13, compensation.getBasicSalary());
            pstmt.setDouble(14, compensation.getRiceSubsidy());
            pstmt.setDouble(15, compensation.getPhoneAllowance());
            pstmt.setDouble(16, compensation.getClothingAllowance());
            pstmt.setDouble(17, compensation.getGrossSemiMonthlyRate());
            pstmt.setDouble(18, compensation.getHourlyRate());
            pstmt.setInt(19, Integer.parseInt(employee.getEmployeeNumber()));
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete employee by employee number
     * @param employeeNum Employee number
     * @return true if deletion successful, false otherwise
     */
    public static boolean deleteEmployee(String employeeNum) {
        // We don't need to delete from employees table explicitly
        // because of the CASCADE constraint in the database
        return UserDAO.deleteUser(employeeNum);
    }
    
    /**
     * Search employees by name
     * @param searchTerm Search term for name
     * @return List of matching employees
     */
    public static List<EmployeeInformation> searchEmployeesByName(String searchTerm) {
        List<EmployeeInformation> employees = new ArrayList<>();
        String sql = """
            SELECT e.*, u.username
            FROM employees e
            JOIN users u ON e.employee_num = u.employee_num
            WHERE e.last_name ILIKE ? OR e.first_name ILIKE ? 
               OR CONCAT(e.first_name, ' ', e.last_name) ILIKE ?
               OR CONCAT(e.last_name, ' ', e.first_name) ILIKE ?
            ORDER BY e.last_name, e.first_name
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
     * Search employees by name or position
     * @param searchTerm Search term
     * @return List of matching employees
     */
    public static List<EmployeeInformation> searchEmployees(String searchTerm) {
        List<EmployeeInformation> employees = new ArrayList<>();
        String sql = """
            SELECT e.*, u.username
            FROM employees e
            JOIN users u ON e.employee_num = u.employee_num
            WHERE e.last_name ILIKE ? OR e.first_name ILIKE ? OR e.position ILIKE ?
            ORDER BY e.last_name, e.first_name
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    employees.add(mapResultSetToEmployee(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching employees: " + e.getMessage());
            e.printStackTrace();
        }
        return employees;
    }
    
    /**
     * Helper method to map ResultSet to EmployeeInformation object
     */
    private static EmployeeInformation mapResultSetToEmployee(ResultSet rs) throws SQLException {
        EmployeeInformation employee = new EmployeeInformation(String.valueOf(rs.getInt("employee_num")));
        
        employee.setLastName(rs.getString("last_name"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setBirthday(rs.getString("birthday"));
        employee.setAddress(rs.getString("address"));
        employee.setPhoneNumber(rs.getString("phone_number"));
        employee.setStatus(rs.getString("status"));
        employee.setPosition(rs.getString("position"));
        employee.setSupervisor(rs.getString("immediate_supervisor"));
        employee.setHourlyRate(rs.getDouble("hourly_rate"));
        
        // Set UserId from the joined users table
        employee.setUserId(rs.getString("username"));
        
        return employee;
    }
    
    /**
     * Helper method to get a string field value for an employee
     */
    private static String getStringEmployeeField(String employeeNum, String fieldName) {
        String sql = "SELECT " + fieldName + " FROM employees WHERE employee_num = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(employeeNum));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(fieldName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting employee field: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Helper method to get a double field value for an employee
     */
    private static double getDoubleEmployeeField(String employeeNum, String fieldName) {
        String sql = "SELECT " + fieldName + " FROM employees WHERE employee_num = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(employeeNum));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(fieldName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting employee field: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }
    
    /**
     * Helper method to get a long field value for an employee
     */
    private static long getLongEmployeeField(String employeeNum, String fieldName) {
        String sql = "SELECT " + fieldName + " FROM employees WHERE employee_num = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(employeeNum));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(fieldName);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting employee field: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}
