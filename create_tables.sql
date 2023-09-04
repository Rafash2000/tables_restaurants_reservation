-- Creating the "Restaurants" table
CREATE TABLE IF NOT EXISTS restaurants (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             city VARCHAR(255) NOT NULL,
                             address VARCHAR(255) NOT NULL,
                             phone VARCHAR(20),
                             opening_time TIME NOT NULL,
                             closing_time TIME NOT NULL
);

-- Creating the "Tables" table
CREATE TABLE IF NOT EXISTS tables (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        number INT NOT NULL,
                        capacity INT NOT NULL,
                        availability ENUM('available', 'reserved') NOT NULL,
                        restaurant_id INT,
                        FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

-- Creating the "Customers" table
CREATE TABLE IF NOT EXISTS customers (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           first_name VARCHAR(50) NOT NULL,
                           last_name VARCHAR(50) NOT NULL,
                           email VARCHAR(100),
                           phone VARCHAR(20)
);

-- Creating the "Reservations" table with "start_time" and "end_time" columns
CREATE TABLE IF NOT EXISTS reservations (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              date DATE NOT NULL,
                              start_time TIME NOT NULL,
                              end_time TIME NOT NULL,
                              number_of_people INT NOT NULL,
                              status ENUM('confirmed', 'canceled') NOT NULL,
                              restaurant_id INT,
                              customer_id INT,
                              table_id INT,
                              FOREIGN KEY (restaurant_id) REFERENCES restaurants(id),
                              FOREIGN KEY (customer_id) REFERENCES customers(id),
                              FOREIGN KEY (table_id) REFERENCES tables(id)
);
