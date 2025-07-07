-- install_database_objects.sql
-- Script to install all database views and stored procedures

-- First execute all view scripts
\echo 'Installing database views...'

\echo 'Installing Employee views...'
\i 'EmployeeViews.sql'

\echo 'Installing Attendance views...'
\i 'AttendanceViews.sql'

\echo 'Installing LeaveRequest views...'
\i 'LeaveRequestViews.sql'

\echo 'Installing User views...'
\i 'UserViews.sql'

\echo 'Installing UpdateRequest views...'
\i 'UpdateRequestViews.sql'

-- Then execute all stored procedure scripts
\echo 'Installing stored procedures...'

\echo 'Installing Employee procedures...'
\i 'EmployeeProcedures.sql'

\echo 'Installing Attendance procedures...'
\i 'AttendanceProcedures.sql'

\echo 'Installing LeaveRequest procedures...'
\i 'LeaveRequestProcedures.sql'

\echo 'Installing User procedures...'
\i 'UserProcedures.sql'

\echo 'Installing UpdateRequest procedures...'
\i 'UpdateRequestProcedures.sql'

\echo 'All database objects have been installed successfully!'
