package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    
    private static final String URL = "jdbc:postgresql://localhost:5432/motorph_payroll";
    private static final String USERNAME = "camulite_admin";
    private static final String PASSWORD = "123";
    
    private static Connection connection = null;
    private static boolean showedConnectionError = false;
    
    // Private constructor to prevent instantiation
    private DatabaseConnection() {}
    
    /**
     * Get a new database connection (each call returns a new connection)
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            Connection newConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connection established successfully!");
            return newConnection;
        } catch (ClassNotFoundException e) {
            String errorMsg = "PostgreSQL JDBC Driver not found. Please make sure it's in your classpath.";
            System.err.println(errorMsg);
            showErrorDialog(errorMsg, e);
            throw new SQLException(errorMsg, e);
        } catch (SQLException e) {
            String errorMsg = "Failed to connect to the database. Please check that PostgreSQL is running and the database exists.";
            System.err.println(errorMsg + " Error: " + e.getMessage());
            showErrorDialog(errorMsg, e);
            throw e;
        }
    }
    
    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        } finally {
            connection = null;
        }
    }
    
    /**
     * Test database connection
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Show error dialog only once to avoid overwhelming the user
     * @param message Error message
     * @param e Exception that occurred
     */
    private static void showErrorDialog(String message, Exception e) {
        if (!showedConnectionError) {
            JOptionPane.showMessageDialog(
                null,
                message + "\n\nError details: " + e.getMessage() + 
                "\n\nPlease make sure PostgreSQL is running and the database is set up correctly.",
                "Database Connection Error",
                JOptionPane.ERROR_MESSAGE
            );
            showedConnectionError = true;
        }
    }
    
    /**
     * Reset the connection error flag (mainly for testing)
     */
    public static void resetErrorFlag() {
        showedConnectionError = false;
    }
}
