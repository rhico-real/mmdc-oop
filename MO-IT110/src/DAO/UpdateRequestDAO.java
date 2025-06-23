package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Classes.UpdateRequest;
import Classes.Compensation;
import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
import Database.DatabaseConnection;

public class UpdateRequestDAO {
    
    // Make sure the table exists before any operation
    static {
        DatabaseStructureDAO.ensureUpdateRequestsTableExists();
    }
    
    /**
     * Create a new update request
     * @param request UpdateRequest object
     * @return true if creation successful, false otherwise
     */
    public static boolean createUpdateRequest(UpdateRequest request) {
        String sql = """
            INSERT INTO employee_update_requests 
            (employee_number, first_name, last_name, birthday, address, phone_number, 
             sss_number, philhealth_number, tin_number, pagibig_number, request_date, status) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, request.getEmployeeNumber());
            pstmt.setString(2, request.getFirstName());
            pstmt.setString(3, request.getLastName());
            pstmt.setString(4, request.getBirthday());
            pstmt.setString(5, request.getAddress());
            pstmt.setString(6, request.getPhoneNumber());
            pstmt.setString(7, request.getSssNumber());
            pstmt.setString(8, request.getPhilhealthNumber());
            pstmt.setString(9, request.getTinNumber());
            pstmt.setString(10, request.getPagibigNumber());
            pstmt.setString(11, request.getStatus());
            
            int rowsAffected = pstmt.executeUpdate();
            
            // Get the generated request ID
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        request.setRequestId(rs.getInt(1));
                    }
                }
            }
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating update request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get all update requests with the given status
     * @param status Status to filter by, null for all statuses
     * @return List of update requests
     */
    public static List<UpdateRequest> getUpdateRequests(String status) {
        List<UpdateRequest> requests = new ArrayList<>();
        
        String sql = """
            SELECT * FROM employee_update_requests 
        """ + (status != null ? " WHERE status = ? " : "") + 
        " ORDER BY request_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (status != null) {
                pstmt.setString(1, status);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    UpdateRequest request = mapResultSetToUpdateRequest(rs);
                    requests.add(request);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting update requests: " + e.getMessage());
            e.printStackTrace();
        }
        
        return requests;
    }
    
    /**
     * Get a specific update request by ID
     * @param requestId Request ID
     * @return UpdateRequest object if found, null otherwise
     */
    public static UpdateRequest getUpdateRequestById(int requestId) {
        String sql = "SELECT * FROM employee_update_requests WHERE request_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, requestId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUpdateRequest(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting update request: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Update the status of an update request
     * @param requestId Request ID
     * @param status New status
     * @param adminNotes Admin notes
     * @return true if update successful, false otherwise
     */
    public static boolean updateRequestStatus(int requestId, String status, String adminNotes) {
        String sql = "UPDATE employee_update_requests SET status = ?, admin_notes = ? WHERE request_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, adminNotes);
            pstmt.setInt(3, requestId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating request status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Approve an update request and apply the changes to the employee record
     * @param requestId Request ID
     * @param adminNotes Admin notes
     * @return true if approval successful, false otherwise
     */
    public static boolean approveUpdateRequest(int requestId, String adminNotes) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Get the update request
            UpdateRequest request = getUpdateRequestById(requestId);
            if (request == null) {
                return false;
            }
            
            // Create employee objects with the updated information
            EmployeeInformation employee = new EmployeeInformation(request.getEmployeeNumber());
            GovernmentIdentification govId = new GovernmentIdentification(request.getEmployeeNumber());
            
            // Set updated employee information
            employee.setFirstName(request.getFirstName());
            employee.setLastName(request.getLastName());
            employee.setBirthday(request.getBirthday());
            employee.setAddress(request.getAddress());
            employee.setPhoneNumber(request.getPhoneNumber());
            
            // Get current employee data for fields that shouldn't change
            EmployeeInformation currentEmployee = EmployeeDAO.getEmployeeByNumber(request.getEmployeeNumber());
            employee.setStatus(currentEmployee.getStatus());
            employee.setPosition(currentEmployee.getPosition());
            employee.setSupervisor(currentEmployee.getSupervisor());
            employee.setHourlyRate(currentEmployee.getHourlyRate());
            
            // Set updated government ID information
            govId.setSSSNumber(request.getSssNumber());
            govId.setPhilHealthNumber(request.getPhilhealthNumber());
            govId.setTinNumber(request.getTinNumber());
            govId.setPagibigNumber(request.getPagibigNumber());
            
            // Get compensation data (unchanged)
            Compensation compensation = EmployeeDAO.getEmployeeCompensation(request.getEmployeeNumber());
            
            // Update the employee in the database
            boolean employeeUpdated = EmployeeDAO.updateEmployee(employee, govId, compensation);
            
            if (employeeUpdated) {
                // Update username in the users table if name has changed
                String newUsername = (request.getFirstName() + "." + request.getLastName()).toLowerCase();
                UserDAO.updateUsername(request.getEmployeeNumber(), newUsername);
                
                // Update the request status
                boolean statusUpdated = updateRequestStatus(requestId, "APPROVED", adminNotes);
                
                if (statusUpdated) {
                    conn.commit();
                    return true;
                }
            }
            
            conn.rollback();
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error approving update request: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
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
     * Reject an update request
     * @param requestId Request ID
     * @param adminNotes Admin notes
     * @return true if rejection successful, false otherwise
     */
    public static boolean rejectUpdateRequest(int requestId, String adminNotes) {
        return updateRequestStatus(requestId, "REJECTED", adminNotes);
    }
    
    /**
     * Check if an employee has any pending update requests
     * @param employeeNumber Employee number
     * @return true if there are pending requests, false otherwise
     */
    public static boolean hasEmployeePendingRequests(String employeeNumber) {
        String sql = "SELECT COUNT(*) FROM employee_update_requests WHERE employee_number = ? AND status = 'PENDING'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking for pending requests: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Helper methods
    
    private static UpdateRequest mapResultSetToUpdateRequest(ResultSet rs) throws SQLException {
        UpdateRequest request = new UpdateRequest();
        
        request.setRequestId(rs.getInt("request_id"));
        request.setEmployeeNumber(rs.getString("employee_number"));
        request.setFirstName(rs.getString("first_name"));
        request.setLastName(rs.getString("last_name"));
        request.setBirthday(rs.getString("birthday"));
        request.setAddress(rs.getString("address"));
        request.setPhoneNumber(rs.getString("phone_number"));
        request.setSssNumber(rs.getString("sss_number"));
        request.setPhilhealthNumber(rs.getString("philhealth_number"));
        request.setTinNumber(rs.getString("tin_number"));
        request.setPagibigNumber(rs.getString("pagibig_number"));
        request.setRequestDate(rs.getTimestamp("request_date"));
        request.setStatus(rs.getString("status"));
        request.setAdminNotes(rs.getString("admin_notes"));
        
        return request;
    }
}
