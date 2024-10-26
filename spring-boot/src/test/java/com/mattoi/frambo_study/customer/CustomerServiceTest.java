package com.mattoi.frambo_study.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;

@JdbcTest
@Import(CustomerService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerServiceTest {
	@Autowired
	private CustomerService service;

	@BeforeEach
	void setup() throws InvalidRequestException {
		service.create(
				new Customer(
						null,
						"Cecilia",
						"cecilia@email.com",
						"559811111111"));
		service.create(
				new Customer(
						null,
						"Matheus",
						"matheus@email.com",
						"559822222222"));
	}

	@Test
	public void shouldCreateNewCustomer() throws InvalidRequestException {
		service.create(new Customer(null, "Ayla", "ayla@email.com", "559833333333"));
		var customers = service.findAll();
		assertEquals(3, customers.size());
	}

	@Test
	public void shouldFindAllCustomers() {
		List<Customer> customers = service.findAll();
		assertEquals(2, customers.size());
	}

	// It's not possible to test findById() with this test framework because
	// the id of the @BeforeEach customers keeps changing, so I'm using
	// findByPhoneNumber to get the ID first
	@Test
	public void shouldFindByPhoneNumber() throws EntityNotFoundException {
		var customer = service.findByPhoneNumber("559811111111");
		assertEquals("Cecilia", customer.name());
	}

	@Test
	public void shouldFindById() throws EntityNotFoundException {
		var customerFromPhoneNumber = service.findByPhoneNumber("559811111111");
		var customerFromId = service.findById(customerFromPhoneNumber.id());
		assertEquals(customerFromId, customerFromPhoneNumber);
	}

	@Test
	public void shouldUpdateClient() throws EntityNotFoundException, InvalidRequestException {
		var customer = service.findByPhoneNumber("559811111111");
		service.update(customer.id(), new Customer(customer.id(), "Matheus Soares", null, null));
		var updatedClient = service.findByPhoneNumber("559811111111");
		assertEquals(updatedClient.name(), "Matheus Soares");
	}

	/*
	 * @Test
	 * public void shouldDeleteClient() {
	 * var customer = service.findByPhoneNumber("559811111111");
	 * service.delete(customer.id());
	 * var customers = service.findAll();
	 * assertEquals(1, customers.size());
	 * }
	 */
}
