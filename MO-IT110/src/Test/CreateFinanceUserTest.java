package Test;

import DAO.UserDAO;

public class CreateFinanceUserTest {
    
    public static void main(String[] args) {
        System.out.println("Creating Finance User Test");
        System.out.println("=========================");
        
        try {
            // Create finance user with username 'finance_user' and password 'finance123'
            int userId = UserDAO.createUserWithRole(
                "finance_user",    // username
                "finance123",      // password
                "finance@motorph.com", // email
                "FINANCE"          // role
            );
            
            if (userId > 0) {
                System.out.println("SUCCESS: Finance user created successfully!");
                System.out.println("User ID: " + userId);
                System.out.println("Username: finance_user");
                System.out.println("Password: finance123");
                System.out.println("Role: FINANCE");
                System.out.println("");
                System.out.println("You can now login with these credentials:");
                System.out.println("- Username: finance_user");
                System.out.println("- Password: finance123");
                System.out.println("");
                System.out.println("The finance user will have access to all HR functionalities:");
                System.out.println("- Search Employee & Create Payslip");
                System.out.println("- View All Employees & Payslips");
            } else {
                System.out.println("ERROR: Failed to create finance user");
            }
            
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
