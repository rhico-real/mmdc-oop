-- UpdateRequestViews.sql
-- Contains Views for Update Request DAO

-- View for employee update requests
CREATE OR REPLACE VIEW vw_employee_update_requests AS
SELECT 
    eur.request_id,
    eur.employee_number,
    eur.first_name,
    eur.last_name,
    eur.birthday,
    eur.address,
    eur.phone_number,
    eur.sss_number,
    eur.philhealth_number,
    eur.tin_number,
    eur.pagibig_number,
    eur.request_date,
    eur.status,
    eur.admin_notes,
    e.employee_id,
    pi.first_name AS current_first_name,
    pi.last_name AS current_last_name,
    pi.birthday AS current_birthday,
    ci.home_address AS current_address,
    ci.phone_number AS current_phone_number
FROM employee_update_requests eur
JOIN employees e ON eur.employee_number = e.employee_number
LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id
LEFT JOIN contact_information ci ON e.employee_id = ci.employee_id;
