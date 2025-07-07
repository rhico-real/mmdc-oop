package DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import Database.DatabaseConnection;

/**
 * This class provides methods to ensure database structures are in place
 * when needed by the application.
 */
public class DatabaseStructureDAO {
    
    /**
     * Create the employee_update_requests table directly
     * This can be called from the DAO class before accessing the table
     */
    public static void ensureUpdateRequestsTableExists() {
        // Using the stored procedure sp_ensure_update_requests_table_exists
        String sql = "{CALL sp_ensure_update_requests_table_exists()}";
        
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.execute();
            System.out.println("Employee update requests table created/verified.");
            
        } catch (SQLException e) {
            System.err.println("Error creating employee_update_requests table: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
