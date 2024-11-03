package com.mattoi.frambo_study.customer;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
	@Mock
	private CustomerRepository repository;

	@InjectMocks
	private CustomerService service;

	Customer testCustomer = new Customer(
			null,
			"Matheus",
			null,
			"5598111111111");
	Customer invalidCustomer = new Customer(
			null,
			null,
			null,
			null);

	@Test
	public void shouldCreateNewCustomer() {
		try {
			when(repository.create(testCustomer)).thenReturn(1);
			assertEquals(1, service.create(testCustomer));
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void shouldNotCreateInvalidCustomer() {
		assertThrows(InvalidRequestException.class, () -> {
			service.create(invalidCustomer);
		});
	}

	@Test
	public void shouldFindAllCustomers() {
		try {
			when(repository.findAll()).thenReturn(List.of(testCustomer));
			var customers = service.findAll();
			assertEquals(1, customers.size());
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void shouldFindById() {
		try {
			when(repository.findById(1)).thenReturn(testCustomer);
			System.out.println(service.findById(1));
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void shouldNotFindNonexistentId() {
		when(repository.findById(0))
				.thenThrow(new EntityNotFoundException("Could not find a user with ID 0", null));
		assertThrows(EntityNotFoundException.class, () -> {
			service.findById(0);
		});
	}

	@Test
	public void shouldUpdateCustomer() {
		try {
			Customer customer = new Customer(null, "Matheus Soares", null, "5598111111111");
			when(repository.update(1, customer)).thenReturn(true);
			service.update(1, customer);
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

}
