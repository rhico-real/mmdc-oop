package Test;

import UtilityClasses.JsonToPostgresMigrator;

/**
 * Test runner for JSON to PostgreSQL migration
 * Use this class to run the migration process
 */
public class JsonMigrationRunner {
    
    public static void main(String[] args) {
        try {
            System.out.println("===============================================");
            System.out.println("JSON to PostgreSQL Migration Tool");
            System.out.println("===============================================");
            
            // Run the complete migration
            JsonToPostgresMigrator.migrateAllJsonToPostgres();
            
            // Verify the results
            JsonToPostgresMigrator.verifyMigration();
            
            System.out.println("===============================================");
            System.out.println("Migration completed successfully!");
            System.out.println("Check your PostgreSQL database for the data.");
            System.out.println("===============================================");
            
        } catch (Exception e) {
            System.err.println("Migration failed with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Run individual migration components for testing
     */
    public static void runIndividualMigrations() {
        try {
            System.out.println("Running individual migrations for testing...");
            
            // Test login credentials migration
            System.out.println("\n--- Testing Login Credentials Migration ---");
            JsonToPostgresMigrator.migrateLoginCredentials();
            
            // Test employee data migration
            System.out.println("\n--- Testing Employee Data Migration ---");
            JsonToPostgresMigrator.migrateEmployees();
            
            // Test attendance migration
            System.out.println("\n--- Testing Attendance Migration ---");
            JsonToPostgresMigrator.migrateAttendanceRecords();
            
            // Test leave requests migration
            System.out.println("\n--- Testing Leave Requests Migration ---");
            JsonToPostgresMigrator.migrateLeaveRequests();
            
            // Verify results
            JsonToPostgresMigrator.verifyMigration();
            
        } catch (Exception e) {
            System.err.println("Individual migration test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
