package Test;

import DAO.LeaveRequestDAO;
import Classes.LeaveRequest;
import java.util.List;

/**
 * Quick test to show leave request IDs in database
 */
public class QuickLeaveRequestIDTest {
    
    public static void main(String[] args) {
        System.out.println("=== Current Leave Requests in Database ===\n");
        
        try {
            List<LeaveRequest> allRequests = LeaveRequestDAO.getAllLeaveRequests();
            
            if (allRequests.isEmpty()) {
                System.out.println("No leave requests found in database.");
            } else {
                System.out.println("Found " + allRequests.size() + " leave request(s):\n");
                
                for (int i = 0; i < allRequests.size(); i++) {
                    LeaveRequest request = allRequests.get(i);
                    System.out.println((i + 1) + ". ID: " + request.getId());
                    System.out.println("   Employee: " + request.getEmployeeNum() + " - " + 
                                     request.getFirstName() + " " + request.getLastName());
                    System.out.println("   Status: " + request.isApproved());
                    System.out.println("   Type: " + request.getLeaveType());
                    System.out.println("   Start: " + request.getStartDate());
                    System.out.println("   End: " + request.getEndDate());
                    System.out.println();
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
