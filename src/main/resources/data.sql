CREATE TABLE transaction (id BIGINT AUTO_INCREMENT PRIMARY KEY,customer_id VARCHAR(255) NOT NULL,date DATE NOT NULL,amount DOUBLE PRECISION NOT NULL);
INSERT INTO transaction (customer_id, date, amount) VALUES ('cust1', '2025-04-10', 120.0);
INSERT INTO transaction (customer_id, date, amount) VALUES ('cust1', '2025-05-15', 80.0);
INSERT INTO transaction (customer_id, date, amount) VALUES ('cust2', '2025-06-05', 90.0);

