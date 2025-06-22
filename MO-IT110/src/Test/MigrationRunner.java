package Test;

import Database.DatabaseInitializer;
import UtilityClasses.DataMigrationUtility;
import Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Fixed Utility class to run the migration with proper validation
 */
public class MigrationRunner{
    
    public static void main(String[] args) {
        System.out.println("=== MotorPH Payroll System - Database Migration (Fixed) ===");
        System.out.println("Converting from 4-table structure to 15+ table normalized structure");
        System.out.println();
        
        try {
            // Step 0: Diagnose and validate database state
            System.out.println("Step 0: Diagnosing current database state...");
            validateDatabaseState();
            System.out.println("✓ Database state validated!");
            System.out.println();
            
            // Step 1: Initialize new normalized database structure
            System.out.println("Step 1: Creating new normalized database structure...");
            DatabaseInitializer.initializeDatabase();
            System.out.println("✓ New database structure created successfully!");
            System.out.println();
            
            // Step 1.5: Verify critical tables exist before migration
            System.out.println("Step 1.5: Verifying required tables exist...");
            verifyRequiredTables();
            System.out.println("✓ Required tables verified!");
            System.out.println();
            
            // Step 2: Migrate existing data
            System.out.println("Step 2: Migrating existing data...");
            DataMigrationUtility.migrateToNormalizedStructure();
            System.out.println("✓ Data migration completed successfully!");
            System.out.println();
            
            // Step 3: Verify migration
            System.out.println("Step 3: Verifying migration...");
            DataMigrationUtility.verifyMigration();
            System.out.println("✓ Migration verification completed!");
            System.out.println();
            
            System.out.println("=== MIGRATION COMPLETED SUCCESSFULLY ===");
            System.out.println("Your database now has 15+ tables with proper normalization!");
            System.out.println("You can now:");
            System.out.println("1. Run your application normally");
            System.out.println("2. Login with existing credentials");
            System.out.println("3. All functionality should work as before");
            System.out.println("4. Optionally run DataMigrationUtility.cleanupOldTables() to remove backup tables");
            
        } catch (Exception e) {
            System.err.println("❌ Migration failed: " + e.getMessage());
            e.printStackTrace();
            System.out.println();
            System.out.println("TROUBLESHOOTING STEPS:");
            System.out.println("1. Check if your PostgreSQL server is running");
            System.out.println("2. Verify database connection settings");
            System.out.println("3. Ensure you have proper database permissions");
            System.out.println("4. Check if old tables (users, employees, etc.) exist");
            System.out.println("5. If migration fails, your original data is safe in backup tables (*_old)");
        }
    }
    
    /**
     * Validate the current database state before migration
     */
    private static void validateDatabaseState() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Checking database connection...");
            
            // Check if we can connect
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Cannot establish database connection");
            }
            
            // Check existing tables
            String checkTablesSql = """
                SELECT table_name 
                FROM information_schema.tables 
                WHERE table_schema = 'public' 
                AND table_name IN ('users', 'employees', 'attendance', 'leave_requests')
                ORDER BY table_name
            """;
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(checkTablesSql)) {
                
                System.out.println("Found existing tables:");
                boolean hasExistingData = false;
                while (rs.next()) {
                    String tableName = rs.getString("table_name");
                    System.out.println("  - " + tableName);
                    hasExistingData = true;
                }
                
                if (!hasExistingData) {
                    System.out.println("  No existing tables found. This appears to be a fresh installation.");
                }
            }
        }
    }
    
    /**
     * Verify that all required tables exist after initialization
     */
    private static void verifyRequiredTables() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String[] requiredTables = {
                "users", "roles", "user_roles", "departments", "employees", 
                "personal_information", "contact_information", "positions", 
                "employee_compensation", "attendance_records", "leave_requests"
            };
            
            String checkTableSql = """
                SELECT COUNT(*) FROM information_schema.tables 
                WHERE table_schema = 'public' AND table_name = ?
            """;
            
            try (var pstmt = conn.prepareStatement(checkTableSql)) {
                for (String table : requiredTables) {
                    pstmt.setString(1, table);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) == 0) {
                            throw new SQLException("Required table '" + table + "' does not exist!");
                        }
                    }
                }
            }
            
            // Specifically check users table structure
            verifyUsersTableStructure(conn);
        }
    }
    
    /**
     * Specifically verify that the users table has the expected structure
     */
    private static void verifyUsersTableStructure(Connection conn) throws SQLException {
        String checkColumnsSql = """
            SELECT column_name 
            FROM information_schema.columns 
            WHERE table_name = 'users' 
            AND column_name IN ('user_id', 'username', 'password', 'email', 'is_active')
        """;
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkColumnsSql)) {
            
            java.util.Set<String> foundColumns = new java.util.HashSet<>();
            while (rs.next()) {
                foundColumns.add(rs.getString("column_name"));
            }
            
            String[] requiredColumns = {"user_id", "username", "password", "email", "is_active"};
            for (String column : requiredColumns) {
                if (!foundColumns.contains(column)) {
                    throw new SQLException("Users table is missing required column: " + column);
                }
            }
            
            System.out.println("✓ Users table structure is correct");
        }
    }
    
    /**
     * Alternative method to just initialize the new structure without migration
     */
    public static void initializeNewStructureOnly() {
        System.out.println("Creating new normalized database structure without migration...");
        DatabaseInitializer.initializeDatabase();
        System.out.println("New structure created successfully!");
    }
    
    /**
     * Method to clean up old backup tables after successful migration
     */
    public static void cleanupAfterMigration() {
        System.out.println("Cleaning up old backup tables...");
        DataMigrationUtility.cleanupOldTables();
        System.out.println("Cleanup completed!");
    }
    
    /**
     * Diagnostic method to show current database state
     */
    public static void showDatabaseDiagnostics() {
        System.out.println("=== DATABASE DIAGNOSTICS ===");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            // Show all tables
            String showTablesSql = """
                SELECT table_name, 
                       CASE WHEN table_name LIKE '%_old' THEN 'Backup Table'
                            ELSE 'Active Table' END as table_type
                FROM information_schema.tables 
                WHERE table_schema = 'public' 
                ORDER BY table_type, table_name
            """;
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(showTablesSql)) {
                
                System.out.println("\nCurrent Tables:");
                while (rs.next()) {
                    System.out.printf("  %-30s [%s]%n", 
                        rs.getString("table_name"), 
                        rs.getString("table_type"));
                }
            }
            
            // Show users table structure if it exists
            String userStructureSql = """
                SELECT column_name, data_type, is_nullable 
                FROM information_schema.columns 
                WHERE table_name = 'users' 
                ORDER BY ordinal_position
            """;
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(userStructureSql)) {
                
                System.out.println("\nUsers Table Structure:");
                boolean hasUsers = false;
                while (rs.next()) {
                    hasUsers = true;
                    System.out.printf("  %-20s %-15s %s%n", 
                        rs.getString("column_name"),
                        rs.getString("data_type"),
                        rs.getString("is_nullable").equals("YES") ? "NULL" : "NOT NULL");
                }
                
                if (!hasUsers) {
                    System.out.println("  Users table does not exist!");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error running diagnostics: " + e.getMessage());
        }
        
        System.out.println("=== END DIAGNOSTICS ===\n");
    }
}