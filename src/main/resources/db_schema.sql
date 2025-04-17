-- Drop database if exists
DROP DATABASE IF EXISTS sunbeam;

-- Create database
CREATE DATABASE sunbeam;
USE sunbeam;

-- Create users table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    mobile VARCHAR(20),
    address VARCHAR(100),
    time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create categories table
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(200),
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create blogs table
CREATE TABLE blogs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    category_id INT NOT NULL,
    content TEXT,
    user_id INT NOT NULL,
    creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);

-- Insert sample categories
INSERT INTO categories (title, description) VALUES 
('Books', 'Books registered'),
('Technology', 'Technology registered'),
('Sports', 'Sports registered'),
('Movies', 'Movies registered'),
('Music', 'Music registered'),
('Food', 'Food registered'),
('Health', 'Health registered'),
('Science', 'Science registered'),
('Travel', 'Travel registered');

-- Insert sample users
INSERT INTO users (name, email, password, mobile, address) VALUES
('Aman', 'aman@gmail.com', 'aman', '7821081179', 'Gondia'),
('Bhagirath', 'bhagi@gmail.com', 'bagi', '9024973969', 'Jodhpur'),
('Sunbeam', 'sunbeam@gmail.com', 'sun', '987654324', 'Pune');

-- Insert sample blogs
INSERT INTO blogs (title, category_id, content, user_id) VALUES
('Fountain', 1, 'Book Fountain is very good.', 1),
('Godan', 2, 'Godan Book has written by Indian legend Munshi premchand', 1),
('Nirmla', 3, 'this is also written by legend himself Munshi Premchand', 2),
('Rich dad-Poor dad', 4, 'this book is legendry for those who want to learn about money', 2);