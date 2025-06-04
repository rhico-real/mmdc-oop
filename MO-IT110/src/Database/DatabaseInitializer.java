package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    
    /**
     * Initialize database schema - create all required tables
     */
    public static void initializeDatabase() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            createTables(conn);
            System.out.println("Database schema initialized successfully!");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Create all required tables
     */
    private static void createTables(Connection conn) throws SQLException {
        createUsersTable(conn);
        createEmployeesTable(conn);
        createAttendanceTable(conn);
        createLeaveRequestsTable(conn);
    }
    
    /**
     * Create users table for login credentials
     */
    private static void createUsersTable(Connection conn) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                employee_num INTEGER PRIMARY KEY,
                username VARCHAR(100) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                is_admin BOOLEAN DEFAULT FALSE,
                is_hr BOOLEAN DEFAULT FALSE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Users table created/verified successfully!");
        } catch(SQLException e) {
            System.err.println("Error creating users table: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Create employees table for employee information
     */
    private static void createEmployeesTable(Connection conn) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS employees (
                employee_num INTEGER PRIMARY KEY,
                last_name VARCHAR(100) NOT NULL,
                first_name VARCHAR(100) NOT NULL,
                birthday VARCHAR(50),
                address TEXT,
                phone_number VARCHAR(20),
                sss VARCHAR(20),
                philhealth BIGINT,
                tin VARCHAR(20),
                pagibig BIGINT,
                status VARCHAR(50),
                position VARCHAR(100),
                immediate_supervisor VARCHAR(200),
                basic_salary DECIMAL(12,2) DEFAULT 0.00,
                rice_subsidy DECIMAL(12,2) DEFAULT 0.00,
                phone_allowance DECIMAL(12,2) DEFAULT 0.00,
                clothing_allowance DECIMAL(12,2) DEFAULT 0.00,
                gross_semi_monthly_rate DECIMAL(12,2) DEFAULT 0.00,
                hourly_rate DECIMAL(8,2) DEFAULT 0.00,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (employee_num) REFERENCES users(employee_num) ON DELETE CASCADE
            )
        """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Employees table created/verified successfully!");
        } catch(SQLException e) {
            System.err.println("Error creating employees table: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Create attendance table
     */
    private static void createAttendanceTable(Connection conn) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS attendance (
                id SERIAL PRIMARY KEY,
                employee_num INTEGER NOT NULL,
                last_name VARCHAR(100),
                first_name VARCHAR(100),
                date VARCHAR(20) NOT NULL,
                time_in VARCHAR(10),
                time_out VARCHAR(10),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (employee_num) REFERENCES users(employee_num) ON DELETE CASCADE,
                UNIQUE(employee_num, date)
            )
        """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Attendance table created/verified successfully!");
        } catch(SQLException e) {
            System.err.println("Error creating attendance table: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Create leave requests table
     */
    private static void createLeaveRequestsTable(Connection conn) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS leave_requests (
                id VARCHAR(50) PRIMARY KEY,
                employee_num INTEGER NOT NULL,
                first_name VARCHAR(100),
                last_name VARCHAR(100),
                start_date VARCHAR(100),
                end_date VARCHAR(100),
                notes TEXT,
                leave_type VARCHAR(50),
                approved VARCHAR(50) DEFAULT 'Not Approved Yet',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (employee_num) REFERENCES users(employee_num) ON DELETE CASCADE
            )
        """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Leave requests table created/verified successfully!");
        } catch(SQLException e) {
            System.err.println("Error creating leave requests table: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Drop all tables (for testing purposes)
     */
    public static void dropAllTables() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String[] tables = {"attendance", "leave_requests", "employees", "users"};
            
            for (String table : tables) {
                String sql = "DROP TABLE IF EXISTS " + table + " CASCADE";
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(sql);
                    System.out.println("Dropped table: " + table);
                }
            }
            System.out.println("All tables dropped successfully!");
        } catch (SQLException e) {
            System.err.println("Error dropping tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
}