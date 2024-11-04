package com.mattoi.frambo_study.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import com.mattoi.frambo_study.customer.Customer;
import com.mattoi.frambo_study.customer.CustomerService;
import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;
import com.mattoi.frambo_study.product.Category;
import com.mattoi.frambo_study.product.Product;
import com.mattoi.frambo_study.product.ProductService;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
	@Mock
	private OrderRepository repository;

	@InjectMocks
	private OrderService service;
	List<Product> products = new ArrayList<Product>();
	List<Order> testOrders = List.of(new Order(null,
			1,
			List.of(
					new OrderItem(
							1,
							null,
							14d,
							2),
					new OrderItem(
							2,
							null,
							12d,
							3)),
			"PAYMENT_PENDING",
			LocalDateTime.now(),
			LocalDateTime.now()),
			new Order(null,
					2,
					List.of(
							new OrderItem(
									1,
									null,
									14d,
									1),
							new OrderItem(
									2,
									null,
									12d,
									1)),
					"DELIVERED",
					LocalDateTime.now(),
					LocalDateTime.now()));

	@BeforeEach
	void setup() throws InvalidRequestException, EntityNotFoundException {
		/* CustomerService customerService = new CustomerService(); */
		// service.initializeStatus();
		/*
		 * var firstCustomerId = customerService.create(
		 * new Customer(
		 * null,
		 * "Matheus",
		 * "testmatheus@email.com",
		 * "55598222222222"));
		 * var secondCustomerId = customerService.create(
		 * new Customer(
		 * null,
		 * "Cecilia",
		 * "testcecilia@email.com",
		 * "55598111111111"));
		 * ProductService productService = new ProductService();
		 * productService.createCategory(new Category(null, "Test Cookie"));
		 */
		/*
		 * var testProducts = List.of(new Product(null,
		 * "Test Cookie Original",
		 * "tCookie à base de manteiga com gotas de chocolate",
		 * null,
		 * 120,
		 * 14.00,
		 * true,
		 * "Test Cookie"),
		 * new Product(
		 * null,
		 * "Test Cookie pink lemonade",
		 * "tCookie à base de limão siciliano com gotas de Ruby Chocolate",
		 * null,
		 * 120,
		 * 12.00,
		 * true,
		 * "Test Cookie"));
		 */

		/* products = productService.findAll(); */

	}

	@Test
	public void shouldCreateNewOrder() {
		try {
			service.create(testOrders.get(0));
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void shouldNotCreateInvalidOrder() {
		assertThrows(InvalidRequestException.class, () -> {
			service.create(new Order(null, null, null, null, null, null));
		});
	}

	@Test
	public void shouldUpdateOrderStatus() {
		when(repository.updateOrderStatus(1, "SHIPPED")).thenReturn(true);
		try {
			var result = service.updateOrderStatus(1, "SHIPPED");
			assertEquals(true, result);
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void shouldNotUpdateOnInvalidId() {
		assertThrows(EntityNotFoundException.class, () -> {
			service.updateOrderStatus(0, null);
		});
	}

	@Test
	public void shouldNotUpdateWithInvalidStatus() {
		when(repository.updateOrderStatus(1, "NOT A VALID STATUS")).thenThrow(new InvalidRequestException("Invalid request fields", List.of("Invalid status"), null));
		assertThrows(InvalidRequestException.class, () -> {
			service.updateOrderStatus(1, "NOT A VALID STATUS");
		});
	}

	@Test
	public void shouldFindAllOrders() {
		try {
			when(repository.findAll()).thenReturn(testOrders);
			var orders = service.findAll();
			assertEquals(true, orders.size() >= 1);
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void shouldFindOrderById() {
		try {
			when(repository.findById(1)).thenReturn(testOrders.get(0));
			var savedOrder = service.findById(1);
			assertEquals(testOrders.get(0).customerId(), savedOrder.customerId());
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void shouldNotFindInvalidId() {
		when(repository.findById(0)).thenThrow(new EntityNotFoundException("Could not find an order with ID 0", null));
		assertThrows(EntityNotFoundException.class, () -> {
			service.findById(0);
		});
	}

	@Test
	public void shouldFindOrdersByCustomerId() {
		try {
			when(repository.findAllByCustomerId(1)).thenReturn(List.of(testOrders.get(0)));
			assertEquals(List.of(testOrders.get(0)), service.findAllByCustomerId(1));
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void shouldNotFindInvalidCustomerId() {
			when(repository.findAllByCustomerId(0)).thenThrow(new EntityNotFoundException("Could not find a customer with ID 0", null));
		assertThrows(EntityNotFoundException.class, () -> {
			service.findAllByCustomerId(0);
		});
	}

	@Test
	public void shouldFindOrdersByStatus() {
		try {
			when(repository.findAllByStatus("PAYMENT_PENDING")).thenReturn(List.of(testOrders.get(0)));
			var pendingOrders = service.findAllByStatus("PAYMENT_PENDING");
			assertEquals(List.of(testOrders.get(0)), pendingOrders);
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void shouldNotFindWithInvalidStatus() {
			when(repository.findAllByStatus("STATUS THAT DOESN'T EXIST")).thenThrow(new InvalidRequestException("Invalid status", null, null));
		assertThrows(InvalidRequestException.class, () -> {
			service.findAllByStatus("STATUS THAT DOESN'T EXIST");
		});
	}
}
