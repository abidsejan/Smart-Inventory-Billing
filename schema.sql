CREATE DATABASE IF NOT EXISTS smart_inventory;

USE smart_inventory;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL -- 'Admin' or 'Staff'
);

CREATE TABLE IF NOT EXISTS items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    price DOUBLE NOT NULL,
    supplier VARCHAR(100),
    stock INT NOT NULL
);

CREATE TABLE IF NOT EXISTS bills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    bill_date DATETIME NOT NULL,
    subtotal DOUBLE NOT NULL,
    discount DOUBLE NOT NULL,
    tax DOUBLE NOT NULL,
    total DOUBLE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS bill_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bill_id INT,
    item_id INT,
    quantity INT NOT NULL,
    price_per_item DOUBLE NOT NULL,
    FOREIGN KEY (bill_id) REFERENCES bills(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
);

-- Insert a default admin user (password: admin)
-- The password 'admin' is stored as a BCrypt hash.
INSERT INTO users (username, password, role) VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin') ON DUPLICATE KEY UPDATE password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy';

