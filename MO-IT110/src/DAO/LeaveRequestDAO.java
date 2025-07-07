package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import Classes.LeaveRequest;
import Database.DatabaseConnection;

public class LeaveRequestDAO {
    
    /**
     * Get leave request by ID
     * @param id Leave request ID
     * @return LeaveRequest object if found, null otherwise
     */
    public static LeaveRequest getLeaveRequestById(String id) {
        // Using the stored procedure sp_get_leave_request_by_id
        String sql = "SELECT * FROM sp_get_leave_request_by_id(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLeaveRequest(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting leave request: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get all leave requests
     * @return List of all leave requests
     */
    public static List<LeaveRequest> getAllLeaveRequests() {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        
        // Using the stored procedure sp_get_all_leave_requests
        String sql = "SELECT * FROM sp_get_all_leave_requests()";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                leaveRequests.add(mapResultSetToLeaveRequest(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all leave requests: " + e.getMessage());
            e.printStackTrace();
        }
        return leaveRequests;
    }
    
    /**
     * Get leave requests by employee number
     * @param employeeNum Employee number
     * @return List of leave requests for the employee
     */
    public static List<LeaveRequest> getLeaveRequestsByEmployee(String employeeNum) {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        
        // Using the stored procedure sp_get_leave_requests_by_employee
        String sql = "SELECT * FROM sp_get_leave_requests_by_employee(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeNum);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    leaveRequests.add(mapResultSetToLeaveRequest(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting leave requests by employee: " + e.getMessage());
            e.printStackTrace();
        }
        return leaveRequests;
    }
    
    /**
     * Get pending leave requests
     * @return List of pending leave requests
     */
    public static List<LeaveRequest> getPendingLeaveRequests() {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        
        // Using the stored procedure sp_get_pending_leave_requests
        String sql = "SELECT * FROM sp_get_pending_leave_requests()";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                leaveRequests.add(mapResultSetToLeaveRequest(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting pending leave requests: " + e.getMessage());
            e.printStackTrace();
        }
        return leaveRequests;
    }
    
    /**
     * Create a new leave request
     * @param leaveRequest LeaveRequest object to create
     * @return true if creation successful, false otherwise
     */
    public static boolean createLeaveRequest(LeaveRequest leaveRequest) {
        // Using the stored procedure sp_create_leave_request
        String sql = "{CALL sp_create_leave_request(?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setString(1, leaveRequest.getEmployeeNum());
            cstmt.setString(2, leaveRequest.getLeaveType());
            cstmt.setString(3, leaveRequest.getStartDate());
            cstmt.setString(4, leaveRequest.getEndDate());
            cstmt.setString(5, leaveRequest.getNotes());
            
            // Register output parameters
            cstmt.registerOutParameter(6, Types.VARCHAR); // request_number
            cstmt.registerOutParameter(7, Types.BOOLEAN); // success
            cstmt.registerOutParameter(8, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(7);
            if (success) {
                leaveRequest.setId(cstmt.getString(6)); // Set the generated request number
            } else {
                String errorMessage = cstmt.getString(8);
                System.err.println("Error creating leave request: " + errorMessage);
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Error creating leave request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update leave request status
     * @param id Leave request ID
     * @param status Approval status ("Approved" or "Rejected")
     * @return true if update successful, false otherwise
     */
    public static boolean updateLeaveRequestStatus(String id, String status) {
        // Add debugging information
        System.out.println("[DEBUG] Updating leave request status:");
        System.out.println("[DEBUG] ID: " + id);
        System.out.println("[DEBUG] New Status: " + status);
        
        // Using the stored procedure sp_update_leave_request_status
        String sql = "{CALL sp_update_leave_request_status(?, ?, NULL, NULL, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setString(1, id);
            cstmt.setString(2, status);
            
            // Register output parameters
            cstmt.registerOutParameter(3, Types.BOOLEAN); // success
            cstmt.registerOutParameter(4, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(3);
            if (!success) {
                String errorMessage = cstmt.getString(4);
                System.err.println("[ERROR] Error updating leave request status: " + errorMessage);
            } else {
                System.out.println("[DEBUG] Leave request status updated successfully!");
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("[ERROR] Error updating leave request status: " + e.getMessage());
            System.err.println("[ERROR] SQL State: " + e.getSQLState());
            System.err.println("[ERROR] Error Code: " + e.getErrorCode());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete leave request by ID
     * @param id Leave request ID
     * @return true if deletion successful, false otherwise
     */
    public static boolean deleteLeaveRequest(String id) {
        // Using the stored procedure sp_delete_leave_request
        String sql = "{CALL sp_delete_leave_request(?, ?, ?)}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Set input parameters
            cstmt.setString(1, id);
            
            // Register output parameters
            cstmt.registerOutParameter(2, Types.BOOLEAN); // success
            cstmt.registerOutParameter(3, Types.VARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(2);
            if (!success) {
                String errorMessage = cstmt.getString(3);
                System.err.println("Error deleting leave request: " + errorMessage);
            }
            
            return success;
            
        } catch (SQLException e) {
            System.err.println("Error deleting leave request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get leave requests by status
     * @param status Approval status to filter by
     * @return List of leave requests with the specified status
     */
    public static List<LeaveRequest> getLeaveRequestsByStatus(String status) {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        
        // Using the stored procedure sp_get_leave_requests_by_status
        String sql = "SELECT * FROM sp_get_leave_requests_by_status(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    leaveRequests.add(mapResultSetToLeaveRequest(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting leave requests by status: " + e.getMessage());
            e.printStackTrace();
        }
        return leaveRequests;
    }
    
    /**
     * Helper method to map ResultSet to LeaveRequest object
     */
    private static LeaveRequest mapResultSetToLeaveRequest(ResultSet rs) throws SQLException {
        LeaveRequest leaveRequest = new LeaveRequest(rs.getString("employee_number"));
        
        leaveRequest.setId(rs.getString("request_number"));
        leaveRequest.setFirstName(rs.getString("first_name"));
        leaveRequest.setLastName(rs.getString("last_name"));
        leaveRequest.setStartDate(rs.getString("start_date"));
        leaveRequest.setEndDate(rs.getString("end_date"));
        leaveRequest.setNotes(rs.getString("reason"));
        leaveRequest.setLeaveType(rs.getString("leave_type"));
        leaveRequest.setApproved(rs.getString("status"));
        
        return leaveRequest;
    }
}
