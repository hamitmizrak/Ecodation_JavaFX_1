-- Database Create
CREATE DATABASE user_management;

-- Oluşturulmuş database seçmek
USE user_management;

-- user adında bir tablo oluştur
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- ###################################################################################################################

CREATE TABLE receipts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    receipt_number VARCHAR(50) NOT NULL UNIQUE,
    receipt_date DATE NOT NULL,
    tax_number VARCHAR(20) NOT NULL,
    company_name VARCHAR(100) NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    description TEXT,
    created_by VARCHAR(100) NOT NULL,
    account_code VARCHAR(50) NOT NULL,
    receipt_type ENUM('Ödeme', 'Tahsilat', 'Masraf', 'Gelir') NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    vat_rate DECIMAL(5,2) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_type ENUM('Nakit', 'Kredi Kartı', 'Havale', 'Çek') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ###################################################################################################################

-- Insert
INSERT INTO users(username,password,email) VALUES ("Hamit","123456","hamitmizrak@gmail.com");

-- Select
select * FROM users;

-- Find User
SELECT  *  FROM users WHERE username="Hamit" AND "123456";

-- Update
UPDATE users SET username="Hamit44", password="12345644", email="hamitmizrak@gmail.com44" WHERE id=1;

-- delete
DELETE FROM users  WHERE id=1;
