package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String sql = "SELECT * FROM leave_requests WHERE id = ?";
        
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
        String sql = "SELECT * FROM leave_requests ORDER BY created_at DESC";
        
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
        String sql = "SELECT * FROM leave_requests WHERE employee_num = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, Integer.parseInt(employeeNum));
            
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
        String sql = "SELECT * FROM leave_requests WHERE approved = 'Not Approved Yet' ORDER BY created_at ASC";
        
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
        String sql = """
            INSERT INTO leave_requests (
                id, employee_num, first_name, last_name, start_date, end_date, 
                notes, leave_type, approved
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, leaveRequest.getId());
            pstmt.setInt(2, Integer.parseInt(leaveRequest.getEmployeeNum()));
            pstmt.setString(3, leaveRequest.getFirstName());
            pstmt.setString(4, leaveRequest.getLastName());
            pstmt.setString(5, leaveRequest.getStartDate());
            pstmt.setString(6, leaveRequest.getEndDate());
            pstmt.setString(7, leaveRequest.getNotes());
            pstmt.setString(8, leaveRequest.getLeaveType());
            pstmt.setString(9, leaveRequest.isApproved());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating leave request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update leave request
     * @param leaveRequest LeaveRequest object with updated information
     * @return true if update successful, false otherwise
     */
    public static boolean updateLeaveRequest(LeaveRequest leaveRequest) {
        String sql = """
            UPDATE leave_requests SET 
                first_name = ?, last_name = ?, start_date = ?, end_date = ?, 
                notes = ?, leave_type = ?, approved = ?, updated_at = CURRENT_TIMESTAMP
            WHERE id = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, leaveRequest.getFirstName());
            pstmt.setString(2, leaveRequest.getLastName());
            pstmt.setString(3, leaveRequest.getStartDate());
            pstmt.setString(4, leaveRequest.getEndDate());
            pstmt.setString(5, leaveRequest.getNotes());
            pstmt.setString(6, leaveRequest.getLeaveType());
            pstmt.setString(7, leaveRequest.isApproved());
            pstmt.setString(8, leaveRequest.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating leave request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Approve or reject leave request
     * @param id Leave request ID
     * @param status Approval status ("Approved" or "Rejected")
     * @return true if update successful, false otherwise
     */
    public static boolean updateLeaveRequestStatus(String id, String status) {
        String sql = "UPDATE leave_requests SET approved = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setString(2, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating leave request status: " + e.getMessage());
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
        String sql = "DELETE FROM leave_requests WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
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
        String sql = "SELECT * FROM leave_requests WHERE approved = ? ORDER BY created_at DESC";
        
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
        LeaveRequest leaveRequest = new LeaveRequest(String.valueOf(rs.getInt("employee_num")));
        
        leaveRequest.setId(rs.getString("id"));
        leaveRequest.setFirstName(rs.getString("first_name"));
        leaveRequest.setLastName(rs.getString("last_name"));
        leaveRequest.setStartDate(rs.getString("start_date"));
        leaveRequest.setEndDate(rs.getString("end_date"));
        leaveRequest.setNotes(rs.getString("notes"));
        leaveRequest.setLeaveType(rs.getString("leave_type"));
        leaveRequest.setApproved(rs.getString("approved"));
        
        return leaveRequest;
    }
}
