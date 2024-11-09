package com.mattoi.frambo_study.customer;

import java.util.ArrayList;
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

	public int create(Customer customer) {
		var newCustomerId = jdbcClient
				.sql("INSERT INTO Customers(customer_name, email_address, phone_number) values(?,?,?) RETURNING customer_id")
				.params(customer.name(), customer.email(), customer.phoneNumber()).query(int.class)
				.single();
		return newCustomerId;
	}

	public boolean update(int id, Customer customer) {
		String query = "UPDATE Customers SET   ";
		var updatedFields = new ArrayList<Object>();

		if (customer.name() != null) {
			updatedFields.add(customer.name());
			query += "customer_name = ?, ";
		}
		if (customer.email() != null) {
			updatedFields.add(customer.email());
			query += "email_address = ?, ";
		}
		if (customer.phoneNumber() != null) {
			updatedFields.add(customer.phoneNumber());
			query += "phone_number = ?, ";
		}
		updatedFields.add(id);
		// Formatting query to remove last comma and add where
		query = query.substring(0, query.length() - 2) + " WHERE customer_id = ?";

		var updated = jdbcClient.sql(query)
				.params(updatedFields)
				.update();
		return updated == 1;
	}

	public List<Customer> findAll() {
		return jdbcClient.sql("SELECT * from Customers ORDER BY customer_id")
				.query((rs, rowNum) -> new Customer(
						rs.getInt("customer_id"),
						rs.getString("customer_name"),
						rs.getString("email_address"),
						rs.getString("phone_number")))
				.list();
	}

	public Customer findById(Integer id) {
		return jdbcClient.sql("SELECT * from Customers WHERE customer_id = :id")
				.param("id", id).query((rs, rowNum) -> new Customer(
						rs.getInt("customer_id"),
						rs.getString("customer_name"),
						rs.getString("email_address"),
						rs.getString("phone_number")))
				.list().get(0);
	}

	public Customer findByPhoneNumber(String phoneNumber) {
		return jdbcClient.sql("SELECT * from Customers WHERE phone_number = :phone_number")
				.param("phone_number", phoneNumber).query((rs, rowNum) -> new Customer(
						rs.getInt("customer_id"),
						rs.getString("customer_name"),
						rs.getString("email_address"),
						rs.getString("phone_number")))
				.list().get(0);
	}

	public boolean delete(Integer id) {
		var updated = jdbcClient.sql("DELETE FROM Customers WHERE customer_id = :id")
				.param("id", id).update();
		return updated == 1;
	}
}
