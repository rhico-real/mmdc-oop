package Database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Alternative installer for database objects using direct execution
 * This class can be used if the regular installer fails
 */
public class DirectDatabaseInstaller {
    
    /**
     * Install the database objects directly
     * @return true if installation successful, false otherwise
     */
    public static boolean installDatabaseObjects() {
        System.out.println("Installing database objects directly...");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Create the views first
            createViews(conn);
            
            // Then create the stored procedures
            createProcedures(conn);
            
            System.out.println("All database objects installed successfully!");
            return true;
        } catch (SQLException e) {
            System.err.println("Error installing database objects: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Create views in the database
     * @param conn Database connection
     * @throws SQLException if an error occurs
     */
    private static void createViews(Connection conn) throws SQLException {
        System.out.println("Creating views...");
        
        // Employee information view
        executeSQL(conn, 
            "CREATE OR REPLACE VIEW vw_employee_information AS " +
            "SELECT " +
            "    e.employee_id, " +
            "    e.employee_number, " +
            "    e.hire_date, " +
            "    e.employment_type, " +
            "    e.is_active, " +
            "    u.username, " +
            "    u.email, " +
            "    pi.first_name, " +
            "    pi.last_name, " +
            "    pi.middle_name, " +
            "    pi.birthday, " +
            "    pi.gender, " +
            "    pi.civil_status, " +
            "    pi.nationality, " +
            "    ci.home_address, " +
            "    ci.city, " +
            "    ci.province, " +
            "    ci.postal_code, " +
            "    ci.phone_number, " +
            "    ci.mobile_number, " +
            "    ci.emergency_contact_name, " +
            "    ci.emergency_contact_number, " +
            "    p.position_title, " +
            "    d.department_name, " +
            "    ep.supervisor_id, " +
            "    ep.status as position_status " +
            "FROM employees e " +
            "JOIN users u ON e.user_id = u.user_id " +
            "LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id " +
            "LEFT JOIN contact_information ci ON e.employee_id = ci.employee_id " +
            "LEFT JOIN employee_positions ep ON e.employee_id = ep.employee_id AND ep.is_current = TRUE " +
            "LEFT JOIN positions p ON ep.position_id = p.position_id " +
            "LEFT JOIN departments d ON p.department_id = d.department_id"
        );
        
        // Employee compensation view
        executeSQL(conn, 
            "CREATE OR REPLACE VIEW vw_employee_compensation AS " +
            "SELECT " +
            "    e.employee_id, " +
            "    e.employee_number, " +
            "    pi.first_name, " +
            "    pi.last_name, " +
            "    p.position_title, " +
            "    ep.status, " +
            "    ec.basic_salary, " +
            "    ec.hourly_rate, " +
            "    ec.gross_semi_monthly_rate, " +
            "    sg.grade_name, " +
            "    sg.grade_level " +
            "FROM employees e " +
            "LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id " +
            "LEFT JOIN employee_positions ep ON e.employee_id = ep.employee_id AND ep.is_current = TRUE " +
            "LEFT JOIN positions p ON ep.position_id = p.position_id " +
            "LEFT JOIN employee_compensation ec ON e.employee_id = ec.employee_id AND ec.is_current = TRUE " +
            "LEFT JOIN salary_grades sg ON ec.salary_grade_id = sg.grade_id"
        );
        
        // Employee government IDs view
        executeSQL(conn, 
            "CREATE OR REPLACE VIEW vw_employee_government_ids AS " +
            "SELECT " +
            "    e.employee_id, " +
            "    e.employee_number, " +
            "    pi.first_name, " +
            "    pi.last_name, " +
            "    pi.birthday, " +
            "    ci.home_address, " +
            "    ci.phone_number, " +
            "    p.position_title, " +
            "    ep.status, " +
            "    ep.supervisor_id, " +
            "    gi.id_type, " +
            "    gi.id_number " +
            "FROM employees e " +
            "JOIN personal_information pi ON e.employee_id = pi.employee_id " +
            "LEFT JOIN contact_information ci ON e.employee_id = ci.employee_id " +
            "LEFT JOIN employee_positions ep ON e.employee_id = ep.employee_id AND ep.is_current = TRUE " +
            "LEFT JOIN positions p ON ep.position_id = p.position_id " +
            "LEFT JOIN government_ids gi ON e.employee_id = gi.employee_id"
        );
        
        // Employee allowances view
        executeSQL(conn, 
            "CREATE OR REPLACE VIEW vw_employee_allowances AS " +
            "SELECT " +
            "    e.employee_id, " +
            "    e.employee_number, " +
            "    at.allowance_name, " +
            "    ea.amount " +
            "FROM employees e " +
            "JOIN employee_allowances ea ON e.employee_id = ea.employee_id " +
            "JOIN allowance_types at ON ea.allowance_type_id = at.allowance_type_id " +
            "WHERE ea.is_active = TRUE"
        );
        
        // Attendance records view
        executeSQL(conn, 
            "CREATE OR REPLACE VIEW vw_attendance_records AS " +
            "SELECT " +
            "    ar.attendance_id, " +
            "    ar.attendance_date, " +
            "    ar.time_in, " +
            "    ar.time_out, " +
            "    ar.break_time_minutes, " +
            "    ar.overtime_hours, " +
            "    ar.status, " +
            "    ar.notes, " +
            "    e.employee_id, " +
            "    e.employee_number, " +
            "    pi.first_name, " +
            "    pi.last_name " +
            "FROM attendance_records ar " +
            "JOIN employees e ON ar.employee_id = e.employee_id " +
            "LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id"
        );
        
        // Leave requests view
        executeSQL(conn, 
            "CREATE OR REPLACE VIEW vw_leave_requests AS " +
            "SELECT " +
            "    lr.leave_request_id, " +
            "    lr.request_number, " +
            "    e.employee_id, " +
            "    e.employee_number, " +
            "    pi.first_name, " +
            "    pi.last_name, " +
            "    lr.start_date, " +
            "    lr.end_date, " +
            "    lr.total_days, " +
            "    lr.reason, " +
            "    lt.leave_name AS leave_type, " +
            "    lr.status, " +
            "    lr.submitted_date, " +
            "    lr.approved_by, " +
            "    lr.approved_date, " +
            "    lr.remarks, " +
            "    lr.created_at, " +
            "    lr.updated_at " +
            "FROM leave_requests lr " +
            "JOIN employees e ON lr.employee_id = e.employee_id " +
            "LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id " +
            "LEFT JOIN leave_types lt ON lr.leave_type_id = lt.leave_type_id"
        );
        
        // Users with roles view
        executeSQL(conn, 
            "CREATE OR REPLACE VIEW vw_users_with_roles AS " +
            "SELECT " +
            "    u.user_id, " +
            "    u.username, " +
            "    u.password, " +
            "    u.email, " +
            "    u.is_active, " +
            "    e.employee_id, " +
            "    e.employee_number, " +
            "    STRING_AGG(r.role_name, ',') as roles " +
            "FROM users u " +
            "LEFT JOIN employees e ON u.user_id = e.user_id " +
            "LEFT JOIN user_roles ur ON u.user_id = ur.user_id AND ur.is_active = TRUE " +
            "LEFT JOIN roles r ON ur.role_id = r.role_id " +
            "GROUP BY u.user_id, u.username, u.password, u.email, u.is_active, e.employee_id, e.employee_number"
        );
        
        // Employee update requests view
        executeSQL(conn, 
            "CREATE OR REPLACE VIEW vw_employee_update_requests AS " +
            "SELECT " +
            "    eur.request_id, " +
            "    eur.employee_number, " +
            "    eur.first_name, " +
            "    eur.last_name, " +
            "    eur.birthday, " +
            "    eur.address, " +
            "    eur.phone_number, " +
            "    eur.sss_number, " +
            "    eur.philhealth_number, " +
            "    eur.tin_number, " +
            "    eur.pagibig_number, " +
            "    eur.request_date, " +
            "    eur.status, " +
            "    eur.admin_notes, " +
            "    e.employee_id, " +
            "    pi.first_name AS current_first_name, " +
            "    pi.last_name AS current_last_name, " +
            "    pi.birthday AS current_birthday, " +
            "    ci.home_address AS current_address, " +
            "    ci.phone_number AS current_phone_number " +
            "FROM employee_update_requests eur " +
            "JOIN employees e ON eur.employee_number = e.employee_number " +
            "LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id " +
            "LEFT JOIN contact_information ci ON e.employee_id = ci.employee_id"
        );
        
        System.out.println("Views created successfully");
    }
    
    /**
     * Create stored procedures in the database
     * @param conn Database connection
     * @throws SQLException if an error occurs
     */
    private static void createProcedures(Connection conn) throws SQLException {
        System.out.println("Creating stored procedures...");
        
        // Update requests table creation procedure
        executeSQL(conn, 
            "CREATE OR REPLACE PROCEDURE sp_ensure_update_requests_table_exists() " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    IF NOT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'employee_update_requests') THEN " +
            "        CREATE TABLE employee_update_requests ( " +
            "            request_id SERIAL PRIMARY KEY, " +
            "            employee_number VARCHAR(50) NOT NULL, " +
            "            first_name VARCHAR(100) NOT NULL, " +
            "            last_name VARCHAR(100) NOT NULL, " +
            "            birthday VARCHAR(20), " +
            "            address TEXT, " +
            "            phone_number VARCHAR(20), " +
            "            sss_number VARCHAR(20), " +
            "            philhealth_number VARCHAR(20), " +
            "            tin_number VARCHAR(20), " +
            "            pagibig_number VARCHAR(20), " +
            "            request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "            status VARCHAR(20) DEFAULT 'PENDING', " +
            "            admin_notes TEXT " +
            "        ); " +
            "        " +
            "        CREATE INDEX idx_update_requests_employee_number " +
            "        ON employee_update_requests(employee_number); " +
            "        " +
            "        CREATE INDEX idx_update_requests_status " +
            "        ON employee_update_requests(status); " +
            "    END IF; " +
            "END; " +
            "$BODY$;"
        );
        
        // Create update request procedure
        executeSQL(conn, 
            "CREATE OR REPLACE PROCEDURE sp_create_update_request( " +
            "    IN p_employee_number VARCHAR(50), " +
            "    IN p_first_name VARCHAR(100), " +
            "    IN p_last_name VARCHAR(100), " +
            "    IN p_birthday VARCHAR(20), " +
            "    IN p_address TEXT, " +
            "    IN p_phone_number VARCHAR(20), " +
            "    IN p_sss_number VARCHAR(20), " +
            "    IN p_philhealth_number VARCHAR(20), " +
            "    IN p_tin_number VARCHAR(20), " +
            "    IN p_pagibig_number VARCHAR(20), " +
            "    IN p_status VARCHAR(20), " +
            "    OUT p_request_id INTEGER, " +
            "    OUT p_success BOOLEAN, " +
            "    OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    CALL sp_ensure_update_requests_table_exists(); " +
            "    " +
            "    IF NOT EXISTS (SELECT 1 FROM employees WHERE employee_number = p_employee_number) THEN " +
            "        p_success := FALSE; " +
            "        p_error_message := 'Employee not found: ' || p_employee_number; " +
            "        RETURN; " +
            "    END IF; " +
            "    " +
            "    INSERT INTO employee_update_requests ( " +
            "        employee_number, first_name, last_name, birthday, address, phone_number, " +
            "        sss_number, philhealth_number, tin_number, pagibig_number, request_date, status " +
            "    ) " +
            "    VALUES ( " +
            "        p_employee_number, p_first_name, p_last_name, p_birthday, p_address, p_phone_number, " +
            "        p_sss_number, p_philhealth_number, p_tin_number, p_pagibig_number, CURRENT_TIMESTAMP, " +
            "        COALESCE(p_status, 'PENDING') " +
            "    ) " +
            "    RETURNING request_id INTO p_request_id; " +
            "    " +
            "    IF FOUND THEN " +
            "        p_success := TRUE; " +
            "    ELSE " +
            "        p_success := FALSE; " +
            "        p_error_message := 'Failed to create update request'; " +
            "    END IF; " +
            "EXCEPTION " +
            "    WHEN OTHERS THEN " +
            "        p_success := FALSE; " +
            "        p_error_message := SQLERRM; " +
            "END; " +
            "$BODY$;"
        );
        
        // Function to check if employee has pending requests
        executeSQL(conn, 
            "CREATE OR REPLACE FUNCTION sp_has_employee_pending_requests(p_employee_number VARCHAR(50)) " +
            "RETURNS BOOLEAN " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "DECLARE " +
            "    v_has_pending BOOLEAN; " +
            "BEGIN " +
            "    CALL sp_ensure_update_requests_table_exists(); " +
            "    " +
            "    SELECT EXISTS( " +
            "        SELECT 1 FROM employee_update_requests " +
            "        WHERE employee_number = p_employee_number " +
            "        AND status = 'PENDING' " +
            "    ) INTO v_has_pending; " +
            "    " +
            "    RETURN v_has_pending; " +
            "END; " +
            "$BODY$;"
        );
        
        // Add essential missing procedures that DAOs expect
        
        // Drop existing functions first to avoid conflicts
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_authenticate_user(VARCHAR, VARCHAR)");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_all_employees()");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_employee_compensation(VARCHAR)");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_employee_gov_ids(VARCHAR)");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_search_employees_by_name(VARCHAR)");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_user_by_employee_number(VARCHAR)");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_user_by_username(VARCHAR)");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_all_users()");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_update_requests(VARCHAR)");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_update_request_by_id(INTEGER)");
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_create_employee");
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_update_employee");
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_delete_employee");
        
        System.out.println("Dropped existing functions/procedures...");
        
        // User authentication function
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_authenticate_user(p_username VARCHAR(100), p_password VARCHAR(255)) " +
            "RETURNS TABLE ( " +
            "    user_id INTEGER, " +
            "    username VARCHAR(100), " +
            "    password VARCHAR(255), " +
            "    email VARCHAR(255), " +
            "    is_active BOOLEAN, " +
            "    employee_id INTEGER, " +
            "    employee_number VARCHAR(20), " +
            "    roles TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_users_with_roles " +
            "    WHERE vw_users_with_roles.username = p_username " +
            "    AND vw_users_with_roles.password = p_password " +
            "    AND vw_users_with_roles.is_active = TRUE; " +
            "END; " +
            "$BODY$;"
        );
        
        // Get all employees function
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_all_employees() " +
            "RETURNS SETOF vw_employee_information " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_employee_information " +
            "    WHERE is_active = TRUE " +
            "    ORDER BY employee_number; " +
            "END; " +
            "$BODY$;"
        );
        
        // Get employee compensation function
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_employee_compensation(p_employee_number VARCHAR(20)) " +
            "RETURNS TABLE ( " +
            "    employee_id INT, " +
            "    employee_number VARCHAR(20), " +
            "    first_name VARCHAR(100), " +
            "    last_name VARCHAR(100), " +
            "    position_title VARCHAR(100), " +
            "    status VARCHAR(50), " +
            "    basic_salary DECIMAL(12,2), " +
            "    hourly_rate DECIMAL(8,2), " +
            "    gross_semi_monthly_rate DECIMAL(12,2), " +
            "    grade_name VARCHAR(50), " +
            "    grade_level INTEGER, " +
            "    allowance_name VARCHAR(100), " +
            "    amount DECIMAL(12,2) " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT " +
            "        c.employee_id, " +
            "        c.employee_number, " +
            "        c.first_name, " +
            "        c.last_name, " +
            "        c.position_title, " +
            "        c.status, " +
            "        c.basic_salary, " +
            "        c.hourly_rate, " +
            "        c.gross_semi_monthly_rate, " +
            "        c.grade_name, " +
            "        c.grade_level, " +
            "        a.allowance_name, " +
            "        a.amount " +
            "    FROM vw_employee_compensation c " +
            "    LEFT JOIN vw_employee_allowances a ON c.employee_id = a.employee_id " +
            "    WHERE c.employee_number = p_employee_number; " +
            "END; " +
            "$BODY$;"
        );
        
        // Get employee gov IDs function
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_employee_gov_ids(p_employee_number VARCHAR(20)) " +
            "RETURNS SETOF vw_employee_government_ids " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_employee_government_ids " +
            "    WHERE employee_number = p_employee_number; " +
            "END; " +
            "$BODY$;"
        );
        
        // Search employees by name function
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_search_employees_by_name(p_search_term VARCHAR(100)) " +
            "RETURNS SETOF vw_employee_information " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_employee_information " +
            "    WHERE is_active = TRUE " +
            "    AND ( " +
            "        last_name ILIKE '%' || p_search_term || '%' OR " +
            "        first_name ILIKE '%' || p_search_term || '%' OR " +
            "        CONCAT(first_name, ' ', last_name) ILIKE '%' || p_search_term || '%' " +
            "    ) " +
            "    ORDER BY last_name, first_name; " +
            "END; " +
            "$BODY$;"
        );
        
        // Get user functions
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_user_by_employee_number(p_employee_number VARCHAR(20)) " +
            "RETURNS SETOF vw_users_with_roles " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_users_with_roles " +
            "    WHERE employee_number = p_employee_number " +
            "    AND is_active = TRUE; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_user_by_username(p_username VARCHAR(100)) " +
            "RETURNS SETOF vw_users_with_roles " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_users_with_roles " +
            "    WHERE username = p_username " +
            "    AND is_active = TRUE; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_all_users() " +
            "RETURNS SETOF vw_users_with_roles " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_users_with_roles " +
            "    WHERE is_active = TRUE " +
            "    ORDER BY username; " +
            "END; " +
            "$BODY$;"
        );
        
        // UpdateRequest functions
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_update_requests(p_status VARCHAR(20) DEFAULT NULL) " +
            "RETURNS SETOF vw_employee_update_requests " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    CALL sp_ensure_update_requests_table_exists(); " +
            "    " +
            "    IF p_status IS NOT NULL THEN " +
            "        RETURN QUERY " +
            "        SELECT * FROM vw_employee_update_requests " +
            "        WHERE status = p_status " +
            "        ORDER BY request_date DESC; " +
            "    ELSE " +
            "        RETURN QUERY " +
            "        SELECT * FROM vw_employee_update_requests " +
            "        ORDER BY request_date DESC; " +
            "    END IF; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_update_request_by_id(p_request_id INTEGER) " +
            "RETURNS SETOF vw_employee_update_requests " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    CALL sp_ensure_update_requests_table_exists(); " +
            "    " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_employee_update_requests " +
            "    WHERE request_id = p_request_id; " +
            "END; " +
            "$BODY$;"
        );
        
        // Add missing AttendanceDAO procedures
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_attendance_record(VARCHAR, TEXT)");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_attendance_by_employee(VARCHAR)");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_attendance_by_date(TEXT)");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_all_attendance_records()");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_attendance_by_date_range(TEXT, TEXT)");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_calculate_monthly_attendance(VARCHAR, INTEGER, INTEGER)");
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_save_attendance_record");
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_delete_attendance_record");
        
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_attendance_record(p_employee_number VARCHAR(20), p_date TEXT) " +
            "RETURNS SETOF vw_attendance_records " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_attendance_records " +
            "    WHERE employee_number = p_employee_number " +
            "    AND attendance_date = p_date::date; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_attendance_by_employee(p_employee_number VARCHAR(20)) " +
            "RETURNS SETOF vw_attendance_records " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_attendance_records " +
            "    WHERE employee_number = p_employee_number " +
            "    ORDER BY attendance_date DESC; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_attendance_by_date(p_date TEXT) " +
            "RETURNS SETOF vw_attendance_records " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_attendance_records " +
            "    WHERE attendance_date = p_date::date " +
            "    ORDER BY employee_number; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_all_attendance_records() " +
            "RETURNS SETOF vw_attendance_records " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_attendance_records " +
            "    ORDER BY attendance_date DESC, employee_number; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_attendance_by_date_range(p_start_date TEXT, p_end_date TEXT) " +
            "RETURNS SETOF vw_attendance_records " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_attendance_records " +
            "    WHERE attendance_date >= p_start_date::date " +
            "    AND attendance_date <= p_end_date::date " +
            "    ORDER BY attendance_date, employee_number; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_calculate_monthly_attendance(p_employee_number VARCHAR(20), p_month INTEGER, p_year INTEGER) " +
            "RETURNS TABLE (days_worked INTEGER, overtime_hours DECIMAL(5,2)) " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "DECLARE " +
            "    v_start_date DATE; " +
            "    v_end_date DATE; " +
            "    v_days_worked INTEGER := 0; " +
            "    v_total_overtime_hours DECIMAL(5,2) := 0; " +
            "BEGIN " +
            "    v_start_date := make_date(p_year, p_month, 1); " +
            "    v_end_date := (v_start_date + INTERVAL '1 month - 1 day')::date; " +
            "    " +
            "    SELECT COUNT(*), COALESCE(SUM(overtime_hours), 0) " +
            "    INTO v_days_worked, v_total_overtime_hours " +
            "    FROM vw_attendance_records " +
            "    WHERE employee_number = p_employee_number " +
            "    AND attendance_date BETWEEN v_start_date AND v_end_date " +
            "    AND time_in IS NOT NULL AND time_out IS NOT NULL; " +
            "    " +
            "    RETURN QUERY SELECT v_days_worked, v_total_overtime_hours; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_save_attendance_record( " +
            "    IN p_employee_number VARCHAR(20), IN p_date TEXT, " +
            "    IN p_time_in TEXT, IN p_time_out TEXT, " +
            "    OUT p_success BOOLEAN, OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    p_success := FALSE; " +
            "    p_error_message := 'Attendance record saving not implemented'; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_delete_attendance_record( " +
            "    IN p_employee_number VARCHAR(20), IN p_date TEXT, " +
            "    OUT p_success BOOLEAN, OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    p_success := FALSE; " +
            "    p_error_message := 'Attendance record deletion not implemented'; " +
            "END; " +
            "$BODY$;"
        );
        
        // Add missing LeaveRequestDAO procedures
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_leave_request_by_id(VARCHAR)");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_all_leave_requests()");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_leave_requests_by_employee(VARCHAR)");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_pending_leave_requests()");
        executeSQL(conn, "DROP FUNCTION IF EXISTS sp_get_leave_requests_by_status(VARCHAR)");
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_create_leave_request");
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_update_leave_request_status");
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_delete_leave_request");
        
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_leave_request_by_id(p_request_number VARCHAR(50)) " +
            "RETURNS SETOF vw_leave_requests " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_leave_requests " +
            "    WHERE request_number = p_request_number; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_all_leave_requests() " +
            "RETURNS SETOF vw_leave_requests " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_leave_requests " +
            "    ORDER BY created_at DESC; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_leave_requests_by_employee(p_employee_number VARCHAR(20)) " +
            "RETURNS SETOF vw_leave_requests " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_leave_requests " +
            "    WHERE employee_number = p_employee_number " +
            "    ORDER BY created_at DESC; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_pending_leave_requests() " +
            "RETURNS SETOF vw_leave_requests " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_leave_requests " +
            "    WHERE status = 'Pending' " +
            "    ORDER BY created_at ASC; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE FUNCTION sp_get_leave_requests_by_status(p_status VARCHAR(20)) " +
            "RETURNS SETOF vw_leave_requests " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    RETURN QUERY " +
            "    SELECT * FROM vw_leave_requests " +
            "    WHERE status = p_status " +
            "    ORDER BY created_at DESC; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_create_leave_request( " +
            "    IN p_employee_number VARCHAR(20), IN p_leave_type VARCHAR(100), " +
            "    IN p_start_date VARCHAR(20), IN p_end_date VARCHAR(20), " +
            "    IN p_reason TEXT, OUT p_request_number VARCHAR(50), " +
            "    OUT p_success BOOLEAN, OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    p_success := FALSE; " +
            "    p_error_message := 'Leave request creation not implemented'; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_update_leave_request_status( " +
            "    IN p_request_number VARCHAR(50), IN p_status VARCHAR(20), " +
            "    IN p_approved_by INTEGER, IN p_remarks TEXT, " +
            "    OUT p_success BOOLEAN, OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    p_success := FALSE; " +
            "    p_error_message := 'Leave request status update not implemented'; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_delete_leave_request( " +
            "    IN p_request_number VARCHAR(50), " +
            "    OUT p_success BOOLEAN, OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    p_success := FALSE; " +
            "    p_error_message := 'Leave request deletion not implemented'; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_create_user_with_role");
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_update_user");
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_assign_role_to_user");
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_remove_role_from_user");
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_delete_user");
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_update_username");
        
        // User management stub procedures
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_create_user_with_role( " +
            "    IN p_username VARCHAR(100), IN p_password VARCHAR(255), " +
            "    IN p_email VARCHAR(255), IN p_role_name VARCHAR(50), " +
            "    OUT p_user_id INTEGER, OUT p_success BOOLEAN, OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    p_success := FALSE; " +
            "    p_error_message := 'User creation not implemented'; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_update_user( " +
            "    IN p_old_username VARCHAR(100), IN p_new_username VARCHAR(100), " +
            "    IN p_password VARCHAR(255), OUT p_success BOOLEAN, OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    p_success := FALSE; " +
            "    p_error_message := 'User update not implemented'; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_assign_role_to_user( " +
            "    IN p_username VARCHAR(100), IN p_role_name VARCHAR(50), " +
            "    OUT p_success BOOLEAN, OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    p_success := FALSE; " +
            "    p_error_message := 'Role assignment not implemented'; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_remove_role_from_user( " +
            "    IN p_username VARCHAR(100), IN p_role_name VARCHAR(50), " +
            "    OUT p_success BOOLEAN, OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    p_success := FALSE; " +
            "    p_error_message := 'Role removal not implemented'; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_delete_user( " +
            "    IN p_employee_number VARCHAR(20), " +
            "    OUT p_success BOOLEAN, OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    p_success := FALSE; " +
            "    p_error_message := 'User deletion not implemented'; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_update_username( " +
            "    IN p_employee_number VARCHAR(20), IN p_new_username VARCHAR(100), " +
            "    OUT p_success BOOLEAN, OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    p_success := FALSE; " +
            "    p_error_message := 'Username update not implemented'; " +
            "END; " +
            "$BODY$;"
        );
        
        // UpdateRequest management procedures
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_update_request_status");
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_approve_update_request");
        executeSQL(conn, "DROP PROCEDURE IF EXISTS sp_reject_update_request");
        
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_update_request_status( " +
            "    IN p_request_id INTEGER, IN p_status VARCHAR(20), " +
            "    IN p_admin_notes TEXT, OUT p_success BOOLEAN, OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    CALL sp_ensure_update_requests_table_exists(); " +
            "    " +
            "    UPDATE employee_update_requests " +
            "    SET status = p_status, admin_notes = p_admin_notes " +
            "    WHERE request_id = p_request_id; " +
            "    " +
            "    IF FOUND THEN " +
            "        p_success := TRUE; " +
            "    ELSE " +
            "        p_success := FALSE; " +
            "        p_error_message := 'Update request not found'; " +
            "    END IF; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_approve_update_request( " +
            "    IN p_request_id INTEGER, IN p_admin_notes TEXT, " +
            "    OUT p_success BOOLEAN, OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    p_success := FALSE; " +
            "    p_error_message := 'Update request approval not implemented'; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_reject_update_request( " +
            "    IN p_request_id INTEGER, IN p_admin_notes TEXT, " +
            "    OUT p_success BOOLEAN, OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    CALL sp_update_request_status(p_request_id, 'REJECTED', p_admin_notes, p_success, p_error_message); " +
            "END; " +
            "$BODY$;"
        );
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_create_employee( " +
            "    IN p_employee_number VARCHAR(20), IN p_username VARCHAR(100), IN p_password VARCHAR(255), " +
            "    IN p_email VARCHAR(255), IN p_first_name VARCHAR(100), IN p_last_name VARCHAR(100), " +
            "    IN p_birthday DATE, IN p_address TEXT, IN p_phone_number VARCHAR(20), " +
            "    IN p_position_title VARCHAR(100), IN p_department_name VARCHAR(100), " +
            "    IN p_basic_salary DECIMAL(12,2), IN p_hourly_rate DECIMAL(8,2), " +
            "    IN p_gross_semi_monthly_rate DECIMAL(12,2), IN p_sss_number VARCHAR(50), " +
            "    IN p_philhealth_number VARCHAR(50), IN p_tin_number VARCHAR(50), " +
            "    IN p_pagibig_number VARCHAR(50), IN p_rice_subsidy DECIMAL(12,2), " +
            "    IN p_phone_allowance DECIMAL(12,2), IN p_clothing_allowance DECIMAL(12,2), " +
            "    OUT p_success BOOLEAN, OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    p_success := FALSE; " +
            "    p_error_message := 'Employee creation not implemented in DirectDatabaseInstaller'; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_update_employee( " +
            "    IN p_employee_number VARCHAR(20), IN p_first_name VARCHAR(100), " +
            "    IN p_last_name VARCHAR(100), IN p_birthday DATE, IN p_address TEXT, " +
            "    IN p_phone_number VARCHAR(20), IN p_basic_salary DECIMAL(12,2), " +
            "    IN p_hourly_rate DECIMAL(8,2), IN p_gross_semi_monthly_rate DECIMAL(12,2), " +
            "    IN p_sss_number VARCHAR(50), IN p_philhealth_number VARCHAR(50), " +
            "    IN p_tin_number VARCHAR(50), IN p_pagibig_number VARCHAR(50), " +
            "    IN p_rice_subsidy DECIMAL(12,2), IN p_phone_allowance DECIMAL(12,2), " +
            "    IN p_clothing_allowance DECIMAL(12,2), OUT p_success BOOLEAN, " +
            "    OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    p_success := FALSE; " +
            "    p_error_message := 'Employee update not implemented in DirectDatabaseInstaller'; " +
            "END; " +
            "$BODY$;"
        );
        
        executeSQL(conn,
            "CREATE OR REPLACE PROCEDURE sp_delete_employee( " +
            "    IN p_employee_number VARCHAR(20), OUT p_success BOOLEAN, " +
            "    OUT p_error_message TEXT " +
            ") " +
            "LANGUAGE plpgsql " +
            "AS $BODY$ " +
            "BEGIN " +
            "    p_success := FALSE; " +
            "    p_error_message := 'Employee deletion not implemented in DirectDatabaseInstaller'; " +
            "END; " +
            "$BODY$;"
        );
        
        System.out.println("Created essential stored procedures and functions...");
        System.out.println("Stored procedures created successfully");
    }
    
    /**
     * Execute SQL statement
     * @param conn Database connection
     * @param sql SQL statement
     * @throws SQLException if an error occurs
     */
    private static void executeSQL(Connection conn, String sql) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error executing SQL: " + e.getMessage());
            System.err.println("SQL: " + sql.substring(0, Math.min(200, sql.length())) + "...");
            throw e;
        }
    }
}
