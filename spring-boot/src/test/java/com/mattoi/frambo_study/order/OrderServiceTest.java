package com.mattoi.frambo_study.order;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import com.mattoi.frambo_study.customer.Customer;
import com.mattoi.frambo_study.customer.CustomerService;
import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;
import com.mattoi.frambo_study.product.Category;
import com.mattoi.frambo_study.product.Product;
import com.mattoi.frambo_study.product.ProductService;

@JdbcTest
@Import({ OrderService.class, CustomerService.class, ProductService.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderServiceTest {
	@Autowired
	private OrderService service;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ProductService productService;

	List<Product> products = new ArrayList<Product>();
	List<Order> testOrders = new ArrayList<Order>();

	@BeforeEach
	void setup() throws InvalidRequestException, EntityNotFoundException {
		// service.initializeStatus();
		var firstCustomerId = customerService.create(
				new Customer(
						null,
						"Matheus",
						"testmatheus@email.com",
						"55598222222222"));
		var secondCustomerId = customerService.create(
				new Customer(
						null,
						"Cecilia",
						"testcecilia@email.com",
						"55598111111111"));
		productService.createCategory(new Category(null, "Test Cookie"));
		productService.create(new Product(null,
				"Test Cookie Original",
				"tCookie à base de manteiga com gotas de chocolate",
				null,
				120,
				14.00,
				true,
				"Test Cookie"));

		productService.create(new Product(
				null,
				"Test Cookie pink lemonade",
				"tCookie à base de limão siciliano com gotas de Ruby Chocolate",
				null,
				120,
				12.00,
				true,
				"Test Cookie"));
		products = productService.findAll();
		testOrders = List.of(new Order(null,
				firstCustomerId,
				List.of(
						new OrderItem(
								products.get(0).id(),
								null,
								products.get(0).price(),
								2),
						new OrderItem(
								products.get(1).id(),
								null,
								products.get(1).price(),
								3)),
				"PAYMENT_PENDING",
				LocalDateTime.now(),
				LocalDateTime.now()),
				new Order(null,
						secondCustomerId,
						List.of(
								new OrderItem(
										products.get(0).id(),
										null,
										products.get(0).price(),
										1),
								new OrderItem(
										products.get(1).id(),
										null,
										products.get(1).price(),
										1)),
						"DELIVERED",
						LocalDateTime.now(),
						LocalDateTime.now()));
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
		try {
			var orderId = service.create(testOrders.get(0));
			var result = service.updateOrderStatus(orderId, "SHIPPED");
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
		assertThrows(InvalidRequestException.class, () -> {
			// TO
			var orderId = service.create(testOrders.get(0));
			service.updateOrderStatus(orderId, "NOT A VALID STATUS");
		});
	}

	@Test
	public void shouldFindAllOrders() {
		try {
			service.create(testOrders.get(0));
			var orders = service.findAll();
			assertEquals(true, orders.size() >= 1);
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void shouldFindOrderById() {
		try {
			var orderId = service.create(testOrders.get(0));
			var savedOrder = service.findById(orderId);
			assertEquals(orderId, savedOrder.id());
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void shouldNotFindInvalidId() {
		assertThrows(InvalidRequestException.class, () -> {
			service.findById(0);
		});
	}

	@Test
	public void shouldFindOrdersByCustomerId() {
		try {
			service.create(testOrders.get(0));
			var secondTestOrderId = service.create(testOrders.get(1));
			var ordersFromFirstCustomer = service.findAllByCustomerId(testOrders.get(0).customerId());
			for (var order : ordersFromFirstCustomer) {
				if (order.id() == secondTestOrderId) {
					fail("Unexpected order found");
				}
			}
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void shouldNotFindInvalidCustomerId() {
		assertThrows(EntityNotFoundException.class, () -> {
			service.findAllByCustomerId(0);

		});
	}

	@Test
	public void shouldFindOrdersByStatus() {
		try {
			var orderId = service.create(testOrders.get(0));
			var pendingOrders = service.findAllByStatus("PAYMENT_PENDING");
			var found = false;
			for (var order : pendingOrders) {
				if (order.id() == orderId) {
					found = true;
					break;
				}
			}
			if (!found) {
				fail("Newly created order not found within pending list");
			}
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void shouldNotFindWithInvalidStatus() {
		assertThrows(InvalidRequestException.class, () -> {
			service.findAllByStatus("STATUS THAT DOESN'T EXIST");
		});
	}
}
