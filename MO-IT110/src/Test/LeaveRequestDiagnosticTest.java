package Test;

import Database.DatabaseConnection;
import Database.DatabaseInitializer;
import DAO.LeaveRequestDAO;
import Classes.LeaveRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Diagnostic test to troubleshoot leave request approval/rejection issues
 */
public class LeaveRequestDiagnosticTest {
    
    public static void main(String[] args) {
        System.out.println("=== Leave Request Diagnostic Test ===\n");
        
        try {
            // 1. Test database connection
            System.out.println("1. Testing database connection...");
            if (DatabaseConnection.testConnection()) {
                System.out.println("✓ Database connection successful!\n");
            } else {
                System.out.println("✗ Database connection failed!");
                System.out.println("Please check your database configuration.\n");
                return;
            }
            
            // 2. Check if table exists and has correct structure
            System.out.println("2. Checking leave_requests table structure...");
            checkTableStructure();
            
            // 3. List all current leave requests
            System.out.println("3. Current leave requests in database:");
            List<LeaveRequest> allRequests = LeaveRequestDAO.getAllLeaveRequests();
            if (allRequests.isEmpty()) {
                System.out.println("   No leave requests found in database.");
                System.out.println("   Creating a test leave request...\n");
                createTestLeaveRequest();
            } else {
                for (LeaveRequest request : allRequests) {
                    System.out.println("   ID: " + request.getId());
                    System.out.println("   Employee: " + request.getEmployeeNum() + " - " + 
                                     request.getFirstName() + " " + request.getLastName());
                    System.out.println("   Status: " + request.isApproved());
                    System.out.println("   Type: " + request.getLeaveType());
                    System.out.println("   ---");
                }
            }
            
            // 4. Test update functionality on existing requests
            System.out.println("\n4. Testing status update functionality...");
            allRequests = LeaveRequestDAO.getAllLeaveRequests();
            if (!allRequests.isEmpty()) {
                LeaveRequest testRequest = allRequests.get(0);
                String originalStatus = testRequest.isApproved();
                System.out.println("   Testing with request ID: " + testRequest.getId());
                System.out.println("   Original status: " + originalStatus);
                
                // Test rejection
                System.out.println("   Testing rejection...");
                boolean rejectSuccess = LeaveRequestDAO.updateLeaveRequestStatus(testRequest.getId(), "Rejected");
                if (rejectSuccess) {
                    System.out.println("   ✓ Rejection successful!");
                    
                    // Verify the change
                    LeaveRequest updatedRequest = LeaveRequestDAO.getLeaveRequestById(testRequest.getId());
                    if (updatedRequest != null && "Rejected".equals(updatedRequest.isApproved())) {
                        System.out.println("   ✓ Status correctly updated to: " + updatedRequest.isApproved());
                    } else {
                        System.out.println("   ✗ Status verification failed!");
                    }
                } else {
                    System.out.println("   ✗ Rejection failed!");
                }
                
                // Test approval
                System.out.println("   Testing approval...");
                boolean approveSuccess = LeaveRequestDAO.updateLeaveRequestStatus(testRequest.getId(), "Approved");
                if (approveSuccess) {
                    System.out.println("   ✓ Approval successful!");
                    
                    // Verify the change
                    LeaveRequest updatedRequest = LeaveRequestDAO.getLeaveRequestById(testRequest.getId());
                    if (updatedRequest != null && "Approved".equals(updatedRequest.isApproved())) {
                        System.out.println("   ✓ Status correctly updated to: " + updatedRequest.isApproved());
                    } else {
                        System.out.println("   ✗ Status verification failed!");
                    }
                } else {
                    System.out.println("   ✗ Approval failed!");
                }
                
                // Restore original status
                LeaveRequestDAO.updateLeaveRequestStatus(testRequest.getId(), originalStatus);
                System.out.println("   Restored original status: " + originalStatus);
            }
            
            System.out.println("\n=== Diagnostic Complete ===");
            System.out.println("If any tests failed, please review the error messages above.");
            
        } catch (Exception e) {
            System.err.println("Error during diagnostic: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void checkTableStructure() {
        String sql = """
            SELECT column_name, data_type, is_nullable, column_default 
            FROM information_schema.columns 
            WHERE table_name = 'leave_requests' 
            ORDER BY ordinal_position
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            System.out.println("   Table structure:");
            while (rs.next()) {
                System.out.printf("   - %s (%s) %s%n", 
                    rs.getString("column_name"),
                    rs.getString("data_type"),
                    "YES".equals(rs.getString("is_nullable")) ? "NULL" : "NOT NULL"
                );
            }
            System.out.println("   ✓ Table structure verified!\n");
            
        } catch (SQLException e) {
            System.err.println("   ✗ Error checking table structure: " + e.getMessage());
            System.out.println("   Attempting to create table...");
            DatabaseInitializer.initializeDatabase();
        }
    }
    
    private static void createTestLeaveRequest() {
        LeaveRequest testRequest = new LeaveRequest("10001");
        testRequest.setFirstName("Test");
        testRequest.setLastName("Employee");
        testRequest.setStartDate("2025-06-10");
        testRequest.setEndDate("2025-06-12");
        testRequest.setNotes("Test leave request for diagnostic purposes");
        testRequest.setLeaveType("Sick Leave");
        testRequest.setApproved("Not Approved Yet");
        
        boolean success = LeaveRequestDAO.createLeaveRequest(testRequest);
        if (success) {
            System.out.println("   ✓ Test leave request created with ID: " + testRequest.getId());
        } else {
            System.out.println("   ✗ Failed to create test leave request");
        }
    }
}
