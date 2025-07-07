-- LeaveRequestProcedures.sql
-- Contains Stored Procedures and Functions for Leave Request DAO

-- Function to get leave request by ID
CREATE OR REPLACE FUNCTION fn_get_leave_request_by_id(p_request_number VARCHAR(50))
RETURNS TABLE (
    leave_request_id INTEGER,
    request_number VARCHAR(50),
    employee_id INTEGER,
    employee_number VARCHAR(20),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    start_date DATE,
    end_date DATE,
    total_days INTEGER,
    reason TEXT,
    leave_type VARCHAR(100),
    status VARCHAR(20),
    submitted_date TIMESTAMP,
    approved_by INTEGER,
    approved_date TIMESTAMP,
    remarks TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM vw_leave_requests
    WHERE request_number = p_request_number;
END;
$$;

-- Function to get all leave requests
CREATE OR REPLACE FUNCTION fn_get_all_leave_requests()
RETURNS TABLE (
    leave_request_id INTEGER,
    request_number VARCHAR(50),
    employee_id INTEGER,
    employee_number VARCHAR(20),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    start_date DATE,
    end_date DATE,
    total_days INTEGER,
    reason TEXT,
    leave_type VARCHAR(100),
    status VARCHAR(20),
    submitted_date TIMESTAMP,
    approved_by INTEGER,
    approved_date TIMESTAMP,
    remarks TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM vw_leave_requests
    ORDER BY created_at DESC;
END;
$$;

-- Function to get leave requests by employee number
CREATE OR REPLACE FUNCTION fn_get_leave_requests_by_employee(p_employee_number VARCHAR(20))
RETURNS TABLE (
    leave_request_id INTEGER,
    request_number VARCHAR(50),
    employee_id INTEGER,
    employee_number VARCHAR(20),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    start_date DATE,
    end_date DATE,
    total_days INTEGER,
    reason TEXT,
    leave_type VARCHAR(100),
    status VARCHAR(20),
    submitted_date TIMESTAMP,
    approved_by INTEGER,
    approved_date TIMESTAMP,
    remarks TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM vw_leave_requests
    WHERE employee_number = p_employee_number
    ORDER BY created_at DESC;
END;
$$;

-- Function to get pending leave requests
CREATE OR REPLACE FUNCTION fn_get_pending_leave_requests()
RETURNS TABLE (
    leave_request_id INTEGER,
    request_number VARCHAR(50),
    employee_id INTEGER,
    employee_number VARCHAR(20),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    start_date DATE,
    end_date DATE,
    total_days INTEGER,
    reason TEXT,
    leave_type VARCHAR(100),
    status VARCHAR(20),
    submitted_date TIMESTAMP,
    approved_by INTEGER,
    approved_date TIMESTAMP,
    remarks TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM vw_leave_requests
    WHERE status = 'Pending'
    ORDER BY created_at ASC;
END;
$$;

-- Stored procedure to create a new leave request
CREATE OR REPLACE PROCEDURE sp_create_leave_request(
    IN p_employee_number VARCHAR(20),
    IN p_leave_type VARCHAR(100),
    IN p_start_date DATE,
    IN p_end_date DATE,
    IN p_reason TEXT,
    OUT p_request_number VARCHAR(50),
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_employee_id INTEGER;
    v_leave_type_id INTEGER;
    v_total_days INTEGER;
BEGIN
    -- Generate request number
    p_request_number := 'LR-' || TO_CHAR(CURRENT_DATE, 'YYYYMMDD') || '-' || 
                         LPAD(CAST(NEXTVAL('leave_request_id_seq') AS TEXT), 4, '0');
    
    -- Get employee ID
    SELECT employee_id INTO v_employee_id
    FROM employees
    WHERE employee_number = p_employee_number;
    
    IF v_employee_id IS NULL THEN
        p_success := FALSE;
        p_error_message := 'Employee not found with number: ' || p_employee_number;
        RETURN;
    END IF;
    
    -- Get leave type ID
    SELECT leave_type_id INTO v_leave_type_id
    FROM leave_types
    WHERE leave_name = p_leave_type;
    
    IF v_leave_type_id IS NULL THEN
        p_success := FALSE;
        p_error_message := 'Leave type not found: ' || p_leave_type;
        RETURN;
    END IF;
    
    -- Calculate total days
    v_total_days := p_end_date - p_start_date + 1;
    
    -- Insert new leave request
    INSERT INTO leave_requests (
        request_number, employee_id, leave_type_id, start_date, end_date, 
        total_days, reason, status, submitted_date
    )
    VALUES (
        p_request_number, v_employee_id, v_leave_type_id, p_start_date, p_end_date,
        v_total_days, p_reason, 'Pending', CURRENT_TIMESTAMP
    );
    
    IF FOUND THEN
        p_success := TRUE;
    ELSE
        p_success := FALSE;
        p_error_message := 'Failed to create leave request';
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        p_success := FALSE;
        p_error_message := SQLERRM;
END;
$$;

-- Stored procedure to update leave request status
CREATE OR REPLACE PROCEDURE sp_update_leave_request_status(
    IN p_request_number VARCHAR(50),
    IN p_status VARCHAR(20),
    IN p_approved_by INTEGER, -- Employee ID of the approver
    IN p_remarks TEXT,
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Validate inputs
    IF p_request_number IS NULL OR p_request_number = '' THEN
        p_success := FALSE;
        p_error_message := 'Leave request number is required';
        RETURN;
    END IF;
    
    IF p_status IS NULL OR p_status = '' THEN
        p_success := FALSE;
        p_error_message := 'Status is required';
        RETURN;
    END IF;
    
    -- Check if the leave request exists
    IF NOT EXISTS (SELECT 1 FROM leave_requests WHERE request_number = p_request_number) THEN
        p_success := FALSE;
        p_error_message := 'Leave request not found: ' || p_request_number;
        RETURN;
    END IF;
    
    -- Update the leave request status
    UPDATE leave_requests
    SET 
        status = p_status,
        approved_by = p_approved_by,
        approved_date = CASE WHEN p_status IN ('Approved', 'Rejected') THEN CURRENT_TIMESTAMP ELSE NULL END,
        remarks = p_remarks,
        updated_at = CURRENT_TIMESTAMP
    WHERE request_number = p_request_number;
    
    IF FOUND THEN
        p_success := TRUE;
    ELSE
        p_success := FALSE;
        p_error_message := 'Failed to update leave request status';
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        p_success := FALSE;
        p_error_message := SQLERRM;
END;
$$;

-- Stored procedure to delete leave request
CREATE OR REPLACE PROCEDURE sp_delete_leave_request(
    IN p_request_number VARCHAR(50),
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM leave_requests
    WHERE request_number = p_request_number;
    
    IF FOUND THEN
        p_success := TRUE;
    ELSE
        p_success := FALSE;
        p_error_message := 'Leave request not found: ' || p_request_number;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        p_success := FALSE;
        p_error_message := SQLERRM;
END;
$$;

-- Function to get leave requests by status
CREATE OR REPLACE FUNCTION fn_get_leave_requests_by_status(p_status VARCHAR(20))
RETURNS TABLE (
    leave_request_id INTEGER,
    request_number VARCHAR(50),
    employee_id INTEGER,
    employee_number VARCHAR(20),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    start_date DATE,
    end_date DATE,
    total_days INTEGER,
    reason TEXT,
    leave_type VARCHAR(100),
    status VARCHAR(20),
    submitted_date TIMESTAMP,
    approved_by INTEGER,
    approved_date TIMESTAMP,
    remarks TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM vw_leave_requests
    WHERE status = p_status
    ORDER BY created_at DESC;
END;
$$;