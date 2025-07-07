-- UserProcedures.sql
-- Contains Stored Procedures and Functions for User DAO

-- Function for user authentication
CREATE OR REPLACE FUNCTION fn_authenticate_user(
    p_username VARCHAR(100),
    p_password VARCHAR(255)
)
RETURNS TABLE (
    user_id INTEGER,
    username VARCHAR(100),
    password VARCHAR(255),
    email VARCHAR(255),
    is_active BOOLEAN,
    employee_id INTEGER,
    employee_number VARCHAR(20),
    roles TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM vw_users_with_roles
    WHERE username = p_username 
    AND password = p_password 
    AND is_active = TRUE;
END;
$$;

-- Function to get user by employee number
CREATE OR REPLACE FUNCTION fn_get_user_by_employee_number(p_employee_number VARCHAR(20))
RETURNS TABLE (
    user_id INTEGER,
    username VARCHAR(100),
    password VARCHAR(255),
    email VARCHAR(255),
    is_active BOOLEAN,
    employee_id INTEGER,
    employee_number VARCHAR(20),
    roles TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM vw_users_with_roles
    WHERE employee_number = p_employee_number
    AND is_active = TRUE;
END;
$$;

-- Function to get user by username
CREATE OR REPLACE FUNCTION fn_get_user_by_username(p_username VARCHAR(100))
RETURNS TABLE (
    user_id INTEGER,
    username VARCHAR(100),
    password VARCHAR(255),
    email VARCHAR(255),
    is_active BOOLEAN,
    employee_id INTEGER,
    employee_number VARCHAR(20),
    roles TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM vw_users_with_roles
    WHERE username = p_username
    AND is_active = TRUE;
END;
$$;

-- Function to get all users
CREATE OR REPLACE FUNCTION fn_get_all_users()
RETURNS TABLE (
    user_id INTEGER,
    username VARCHAR(100),
    password VARCHAR(255),
    email VARCHAR(255),
    is_active BOOLEAN,
    employee_id INTEGER,
    employee_number VARCHAR(20),
    roles TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT * FROM vw_users_with_roles
    WHERE is_active = TRUE
    ORDER BY username;
END;
$$;

-- Stored procedure to create user with role
CREATE OR REPLACE PROCEDURE sp_create_user_with_role(
    IN p_username VARCHAR(100),
    IN p_password VARCHAR(255),
    IN p_email VARCHAR(255),
    IN p_role_name VARCHAR(50),
    OUT p_user_id INTEGER,
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_role_id INTEGER;
BEGIN
    -- Start transaction
    BEGIN
        -- Check if username already exists
        IF EXISTS (SELECT 1 FROM users WHERE username = p_username) THEN
            p_success := FALSE;
            p_error_message := 'Username already exists: ' || p_username;
            RETURN;
        END IF;
        
        -- Check if role exists
        SELECT role_id INTO v_role_id FROM roles WHERE role_name = p_role_name;
        IF v_role_id IS NULL THEN
            p_success := FALSE;
            p_error_message := 'Role not found: ' || p_role_name;
            RETURN;
        END IF;
        
        -- Create user
        INSERT INTO users (username, password, email, is_active)
        VALUES (p_username, p_password, p_email, TRUE)
        RETURNING user_id INTO p_user_id;
        
        -- Assign role
        INSERT INTO user_roles (user_id, role_id, is_active)
        VALUES (p_user_id, v_role_id, TRUE);
        
        -- Create specific role record if needed
        IF p_role_name = 'ADMIN' THEN
            INSERT INTO admins (user_id, admin_level, permissions)
            VALUES (p_user_id, 1, 'BASIC');
        ELSIF p_role_name = 'HR' THEN
            INSERT INTO hr_personnel (user_id, hr_level)
            VALUES (p_user_id, 'Junior');
        ELSIF p_role_name = 'FINANCE' THEN
            -- Ensure this table exists in your schema
            INSERT INTO finance_personnel (user_id, finance_level)
            VALUES (p_user_id, 'Junior');
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
            p_user_id := -1;
    END;
END;
$$;

-- Stored procedure to update user
CREATE OR REPLACE PROCEDURE sp_update_user(
    IN p_old_username VARCHAR(100),
    IN p_new_username VARCHAR(100),
    IN p_password VARCHAR(255),
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Check if user exists
    IF NOT EXISTS (SELECT 1 FROM users WHERE username = p_old_username) THEN
        p_success := FALSE;
        p_error_message := 'User not found: ' || p_old_username;
        RETURN;
    END IF;
    
    -- Check if new username already exists (if changing username)
    IF p_old_username <> p_new_username AND 
       EXISTS (SELECT 1 FROM users WHERE username = p_new_username) THEN
        p_success := FALSE;
        p_error_message := 'Username already exists: ' || p_new_username;
        RETURN;
    END IF;
    
    -- Update user
    UPDATE users
    SET 
        username = p_new_username,
        password = p_password,
        updated_at = CURRENT_TIMESTAMP
    WHERE username = p_old_username;
    
    IF FOUND THEN
        p_success := TRUE;
    ELSE
        p_success := FALSE;
        p_error_message := 'Failed to update user';
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        p_success := FALSE;
        p_error_message := SQLERRM;
END;
$$;

-- Stored procedure to assign role to user
CREATE OR REPLACE PROCEDURE sp_assign_role_to_user(
    IN p_username VARCHAR(100),
    IN p_role_name VARCHAR(50),
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id INTEGER;
    v_role_id INTEGER;
BEGIN
    -- Get user ID
    SELECT user_id INTO v_user_id FROM users WHERE username = p_username;
    IF v_user_id IS NULL THEN
        p_success := FALSE;
        p_error_message := 'User not found: ' || p_username;
        RETURN;
    END IF;
    
    -- Get role ID
    SELECT role_id INTO v_role_id FROM roles WHERE role_name = p_role_name;
    IF v_role_id IS NULL THEN
        p_success := FALSE;
        p_error_message := 'Role not found: ' || p_role_name;
        RETURN;
    END IF;
    
    -- Check if role assignment already exists
    IF EXISTS (SELECT 1 FROM user_roles WHERE user_id = v_user_id AND role_id = v_role_id) THEN
        -- Update existing role assignment
        UPDATE user_roles
        SET is_active = TRUE
        WHERE user_id = v_user_id AND role_id = v_role_id;
    ELSE
        -- Create new role assignment
        INSERT INTO user_roles (user_id, role_id, is_active)
        VALUES (v_user_id, v_role_id, TRUE);
    END IF;
    
    -- Create specific role record if needed
    IF p_role_name = 'ADMIN' AND 
       NOT EXISTS (SELECT 1 FROM admins WHERE user_id = v_user_id) THEN
        INSERT INTO admins (user_id, admin_level, permissions)
        VALUES (v_user_id, 1, 'BASIC');
    ELSIF p_role_name = 'HR' AND 
          NOT EXISTS (SELECT 1 FROM hr_personnel WHERE user_id = v_user_id) THEN
        INSERT INTO hr_personnel (user_id, hr_level)
        VALUES (v_user_id, 'Junior');
    ELSIF p_role_name = 'FINANCE' AND 
          NOT EXISTS (SELECT 1 FROM finance_personnel WHERE user_id = v_user_id) THEN
        INSERT INTO finance_personnel (user_id, finance_level)
        VALUES (v_user_id, 'Junior');
    END IF;
    
    IF FOUND THEN
        p_success := TRUE;
    ELSE
        p_success := FALSE;
        p_error_message := 'Failed to assign role to user';
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        p_success := FALSE;
        p_error_message := SQLERRM;
END;
$$;

-- Stored procedure to remove role from user
CREATE OR REPLACE PROCEDURE sp_remove_role_from_user(
    IN p_username VARCHAR(100),
    IN p_role_name VARCHAR(50),
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE user_roles
    SET is_active = FALSE
    WHERE user_id = (SELECT user_id FROM users WHERE username = p_username)
    AND role_id = (SELECT role_id FROM roles WHERE role_name = p_role_name);
    
    IF FOUND THEN
        p_success := TRUE;
    ELSE
        p_success := FALSE;
        p_error_message := 'Role not found for user or already inactive';
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        p_success := FALSE;
        p_error_message := SQLERRM;
END;
$$;

-- Stored procedure to delete user (soft delete)
CREATE OR REPLACE PROCEDURE sp_delete_user(
    IN p_employee_number VARCHAR(20),
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE users
    SET 
        is_active = FALSE,
        updated_at = CURRENT_TIMESTAMP
    WHERE user_id = (
        SELECT user_id FROM employees WHERE employee_number = p_employee_number
    );
    
    IF FOUND THEN
        p_success := TRUE;
    ELSE
        p_success := FALSE;
        p_error_message := 'User not found for employee number: ' || p_employee_number;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        p_success := FALSE;
        p_error_message := SQLERRM;
END;
$$;

-- Stored procedure to update username for employee
CREATE OR REPLACE PROCEDURE sp_update_username(
    IN p_employee_number VARCHAR(20),
    IN p_new_username VARCHAR(100),
    OUT p_success BOOLEAN,
    OUT p_error_message TEXT
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Check if new username already exists
    IF EXISTS (SELECT 1 FROM users WHERE username = p_new_username) THEN
        p_success := FALSE;
        p_error_message := 'Username already exists: ' || p_new_username;
        RETURN;
    END IF;
    
    UPDATE users
    SET 
        username = p_new_username,
        updated_at = CURRENT_TIMESTAMP
    WHERE user_id = (
        SELECT user_id FROM employees WHERE employee_number = p_employee_number
    );
    
    IF FOUND THEN
        p_success := TRUE;
    ELSE
        p_success := FALSE;
        p_error_message := 'User not found for employee number: ' || p_employee_number;
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        p_success := FALSE;
        p_error_message := SQLERRM;
END;
$$;