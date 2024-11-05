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

import com.mattoi.frambo_study.exception.EntityNotFoundException;
import com.mattoi.frambo_study.exception.InvalidRequestException;
import com.mattoi.frambo_study.product.Product;

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

	@Test
	public void shouldCreateNewOrder() {
		try {
			when(repository.create(testOrders.get(0))).thenReturn(1);
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
		when(repository.updateOrderStatus(0,"SHIPPED")).thenThrow(new EntityNotFoundException("Couldn't find an order with ID 0", null));
		assertThrows(EntityNotFoundException.class, () -> {
			service.updateOrderStatus(0, "SHIPPED");
		});
	}

	@Test
	public void shouldNotUpdateWithInvalidStatus() {
		when(repository.updateOrderStatus(1, "NOT A VALID STATUS")).thenThrow(new InvalidRequestException( List.of("Invalid status"), null));
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
			when(repository.findAllByStatus("STATUS THAT DOESN'T EXIST")).thenThrow(new InvalidRequestException( List.of("Invalid status"), null));
		assertThrows(InvalidRequestException.class, () -> {
			service.findAllByStatus("STATUS THAT DOESN'T EXIST");
		});
	}
}
