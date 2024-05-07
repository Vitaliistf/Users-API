CREATE TABLE user (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       birth_date DATE NOT NULL,
                       address VARCHAR(255),
                       phone_number VARCHAR(255)
);