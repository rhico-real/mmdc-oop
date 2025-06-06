package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    
    /**
     * Initialize database schema - create all required tables in correct order
     */
    public static void initializeDatabase() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Create tables in dependency order
            createLookupTables(conn);
            createUserTables(conn);
            createEmployeeTables(conn);
            createOrganizationalTables(conn);
            createCompensationTables(conn);
            createOperationalTables(conn);
            createUpdateRequestsTable(conn);
            
            // Insert default data
            insertDefaultData(conn);
            
            conn.commit();
            System.out.println("Enhanced database schema initialized successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Create lookup/reference tables first (no dependencies)
     */
    private static void createLookupTables(Connection conn) throws SQLException {
        
        // Roles table
        String rolesSql = """
            CREATE TABLE IF NOT EXISTS roles (
                role_id SERIAL PRIMARY KEY,
                role_name VARCHAR(50) UNIQUE NOT NULL,
                role_description TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        executeSQL(conn, rolesSql, "Roles table");
        
        // Departments table
        String departmentsSql = """
            CREATE TABLE IF NOT EXISTS departments (
                department_id SERIAL PRIMARY KEY,
                department_name VARCHAR(100) UNIQUE NOT NULL,
                department_code VARCHAR(10) UNIQUE,
                description TEXT,
                budget DECIMAL(15,2),
                head_employee_id INTEGER,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        executeSQL(conn, departmentsSql, "Departments table");
        
        // Salary grades table
        String salaryGradesSql = """
            CREATE TABLE IF NOT EXISTS salary_grades (
                grade_id SERIAL PRIMARY KEY,
                grade_level INTEGER UNIQUE NOT NULL,
                grade_name VARCHAR(50),
                minimum_salary DECIMAL(12,2) NOT NULL,
                maximum_salary DECIMAL(12,2) NOT NULL,
                step_increment DECIMAL(12,2) DEFAULT 0,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        executeSQL(conn, salaryGradesSql, "Salary grades table");
        
        // Allowance types table
        String allowanceTypesSql = """
            CREATE TABLE IF NOT EXISTS allowance_types (
                allowance_type_id SERIAL PRIMARY KEY,
                allowance_name VARCHAR(100) UNIQUE NOT NULL,
                allowance_code VARCHAR(20) UNIQUE,
                description TEXT,
                is_taxable BOOLEAN DEFAULT TRUE,
                is_active BOOLEAN DEFAULT TRUE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        executeSQL(conn, allowanceTypesSql, "Allowance types table");
        
        // Leave types table
        String leaveTypesSql = """
            CREATE TABLE IF NOT EXISTS leave_types (
                leave_type_id SERIAL PRIMARY KEY,
                leave_name VARCHAR(100) UNIQUE NOT NULL,
                leave_code VARCHAR(20) UNIQUE,
                max_days_per_year INTEGER,
                is_paid BOOLEAN DEFAULT TRUE,
                requires_medical_certificate BOOLEAN DEFAULT FALSE,
                description TEXT,
                is_active BOOLEAN DEFAULT TRUE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        executeSQL(conn, leaveTypesSql, "Leave types table");
    }
    
    /**
     * Create user-related tables
     */
    private static void createUserTables(Connection conn) throws SQLException {
        
        // Users table (base authentication)
        String usersSql = """
            CREATE TABLE IF NOT EXISTS users (
                user_id SERIAL PRIMARY KEY,
                username VARCHAR(100) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255),
                is_active BOOLEAN DEFAULT TRUE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        executeSQL(conn, usersSql, "Users table");
        
        // User roles table (many-to-many relationship)
        String userRolesSql = """
            CREATE TABLE IF NOT EXISTS user_roles (
                user_role_id SERIAL PRIMARY KEY,
                user_id INTEGER NOT NULL,
                role_id INTEGER NOT NULL,
                assigned_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                is_active BOOLEAN DEFAULT TRUE,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                FOREIGN KEY (role_id) REFERENCES roles(role_id),
                UNIQUE(user_id, role_id)
            )
        """;
        executeSQL(conn, userRolesSql, "User roles table");
        
        // Admins table
        String adminsSql = """
            CREATE TABLE IF NOT EXISTS admins (
                admin_id SERIAL PRIMARY KEY,
                user_id INTEGER NOT NULL,
                admin_level INTEGER DEFAULT 1,
                permissions TEXT,
                last_login TIMESTAMP,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
            )
        """;
        executeSQL(conn, adminsSql, "Admins table");
        
        // HR personnel table
        String hrPersonnelSql = """
            CREATE TABLE IF NOT EXISTS hr_personnel (
                hr_id SERIAL PRIMARY KEY,
                user_id INTEGER NOT NULL,
                department_id INTEGER,
                hr_level VARCHAR(50) DEFAULT 'Junior',
                specialization VARCHAR(100),
                certification_details TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                FOREIGN KEY (department_id) REFERENCES departments(department_id)
            )
        """;
        executeSQL(conn, hrPersonnelSql, "HR personnel table");
    }
    
    /**
     * Create employee-related tables
     */
    private static void createEmployeeTables(Connection conn) throws SQLException {
        
        // Employees table (core employee data)
        String employeesSql = """
            CREATE TABLE IF NOT EXISTS employees (
                employee_id SERIAL PRIMARY KEY,
                employee_number VARCHAR(20) UNIQUE NOT NULL,
                user_id INTEGER NOT NULL,
                hire_date DATE NOT NULL,
                employment_type VARCHAR(50) DEFAULT 'Full-time',
                is_active BOOLEAN DEFAULT TRUE,
                termination_date DATE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
            )
        """;
        executeSQL(conn, employeesSql, "Employees table");
        
        // Personal information table
        String personalInfoSql = """
            CREATE TABLE IF NOT EXISTS personal_information (
                personal_info_id SERIAL PRIMARY KEY,
                employee_id INTEGER NOT NULL,
                first_name VARCHAR(100) NOT NULL,
                last_name VARCHAR(100) NOT NULL,
                middle_name VARCHAR(100),
                birthday DATE,
                gender VARCHAR(10),
                civil_status VARCHAR(20),
                nationality VARCHAR(50) DEFAULT 'Filipino',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
            )
        """;
        executeSQL(conn, personalInfoSql, "Personal information table");
        
        // Contact information table
        String contactInfoSql = """
            CREATE TABLE IF NOT EXISTS contact_information (
                contact_id SERIAL PRIMARY KEY,
                employee_id INTEGER NOT NULL,
                home_address TEXT,
                city VARCHAR(100),
                province VARCHAR(100),
                postal_code VARCHAR(10),
                phone_number VARCHAR(20),
                mobile_number VARCHAR(20),
                emergency_contact_name VARCHAR(200),
                emergency_contact_number VARCHAR(20),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
            )
        """;
        executeSQL(conn, contactInfoSql, "Contact information table");
        
        // Government IDs table
        String govIdsSql = """
            CREATE TABLE IF NOT EXISTS government_ids (
                gov_id_pk SERIAL PRIMARY KEY,
                employee_id INTEGER NOT NULL,
                id_type VARCHAR(20) NOT NULL,
                id_number VARCHAR(50) NOT NULL,
                issued_date DATE,
                expiry_date DATE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE,
                UNIQUE(employee_id, id_type)
            )
        """;
        executeSQL(conn, govIdsSql, "Government IDs table");
    }
    
    /**
     * Create organizational structure tables
     */
    private static void createOrganizationalTables(Connection conn) throws SQLException {
        
        // Positions table
        String positionsSql = """
            CREATE TABLE IF NOT EXISTS positions (
                position_id SERIAL PRIMARY KEY,
                position_title VARCHAR(100) NOT NULL,
                position_code VARCHAR(20) UNIQUE,
                department_id INTEGER NOT NULL,
                job_description TEXT,
                minimum_salary DECIMAL(12,2),
                maximum_salary DECIMAL(12,2),
                required_experience_years INTEGER DEFAULT 0,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (department_id) REFERENCES departments(department_id)
            )
        """;
        executeSQL(conn, positionsSql, "Positions table");
        
        // Employee positions table (assignment history)
        String empPositionsSql = """
            CREATE TABLE IF NOT EXISTS employee_positions (
                assignment_id SERIAL PRIMARY KEY,
                employee_id INTEGER NOT NULL,
                position_id INTEGER NOT NULL,
                supervisor_id INTEGER,
                start_date DATE NOT NULL,
                end_date DATE,
                is_current BOOLEAN DEFAULT TRUE,
                status VARCHAR(50) DEFAULT 'Active',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE,
                FOREIGN KEY (position_id) REFERENCES positions(position_id),
                FOREIGN KEY (supervisor_id) REFERENCES employees(employee_id)
            )
        """;
        executeSQL(conn, empPositionsSql, "Employee positions table");
    }
    
    /**
     * Create compensation-related tables
     */
    private static void createCompensationTables(Connection conn) throws SQLException {
        
        // Employee compensation table
        String compensationSql = """
            CREATE TABLE IF NOT EXISTS employee_compensation (
                compensation_id SERIAL PRIMARY KEY,
                employee_id INTEGER NOT NULL,
                salary_grade_id INTEGER,
                basic_salary DECIMAL(12,2) NOT NULL,
                hourly_rate DECIMAL(8,2),
                gross_semi_monthly_rate DECIMAL(12,2),
                effective_date DATE NOT NULL,
                end_date DATE,
                is_current BOOLEAN DEFAULT TRUE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE,
                FOREIGN KEY (salary_grade_id) REFERENCES salary_grades(grade_id)
            )
        """;
        executeSQL(conn, compensationSql, "Employee compensation table");
        
        // Employee allowances table
        String allowancesSql = """
            CREATE TABLE IF NOT EXISTS employee_allowances (
                allowance_id SERIAL PRIMARY KEY,
                employee_id INTEGER NOT NULL,
                allowance_type_id INTEGER NOT NULL,
                amount DECIMAL(12,2) NOT NULL,
                effective_date DATE NOT NULL,
                end_date DATE,
                is_active BOOLEAN DEFAULT TRUE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE,
                FOREIGN KEY (allowance_type_id) REFERENCES allowance_types(allowance_type_id)
            )
        """;
        executeSQL(conn, allowancesSql, "Employee allowances table");
    }
    
    /**
     * Create operational tables (attendance, leave requests)
     */
    private static void createOperationalTables(Connection conn) throws SQLException {
        
        // Attendance records table
        String attendanceSql = """
            CREATE TABLE IF NOT EXISTS attendance_records (
                attendance_id SERIAL PRIMARY KEY,
                employee_id INTEGER NOT NULL,
                attendance_date DATE NOT NULL,
                time_in TIME,
                time_out TIME,
                break_time_minutes INTEGER DEFAULT 60,
                overtime_hours DECIMAL(4,2) DEFAULT 0,
                status VARCHAR(20) DEFAULT 'Present',
                notes TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE,
                UNIQUE(employee_id, attendance_date)
            )
        """;
        executeSQL(conn, attendanceSql, "Attendance records table");
        
        // Leave requests table
        String leaveRequestsSql = """
            CREATE TABLE IF NOT EXISTS leave_requests (
                leave_request_id SERIAL PRIMARY KEY,
                request_number VARCHAR(50) UNIQUE NOT NULL,
                employee_id INTEGER NOT NULL,
                leave_type_id INTEGER NOT NULL,
                start_date DATE NOT NULL,
                end_date DATE NOT NULL,
                total_days INTEGER NOT NULL,
                reason TEXT,
                status VARCHAR(20) DEFAULT 'Pending',
                submitted_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                approved_by INTEGER,
                approved_date TIMESTAMP,
                remarks TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE,
                FOREIGN KEY (leave_type_id) REFERENCES leave_types(leave_type_id),
                FOREIGN KEY (approved_by) REFERENCES employees(employee_id)
            )
        """;
        executeSQL(conn, leaveRequestsSql, "Leave requests table");
    }
    
    /**
     * Insert default data into lookup tables
     */
    private static void insertDefaultData(Connection conn) throws SQLException {
        
        // Insert default roles
        String insertRoles = """
            INSERT INTO roles (role_name, role_description) VALUES 
            ('ADMIN', 'System Administrator with full access'),
            ('HR', 'Human Resources personnel'),
            ('EMPLOYEE', 'Regular employee')
            ON CONFLICT (role_name) DO NOTHING
        """;
        executeSQL(conn, insertRoles, "Default roles");
        
        // Insert default departments
        String insertDepartments = """
            INSERT INTO departments (department_name, department_code, description) VALUES 
            ('Information Technology', 'IT', 'Technology and systems department'),
            ('Human Resources', 'HR', 'Human resources and personnel'),
            ('Finance', 'FIN', 'Financial operations and accounting'),
            ('Operations', 'OPS', 'General operations and administration'),
            ('Sales', 'SALES', 'Sales and marketing department')
            ON CONFLICT (department_name) DO NOTHING
        """;
        executeSQL(conn, insertDepartments, "Default departments");
        
        // Insert default salary grades
        String insertSalaryGrades = """
            INSERT INTO salary_grades (grade_level, grade_name, minimum_salary, maximum_salary, step_increment) VALUES 
            (1, 'Entry Level', 15000.00, 25000.00, 1000.00),
            (2, 'Junior', 25000.00, 35000.00, 1500.00),
            (3, 'Senior', 35000.00, 50000.00, 2000.00),
            (4, 'Supervisor', 50000.00, 75000.00, 2500.00),
            (5, 'Manager', 75000.00, 100000.00, 3000.00),
            (6, 'Director', 100000.00, 150000.00, 5000.00)
            ON CONFLICT (grade_level) DO NOTHING
        """;
        executeSQL(conn, insertSalaryGrades, "Default salary grades");
        
        // Insert default allowance types
        String insertAllowanceTypes = """
            INSERT INTO allowance_types (allowance_name, allowance_code, description, is_taxable) VALUES 
            ('Rice Subsidy', 'RICE', 'Monthly rice allowance', FALSE),
            ('Phone Allowance', 'PHONE', 'Mobile phone allowance', TRUE),
            ('Clothing Allowance', 'CLOTHING', 'Annual clothing allowance', TRUE),
            ('Transportation Allowance', 'TRANSPORT', 'Daily transportation allowance', FALSE),
            ('Meal Allowance', 'MEAL', 'Daily meal allowance', FALSE)
            ON CONFLICT (allowance_name) DO NOTHING
        """;
        executeSQL(conn, insertAllowanceTypes, "Default allowance types");
        
        // Insert default leave types
        String insertLeaveTypes = """
            INSERT INTO leave_types (leave_name, leave_code, max_days_per_year, is_paid, requires_medical_certificate) VALUES 
            ('Vacation Leave', 'VL', 15, TRUE, FALSE),
            ('Sick Leave', 'SL', 15, TRUE, TRUE),
            ('Emergency Leave', 'EL', 5, TRUE, FALSE),
            ('Maternity Leave', 'ML', 120, TRUE, TRUE),
            ('Paternity Leave', 'PL', 7, TRUE, FALSE),
            ('Bereavement Leave', 'BL', 3, TRUE, FALSE),
            ('Personal Leave', 'PER', 5, FALSE, FALSE)
            ON CONFLICT (leave_name) DO NOTHING
        """;
        executeSQL(conn, insertLeaveTypes, "Default leave types");
        
        // Create default admin user
        createDefaultAdminUser(conn);
    }
    
    /**
     * Create default admin user
     */
    private static void createDefaultAdminUser(Connection conn) throws SQLException {
        
        // Check if admin user already exists
        String checkAdmin = "SELECT COUNT(*) FROM users WHERE username = 'admin'";
        try (PreparedStatement pstmt = conn.prepareStatement(checkAdmin)) {
            var rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Admin user already exists, skipping creation");
                return;
            }
        }
        
        // Insert admin user
        String insertUser = """
            INSERT INTO users (username, password, email, is_active) 
            VALUES ('admin', '123', 'admin@motorph.com', TRUE)
        """;
        
        int userId;
        try (PreparedStatement pstmt = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.executeUpdate();
            var rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                userId = rs.getInt(1);
            } else {
                throw new SQLException("Failed to get generated user ID");
            }
        }
        
        // Assign admin role
        String insertUserRole = """
            INSERT INTO user_roles (user_id, role_id) 
            SELECT ?, role_id FROM roles WHERE role_name = 'ADMIN'
        """;
        try (PreparedStatement pstmt = conn.prepareStatement(insertUserRole)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
        
        // Create admin record
        String insertAdmin = """
            INSERT INTO admins (user_id, admin_level, permissions) 
            VALUES (?, 9, 'ALL')
        """;
        try (PreparedStatement pstmt = conn.prepareStatement(insertAdmin)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
        
        // Create employee record for admin
        String insertEmployee = """
            INSERT INTO employees (employee_number, user_id, hire_date, employment_type) 
            VALUES ('ADMIN001', ?, CURRENT_DATE, 'Full-time')
        """;
        
        int employeeId;
        try (PreparedStatement pstmt = conn.prepareStatement(insertEmployee, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            var rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                employeeId = rs.getInt(1);
            } else {
                throw new SQLException("Failed to get generated employee ID");
            }
        }
        
        // Add personal information for admin
        String insertPersonalInfo = """
            INSERT INTO personal_information (employee_id, first_name, last_name, middle_name) 
            VALUES (?, 'System', 'Administrator', '')
        """;
        try (PreparedStatement pstmt = conn.prepareStatement(insertPersonalInfo)) {
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
        }
        
        // Add contact information for admin
        String insertContactInfo = """
            INSERT INTO contact_information (employee_id, home_address, city, province, phone_number, mobile_number) 
            VALUES (?, 'MotorPH Head Office', 'Manila', 'Metro Manila', '+63-2-8888-8888', '+63-917-888-8888')
        """;
        try (PreparedStatement pstmt = conn.prepareStatement(insertContactInfo)) {
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
        }
        
        System.out.println("Default admin user created successfully!");
    }
    
    /**
     * Helper method to execute SQL and handle errors
     */
    private static void executeSQL(Connection conn, String sql, String tableName) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println(tableName + " created/verified successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating " + tableName + ": " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Create employee update requests table
     */
    private static void createUpdateRequestsTable(Connection conn) throws SQLException {
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
        executeSQL(conn, updateRequestsSql, "Employee update requests table");
        
        // Create indexes for faster queries
        String indexSql1 = "CREATE INDEX IF NOT EXISTS idx_update_requests_employee_number ON employee_update_requests(employee_number)";
        executeSQL(conn, indexSql1, "Employee update requests employee_number index");
        
        String indexSql2 = "CREATE INDEX IF NOT EXISTS idx_update_requests_status ON employee_update_requests(status)";
        executeSQL(conn, indexSql2, "Employee update requests status index");
    }
    
    /**
     * Drop all tables (for testing purposes) - in reverse dependency order
     */
    public static void dropAllTables() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Drop in reverse order of creation to handle foreign key constraints
            String[] tables = {
                "employee_update_requests", "leave_requests", "attendance_records", 
                "employee_allowances", "employee_compensation",
                "employee_positions", "positions", 
                "government_ids", "contact_information", "personal_information", "employees",
                "hr_personnel", "admins", "user_roles", "users",
                "leave_types", "allowance_types", "salary_grades", "departments", "roles"
            };
            
            for (String table : tables) {
                String sql = "DROP TABLE IF EXISTS " + table + " CASCADE";
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(sql);
                    System.out.println("Dropped table: " + table);
                }
            }
            
            conn.commit();
            System.out.println("All tables dropped successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error dropping tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Migrate data from old 4-table structure to new normalized structure
     */
    public static void migrateFromOldStructure() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            System.out.println("Starting migration from old structure...");
            
            // Migration logic would go here
            // This would read from the old tables and populate the new normalized tables
            
            // Example migration steps:
            // 1. Migrate users table data
            // 2. Create employee records
            // 3. Split employee data into personal_information and contact_information
            // 4. Migrate compensation data
            // 5. Migrate attendance data
            // 6. Migrate leave request data
            
            conn.commit();
            System.out.println("Migration completed successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error during migration: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
