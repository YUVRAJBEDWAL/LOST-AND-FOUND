-- Sample Data for Campus Lost & Found with BCrypt-encoded passwords
-- Run this AFTER running database.sql
-- This adds test users and items for testing

USE campus_lost_found;

-- Clear existing users to avoid conflicts
DELETE FROM claims;
DELETE FROM lost_items;
DELETE FROM found_items;
DELETE FROM users;

-- Add test users with BCrypt-encoded passwords
-- Passwords: admin123, password123
INSERT INTO users (name, email, password, role) VALUES
('Admin User', 'admin@campus.com', '$2a$10$dvkWhQnwm7eMhg5yxPoKfOCwoCSuOTRJaTpjn2UGVYeJaTWtJGpHS', 'admin'),
('John Doe', 'john@campus.com', '$2a$10$kPXNtBuv70Er8tVHORZNfONkeK9BLR29OCuJe2nKQxKB2.SYvKT0y', 'user'),
('Jane Smith', 'jane@campus.com', '$2a$10$kPXNtBuv70Er8tVHORZNfONkeK9BLR29OCuJe2nKQxKB2.SYvKT0y', 'user'),
('Mike Johnson', 'mike@campus.com', '$2a$10$kPXNtBuv70Er8tVHORZNfONkeK9BLR29OCuJe2nKQxKB2.SYvKT0y', 'user'),
('Sarah Williams', 'sarah@campus.com', '$2a$10$kPXNtBuv70Er8tVHORZNfONkeK9BLR29OCuJe2nKQxKB2.SYvKT0y', 'user');

-- Add test lost items
-- Note: user_id 1 is admin, user_id 2+ are the users above
INSERT INTO lost_items (item_name, description, location, lost_date, user_id, status) VALUES
('Laptop', 'Dell Inspiron 15, Black, 15.6 inch screen', 'Library - 2nd Floor', '2024-01-15', 2, 'open'),
('iPhone 13 Pro', 'Blue iPhone 13 Pro with black case', 'Cafeteria - Table 5', '2024-01-20', 2, 'open'),
('Backpack', 'Red Nike backpack with laptop compartment', 'Gym - Locker Room', '2024-01-18', 3, 'open'),
('Wallet', 'Brown leather wallet with ID card', 'Parking Lot - Section B', '2024-01-22', 4, 'open'),
('Keys', 'Keychain with 5 keys and car remote', 'Student Center - Main Hall', '2024-01-25', 2, 'open');

-- Add test found items
INSERT INTO found_items (item_name, description, location, found_date, user_id, status) VALUES
('Keys', 'Car keys with keychain, found near parking', 'Parking Lot - Section A', '2024-01-26', 2, 'open'),
('Wallet', 'Black leather wallet, no cash inside', 'Library - 1st Floor', '2024-01-27', 3, 'open'),
('Watch', 'Silver wristwatch, found in restroom', 'Main Building - 3rd Floor', '2024-01-28', 4, 'open'),
('Glasses', 'Black frame glasses in case', 'Cafeteria - Lost & Found Box', '2024-01-29', 2, 'open'),
('USB Drive', '32GB USB drive, no label', 'Computer Lab - Room 205', '2024-01-30', 3, 'open');

-- View all data
SELECT 'Users:' as TableName;
SELECT * FROM users;

SELECT 'Lost Items:' as TableName;
SELECT * FROM lost_items;

SELECT 'Found Items:' as TableName;
SELECT * FROM found_items;
