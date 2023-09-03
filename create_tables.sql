-- Creating the "Restaurants" table
CREATE TABLE IF NOT EXISTS Restaurants (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             address VARCHAR(255) NOT NULL,
                             phone VARCHAR(20),
                             opening_hours VARCHAR(100)
);

-- Creating the "Tables" table
CREATE TABLE IF NOT EXISTS Tables (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        number INT NOT NULL,
                        capacity INT NOT NULL,
                        availability ENUM('available', 'reserved') NOT NULL,
                        restaurant_id INT,
                        FOREIGN KEY (restaurant_id) REFERENCES Restaurants(id)
);

-- Creating the "Customers" table
CREATE TABLE IF NOT EXISTS Customers (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           first_name VARCHAR(50) NOT NULL,
                           last_name VARCHAR(50) NOT NULL,
                           email VARCHAR(100),
                           phone VARCHAR(20)
);

-- Creating the "Reservations" table with "start_time" and "end_time" columns
CREATE TABLE IF NOT EXISTS Reservations (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              date DATE NOT NULL,
                              start_time TIME NOT NULL,
                              end_time TIME NOT NULL,
                              number_of_people INT NOT NULL,
                              status ENUM('confirmed', 'canceled') NOT NULL,
                              restaurant_id INT,
                              customer_id INT,
                              table_id INT,
                              FOREIGN KEY (restaurant_id) REFERENCES Restaurants(id),
                              FOREIGN KEY (customer_id) REFERENCES Customers(id),
                              FOREIGN KEY (table_id) REFERENCES Tables(id)
);
