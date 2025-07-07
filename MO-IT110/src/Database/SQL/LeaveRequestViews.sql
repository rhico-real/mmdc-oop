-- LeaveRequestViews.sql
-- Contains Views for Leave Request DAO

-- View for leave requests with employee information
CREATE OR REPLACE VIEW vw_leave_requests AS
SELECT 
    lr.leave_request_id,
    lr.request_number,
    e.employee_id,
    e.employee_number,
    pi.first_name,
    pi.last_name,
    lr.start_date,
    lr.end_date,
    lr.total_days,
    lr.reason,
    lt.leave_name AS leave_type,
    lr.status,
    lr.submitted_date,
    lr.approved_by,
    lr.approved_date,
    lr.remarks,
    lr.created_at,
    lr.updated_at
FROM leave_requests lr
JOIN employees e ON lr.employee_id = e.employee_id
LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id
LEFT JOIN leave_types lt ON lr.leave_type_id = lt.leave_type_id;
