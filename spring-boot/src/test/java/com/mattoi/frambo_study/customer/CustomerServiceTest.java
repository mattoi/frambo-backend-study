package com.mattoi.frambo_study.customer;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
	@Mock
	private CustomerRepository repository;

	@InjectMocks
	private CustomerService service;

	@BeforeEach
	void setup() throws InvalidRequestException {
		service.create(
				new Customer(
						null,
						"Cecilia",
						null,
						"5598111111111"));
		service.create(
				new Customer(
						null,
						"Matheus",
						null,
						"5598222222222"));
	}

	// * looking fine
	@Test
	public void shouldCreateNewCustomer() {
		try {
			Customer customer = new Customer(null, "Ayla", null, "5598333333333");
			/* when(repository.create(customer)).thenReturn(1); */
			service.create(customer);
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// * looking fine
	@Test
	public void shouldNotCreateInvalidCustomer() {
		assertThrows(InvalidRequestException.class, () -> {
			Customer customer = new Customer(null, null, null, "559833333333");
			service.create(customer);
		});
	}

	@Test
	public void shouldFindAllCustomers() {
		try {
			service.findAll();
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	// It's not possible to test findById() with this test framework because
	// the id of the @BeforeEach customers keeps changing, so I'm using
	// findByPhoneNumber to get the ID first
	@Test
	public void shouldFindByPhoneNumber() {
		try {
			var customer = service.findByPhoneNumber("5598111111111");
			assertEquals("Cecilia", customer.name());
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void shouldNotFindNonexistentNumber() {
		assertThrows(EntityNotFoundException.class, () -> {
			service.findByPhoneNumber("2");
		});
	}

	@Test
	public void shouldFindById() {
		try {
			var customerFromPhoneNumber = service.findByPhoneNumber("5598111111111");
			var customerFromId = service.findById(customerFromPhoneNumber.id());
			assertEquals(customerFromId, customerFromPhoneNumber);
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void shouldNotFindNonexistentId() {
		assertThrows(EntityNotFoundException.class, () -> {
			service.findById(0);
		});
	}

	@Test
	public void shouldUpdateCustomer() {
		try {
			var customer = service.findByPhoneNumber("5598111111111");
			service.update(customer.id(), new Customer(null, "Matheus Soares", null, null));
			var updatedCustomer = service.findByPhoneNumber("5598111111111");
			assertEquals(updatedCustomer.name(), "Matheus Soares");
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}

	}

	@Test
	public void shouldNotUpdateInvalidFields() {
		assertThrows(InvalidRequestException.class, () -> {
			service.update(1, new Customer(null, "", null, null));
		});
	}

	@Test
	public void shouldNotUpdateNonexistentId() {
		assertThrows(EntityNotFoundException.class, () -> {
			service.update(0, new Customer(null, "Matheus", null, null));
		});
	}
	/*
	 * @Test
	 * public void shouldDeleteCustomer() {
	 * var customer = service.findByPhoneNumber("559811111111");
	 * service.delete(customer.id());
	 * var customers = service.findAll();
	 * assertEquals(1, customers.size());
	 * }
	 */
}
