package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Types;
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
        // Using the view vw_employee_information
        String sql = "SELECT * FROM vw_employee_information WHERE employee_number = ? AND is_active = TRUE";
        
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
        
        // Using the stored procedure sp_get_all_employees
        String sql = "SELECT * FROM sp_get_all_employees()";
        
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
        // Using the stored procedure sp_get_employee_compensation
        String sql = "SELECT * FROM sp_get_employee_compensation(?)";
        
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
                    
                    // Process allowances from the result set
                    do {
                        String allowanceName = rs.getString("allowance_name");
                        if (allowanceName != null) {
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
                    } while (rs.next());
                    
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
        // Using the stored procedure sp_get_employee_gov_ids
        String sql = "SELECT * FROM sp_get_employee_gov_ids(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNum);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    GovernmentIdentification govId = new GovernmentIdentification(employeeNum);
                    
                    // Set basic employee information
                    govId.setFirstName(rs.getString("first_name"));
                    govId.setLastName(rs.getString("last_name"));
                    govId.setBirthday(rs.getDate("birthday") != null ? rs.getDate("birthday").toString() : null);
                    govId.setAddress(rs.getString("home_address"));
                    govId.setPhoneNumber(rs.getString("phone_number"));
                    govId.setPosition(rs.getString("position_title"));
                    govId.setStatus(rs.getString("status"));
                    govId.setSupervisor(null); // Set this if available in the result set
                    
                    // Process government IDs
                    do {
                        String idType = rs.getString("id_type");
                        String idNumber = rs.getString("id_number");
                        
                        if (idType != null && idNumber != null) {
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
                    } while (rs.next());
                    
                    return govId;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting employee government IDs: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
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
        // Using the stored procedure sp_create_employee
        String sql = "{CALL sp_create_employee(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setString(1, employee.getEmployeeNumber());
            cstmt.setString(2, username);
            cstmt.setString(3, password);
            cstmt.setString(4, employee.getFirstName() + "@motorph.com");
            cstmt.setString(5, employee.getFirstName());
            cstmt.setString(6, employee.getLastName());
            cstmt.setDate(7, employee.getBirthday() != null ? Date.valueOf(employee.getBirthday()) : null);
            cstmt.setString(8, employee.getAddress());
            cstmt.setString(9, employee.getPhoneNumber());
            cstmt.setString(10, positionTitle);
            cstmt.setString(11, departmentName);
            cstmt.setDouble(12, compensation.getBasicSalary());
            cstmt.setDouble(13, compensation.getHourlyRate());
            cstmt.setDouble(14, compensation.getGrossSemiMonthlyRate());
            cstmt.setString(15, govId.getSSSNumber());
            cstmt.setString(16, govId.getPhilHealthNumber());
            cstmt.setString(17, govId.getTinNumber());
            cstmt.setString(18, govId.getPagibigNumber());
            cstmt.setDouble(19, compensation.getRiceSubsidy());
            cstmt.setDouble(20, compensation.getPhoneAllowance());
            cstmt.setDouble(21, compensation.getClothingAllowance());
            
            // Register output parameters
            cstmt.registerOutParameter(22, Types.BOOLEAN); // success
            cstmt.registerOutParameter(23, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(22);
            if (!success) {
                String errorMessage = cstmt.getString(23);
                System.err.println("Error creating employee: " + errorMessage);
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Error creating employee: " + e.getMessage());
            e.printStackTrace();
            return false;
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
        // Using the stored procedure sp_update_employee
        String sql = "{CALL sp_update_employee(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setString(1, employee.getEmployeeNumber());
            cstmt.setString(2, employee.getFirstName());
            cstmt.setString(3, employee.getLastName());
            cstmt.setDate(4, employee.getBirthday() != null ? Date.valueOf(employee.getBirthday()) : null);
            cstmt.setString(5, employee.getAddress());
            cstmt.setString(6, employee.getPhoneNumber());
            cstmt.setDouble(7, compensation.getBasicSalary());
            cstmt.setDouble(8, compensation.getHourlyRate());
            cstmt.setDouble(9, compensation.getGrossSemiMonthlyRate());
            cstmt.setString(10, govId.getSSSNumber());
            cstmt.setString(11, govId.getPhilHealthNumber());
            cstmt.setString(12, govId.getTinNumber());
            cstmt.setString(13, govId.getPagibigNumber());
            cstmt.setDouble(14, compensation.getRiceSubsidy());
            cstmt.setDouble(15, compensation.getPhoneAllowance());
            cstmt.setDouble(16, compensation.getClothingAllowance());
            
            // Register output parameters
            cstmt.registerOutParameter(17, Types.BOOLEAN); // success
            cstmt.registerOutParameter(18, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(17);
            if (!success) {
                String errorMessage = cstmt.getString(18);
                System.err.println("Error updating employee: " + errorMessage);
            } else {
                System.out.println("Employee updated successfully: " + employee.getEmployeeNumber());
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Search employees by name
     * @param searchTerm Search term for name
     * @return List of matching employees
     */
    public static List<EmployeeInformation> searchEmployeesByName(String searchTerm) {
        List<EmployeeInformation> employees = new ArrayList<>();
        
        // Using the stored procedure sp_search_employees_by_name
        String sql = "SELECT * FROM sp_search_employees_by_name(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, searchTerm);
            
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
        // Using the stored procedure sp_delete_employee
        String sql = "{CALL sp_delete_employee(?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setString(1, employeeNum);
            
            // Register output parameters
            cstmt.registerOutParameter(2, Types.BOOLEAN); // success
            cstmt.registerOutParameter(3, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(2);
            if (!success) {
                String errorMessage = cstmt.getString(3);
                System.err.println("Error deleting employee: " + errorMessage);
            }
            
            return success;
            
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
}
