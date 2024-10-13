package com.mattoi.frambo_mock.customer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepository {

        private final JdbcClient jdbcClient;
        private static final Logger log = LoggerFactory.getLogger(CustomerRepository.class);

        public CustomerRepository(JdbcClient jdbcClient) {
                this.jdbcClient = jdbcClient;
        }

        public boolean create(Customer customer) {
                var updated = jdbcClient
                                .sql("INSERT INTO Customers(customer_name, email_address, phone_number) values(?,?,?)")
                                .params(customer.name(), customer.email(), customer.phoneNumber()).update();
                return updated == 1;
        }

        public boolean update(Customer updatedClient) {
                var updated = jdbcClient.sql(
                                "UPDATE Customers SET customer_id = ?, customer_name = ?, email_address = ?, phone_number = ? WHERE customer_id = ?")
                                .params(updatedClient.id(), updatedClient.name(), updatedClient.email(),
                                                updatedClient.phoneNumber(), updatedClient.id())
                                .update();
                return updated == 1;
        }

        public List<Customer> findAll() {
                return jdbcClient.sql("SELECT * from Customers ORDER BY customer_id")
                                .query((rows, rowNum) -> new Customer(
                                                rows.getInt("customer_id"),
                                                rows.getString("customer_name"),
                                                rows.getString("email_address"),
                                                rows.getString("phone_number")))
                                .list();
        }

        public Customer findById(Integer id) {
                return jdbcClient.sql("SELECT * from Customers WHERE customer_id = :id")
                                .param("id", id).query((rows, rowNum) -> new Customer(
                                                rows.getInt("customer_id"),
                                                rows.getString("customer_name"),
                                                rows.getString("email_address"),
                                                rows.getString("phone_number")))
                                .list().get(0);
        }

        public Customer findByPhoneNumber(String phoneNumber) {
                return jdbcClient.sql("SELECT * from Customers WHERE phone_number = :phone_number")
                                .param("phone_number", phoneNumber).query((rows, rowNum) -> new Customer(
                                                rows.getInt("customer_id"),
                                                rows.getString("customer_name"),
                                                rows.getString("email_address"),
                                                rows.getString("phone_number")))
                                .list().get(0);
        }

        public boolean delete(Integer id) {
                var updated = jdbcClient.sql("DELETE FROM Customers WHERE customer_id = :id")
                                .param("id", id).update();
                return updated == 1;
        }
}
