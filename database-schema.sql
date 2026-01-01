-- ========================================
-- CAMPUS LOST & FOUND - DATABASE SCHEMA
-- ========================================

-- Create Database
CREATE DATABASE IF NOT EXISTS campus_lost_found 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE campus_lost_found;

-- ========================================
-- USERS TABLE
-- ========================================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_created_at (created_at)
);

-- ========================================
-- LOST ITEMS TABLE
-- ========================================
CREATE TABLE lost_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    description TEXT,
    location VARCHAR(150) NOT NULL,
    lost_date DATE NOT NULL,
    status ENUM('OPEN', 'CLAIMED', 'RESOLVED') NOT NULL DEFAULT 'OPEN',
    category ENUM('ELECTRONICS', 'ACCESSORIES', 'BOOKS', 'IDS', 'CLOTHING', 'OTHERS'),
    photo_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_category (category),
    INDEX idx_lost_date (lost_date),
    INDEX idx_location (location),
    INDEX idx_created_at (created_at),
    
    -- Full-text search index
    FULLTEXT INDEX ft_search (item_name, description, location)
);

-- ========================================
-- FOUND ITEMS TABLE
-- ========================================
CREATE TABLE found_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    description TEXT,
    location VARCHAR(150) NOT NULL,
    found_date DATE NOT NULL,
    status ENUM('OPEN', 'CLAIMED', 'RESOLVED') NOT NULL DEFAULT 'OPEN',
    category ENUM('ELECTRONICS', 'ACCESSORIES', 'BOOKS', 'IDS', 'CLOTHING', 'OTHERS'),
    photo_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_category (category),
    INDEX idx_found_date (found_date),
    INDEX idx_location (location),
    INDEX idx_created_at (created_at),
    
    -- Full-text search index
    FULLTEXT INDEX ft_search (item_name, description, location)
);

-- ========================================
-- CLAIMS TABLE
-- ========================================
CREATE TABLE claims (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    found_item_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    description TEXT,
    claim_date DATE NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    admin_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (found_item_id) REFERENCES found_items(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_found_item_id (found_item_id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_claim_date (claim_date),
    INDEX idx_created_at (created_at)
);

-- ========================================
-- INSERT SAMPLE DATA
-- ========================================

-- Default Admin User (password: admin123 - will be hashed by Spring Security)
INSERT INTO users (name, email, password, role, phone) VALUES 
('Admin User', 'admin@campus.edu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOmANgNOgKQNNBDvAGK', 'ADMIN', '+1234567890')
ON DUPLICATE KEY UPDATE email = email;

-- Sample Users
INSERT INTO users (name, email, password, phone) VALUES 
('John Doe', 'john.doe@campus.edu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOmANgNOgKQNNBDvAGK', '+1234567891'),
('Jane Smith', 'jane.smith@campus.edu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOmANgNOgKQNNBDvAGK', '+1234567892'),
('Mike Johnson', 'mike.johnson@campus.edu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKVjzieMwkOmANgNOgKQNNBDvAGK', '+1234567893');

-- Sample Lost Items
INSERT INTO lost_items (item_name, description, location, lost_date, category, user_id) VALUES 
('MacBook Pro 16"', 'Silver MacBook Pro with stickers, Space Gray color', 'Library - 2nd Floor', '2024-01-15', 'ELECTRONICS', 2),
('iPhone 13 Pro', 'Blue iPhone 13 Pro with cracked screen protector', 'Cafeteria', '2024-01-14', 'ELECTRONICS', 2),
('Student ID Card', 'Campus ID for John Doe, ID#2024001234', 'Engineering Building', '2024-01-13', 'IDS', 3),
('Canvas Backpack', 'Black North Face backpack with laptop compartment', 'Sports Complex', '2024-01-12', 'CLOTHING', 3),
('Calculus Textbook', 'Stewart Calculus 8th Edition with notes', 'Math Department', '2024-01-11', 'BOOKS', 4);

-- Sample Found Items
INSERT INTO found_items (item_name, description, location, found_date, category, user_id) VALUES 
('AirPods Pro', 'White AirPods Pro with charging case', 'Library - Study Room 3', '2024-01-16', 'ELECTRONICS', 4),
('Wallet', 'Brown leather wallet containing cards and cash', 'Student Union', '2024-01-15', 'ACCESSORIES', 4),
('Keys', 'Set of keys with car key fob and dorm key', 'Parking Lot A', '2024-01-14', 'ACCESSORIES', 3),
('iPad Mini', 'Space Gray iPad Mini with Apple Pencil', 'Cafeteria', '2024-01-13', 'ELECTRONICS', 3),
('Glasses', 'Black rimmed glasses in case', 'Lecture Hall B', '2024-01-12', 'ACCESSORIES', 2);

-- Sample Claims
INSERT INTO claims (found_item_id, user_id, description, claim_date) VALUES 
(1, 2, 'I lost my AirPods Pro in the library yesterday. They are white with my name engraved.', '2024-01-17'),
(2, 3, 'This is my wallet. I can identify the contents and specific cards inside.', '2024-01-16');

-- ========================================
-- VIEWS FOR COMMON QUERIES
-- ========================================

-- View for recent items (both lost and found)
CREATE VIEW recent_items AS
SELECT 
    id, 
    item_name, 
    description, 
    location, 
    status, 
    category, 
    created_at,
    'LOST' as type,
    lost_date as item_date,
    user_id
FROM lost_items
UNION ALL
SELECT 
    id, 
    item_name, 
    description, 
    location, 
    status, 
    category, 
    created_at,
    'FOUND' as type,
    found_date as item_date,
    user_id
FROM found_items
ORDER BY created_at DESC;

-- View for user statistics
CREATE VIEW user_stats AS
SELECT 
    u.id,
    u.name,
    u.email,
    COUNT(DISTINCT li.id) as lost_items_count,
    COUNT(DISTINCT fi.id) as found_items_count,
    COUNT(DISTINCT c.id) as claims_count
FROM users u
LEFT JOIN lost_items li ON u.id = li.user_id
LEFT JOIN found_items fi ON u.id = fi.user_id
LEFT JOIN claims c ON u.id = c.user_id
GROUP BY u.id, u.name, u.email;

-- ========================================
-- STORED PROCEDURES
-- ========================================

DELIMITER //

-- Procedure to get dashboard statistics
CREATE PROCEDURE GetDashboardStats()
BEGIN
    SELECT 
        (SELECT COUNT(*) FROM users WHERE role = 'USER') as total_users,
        (SELECT COUNT(*) FROM lost_items WHERE status = 'OPEN') as open_lost_items,
        (SELECT COUNT(*) FROM found_items WHERE status = 'OPEN') as open_found_items,
        (SELECT COUNT(*) FROM claims WHERE status = 'PENDING') as pending_claims;
END //

-- Procedure to search items
CREATE PROCEDURE SearchItems(IN search_keyword VARCHAR(255))
BEGIN
    SELECT 
        'LOST' as item_type,
        id,
        item_name,
        description,
        location,
        lost_date as item_date,
        status,
        category,
        created_at
    FROM lost_items
    WHERE item_name LIKE CONCAT('%', search_keyword, '%')
       OR description LIKE CONCAT('%', search_keyword, '%')
       OR location LIKE CONCAT('%', search_keyword, '%')
    
    UNION ALL
    
    SELECT 
        'FOUND' as item_type,
        id,
        item_name,
        description,
        location,
        found_date as item_date,
        status,
        category,
        created_at
    FROM found_items
    WHERE item_name LIKE CONCAT('%', search_keyword, '%')
       OR description LIKE CONCAT('%', search_keyword, '%')
       OR location LIKE CONCAT('%', search_keyword, '%')
    
    ORDER BY created_at DESC;
END //

DELIMITER ;

-- ========================================
-- TRIGGERS FOR AUDITING
-- ========================================

DELIMITER //

-- Trigger to audit lost item changes
CREATE TRIGGER lost_items_audit
AFTER UPDATE ON lost_items
FOR EACH ROW
BEGIN
    IF OLD.status != NEW.status THEN
        INSERT INTO audit_log (table_name, record_id, field_name, old_value, new_value, changed_at)
        VALUES ('lost_items', NEW.id, 'status', OLD.status, NEW.status, NOW());
    END IF;
END //

DELIMITER ;

-- ========================================
-- PERFORMANCE OPTIMIZATION
-- ========================================

-- Composite indexes for common queries
CREATE INDEX idx_lost_items_status_category ON lost_items(status, category);
CREATE INDEX idx_found_items_status_category ON found_items(status, category);
CREATE INDEX idx_claims_status_date ON claims(status, claim_date);

-- ========================================
-- SECURITY
-- ========================================

-- Create a read-only user for reporting
CREATE USER IF NOT EXISTS 'campus_report'@'localhost' IDENTIFIED BY 'report123';
GRANT SELECT ON campus_lost_found.* TO 'campus_report'@'localhost';

-- Create application user with limited privileges
CREATE USER IF NOT EXISTS 'campus_app'@'localhost' IDENTIFIED BY 'app123';
GRANT SELECT, INSERT, UPDATE, DELETE ON campus_lost_found.* TO 'campus_app'@'localhost';

FLUSH PRIVILEGES;
