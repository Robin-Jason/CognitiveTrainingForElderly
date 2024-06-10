CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_md5 CHAR(32) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, password_md5, email) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', 'admin@example.com'),
('Robin', 'e10adc3949ba59abbe56e057f20f883e', 'robin@example.com');