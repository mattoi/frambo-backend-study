CREATE TABLE IF NOT EXISTS Categories (
    category_id SERIAL NOT NULL,
    category_name VARCHAR(50) UNIQUE NOT NULL CHECK (LENGTH(category_name) >= 3),
    PRIMARY KEY (category_id)
);

CREATE TABLE IF NOT EXISTS Products (
    product_id SERIAL NOT NULL,
    product_name VARCHAR(50) UNIQUE NOT NULL CHECK (LENGTH(product_name) >= 3),
    product_description VARCHAR(250) UNIQUE NOT NULL CHECK (
        LENGTH(product_description) > 0
    ),
    photo_url VARCHAR(250) CHECK (LENGTH(photo_url) > 0),
    net_weight INT NOT NULL CHECK (net_weight > 0),
    price DECIMAL(5, 2) NOT NULL CHECK (price > 0),
    in_stock BOOLEAN NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (product_id),
    FOREIGN KEY (category_id) REFERENCES Categories (category_id)
);

CREATE TABLE IF NOT EXISTS Customers (
    customer_id SERIAL NOT NULL,
    customer_name VARCHAR(250) NOT NULL CHECK (LENGTH(customer_name) >= 3),
    email_address VARCHAR(250) UNIQUE CHECK (LENGTH(email_address) > 0),
    phone_number VARCHAR(20) UNIQUE NOT NULL CHECK (LENGTH(phone_number) >= 7),
    PRIMARY KEY (customer_id)
);

CREATE TABLE IF NOT EXISTS OrderStatus (
    status_id SERIAL NOT NULL,
    status_name VARCHAR(50) UNIQUE NOT NULL,
    PRIMARY KEY (status_name)
);

CREATE TABLE IF NOT EXISTS Orders (
    order_id SERIAL NOT NULL,
    customer_id INT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL CHECK (total_amount > 0),
    status_name VARCHAR(50) NOT NULL,
    date_created TIMESTAMP NOT NULL,
    last_updated TIMESTAMP NOT NULL CHECK (last_updated >= date_created),
    PRIMARY KEY (order_id),
    FOREIGN KEY (customer_id) REFERENCES Customers (customer_id),
    FOREIGN KEY (status_name) REFERENCES OrderStatus (status_name)
);

CREATE TABLE IF NOT EXISTS OrderItems (
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES Orders (order_id),
    FOREIGN KEY (product_id) REFERENCES Products (product_id)
);