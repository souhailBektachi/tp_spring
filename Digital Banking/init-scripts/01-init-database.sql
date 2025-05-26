-- Digital Banking Database Initialization Script

-- Ensure database exists
CREATE DATABASE IF NOT EXISTS ebanking_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use the database
USE ebanking_db;

-- Grant privileges to the user
GRANT ALL PRIVILEGES ON ebanking_db.* TO 'user'@'%';
FLUSH PRIVILEGES;

-- Create some initial configuration if needed
SET TIME_ZONE = '+00:00';

-- Optional: Create audit log table for tracking changes
CREATE TABLE IF NOT EXISTS audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_name VARCHAR(100) NOT NULL,
    entity_id BIGINT NOT NULL,
    action VARCHAR(50) NOT NULL,
    old_values JSON,
    new_values JSON,
    changed_by VARCHAR(100),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_entity_name_id (entity_name, entity_id),
    INDEX idx_changed_at (changed_at)
) ENGINE=InnoDB;

-- Log the initialization
INSERT INTO audit_log (entity_name, entity_id, action, new_values, changed_by) 
VALUES ('SYSTEM', 0, 'DATABASE_INITIALIZED', JSON_OBJECT('timestamp', NOW()), 'SYSTEM');
