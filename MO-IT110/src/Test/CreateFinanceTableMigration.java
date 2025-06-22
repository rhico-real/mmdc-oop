package Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import Database.DatabaseConnection;

public class CreateFinanceTableMigration {
    
    public static void main(String[] args) {
        System.out.println("Creating Finance Personnel Table Migration");
        System.out.println("=========================================");
        
        try {
            createFinancePersonnelTable();
            System.out.println("SUCCESS: finance_personnel table created successfully!");
            System.out.println("");
            System.out.println("Now you can run CreateFinanceUserTest to create the finance user.");
            
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createFinancePersonnelTable() throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            System.out.println("Creating finance_personnel table...");
            
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
                System.out.println("✓ finance_personnel table created");
            }
            
            // Create index for better performance
            String createIndexSQL = """
                CREATE INDEX IF NOT EXISTS idx_finance_personnel_user_id 
                ON finance_personnel(user_id)
            """;
            
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createIndexSQL);
                System.out.println("✓ Index on user_id created");
            }
            
            // Ensure FINANCE role exists in roles table
            String insertRoleSQL = """
                INSERT INTO roles (role_name, description) 
                VALUES ('FINANCE', 'Finance personnel with payroll management access')
                ON CONFLICT (role_name) DO NOTHING
            """;
            
            try (PreparedStatement pstmt = conn.prepareStatement(insertRoleSQL)) {
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("✓ FINANCE role added to roles table");
                } else {
                    System.out.println("✓ FINANCE role already exists in roles table");
                }
            }
            
            conn.commit();
            System.out.println("✓ All changes committed successfully");
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Transaction rolled back due to error");
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
}
