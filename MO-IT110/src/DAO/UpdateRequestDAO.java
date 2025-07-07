package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import Classes.UpdateRequest;
import Classes.Compensation;
import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
import Database.DatabaseConnection;

public class UpdateRequestDAO {
    
    /**
     * Create a new update request
     * @param request UpdateRequest object
     * @return true if creation successful, false otherwise
     */
    public static boolean createUpdateRequest(UpdateRequest request) {
        // Using the stored procedure sp_create_update_request
        String sql = "{CALL sp_create_update_request(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setString(1, request.getEmployeeNumber());
            cstmt.setString(2, request.getFirstName());
            cstmt.setString(3, request.getLastName());
            cstmt.setString(4, request.getBirthday());
            cstmt.setString(5, request.getAddress());
            cstmt.setString(6, request.getPhoneNumber());
            cstmt.setString(7, request.getSssNumber());
            cstmt.setString(8, request.getPhilhealthNumber());
            cstmt.setString(9, request.getTinNumber());
            cstmt.setString(10, request.getPagibigNumber());
            cstmt.setString(11, request.getStatus() != null ? request.getStatus() : "PENDING");
            
            // Register output parameters
            cstmt.registerOutParameter(12, Types.INTEGER); // request_id
            cstmt.registerOutParameter(13, Types.BOOLEAN); // success
            cstmt.registerOutParameter(14, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(13);
            if (success) {
                request.setRequestId(cstmt.getInt(12));
            } else {
                String errorMessage = cstmt.getString(14);
                System.err.println("Error creating update request: " + errorMessage);
            }
            
            return success;
            
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
        
        // Using the stored procedure sp_get_update_requests
        String sql = "SELECT * FROM sp_get_update_requests(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            
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
        // Using the stored procedure sp_get_update_request_by_id
        String sql = "SELECT * FROM sp_get_update_request_by_id(?)";
        
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
        // Using the stored procedure sp_update_request_status
        String sql = "{CALL sp_update_request_status(?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setInt(1, requestId);
            cstmt.setString(2, status);
            cstmt.setString(3, adminNotes);
            
            // Register output parameters
            cstmt.registerOutParameter(4, Types.BOOLEAN); // success
            cstmt.registerOutParameter(5, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(4);
            if (!success) {
                String errorMessage = cstmt.getString(5);
                System.err.println("Error updating request status: " + errorMessage);
            }
            
            return success;
            
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
        // Using the stored procedure sp_approve_update_request
        String sql = "{CALL sp_approve_update_request(?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setInt(1, requestId);
            cstmt.setString(2, adminNotes);
            
            // Register output parameters
            cstmt.registerOutParameter(3, Types.BOOLEAN); // success
            cstmt.registerOutParameter(4, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(3);
            if (!success) {
                String errorMessage = cstmt.getString(4);
                System.err.println("Error approving update request: " + errorMessage);
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Error approving update request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Reject an update request
     * @param requestId Request ID
     * @param adminNotes Admin notes
     * @return true if rejection successful, false otherwise
     */
    public static boolean rejectUpdateRequest(int requestId, String adminNotes) {
        // Using the stored procedure sp_reject_update_request
        String sql = "{CALL sp_reject_update_request(?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setInt(1, requestId);
            cstmt.setString(2, adminNotes);
            
            // Register output parameters
            cstmt.registerOutParameter(3, Types.BOOLEAN); // success
            cstmt.registerOutParameter(4, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(3);
            if (!success) {
                String errorMessage = cstmt.getString(4);
                System.err.println("Error rejecting update request: " + errorMessage);
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Error rejecting update request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if an employee has any pending update requests
     * @param employeeNumber Employee number
     * @return true if there are pending requests, false otherwise
     */
    public static boolean hasEmployeePendingRequests(String employeeNumber) {
        // Using the stored function sp_has_employee_pending_requests
        String sql = "SELECT sp_has_employee_pending_requests(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
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
