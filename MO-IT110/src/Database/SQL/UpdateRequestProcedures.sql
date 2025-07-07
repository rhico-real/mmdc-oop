-- UpdateRequestProcedures.sql
-- Contains Stored Procedures for Update Request DAO

-- Ensure employee_update_requests table exists
CREATE OR REPLACE PROCEDURE sp_ensure_update_requests_table_exists()
LANGUAGE plpgsql
AS $$
BEGIN
    -- Create the table if it doesn't exist
    EXECUTE '
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
            status VARCHAR(20) DEFAULT ''PENDING'',
            admin_notes TEXT,
            FOREIGN KEY (employee_number) REFERENCES employees(employee_number)
        )
    ';
    
    -- Create indexes
    EXECUTE '
        CREATE INDEX IF NOT EXISTS idx_update_requests_employee_number 
        ON employee_update_requests(employee_number)
    ';
    
    EXECUTE '
        CREATE INDEX IF NOT EXISTS idx_update_requests_status 
        ON employee_update_requests(status)
    ';
END;
$$;

-- Stored procedure to create a new update request
CREATE OR REPLACE PROCEDURE sp_create_update_request(
    IN p_employee_number VARCHAR(50),
    IN p_first_name VARCHAR(100),
    IN p_last_name VARCHAR(100),
    IN p_birthday VARCHAR(20),
    IN p_address TEXT,
    IN p_phone_number VARCHAR(20),
    IN p_sss_number VARCHAR(20),
    IN p_philhealth_number VARCHAR(20),
    IN p_tin_number VARCHAR(20),
    IN p_pagibig_number VARCHAR(20),
    IN p_status VARCHAR(20),
    OUT p_request_id INTEGER,
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Ensure the table exists
    CALL sp_ensure_update_requests_table_exists();
    
    -- Check if employee exists
    IF NOT EXISTS (SELECT 1 FROM employees WHERE employee_number = p_employee_number) THEN
        p_success := FALSE;
        p_error_message := 'Employee not found: ' || p_employee_number;
        RETURN;
    END IF;
    
    -- Insert the update request
    INSERT INTO employee_update_requests (
        employee_number, first_name, last_name, birthday, address, phone_number,
        sss_number, philhealth_number, tin_number, pagibig_number, request_date, status
    )
    VALUES (
        p_employee_number, p_first_name, p_last_name, p_birthday, p_address, p_phone_number,
        p_sss_number, p_philhealth_number, p_tin_number, p_pagibig_number, CURRENT_TIMESTAMP, 
        COALESCE(p_status, 'PENDING')
    )
    RETURNING request_id INTO p_request_id;
    
    IF FOUND THEN
        p_success := TRUE;
    ELSE
        p_success := FALSE;
        p_error_message := 'Failed to create update request';
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        p_success := FALSE;
        p_error_message := SQLERRM;
END;
$$;

-- Function to get update requests by status
CREATE OR REPLACE FUNCTION sp_get_update_requests(p_status VARCHAR(20) DEFAULT NULL)
RETURNS TABLE (
    request_id INTEGER,
    employee_number VARCHAR(50),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    birthday VARCHAR(20),
    address TEXT,
    phone_number VARCHAR(20),
    sss_number VARCHAR(20),
    philhealth_number VARCHAR(20),
    tin_number VARCHAR(20),
    pagibig_number VARCHAR(20),
    request_date TIMESTAMP,
    status VARCHAR(20),
    admin_notes TEXT,
    employee_id INTEGER,
    current_first_name VARCHAR(100),
    current_last_name VARCHAR(100),
    current_birthday DATE,
    current_address TEXT,
    current_phone_number VARCHAR(20)
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Ensure the table exists
    CALL sp_ensure_update_requests_table_exists();
    
    IF p_status IS NOT NULL THEN
        RETURN QUERY
        SELECT * FROM vw_employee_update_requests
        WHERE status = p_status
        ORDER BY request_date DESC;
    ELSE
        RETURN QUERY
        SELECT * FROM vw_employee_update_requests
        ORDER BY request_date DESC;
    END IF;
END;
$$;

-- Function to get update request by ID
CREATE OR REPLACE FUNCTION sp_get_update_request_by_id(p_request_id INTEGER)
RETURNS TABLE (
    request_id INTEGER,
    employee_number VARCHAR(50),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    birthday VARCHAR(20),
    address TEXT,
    phone_number VARCHAR(20),
    sss_number VARCHAR(20),
    philhealth_number VARCHAR(20),
    tin_number VARCHAR(20),
    pagibig_number VARCHAR(20),
    request_date TIMESTAMP,
    status VARCHAR(20),
    admin_notes TEXT,
    employee_id INTEGER,
    current_first_name VARCHAR(100),
    current_last_name VARCHAR(100),
    current_birthday DATE,
    current_address TEXT,
    current_phone_number VARCHAR(20)
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Ensure the table exists
    CALL sp_ensure_update_requests_table_exists();
    
    RETURN QUERY
    SELECT * FROM vw_employee_update_requests
    WHERE request_id = p_request_id;
END;
$$;

-- Stored procedure to update request status
CREATE OR REPLACE PROCEDURE sp_update_request_status(
    IN p_request_id INTEGER,
    IN p_status VARCHAR(20),
    IN p_admin_notes TEXT,
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Ensure the table exists
    CALL sp_ensure_update_requests_table_exists();
    
    -- Check if request exists
    IF NOT EXISTS (SELECT 1 FROM employee_update_requests WHERE request_id = p_request_id) THEN
        p_success := FALSE;
        p_error_message := 'Update request not found: ' || p_request_id;
        RETURN;
    END IF;
    
    -- Update the request status
    UPDATE employee_update_requests
    SET 
        status = p_status,
        admin_notes = p_admin_notes
    WHERE request_id = p_request_id;
    
    IF FOUND THEN
        p_success := TRUE;
    ELSE
        p_success := FALSE;
        p_error_message := 'Failed to update request status';
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        p_success := FALSE;
        p_error_message := SQLERRM;
END;
$$;

-- Stored procedure to approve update request
CREATE OR REPLACE PROCEDURE sp_approve_update_request(
    IN p_request_id INTEGER,
    IN p_admin_notes TEXT,
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_employee_number VARCHAR(50);
    v_first_name VARCHAR(100);
    v_last_name VARCHAR(100);
    v_birthday VARCHAR(20);
    v_address TEXT;
    v_phone_number VARCHAR(20);
    v_sss_number VARCHAR(20);
    v_philhealth_number VARCHAR(20);
    v_tin_number VARCHAR(20);
    v_pagibig_number VARCHAR(20);
    v_employee_id INTEGER;
BEGIN
    -- Start transaction
    BEGIN
        -- Ensure the table exists
        CALL sp_ensure_update_requests_table_exists();
        
        -- Get the update request data
        SELECT 
            eur.employee_number, eur.first_name, eur.last_name, eur.birthday, 
            eur.address, eur.phone_number, eur.sss_number, eur.philhealth_number, 
            eur.tin_number, eur.pagibig_number, e.employee_id
        INTO 
            v_employee_number, v_first_name, v_last_name, v_birthday, 
            v_address, v_phone_number, v_sss_number, v_philhealth_number, 
            v_tin_number, v_pagibig_number, v_employee_id
        FROM employee_update_requests eur
        JOIN employees e ON eur.employee_number = e.employee_number
        WHERE eur.request_id = p_request_id;
        
        IF v_employee_number IS NULL THEN
            p_success := FALSE;
            p_error_message := 'Update request not found: ' || p_request_id;
            RETURN;
        END IF;
        
        -- Update personal information
        UPDATE personal_information
        SET 
            first_name = v_first_name,
            last_name = v_last_name,
            birthday = CASE 
                WHEN v_birthday ~ '^\d{4}-\d{2}-\d{2}$' THEN v_birthday::DATE
                WHEN v_birthday ~ '^\d{2}/\d{2}/\d{4}$' THEN TO_DATE(v_birthday, 'MM/DD/YYYY')
                ELSE NULL
            END,
            updated_at = CURRENT_TIMESTAMP
        WHERE employee_id = v_employee_id;
        
        -- Update contact information
        UPDATE contact_information
        SET 
            home_address = v_address,
            phone_number = v_phone_number,
            mobile_number = v_phone_number,
            updated_at = CURRENT_TIMESTAMP
        WHERE employee_id = v_employee_id;
        
        -- Delete and re-create government IDs
        DELETE FROM government_ids WHERE employee_id = v_employee_id;
        
        -- Insert government IDs if provided
        IF v_sss_number IS NOT NULL AND v_sss_number != '' THEN
            INSERT INTO government_ids (employee_id, id_type, id_number)
            VALUES (v_employee_id, 'SSS', v_sss_number);
        END IF;
        
        IF v_philhealth_number IS NOT NULL AND v_philhealth_number != '' THEN
            INSERT INTO government_ids (employee_id, id_type, id_number)
            VALUES (v_employee_id, 'PHILHEALTH', v_philhealth_number);
        END IF;
        
        IF v_tin_number IS NOT NULL AND v_tin_number != '' THEN
            INSERT INTO government_ids (employee_id, id_type, id_number)
            VALUES (v_employee_id, 'TIN', v_tin_number);
        END IF;
        
        IF v_pagibig_number IS NOT NULL AND v_pagibig_number != '' THEN
            INSERT INTO government_ids (employee_id, id_type, id_number)
            VALUES (v_employee_id, 'PAGIBIG', v_pagibig_number);
        END IF;
        
        -- Update username if name has changed
        UPDATE users
        SET 
            username = LOWER(v_first_name || '.' || v_last_name),
            updated_at = CURRENT_TIMESTAMP
        WHERE user_id = (
            SELECT user_id FROM employees WHERE employee_id = v_employee_id
        );
        
        -- Update the request status
        UPDATE employee_update_requests
        SET 
            status = 'APPROVED',
            admin_notes = p_admin_notes
        WHERE request_id = p_request_id;
        
        p_success := TRUE;
        
        -- Commit the transaction
        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            -- Rollback the transaction
            ROLLBACK;
            p_success := FALSE;
            p_error_message := SQLERRM;
    END;
END;
$$;

-- Stored procedure to reject update request
CREATE OR REPLACE PROCEDURE sp_reject_update_request(
    IN p_request_id INTEGER,
    IN p_admin_notes TEXT,
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    CALL sp_update_request_status(p_request_id, 'REJECTED', p_admin_notes, p_success, p_error_message);
END;
$$;

-- Function to check if employee has pending requests
CREATE OR REPLACE FUNCTION sp_has_employee_pending_requests(p_employee_number VARCHAR(50))
RETURNS BOOLEAN
LANGUAGE plpgsql
AS $$
DECLARE
    v_has_pending BOOLEAN;
BEGIN
    -- Ensure the table exists
    CALL sp_ensure_update_requests_table_exists();
    
    SELECT EXISTS(
        SELECT 1 FROM employee_update_requests
        WHERE employee_number = p_employee_number
        AND status = 'PENDING'
    ) INTO v_has_pending;
    
    RETURN v_has_pending;
END;
$$;

-- =========================================================================
-- ADDITIONAL PROCEDURES
-- The following procedures extend the functionality of the UpdateRequestDAO
-- =========================================================================

-- Stored procedure to delete an update request
CREATE OR REPLACE PROCEDURE sp_delete_update_request(
    IN p_request_id INTEGER,
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Ensure the table exists
    CALL sp_ensure_update_requests_table_exists();
    
    -- Check if request exists
    IF NOT EXISTS (SELECT 1 FROM employee_update_requests WHERE request_id = p_request_id) THEN
        p_success := FALSE;
        p_error_message := 'Update request not found: ' || p_request_id;
        RETURN;
    END IF;
    
    -- Delete the update request
    DELETE FROM employee_update_requests
    WHERE request_id = p_request_id;
    
    IF FOUND THEN
        p_success := TRUE;
    ELSE
        p_success := FALSE;
        p_error_message := 'Failed to delete update request';
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        p_success := FALSE;
        p_error_message := SQLERRM;
END;
$$;

-- Function to get update requests for a specific employee
CREATE OR REPLACE FUNCTION sp_get_employee_update_requests(p_employee_number VARCHAR(50))
RETURNS TABLE (
    request_id INTEGER,
    employee_number VARCHAR(50),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    birthday VARCHAR(20),
    address TEXT,
    phone_number VARCHAR(20),
    sss_number VARCHAR(20),
    philhealth_number VARCHAR(20),
    tin_number VARCHAR(20),
    pagibig_number VARCHAR(20),
    request_date TIMESTAMP,
    status VARCHAR(20),
    admin_notes TEXT,
    employee_id INTEGER,
    current_first_name VARCHAR(100),
    current_last_name VARCHAR(100),
    current_birthday DATE,
    current_address TEXT,
    current_phone_number VARCHAR(20)
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Ensure the table exists
    CALL sp_ensure_update_requests_table_exists();
    
    RETURN QUERY
    SELECT * FROM vw_employee_update_requests
    WHERE employee_number = p_employee_number
    ORDER BY request_date DESC;
END;
$$;

-- Function to count update requests by status
CREATE OR REPLACE FUNCTION sp_count_update_requests_by_status()
RETURNS TABLE (
    status VARCHAR(20),
    count BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Ensure the table exists
    CALL sp_ensure_update_requests_table_exists();
    
    RETURN QUERY
    SELECT eur.status, COUNT(*) as count
    FROM employee_update_requests eur
    GROUP BY eur.status
    ORDER BY eur.status;
END;
$$;