-- AttendanceProcedures.sql
-- Contains Stored Procedures and Functions for Attendance DAO

-- Function to convert date format from MM/dd/yyyy to yyyy-MM-dd
CREATE OR REPLACE FUNCTION fn_convert_to_sql_date_format(p_date_string TEXT)
RETURNS TEXT
LANGUAGE plpgsql
AS $$
DECLARE
    v_date DATE;
BEGIN
    -- If already in yyyy-MM-dd format, return as is
    IF p_date_string ~ '^\d{4}-\d{2}-\d{2}$' THEN
        RETURN p_date_string;
    END IF;
    
    -- Convert from MM/dd/yyyy to yyyy-MM-dd
    IF p_date_string ~ '^\d{2}/\d{2}/\d{4}$' THEN
        BEGIN
            v_date := TO_DATE(p_date_string, 'MM/DD/YYYY');
            RETURN TO_CHAR(v_date, 'YYYY-MM-DD');
        EXCEPTION
            WHEN OTHERS THEN
                RETURN p_date_string; -- Return original if conversion fails
        END;
    END IF;
    
    RETURN p_date_string; -- Return as is if format is unknown
END;
$$;

-- Function to get attendance record by employee number and date
CREATE OR REPLACE FUNCTION fn_get_attendance_record(
    p_employee_number VARCHAR(20),
    p_date TEXT
)
RETURNS TABLE (
    attendance_id INTEGER,
    attendance_date DATE,
    time_in TIME,
    time_out TIME,
    break_time_minutes INTEGER,
    overtime_hours DECIMAL(4,2),
    status VARCHAR(20),
    notes TEXT,
    employee_id INTEGER,
    employee_number VARCHAR(20),
    first_name VARCHAR(100),
    last_name VARCHAR(100)
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_formatted_date TEXT;
BEGIN
    v_formatted_date := fn_convert_to_sql_date_format(p_date);
    
    RETURN QUERY
    SELECT * FROM vw_attendance_records
    WHERE employee_number = p_employee_number
    AND attendance_date = v_formatted_date::date;
END;
$$;

-- Function to get all attendance records for an employee
CREATE OR REPLACE FUNCTION fn_get_attendance_by_employee(p_employee_number VARCHAR(20))
RETURNS TABLE (
    attendance_id INTEGER,
    attendance_date DATE,
    time_in TIME,
    time_out TIME,
    break_time_minutes INTEGER,
    overtime_hours DECIMAL(4,2),
    status VARCHAR(20),
    notes TEXT,
    employee_id INTEGER,
    employee_number VARCHAR(20),
    first_name VARCHAR(100),
    last_name VARCHAR(100)
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM vw_attendance_records
    WHERE employee_number = p_employee_number
    ORDER BY attendance_date DESC;
END;
$$;

-- Function to get all attendance records for a specific date
CREATE OR REPLACE FUNCTION fn_get_attendance_by_date(p_date TEXT)
RETURNS TABLE (
    attendance_id INTEGER,
    attendance_date DATE,
    time_in TIME,
    time_out TIME,
    break_time_minutes INTEGER,
    overtime_hours DECIMAL(4,2),
    status VARCHAR(20),
    notes TEXT,
    employee_id INTEGER,
    employee_number VARCHAR(20),
    first_name VARCHAR(100),
    last_name VARCHAR(100)
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_formatted_date TEXT;
BEGIN
    v_formatted_date := fn_convert_to_sql_date_format(p_date);
    
    RETURN QUERY
    SELECT * FROM vw_attendance_records
    WHERE attendance_date = v_formatted_date::date
    ORDER BY employee_number;
END;
$$;

-- Function to get all attendance records
CREATE OR REPLACE FUNCTION fn_get_all_attendance_records()
RETURNS TABLE (
    attendance_id INTEGER,
    attendance_date DATE,
    time_in TIME,
    time_out TIME,
    break_time_minutes INTEGER,
    overtime_hours DECIMAL(4,2),
    status VARCHAR(20),
    notes TEXT,
    employee_id INTEGER,
    employee_number VARCHAR(20),
    first_name VARCHAR(100),
    last_name VARCHAR(100)
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM vw_attendance_records
    ORDER BY attendance_date DESC, employee_number;
END;
$$;

-- Stored procedure to create or update attendance record
CREATE OR REPLACE PROCEDURE sp_save_attendance_record(
    IN p_employee_number VARCHAR(20),
    IN p_date TEXT,
    IN p_time_in TEXT,
    IN p_time_out TEXT,
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_employee_id INTEGER;
    v_formatted_date TEXT;
    v_record_exists BOOLEAN;
BEGIN
    -- Convert date format
    v_formatted_date := fn_convert_to_sql_date_format(p_date);
    
    -- Get employee ID
    SELECT employee_id INTO v_employee_id
    FROM employees
    WHERE employee_number = p_employee_number;
    
    IF v_employee_id IS NULL THEN
        p_success := FALSE;
        p_error_message := 'Employee not found with number: ' || p_employee_number;
        RETURN;
    END IF;
    
    -- Check if record exists
    SELECT EXISTS(
        SELECT 1 FROM attendance_records
        WHERE employee_id = v_employee_id
        AND attendance_date = v_formatted_date::date
    ) INTO v_record_exists;
    
    IF v_record_exists THEN
        -- Update existing record
        UPDATE attendance_records
        SET 
            time_in = p_time_in::time,
            time_out = p_time_out::time,
            updated_at = CURRENT_TIMESTAMP
        WHERE 
            employee_id = v_employee_id
            AND attendance_date = v_formatted_date::date;
    ELSE
        -- Create new record
        INSERT INTO attendance_records (
            employee_id, attendance_date, time_in, time_out
        )
        VALUES (
            v_employee_id, v_formatted_date::date, p_time_in::time, p_time_out::time
        );
    END IF;
    
    IF FOUND THEN
        p_success := TRUE;
    ELSE
        p_success := FALSE;
        p_error_message := 'Failed to save attendance record';
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        p_success := FALSE;
        p_error_message := SQLERRM;
END;
$$;

-- Stored procedure to delete attendance record
CREATE OR REPLACE PROCEDURE sp_delete_attendance_record(
    IN p_employee_number VARCHAR(20),
    IN p_date TEXT,
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_formatted_date TEXT;
BEGIN
    v_formatted_date := fn_convert_to_sql_date_format(p_date);
    
    DELETE FROM attendance_records
    WHERE employee_id = (
        SELECT employee_id FROM employees WHERE employee_number = p_employee_number
    )
    AND attendance_date = v_formatted_date::date;
    
    IF FOUND THEN
        p_success := TRUE;
    ELSE
        p_success := FALSE;
        p_error_message := 'No attendance record found to delete';
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        p_success := FALSE;
        p_error_message := SQLERRM;
END;
$$;

-- Function to get attendance records within date range
CREATE OR REPLACE FUNCTION fn_get_attendance_by_date_range(
    p_start_date TEXT,
    p_end_date TEXT
)
RETURNS TABLE (
    attendance_id INTEGER,
    attendance_date DATE,
    time_in TIME,
    time_out TIME,
    break_time_minutes INTEGER,
    overtime_hours DECIMAL(4,2),
    status VARCHAR(20),
    notes TEXT,
    employee_id INTEGER,
    employee_number VARCHAR(20),
    first_name VARCHAR(100),
    last_name VARCHAR(100)
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_formatted_start_date TEXT;
    v_formatted_end_date TEXT;
BEGIN
    v_formatted_start_date := fn_convert_to_sql_date_format(p_start_date);
    v_formatted_end_date := fn_convert_to_sql_date_format(p_end_date);
    
    RETURN QUERY
    SELECT * FROM vw_attendance_records
    WHERE attendance_date >= v_formatted_start_date::date
    AND attendance_date <= v_formatted_end_date::date
    ORDER BY attendance_date, employee_number;
END;
$$;

-- Function to calculate monthly attendance summary for an employee
CREATE OR REPLACE FUNCTION fn_calculate_monthly_attendance(
    p_employee_number VARCHAR(20),
    p_month INTEGER,
    p_year INTEGER
)
RETURNS TABLE (
    days_worked INTEGER,
    overtime_hours DECIMAL(5,2)
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_start_date DATE;
    v_end_date DATE;
    v_days_worked INTEGER := 0;
    v_total_overtime_hours DECIMAL(5,2) := 0;
BEGIN
    -- Create date range for the month
    v_start_date := make_date(p_year, p_month, 1);
    v_end_date := (v_start_date + INTERVAL '1 month - 1 day')::date;
    
    -- Calculate days worked and overtime hours
    SELECT 
        COUNT(*),
        COALESCE(SUM(overtime_hours), 0)
    INTO
        v_days_worked,
        v_total_overtime_hours
    FROM attendance_records ar
    JOIN employees e ON ar.employee_id = e.employee_id
    WHERE e.employee_number = p_employee_number
    AND ar.attendance_date BETWEEN v_start_date AND v_end_date
    AND ar.time_in IS NOT NULL
    AND ar.time_out IS NOT NULL;
    
    -- Return results
    RETURN QUERY
    SELECT v_days_worked, v_total_overtime_hours;
END;
$$;