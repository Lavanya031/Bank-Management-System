-- Bank Management System Database Setup Script

-- Create database
CREATE DATABASE IF NOT EXISTS bankdb;
USE bankdb;

-- Create accounts table
CREATE TABLE IF NOT EXISTS accounts (
    account_no INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    balance DOUBLE NOT NULL CHECK (balance >= 0)
);

-- Display success message
SELECT 'Database setup completed successfully!' AS Status;

-- Show table structure
DESCRIBE accounts;
