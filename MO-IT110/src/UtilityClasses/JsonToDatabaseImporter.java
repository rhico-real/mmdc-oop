package UtilityClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import Database.DatabaseConnection;
import Database.DatabaseInitializer;

/**
 * Utility class to handle database initialization
 */
public class JsonToDatabaseImporter {
    
    private static boolean importInProgress = false;
    
    private JsonToDatabaseImporter() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Initialize the database schema and setup the admin user
     */
    public static void importAllData() {
        if (importInProgress) {
            System.out.println("Database initialization already in progress, please wait...");
            return;
        }
        
        importInProgress = true;
        
        // Create a SwingWorker to run the initialization in the background
        SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    publish("Initializing database schema...");
                    // Initialize the database schema
                    DatabaseInitializer.initializeDatabase();
                    
                    // Ensure admin user exists
                    publish("Setting up admin user...");
                    setupAdminUser();
                    
                    publish("Database initialization complete!");
                    return true;
                } catch (Exception e) {
                    publish("Error initializing database: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                } finally {
                    importInProgress = false;
                }
            }
            
            @Override
            protected void process(List<String> chunks) {
                // Print progress messages to console
                for (String message : chunks) {
                    System.out.println(message);
                }
            }
            
            @Override
            protected void done() {
                try {
                    if (get()) {
                        System.out.println("Database initialization completed successfully!");
                    } else {
                        JOptionPane.showMessageDialog(
                            null,
                            "Error during database initialization. Check console for details.",
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * Check if a table is empty or has fewer than the specified number of rows
     * @param tableName Name of the table to check
     * @param threshold Minimum number of rows that indicates data exists
     * @return true if the table is empty or has fewer rows than the threshold
     */
    private static boolean isTableEmpty(String tableName, int threshold) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if the table exists first (to handle first run)
            try {
                String checkTableSql = 
                    "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(checkTableSql)) {
                    pstmt.setString(1, tableName);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next() && !rs.getBoolean(1)) {
                            // Table doesn't exist
                            return true;
                        }
                    }
                }
            
                // Now check the count
                String sql = "SELECT COUNT(*) FROM " + tableName;
                try (PreparedStatement pstmt = conn.prepareStatement(sql);
                     ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        return count < threshold;
                    }
                }
            } catch (SQLException e) {
                // Table doesn't exist or other error
                System.out.println("Table " + tableName + " may not exist yet: " + e.getMessage());
                return true;
            }
            
            // Default to importing if we're unsure
            return true;
        } catch (SQLException e) {
            // If there's an error, assume the table is empty to trigger import
            System.err.println("Error checking if " + tableName + " is empty: " + e.getMessage());
            return true;
        }
    }
    
    /**
     * Setup admin user account
     */
    private static void setupAdminUser() throws SQLException {
        String sql = "INSERT INTO users (employee_num, username, password, is_admin) VALUES (?, ?, ?, ?) " +
                     "ON CONFLICT (employee_num) DO UPDATE SET " +
                     "username = EXCLUDED.username, password = EXCLUDED.password";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Add admin user
            pstmt.setInt(1, 99999);  // Special employee number for admin
            pstmt.setString(2, "admin");
            pstmt.setString(3, "123");
            pstmt.setBoolean(4, true);
            
            pstmt.executeUpdate();
            System.out.println("Admin user setup complete");
        }
    }
    
    /**
     * Check if import is currently in progress
     * @return true if import is in progress, false otherwise
     */
    public static boolean isImportInProgress() {
        return importInProgress;
    }
}
