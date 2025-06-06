package DAO;

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
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Create the table
            String updateRequestsSql = """
                CREATE TABLE IF NOT EXISTS employee_update_requests (
                    request_id SERIAL PRIMARY KEY,
                    employee_number VARCHAR(50) NOT NULL,
                    first_name VARCHAR(100) NOT NULL,
                    last_name VARCHAR(100) NOT NULL,
                    birthday VARCHAR(20),
                    address TEXT,
                    phone_number VARCHAR(20),
                    sss_number VARCHAR(20),
                    philhealth_number VARCHAR(20),
                    tin_number VARCHAR(20),
                    pagibig_number VARCHAR(20),
                    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    status VARCHAR(20) DEFAULT 'PENDING',
                    admin_notes TEXT,
                    FOREIGN KEY (employee_number) REFERENCES employees(employee_number)
                )
            """;
            
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(updateRequestsSql);
                System.out.println("Employee update requests table created/verified.");
            }
            
            // Create indexes
            String indexSql1 = "CREATE INDEX IF NOT EXISTS idx_update_requests_employee_number ON employee_update_requests(employee_number)";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(indexSql1);
            }
            
            String indexSql2 = "CREATE INDEX IF NOT EXISTS idx_update_requests_status ON employee_update_requests(status)";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(indexSql2);
            }
            
        } catch (SQLException e) {
            System.err.println("Error creating employee_update_requests table: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
