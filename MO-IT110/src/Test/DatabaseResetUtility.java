package Test;

import Database.DatabaseInitializer;

/**
 * Utility class to reset and reinitialize the database schema
 * This will drop all existing tables and recreate them with the latest schema
 */
public class DatabaseResetUtility {
    
    public static void main(String[] args) {
        System.out.println("=== Database Reset Utility ===");
        System.out.println("This will drop all existing tables and recreate them.");
        System.out.println("WARNING: All data will be lost!");
        System.out.println();
        
        try {
            // Step 1: Drop all existing tables
            System.out.println("Step 1: Dropping all existing tables...");
            DatabaseInitializer.dropAllTables();
            System.out.println("✅ All tables dropped successfully!");
            System.out.println();
            
            // Step 2: Recreate tables with new schema
            System.out.println("Step 2: Creating tables with updated schema...");
            DatabaseInitializer.initializeDatabase();
            System.out.println("✅ Database reinitialized successfully!");
            System.out.println();
            
            System.out.println("=== Database Reset Complete ===");
            System.out.println("Your database now includes:");
            System.out.println("- users table with is_hr field");
            System.out.println("- employees table");
            System.out.println("- attendance table");
            System.out.println("- leave_requests table");
            System.out.println();
            System.out.println("Next steps:");
            System.out.println("1. Import your employee data (if needed)");
            System.out.println("2. Run CreateHRUserTest to create an HR user");
            System.out.println("3. Test the HR functionality");
            
        } catch (Exception e) {
            System.err.println("❌ Error during database reset: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
