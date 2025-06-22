-- Create the table for employee update requests
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
    status VARCHAR(20) DEFAULT 'PENDING',
    admin_notes TEXT,
    FOREIGN KEY (employee_number) REFERENCES employees(employee_number)
);

-- Create index for faster queries
CREATE INDEX IF NOT EXISTS idx_update_requests_employee_number ON employee_update_requests(employee_number);
CREATE INDEX IF NOT EXISTS idx_update_requests_status ON employee_update_requests(status);
