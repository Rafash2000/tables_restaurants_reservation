CREATE TABLE IF NOT EXISTS restaurants (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             city VARCHAR(255) NOT NULL,
                             address VARCHAR(255) NOT NULL,
                             phone VARCHAR(20) NOT NULL,
                             opening_time TIME NOT NULL,
                             closing_time TIME NOT NULL,
                             available BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS restaurant_tables (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            number INT NOT NULL,
                            capacity INT NOT NULL,
                            date DATE NOT NULL,
                            time TIME NOT NULL,
                            availability ENUM('available', 'reserved') NOT NULL,
                            restaurant_id INT NOT NULL,
                            FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

CREATE TABLE IF NOT EXISTS app_users (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL ,
                                         surname VARCHAR(255) NOT NULL,
                                         email VARCHAR(255) NOT NULL UNIQUE,
                                         password VARCHAR(255) NOT NULL ,
                                         role ENUM('admin', 'user') NOT NULL,
                                         enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS reservations (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              date DATETIME NOT NULL,
                              number_of_people INT NOT NULL,
                              status ENUM('confirmed', 'canceled', 'not_confirmed') NOT NULL,
                              restaurant_id INT NOT NULL,
                              table_id INT NOT NULL,
                              user_id INT NOT NULL,
                              FOREIGN KEY (restaurant_id) REFERENCES restaurants(id),
                              FOREIGN KEY (table_id) REFERENCES restaurant_tables(id),
                              FOREIGN KEY (user_id) REFERENCES app_users(id)
);



CREATE TABLE IF NOT EXISTS employees(
                            id integer primary key auto_increment,
                            user_id INT NOT NULL,
                            restaurant_id INT NOT NULL,
                            FOREIGN KEY (user_id) REFERENCES app_users(id),
                            FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
)
