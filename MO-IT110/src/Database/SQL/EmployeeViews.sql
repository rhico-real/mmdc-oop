-- EmployeeViews.sql
-- Contains Views for Employee DAO

-- View for employee information with joined tables
CREATE OR REPLACE VIEW vw_employee_information AS
SELECT 
    e.employee_id, 
    e.employee_number, 
    e.hire_date, 
    e.employment_type, 
    e.is_active,
    u.username, 
    u.email,
    pi.first_name, 
    pi.last_name, 
    pi.middle_name, 
    pi.birthday, 
    pi.gender, 
    pi.civil_status, 
    pi.nationality,
    ci.home_address, 
    ci.city, 
    ci.province, 
    ci.postal_code, 
    ci.phone_number, 
    ci.mobile_number,
    ci.emergency_contact_name, 
    ci.emergency_contact_number,
    p.position_title, 
    d.department_name,
    ep.supervisor_id, 
    ep.status as position_status
FROM employees e
JOIN users u ON e.user_id = u.user_id
LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id
LEFT JOIN contact_information ci ON e.employee_id = ci.employee_id
LEFT JOIN employee_positions ep ON e.employee_id = ep.employee_id AND ep.is_current = TRUE
LEFT JOIN positions p ON ep.position_id = p.position_id
LEFT JOIN departments d ON p.department_id = d.department_id;

-- View for employee compensation
CREATE OR REPLACE VIEW vw_employee_compensation AS
SELECT 
    e.employee_id, 
    e.employee_number,
    pi.first_name, 
    pi.last_name,
    p.position_title, 
    ep.status,
    ec.basic_salary, 
    ec.hourly_rate, 
    ec.gross_semi_monthly_rate,
    sg.grade_name, 
    sg.grade_level
FROM employees e
LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id
LEFT JOIN employee_positions ep ON e.employee_id = ep.employee_id AND ep.is_current = TRUE
LEFT JOIN positions p ON ep.position_id = p.position_id
LEFT JOIN employee_compensation ec ON e.employee_id = ec.employee_id AND ec.is_current = TRUE
LEFT JOIN salary_grades sg ON ec.salary_grade_id = sg.grade_id;

-- View for employee government IDs
CREATE OR REPLACE VIEW vw_employee_government_ids AS
SELECT 
    e.employee_id,
    e.employee_number,
    pi.first_name,
    pi.last_name,
    pi.birthday,
    ci.home_address,
    ci.phone_number,
    p.position_title,
    ep.status,
    ep.supervisor_id,
    gi.id_type,
    gi.id_number
FROM employees e
JOIN personal_information pi ON e.employee_id = pi.employee_id
LEFT JOIN contact_information ci ON e.employee_id = ci.employee_id
LEFT JOIN employee_positions ep ON e.employee_id = ep.employee_id AND ep.is_current = TRUE
LEFT JOIN positions p ON ep.position_id = p.position_id
LEFT JOIN government_ids gi ON e.employee_id = gi.employee_id;

-- View for employee allowances
CREATE OR REPLACE VIEW vw_employee_allowances AS
SELECT 
    e.employee_id,
    e.employee_number,
    at.allowance_name,
    ea.amount
FROM employees e
JOIN employee_allowances ea ON e.employee_id = ea.employee_id
JOIN allowance_types at ON ea.allowance_type_id = at.allowance_type_id
WHERE ea.is_active = TRUE;
