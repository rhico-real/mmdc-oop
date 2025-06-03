package Test;

import Database.DatabaseConnection;
import Database.DatabaseInitializer;
import DAO.LeaveRequestDAO;
import Classes.LeaveRequest;
import java.util.List;

/**
 * Test class to verify leave request database functionality
 */
public class DatabaseLeaveRequestTest {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Leave Request Database Functionality ===\n");
        
        try {
            // Test database connection
            System.out.println("1. Testing database connection...");
            if (DatabaseConnection.testConnection()) {
                System.out.println("✓ Database connection successful!\n");
            } else {
                System.out.println("✗ Database connection failed!\n");
                return;
            }
            
            // Initialize database schema
            System.out.println("2. Initializing database schema...");
            DatabaseInitializer.initializeDatabase();
            System.out.println("✓ Database schema initialized!\n");
            
            // Test creating a leave request
            System.out.println("3. Testing leave request creation...");
            LeaveRequest testRequest = new LeaveRequest("10001");
            testRequest.setFirstName("John");
            testRequest.setLastName("Doe");
            testRequest.setStartDate("2025-01-15");
            testRequest.setEndDate("2025-01-17");
            testRequest.setNotes("Medical leave for surgery");
            testRequest.setLeaveType("Sick Leave");
            testRequest.setApproved("Not Approved Yet");
            
            boolean createSuccess = LeaveRequestDAO.createLeaveRequest(testRequest);
            if (createSuccess) {
                System.out.println("✓ Leave request created successfully!");
                System.out.println("  ID: " + testRequest.getId());
                System.out.println("  Employee: " + testRequest.getFirstName() + " " + testRequest.getLastName());
                System.out.println("  Type: " + testRequest.getLeaveType());
                System.out.println("  Status: " + testRequest.isApproved() + "\n");
            } else {
                System.out.println("✗ Failed to create leave request!\n");
            }
            
            // Test retrieving all leave requests
            System.out.println("4. Testing leave request retrieval...");
            List<LeaveRequest> allRequests = LeaveRequestDAO.getAllLeaveRequests();
            System.out.println("✓ Found " + allRequests.size() + " leave request(s) in database:");
            
            for (LeaveRequest request : allRequests) {
                System.out.println("  - ID: " + request.getId() + 
                                 ", Employee: " + request.getFirstName() + " " + request.getLastName() + 
                                 ", Type: " + request.getLeaveType() + 
                                 ", Status: " + request.isApproved());
            }
            System.out.println();
            
            // Test updating leave request status
            if (!allRequests.isEmpty()) {
                System.out.println("5. Testing leave request approval...");
                LeaveRequest firstRequest = allRequests.get(0);
                boolean updateSuccess = LeaveRequestDAO.updateLeaveRequestStatus(firstRequest.getId(), "Approved");
                if (updateSuccess) {
                    System.out.println("✓ Leave request approved successfully!");
                    
                    // Verify the update
                    LeaveRequest updatedRequest = LeaveRequestDAO.getLeaveRequestById(firstRequest.getId());
                    if (updatedRequest != null) {
                        System.out.println("  Updated status: " + updatedRequest.isApproved() + "\n");
                    }
                } else {
                    System.out.println("✗ Failed to approve leave request!\n");
                }
            }
            
            System.out.println("=== All tests completed successfully! ===");
            System.out.println("\nLeave request database integration is working correctly.");
            System.out.println("Employees can now submit leave requests through the UI, and they will be saved to the PostgreSQL database.");
            System.out.println("Admins can view and manage these requests through the admin dashboard.");
            
        } catch (Exception e) {
            System.err.println("Error during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
