-- EmployeeProcedures.sql
-- Contains Stored Procedures and Functions for Employee DAO

-- Stored procedure to get employee by number
CREATE OR REPLACE PROCEDURE sp_get_employee_by_number(
    IN p_employee_number VARCHAR(20),
    OUT p_found BOOLEAN
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Check if employee exists
    SELECT EXISTS(
        SELECT 1 FROM vw_employee_information 
        WHERE employee_number = p_employee_number AND is_active = TRUE
    ) INTO p_found;
END;
$$;

-- Function to get all active employees
CREATE OR REPLACE FUNCTION fn_get_all_employees()
RETURNS TABLE (
    employee_id INT,
    employee_number VARCHAR(20),
    hire_date DATE,
    employment_type VARCHAR(50),
    is_active BOOLEAN,
    username VARCHAR(100),
    email VARCHAR(255),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    middle_name VARCHAR(100),
    birthday DATE,
    gender VARCHAR(10),
    civil_status VARCHAR(20),
    nationality VARCHAR(50),
    home_address TEXT,
    city VARCHAR(100),
    province VARCHAR(100),
    postal_code VARCHAR(10),
    phone_number VARCHAR(20),
    mobile_number VARCHAR(20),
    emergency_contact_name VARCHAR(200),
    emergency_contact_number VARCHAR(20),
    position_title VARCHAR(100),
    department_name VARCHAR(100),
    supervisor_id INTEGER,
    position_status VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM vw_employee_information
    WHERE is_active = TRUE
    ORDER BY employee_number;
END;
$$;

-- Function to get employee compensation
CREATE OR REPLACE FUNCTION fn_get_employee_compensation(p_employee_number VARCHAR(20))
RETURNS TABLE (
    employee_id INT,
    employee_number VARCHAR(20),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    position_title VARCHAR(100),
    status VARCHAR(50),
    basic_salary DECIMAL(12,2),
    hourly_rate DECIMAL(8,2),
    gross_semi_monthly_rate DECIMAL(12,2),
    grade_name VARCHAR(50),
    grade_level INTEGER,
    allowance_name VARCHAR(100),
    amount DECIMAL(12,2)
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT 
        c.employee_id,
        c.employee_number,
        c.first_name,
        c.last_name,
        c.position_title,
        c.status,
        c.basic_salary,
        c.hourly_rate,
        c.gross_semi_monthly_rate,
        c.grade_name,
        c.grade_level,
        a.allowance_name,
        a.amount
    FROM vw_employee_compensation c
    LEFT JOIN vw_employee_allowances a ON c.employee_id = a.employee_id
    WHERE c.employee_number = p_employee_number AND c.is_active = TRUE;
END;
$$;

-- Function to get employee government IDs
CREATE OR REPLACE FUNCTION fn_get_employee_gov_ids(p_employee_number VARCHAR(20))
RETURNS TABLE (
    employee_id INT,
    employee_number VARCHAR(20),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    birthday DATE,
    home_address TEXT,
    phone_number VARCHAR(20),
    position_title VARCHAR(100),
    status VARCHAR(50),
    supervisor_id INTEGER,
    id_type VARCHAR(20),
    id_number VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM vw_employee_government_ids
    WHERE employee_number = p_employee_number;
END;
$$;

-- Stored procedure to create employee
CREATE OR REPLACE PROCEDURE sp_create_employee(
    IN p_employee_number VARCHAR(20),
    IN p_username VARCHAR(100),
    IN p_password VARCHAR(255),
    IN p_email VARCHAR(255),
    IN p_first_name VARCHAR(100),
    IN p_last_name VARCHAR(100),
    IN p_birthday DATE,
    IN p_address TEXT,
    IN p_phone_number VARCHAR(20),
    IN p_position_title VARCHAR(100),
    IN p_department_name VARCHAR(100),
    IN p_basic_salary DECIMAL(12,2),
    IN p_hourly_rate DECIMAL(8,2),
    IN p_gross_semi_monthly_rate DECIMAL(12,2),
    IN p_sss_number VARCHAR(50),
    IN p_philhealth_number VARCHAR(50),
    IN p_tin_number VARCHAR(50),
    IN p_pagibig_number VARCHAR(50),
    IN p_rice_subsidy DECIMAL(12,2),
    IN p_phone_allowance DECIMAL(12,2),
    IN p_clothing_allowance DECIMAL(12,2),
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id INTEGER;
    v_employee_id INTEGER;
    v_position_id INTEGER;
    v_allowance_type_id INTEGER;
BEGIN
    -- Start transaction
    BEGIN
        -- 1. Create user record
        INSERT INTO users (username, password, email, is_active)
        VALUES (p_username, p_password, p_email, TRUE)
        RETURNING user_id INTO v_user_id;
        
        -- Assign EMPLOYEE role
        INSERT INTO user_roles (user_id, role_id)
        SELECT v_user_id, role_id FROM roles WHERE role_name = 'EMPLOYEE';
        
        -- 2. Create employee record
        INSERT INTO employees (employee_number, user_id, hire_date, employment_type, is_active)
        VALUES (p_employee_number, v_user_id, CURRENT_DATE, 'Full-time', TRUE)
        RETURNING employee_id INTO v_employee_id;
        
        -- 3. Create personal information
        INSERT INTO personal_information (employee_id, first_name, last_name, middle_name, birthday)
        VALUES (v_employee_id, p_first_name, p_last_name, '', p_birthday);
        
        -- 4. Create contact information
        INSERT INTO contact_information (employee_id, home_address, phone_number, mobile_number)
        VALUES (v_employee_id, p_address, p_phone_number, p_phone_number);
        
        -- 5. Create government IDs
        IF p_sss_number IS NOT NULL AND p_sss_number != '' THEN
            INSERT INTO government_ids (employee_id, id_type, id_number)
            VALUES (v_employee_id, 'SSS', p_sss_number);
        END IF;
        
        IF p_philhealth_number IS NOT NULL AND p_philhealth_number != '' THEN
            INSERT INTO government_ids (employee_id, id_type, id_number)
            VALUES (v_employee_id, 'PHILHEALTH', p_philhealth_number);
        END IF;
        
        IF p_tin_number IS NOT NULL AND p_tin_number != '' THEN
            INSERT INTO government_ids (employee_id, id_type, id_number)
            VALUES (v_employee_id, 'TIN', p_tin_number);
        END IF;
        
        IF p_pagibig_number IS NOT NULL AND p_pagibig_number != '' THEN
            INSERT INTO government_ids (employee_id, id_type, id_number)
            VALUES (v_employee_id, 'PAGIBIG', p_pagibig_number);
        END IF;
        
        -- 6. Assign position
        SELECT position_id INTO v_position_id
        FROM positions p
        JOIN departments d ON p.department_id = d.department_id
        WHERE p.position_title = p_position_title
        AND d.department_name = p_department_name;
        
        IF v_position_id IS NOT NULL THEN
            INSERT INTO employee_positions (employee_id, position_id, start_date, is_current)
            VALUES (v_employee_id, v_position_id, CURRENT_DATE, TRUE);
        END IF;
        
        -- 7. Set compensation
        INSERT INTO employee_compensation (
            employee_id, basic_salary, hourly_rate, gross_semi_monthly_rate, 
            effective_date, is_current
        )
        VALUES (
            v_employee_id, p_basic_salary, p_hourly_rate, p_gross_semi_monthly_rate,
            CURRENT_DATE, TRUE
        );
        
        -- 8. Set allowances
        -- Rice Subsidy
        IF p_rice_subsidy > 0 THEN
            SELECT allowance_type_id INTO v_allowance_type_id
            FROM allowance_types
            WHERE allowance_name = 'Rice Subsidy';
            
            INSERT INTO employee_allowances (
                employee_id, allowance_type_id, amount, effective_date, is_active
            )
            VALUES (
                v_employee_id, v_allowance_type_id, p_rice_subsidy, CURRENT_DATE, TRUE
            );
        END IF;
        
        -- Phone Allowance
        IF p_phone_allowance > 0 THEN
            SELECT allowance_type_id INTO v_allowance_type_id
            FROM allowance_types
            WHERE allowance_name = 'Phone Allowance';
            
            INSERT INTO employee_allowances (
                employee_id, allowance_type_id, amount, effective_date, is_active
            )
            VALUES (
                v_employee_id, v_allowance_type_id, p_phone_allowance, CURRENT_DATE, TRUE
            );
        END IF;
        
        -- Clothing Allowance
        IF p_clothing_allowance > 0 THEN
            SELECT allowance_type_id INTO v_allowance_type_id
            FROM allowance_types
            WHERE allowance_name = 'Clothing Allowance';
            
            INSERT INTO employee_allowances (
                employee_id, allowance_type_id, amount, effective_date, is_active
            )
            VALUES (
                v_employee_id, v_allowance_type_id, p_clothing_allowance, CURRENT_DATE, TRUE
            );
        END IF;
        
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

-- Stored procedure to update employee
CREATE OR REPLACE PROCEDURE sp_update_employee(
    IN p_employee_number VARCHAR(20),
    IN p_first_name VARCHAR(100),
    IN p_last_name VARCHAR(100),
    IN p_birthday DATE,
    IN p_address TEXT,
    IN p_phone_number VARCHAR(20),
    IN p_basic_salary DECIMAL(12,2),
    IN p_hourly_rate DECIMAL(8,2),
    IN p_gross_semi_monthly_rate DECIMAL(12,2),
    IN p_sss_number VARCHAR(50),
    IN p_philhealth_number VARCHAR(50),
    IN p_tin_number VARCHAR(50),
    IN p_pagibig_number VARCHAR(50),
    IN p_rice_subsidy DECIMAL(12,2),
    IN p_phone_allowance DECIMAL(12,2),
    IN p_clothing_allowance DECIMAL(12,2),
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_employee_id INTEGER;
    v_allowance_type_id INTEGER;
BEGIN
    -- Start transaction
    BEGIN
        -- Get employee ID
        SELECT employee_id INTO v_employee_id
        FROM employees
        WHERE employee_number = p_employee_number;
        
        IF v_employee_id IS NULL THEN
            p_success := FALSE;
            p_error_message := 'Employee not found: ' || p_employee_number;
            RETURN;
        END IF;
        
        -- Update personal information
        UPDATE personal_information 
        SET 
            first_name = p_first_name,
            last_name = p_last_name,
            birthday = p_birthday,
            updated_at = CURRENT_TIMESTAMP
        WHERE employee_id = v_employee_id;
        
        -- Update contact information
        UPDATE contact_information
        SET
            home_address = p_address,
            phone_number = p_phone_number,
            mobile_number = p_phone_number,
            updated_at = CURRENT_TIMESTAMP
        WHERE employee_id = v_employee_id;
        
        -- Update government IDs (delete and re-create)
        DELETE FROM government_ids WHERE employee_id = v_employee_id;
        
        -- Insert new government IDs
        IF p_sss_number IS NOT NULL AND p_sss_number != '' THEN
            INSERT INTO government_ids (employee_id, id_type, id_number)
            VALUES (v_employee_id, 'SSS', p_sss_number);
        END IF;
        
        IF p_philhealth_number IS NOT NULL AND p_philhealth_number != '' THEN
            INSERT INTO government_ids (employee_id, id_type, id_number)
            VALUES (v_employee_id, 'PHILHEALTH', p_philhealth_number);
        END IF;
        
        IF p_tin_number IS NOT NULL AND p_tin_number != '' THEN
            INSERT INTO government_ids (employee_id, id_type, id_number)
            VALUES (v_employee_id, 'TIN', p_tin_number);
        END IF;
        
        IF p_pagibig_number IS NOT NULL AND p_pagibig_number != '' THEN
            INSERT INTO government_ids (employee_id, id_type, id_number)
            VALUES (v_employee_id, 'PAGIBIG', p_pagibig_number);
        END IF;
        
        -- Update compensation
        -- Set current compensation to false
        UPDATE employee_compensation
        SET is_current = FALSE
        WHERE employee_id = v_employee_id AND is_current = TRUE;
        
        -- Insert new compensation
        INSERT INTO employee_compensation (
            employee_id, basic_salary, hourly_rate, gross_semi_monthly_rate, 
            effective_date, is_current
        )
        VALUES (
            v_employee_id, p_basic_salary, p_hourly_rate, p_gross_semi_monthly_rate,
            CURRENT_DATE, TRUE
        );
        
        -- Update allowances
        -- Set current allowances to false
        UPDATE employee_allowances
        SET is_active = FALSE
        WHERE employee_id = v_employee_id AND is_active = TRUE;
        
        -- Insert new allowances
        -- Rice Subsidy
        IF p_rice_subsidy > 0 THEN
            SELECT allowance_type_id INTO v_allowance_type_id
            FROM allowance_types
            WHERE allowance_name = 'Rice Subsidy';
            
            INSERT INTO employee_allowances (
                employee_id, allowance_type_id, amount, effective_date, is_active
            )
            VALUES (
                v_employee_id, v_allowance_type_id, p_rice_subsidy, CURRENT_DATE, TRUE
            );
        END IF;
        
        -- Phone Allowance
        IF p_phone_allowance > 0 THEN
            SELECT allowance_type_id INTO v_allowance_type_id
            FROM allowance_types
            WHERE allowance_name = 'Phone Allowance';
            
            INSERT INTO employee_allowances (
                employee_id, allowance_type_id, amount, effective_date, is_active
            )
            VALUES (
                v_employee_id, v_allowance_type_id, p_phone_allowance, CURRENT_DATE, TRUE
            );
        END IF;
        
        -- Clothing Allowance
        IF p_clothing_allowance > 0 THEN
            SELECT allowance_type_id INTO v_allowance_type_id
            FROM allowance_types
            WHERE allowance_name = 'Clothing Allowance';
            
            INSERT INTO employee_allowances (
                employee_id, allowance_type_id, amount, effective_date, is_active
            )
            VALUES (
                v_employee_id, v_allowance_type_id, p_clothing_allowance, CURRENT_DATE, TRUE
            );
        END IF;
        
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

-- Function to search employees by name
CREATE OR REPLACE FUNCTION fn_search_employees_by_name(p_search_term VARCHAR(100))
RETURNS TABLE (
    employee_id INT,
    employee_number VARCHAR(20),
    hire_date DATE,
    employment_type VARCHAR(50),
    is_active BOOLEAN,
    username VARCHAR(100),
    email VARCHAR(255),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    middle_name VARCHAR(100),
    birthday DATE,
    gender VARCHAR(10),
    civil_status VARCHAR(20),
    nationality VARCHAR(50),
    home_address TEXT,
    city VARCHAR(100),
    province VARCHAR(100),
    postal_code VARCHAR(10),
    phone_number VARCHAR(20),
    mobile_number VARCHAR(20),
    position_title VARCHAR(100),
    department_name VARCHAR(100),
    position_status VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT 
        employee_id,
        employee_number,
        hire_date,
        employment_type,
        is_active,
        username,
        email,
        first_name,
        last_name,
        middle_name,
        birthday,
        gender,
        civil_status,
        nationality,
        home_address,
        city,
        province,
        postal_code,
        phone_number,
        mobile_number,
        position_title,
        department_name,
        position_status
    FROM vw_employee_information
    WHERE is_active = TRUE
    AND (
        last_name ILIKE '%' || p_search_term || '%' OR 
        first_name ILIKE '%' || p_search_term || '%' OR 
        CONCAT(first_name, ' ', last_name) ILIKE '%' || p_search_term || '%' OR
        CONCAT(last_name, ' ', first_name) ILIKE '%' || p_search_term || '%'
    )
    ORDER BY last_name, first_name;
END;
$$;

-- Stored procedure to delete employee (soft delete)
CREATE OR REPLACE PROCEDURE sp_delete_employee(
    IN p_employee_number VARCHAR(20),
    OUT p_success BOOLEAN
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id INTEGER;
BEGIN
    -- Find user_id for the employee
    SELECT user_id INTO v_user_id 
    FROM employees 
    WHERE employee_number = p_employee_number;
    
    -- Soft delete the employee
    UPDATE employees 
    SET 
        is_active = FALSE, 
        termination_date = CURRENT_DATE, 
        updated_at = CURRENT_TIMESTAMP 
    WHERE employee_number = p_employee_number;
    
    -- Also deactivate the user
    IF FOUND THEN
        UPDATE users
        SET is_active = FALSE, updated_at = CURRENT_TIMESTAMP
        WHERE user_id = v_user_id;
        
        p_success := TRUE;
    ELSE
        p_success := FALSE;
    END IF;
END;
$$;