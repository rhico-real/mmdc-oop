#!/bin/bash

# PostgreSQL database creation script for CamuLite HR System
# This script creates the database and the user with proper permissions

# Database configuration
DB_NAME="motorph_payroll"
DB_USER="camulite_admin"
DB_PASSWORD="123"

# Check if PostgreSQL is running
if ! pg_isready > /dev/null 2>&1; then
    echo "PostgreSQL server is not running. Please start PostgreSQL and try again."
    exit 1
fi

# Create database and user
echo "Creating database '$DB_NAME' and user '$DB_USER'..."

# Connect to PostgreSQL as the postgres user and run SQL commands
psql -U postgres << EOF
-- Create user if not exists
DO \$\$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = '$DB_USER') THEN
        CREATE ROLE $DB_USER WITH LOGIN PASSWORD '$DB_PASSWORD';
    END IF;
END
\$\$;

-- Create database if not exists
SELECT 'CREATE DATABASE $DB_NAME WITH OWNER $DB_USER'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$DB_NAME')\gexec

-- Grant privileges to the user
GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;

-- Connect to the database and set default permissions
\c $DB_NAME

-- Set search path and grant privileges
ALTER ROLE $DB_USER SET search_path TO public;
GRANT ALL ON SCHEMA public TO $DB_USER;
EOF

if [ $? -eq 0 ]; then
    echo "Database '$DB_NAME' and user '$DB_USER' created successfully."
    echo "You can now run the application to create tables and import data."
else
    echo "Error creating database. Check PostgreSQL error messages above."
fi
