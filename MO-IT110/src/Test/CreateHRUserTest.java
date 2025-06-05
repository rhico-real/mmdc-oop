package Test;

import Classes.User;
import DAO.UserDAO;
import Database.DatabaseInitializer;

/**
 * Test class to create an HR user for testing HR functionality
 */
public class CreateHRUserTest {
    
    public static void main(String[] args) {
        System.out.println("Creating HR user for testing...");
        
        // Initialize database first
        DatabaseInitializer.initializeDatabase();
        
        // Create HR user
        User hrUser = new User("99998");
        hrUser.setUserId("hr_user");
        hrUser.setPassword("hr123");
        hrUser.setIsAdmin(false);
        hrUser.setIsHR(true);
        
        int userId = UserDAO.createUserWithRole(hrUser.getUserId(), hrUser.getPassword(), "hr_user@company.com", "HR");
        boolean success = userId > 0;
        
        if (success) {
            System.out.println("HR user created successfully!");
            System.out.println("Username: hr_user");
            System.out.println("Password: hr123");
            System.out.println("Employee Number: 99999");
            System.out.println("You can now login as HR user to test HR functionality.");
        } else {
            System.out.println("Failed to create HR user. User might already exist.");
        }
        
        // Test authentication
        User testAuth = UserDAO.authenticateUser("hr_user", "hr123");
        if (testAuth != null && testAuth.getIsHR()) {
            System.out.println("HR user authentication test: PASSED");
        } else {
            System.out.println("HR user authentication test: FAILED");
        }
    }
}
