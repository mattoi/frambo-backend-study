package com.mattoi.frambo_study.order;

import org.junit.jupiter.api.Test;

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

	Integer customerId;
	List<Product> products = new ArrayList<Product>();
	List<Order> testOrders = new ArrayList<Order>();

	@BeforeEach
	void setup() throws InvalidRequestException, EntityNotFoundException {
		// service.initializeStatus();
		customerService.create(
				new Customer(
						null,
						"Matheus",
						"testmatheus@email.com",
						"5559822222222"));
		customerService.create(
				new Customer(
						null,
						"Cecilia",
						"testcecilia@email.com",
						"5559811111111"));
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
		customerId = customerService.findByPhoneNumber("5559822222222").id();
		products = productService.findAll();
		testOrders = List.of(new Order(null,
				customerId,
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
						customerId + 1,
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
	public void shouldCreateNewOrder() throws InvalidRequestException {
		Integer result = service.create(testOrders.get(0));
		assertEquals(true, result != null);
	}

	@Test
	public void shouldUpdateOrderStatus() throws InvalidRequestException, EntityNotFoundException {
		service.create(testOrders.get(0));
		var orderId = service.findAllByCustomerId(customerId).get(0).id();
		var result = service.updateOrderStatus(orderId, "SHIPPED");
		assertEquals(true, result);
	}

	@Test
	public void shouldFindAllOrders() throws InvalidRequestException {
		service.create(testOrders.get(0));
		var orders = service.findAll();
		assertEquals(true, orders.size() >= 1);
	}

	@Test
	public void shouldFindOrderById() throws InvalidRequestException, EntityNotFoundException {
		service.create(testOrders.get(0));
		var orderId = service.findAll().get(0).id();
		var savedOrder = service.findById(orderId);
		assertEquals(orderId, savedOrder.id());
	}

	@Test
	public void shouldFindOrdersByClientId() throws InvalidRequestException, EntityNotFoundException {
		service.create(testOrders.get(0));
		service.create(testOrders.get(1));
		var ordersFromFirstCustomer = service.findAllByCustomerId(testOrders.get(0).customerId());
		assertEquals(1, ordersFromFirstCustomer.size());
	}

	@Test
	public void shouldFindOrdersByStatus() throws InvalidRequestException {
		service.create(testOrders.get(0));
		var pendingOrders = service.findAllByStatus("PAYMENT_PENDING");
		assertEquals(true, pendingOrders.size() >= 1);
	}

}
