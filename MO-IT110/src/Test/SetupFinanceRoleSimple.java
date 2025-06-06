package Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DAO.UserDAO;
import Database.DatabaseConnection;

public class SetupFinanceRoleSimple {
    
    public static void main(String[] args) {
        System.out.println("Simple Finance Role Setup");
        System.out.println("=========================");
        System.out.println("This will:");
        System.out.println("1. Create the finance_personnel table");
        System.out.println("2. Add FINANCE role (if not exists)");
        System.out.println("3. Create the finance user");
        System.out.println("");
        
        try {
            // Step 1: Create the database table
            createFinancePersonnelTable();
            
            // Step 2: Create the finance user
            createFinanceUser();
            
            System.out.println("");
            System.out.println("🎉 SETUP COMPLETE! 🎉");
            System.out.println("");
            System.out.println("Finance role is now ready to use:");
            System.out.println("- Username: finance_user");
            System.out.println("- Password: finance123");
            System.out.println("- Role: FINANCE");
            
        } catch (Exception e) {
            System.out.println("❌ SETUP FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createFinancePersonnelTable() throws SQLException {
        System.out.println("Step 1: Setting up database tables...");
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Create finance_personnel table
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS finance_personnel (
                    finance_id SERIAL PRIMARY KEY,
                    user_id INTEGER NOT NULL,
                    finance_level VARCHAR(50) DEFAULT 'Junior',
                    department VARCHAR(100) DEFAULT 'Finance',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    is_active BOOLEAN DEFAULT TRUE,
                    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
                )
            """;
            
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createTableSQL);
                System.out.println("  ✓ finance_personnel table created");
            }
            
            // Create index for better performance
            String createIndexSQL = """
                CREATE INDEX IF NOT EXISTS idx_finance_personnel_user_id 
                ON finance_personnel(user_id)
            """;
            
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createIndexSQL);
                System.out.println("  ✓ Index on user_id created");
            }
            
            // Check if FINANCE role exists, and add it if not
            String checkRoleSQL = "SELECT role_id FROM roles WHERE role_name = 'FINANCE'";
            boolean roleExists = false;
            
            try (PreparedStatement pstmt = conn.prepareStatement(checkRoleSQL);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    roleExists = true;
                    System.out.println("  ✓ FINANCE role already exists");
                }
            }
            
            if (!roleExists) {
                // Insert FINANCE role with only role_name (no description column)
                String insertRoleSQL = "INSERT INTO roles (role_name) VALUES ('FINANCE')";
                try (PreparedStatement pstmt = conn.prepareStatement(insertRoleSQL)) {
                    pstmt.executeUpdate();
                    System.out.println("  ✓ FINANCE role added to roles table");
                }
            }
            
            conn.commit();
            System.out.println("  ✓ Database setup completed");
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("  ❌ Database setup rolled back due to error");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static void createFinanceUser() {
        System.out.println("");
        System.out.println("Step 2: Creating finance user...");
        
        try {
            // Check if user already exists
            if (UserDAO.getUserByUsername("finance_user") != null) {
                System.out.println("  ✓ Finance user already exists - skipping creation");
                return;
            }
            
            // Create finance user with username 'finance_user' and password 'finance123'
            int userId = UserDAO.createUserWithRole(
                "finance_user",        // username
                "finance123",          // password
                "finance@motorph.com", // email
                "FINANCE"              // role
            );
            
            if (userId > 0) {
                System.out.println("  ✓ Finance user created successfully!");
                System.out.println("  ✓ User ID: " + userId);
                System.out.println("  ✓ Username: finance_user");
                System.out.println("  ✓ Password: finance123");
                System.out.println("  ✓ Role: FINANCE");
            } else {
                throw new Exception("Failed to create finance user - UserDAO returned invalid ID");
            }
            
        } catch (Exception e) {
            System.out.println("  ❌ Error creating finance user: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
