CREATE SCHEMA IF NOT EXISTS anime;
CREATE TABLE IF NOT EXISTS anime
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);