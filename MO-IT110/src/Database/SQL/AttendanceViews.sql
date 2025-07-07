-- AttendanceViews.sql
-- Contains Views for Attendance DAO

-- View for attendance records with employee information
CREATE OR REPLACE VIEW vw_attendance_records AS
SELECT 
    ar.attendance_id,
    ar.attendance_date,
    ar.time_in,
    ar.time_out,
    ar.break_time_minutes,
    ar.overtime_hours,
    ar.status,
    ar.notes,
    e.employee_id,
    e.employee_number,
    pi.first_name,
    pi.last_name
FROM attendance_records ar
JOIN employees e ON ar.employee_id = e.employee_id
LEFT JOIN personal_information pi ON e.employee_id = pi.employee_id;
