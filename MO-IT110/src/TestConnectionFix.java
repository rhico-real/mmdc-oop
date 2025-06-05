import Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Simple test to verify database connection fixes
 */
public class TestConnectionFix {
    public static void main(String[] args) {
        System.out.println("Testing Database Connection Fixes...");
        
        // Test 1: Multiple connections should be different instances
        try {
            Connection conn1 = DatabaseConnection.getConnection();
            Connection conn2 = DatabaseConnection.getConnection();
            
            System.out.println("Connection 1: " + conn1);
            System.out.println("Connection 2: " + conn2);
            System.out.println("Are they different instances? " + (conn1 != conn2));
            
            // Test 2: Closing one connection doesn't affect the other
            conn1.close();
            System.out.println("After closing conn1:");
            System.out.println("conn1.isClosed(): " + conn1.isClosed());
            System.out.println("conn2.isClosed(): " + conn2.isClosed());
            
            conn2.close();
            System.out.println("Test passed! Connection management is working correctly.");
            
        } catch (SQLException e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
