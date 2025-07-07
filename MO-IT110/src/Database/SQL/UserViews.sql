-- UserViews.sql
-- Contains Views for User DAO

-- View for users with roles
CREATE OR REPLACE VIEW vw_users_with_roles AS
SELECT 
    u.user_id, 
    u.username, 
    u.password, 
    u.email, 
    u.is_active,
    e.employee_id,
    e.employee_number,
    STRING_AGG(r.role_name, ',') as roles
FROM users u
LEFT JOIN employees e ON u.user_id = e.user_id
LEFT JOIN user_roles ur ON u.user_id = ur.user_id AND ur.is_active = TRUE
LEFT JOIN roles r ON ur.role_id = r.role_id
GROUP BY u.user_id, u.username, u.password, u.email, u.is_active, e.employee_id, e.employee_number;
