package Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import Database.DatabaseConnection;

/**
 * Migration utility to add is_hr column to existing users table
 * This preserves existing data while adding the new HR functionality
 */
public class AddHRColumnMigration {
    
    public static void main(String[] args) {
        System.out.println("=== HR Column Migration Utility ===");
        System.out.println("This will add the is_hr column to your existing users table.");
        System.out.println("Your existing data will be preserved.");
        System.out.println();
        
        try {
            addHRColumn();
        } catch (Exception e) {
            System.err.println("❌ Error during migration: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Add is_hr column to users table if it doesn't exist
     */
    private static void addHRColumn() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if column already exists and add it if it doesn't
            String checkColumnSql = """
                SELECT column_name 
                FROM information_schema.columns 
                WHERE table_name = 'users' AND column_name = 'is_hr'
            """;
            
            try (Statement stmt = conn.createStatement()) {
                var rs = stmt.executeQuery(checkColumnSql);
                
                if (rs.next()) {
                    System.out.println("✅ is_hr column already exists in users table!");
                    System.out.println("No migration needed.");
                } else {
                    System.out.println("Adding is_hr column to users table...");
                    
                    // Add the column
                    String addColumnSql = "ALTER TABLE users ADD COLUMN is_hr BOOLEAN DEFAULT FALSE";
                    stmt.execute(addColumnSql);
                    
                    System.out.println("✅ is_hr column added successfully!");
                    
                    // Verify the column was added
                    var verifyRs = stmt.executeQuery(checkColumnSql);
                    if (verifyRs.next()) {
                        System.out.println("✅ Migration verified successfully!");
                    } else {
                        System.out.println("⚠️  Warning: Could not verify column addition.");
                    }
                }
            }
            
            System.out.println();
            System.out.println("=== Migration Complete ===");
            System.out.println("Your users table now supports HR functionality.");
            System.out.println();
            System.out.println("Next steps:");
            System.out.println("1. Run CreateHRUserTest to create an HR user");
            System.out.println("2. Test the HR functionality");
            System.out.println();
            System.out.println("To make an existing user an HR user, run:");
            System.out.println("UPDATE users SET is_hr = TRUE WHERE employee_num = [employee_number];");
            
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
