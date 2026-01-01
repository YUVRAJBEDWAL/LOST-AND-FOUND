-- database.sql

CREATE DATABASE IF NOT EXISTS campus_lost_found;
USE campus_lost_found;

-- USERS TABLE
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('user', 'admin') NOT NULL DEFAULT 'user'
);

-- LOST ITEMS TABLE
CREATE TABLE IF NOT EXISTS lost_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    item_name VARCHAR(100) NOT NULL,
    description TEXT,
    location VARCHAR(150) NOT NULL,
    lost_date DATE NOT NULL,
    user_id INT NOT NULL,
    status ENUM('open', 'claimed') NOT NULL DEFAULT 'open',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- FOUND ITEMS TABLE
CREATE TABLE IF NOT EXISTS found_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    item_name VARCHAR(100) NOT NULL,
    description TEXT,
    location VARCHAR(150) NOT NULL,
    found_date DATE NOT NULL,
    user_id INT NOT NULL,
    status ENUM('open', 'claimed') NOT NULL DEFAULT 'open',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- CLAIMS TABLE
CREATE TABLE IF NOT EXISTS claims (
    id INT PRIMARY KEY AUTO_INCREMENT,
    item_id INT NOT NULL,
    user_id INT NOT NULL,
    claim_date DATE NOT NULL,
    status ENUM('pending', 'approved', 'rejected') NOT NULL DEFAULT 'pending',
    FOREIGN KEY (item_id) REFERENCES found_items(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Default admin user (password: admin123)
INSERT INTO users (name, email, password, role)
VALUES ('Admin', 'admin@campus.com', 'admin123', 'admin')
ON DUPLICATE KEY UPDATE email = email;



