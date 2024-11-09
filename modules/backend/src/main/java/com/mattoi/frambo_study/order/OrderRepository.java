package com.mattoi.frambo_study.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.mattoi.frambo_study.exception.CustomerNotFoundException;
import com.mattoi.frambo_study.exception.ProductsNotFoundException;

@Repository
public class OrderRepository {
	private final JdbcClient jdbcClient;
	private static final Logger log = LoggerFactory.getLogger(OrderRepository.class);

	public OrderRepository(JdbcClient jdbcClient) {
		this.jdbcClient = jdbcClient;
	}

	public boolean initializeStatus() {
		var updated = jdbcClient.sql("INSERT INTO OrderStatus (status_name) VALUES (?),(?),(?),(?)")
				.params(
						"PAYMENT_PENDING",
						"SHIPPED",
						"DELIVERED",
						"CANCELED")
				.update();
		return updated > 0;
	}

	public Integer create(Order order) throws CustomerNotFoundException, ProductsNotFoundException {
		try {
			jdbcClient
					.sql("INSERT INTO Orders(customer_id, total_amount, status_name, date_created, last_updated) values(?,?,?,?,?)")
					.params(
							order.customerId(),
							order.totalAmount(),
							"PAYMENT_PENDING",
							LocalDateTime.now(),
							LocalDateTime.now())
					.update();
		} catch (DataIntegrityViolationException e) {
			throw new CustomerNotFoundException(
					"Couldn't find a customer with ID " + order.customerId(), e);
		}
		var orders = jdbcClient.sql(
				"SELECT o.order_id FROM Orders o "
						+ "FULL OUTER JOIN OrderItems oi ON o.order_id = oi.order_id")
				.query((rows, rowNum) -> rows.getInt("order_id")).list();
		var newOrderId = orders.get(orders.size() - 1);
		int updated = 0;
		List<String> errors = new ArrayList<>();
		for (var item : order.items()) {
			try {
				updated += jdbcClient
						.sql("INSERT INTO OrderItems(order_id, product_id, quantity) values(?,?,?)")
						.params(newOrderId, item.productId(), item.quantity()).update();
			} catch (DataIntegrityViolationException e) {
				errors.add("Couldn't find a product with ID " + item.productId());
			}
		}
		if (errors.size() > 0) {
			throw new ProductsNotFoundException("Products not found", errors, null);
		}
		if (updated > 0 && updated == order.items().size()) {
			return newOrderId;
		}
		return null;
	}

	public boolean updateOrderStatus(Integer id, String newStatus) {
		var updated = jdbcClient.sql("UPDATE Orders SET status_name = ?, last_updated = ? WHERE order_id = ?")
				.params(newStatus, LocalDateTime.now(), id).update();
		return updated == 1;
	}

	public List<Order> findAll() {
		return jdbcClient.sql(
				"SELECT o.order_id, o.customer_id, c.customer_name, o.total_amount, o.status_name, o.date_created, o.last_updated, oi.product_id, p.product_name, p.price, oi.quantity "
						+ "FROM Orders o INNER JOIN Customers c ON o.customer_id = c.customer_id "
						+ "INNER JOIN OrderItems oi ON o.order_id = oi.order_id "
						+ "INNER JOIN Products p ON p.product_id = oi.product_id "
						+ "ORDER BY o.order_id")
				.query((rs, rowNum) -> {
					Map<Integer, Order> orderMap = new HashMap<>();
					do {
						int orderId = rs.getInt("order_id");
						Order order = orderMap.get(orderId);
						if (order == null) {
							order = new Order(
									rs.getInt("order_id"),
									rs.getInt("customer_id"),
									new ArrayList<>(),
									rs.getString("status_name"),
									rs.getTimestamp("date_created")
											.toLocalDateTime(),
									rs.getTimestamp("last_updated")
											.toLocalDateTime());
							orderMap.put(orderId, order);
						}
						OrderItem item = new OrderItem(
								rs.getInt("product_id"),
								rs.getString("product_name"),
								rs.getDouble("price"),
								rs.getInt("quantity"));
						order.items().add(item);
					} while (rs.next());
					return new ArrayList<>(orderMap.values());
				}).single();
	}

	public Order findById(Integer id) {
		return jdbcClient.sql(
				"SELECT o.order_id, c.customer_name, o.customer_id, o.total_amount, o.status_name, o.date_created, o.last_updated, oi.product_id, p.product_name, p.price, oi.quantity "
						+ "FROM Orders o INNER JOIN Customers c ON o.customer_id = c.customer_id "
						+ "INNER JOIN OrderItems oi ON o.order_id = oi.order_id "
						+ "INNER JOIN Products p ON p.product_id = oi.product_id "
						+ "WHERE o.order_id = ?")
				.param(id)
				.query((rs, rowNum) -> {
					Order order = null;
					List<OrderItem> orderItems = new ArrayList<>();

					do {
						if (order == null) {
							order = new Order(
									rs.getInt("order_id"),
									rs.getInt("customer_id"),
									orderItems,
									rs.getString("status_name"),
									rs.getTimestamp("date_created")
											.toLocalDateTime(),
									rs.getTimestamp("last_updated")
											.toLocalDateTime());
						}
						OrderItem item = new OrderItem(
								rs.getInt("product_id"),
								rs.getString("product_name"),
								rs.getDouble("price"),
								rs.getInt("quantity"));

						orderItems.add(item);
					} while (rs.next());

					return order;
				}).single();
	}

	public List<Order> findAllByCustomerId(Integer id) {
		return jdbcClient.sql(
				"SELECT o.order_id, c.customer_name, o.customer_id, o.total_amount, o.status_name, o.date_created, o.last_updated, oi.product_id, p.product_name, p.price, oi.quantity "
						+ "FROM Orders o INNER JOIN Customers c ON o.customer_id = c.customer_id "
						+ "INNER JOIN OrderItems oi ON o.order_id = oi.order_id "
						+ "INNER JOIN Products p ON p.product_id = oi.product_id "
						+ "WHERE o.customer_id = ? "
						+ "ORDER BY o.order_id")
				.param(id)
				.query((rs, rowNum) -> {
					Map<Integer, Order> orderMap = new HashMap<>();
					do {
						int orderId = rs.getInt("order_id");
						Order order = orderMap.get(orderId);
						if (order == null) {
							order = new Order(
									rs.getInt("order_id"),
									rs.getInt("customer_id"),
									new ArrayList<>(),
									rs.getString("status_name"),
									rs.getTimestamp("date_created")
											.toLocalDateTime(),
									rs.getTimestamp("last_updated")
											.toLocalDateTime());
							orderMap.put(orderId, order);
						}
						OrderItem item = new OrderItem(
								rs.getInt("product_id"),
								rs.getString("product_name"),
								rs.getDouble("price"),
								rs.getInt("quantity"));
						order.items().add(item);
					} while (rs.next());
					return new ArrayList<>(orderMap.values());
				}).single();
	}

	public List<Order> findAllByStatus(String status) {
		return jdbcClient.sql(
				"SELECT o.order_id, c.customer_name, o.customer_id, o.total_amount, o.status_name, o.date_created, o.last_updated, oi.product_id, p.product_name, p.price, oi.quantity "
						+ "FROM Orders o INNER JOIN Customers c ON o.customer_id = c.customer_id "
						+ "INNER JOIN OrderItems oi ON o.order_id = oi.order_id "
						+ "INNER JOIN Products p ON p.product_id = oi.product_id "
						+ "WHERE o.status_name = ? "
						+ "ORDER BY o.order_id")
				.param(status)
				.query((rs, rowNum) -> {
					Map<Integer, Order> orderMap = new HashMap<>();
					do {
						int orderId = rs.getInt("order_id");
						Order order = orderMap.get(orderId);
						if (order == null) {
							order = new Order(
									rs.getInt("order_id"),
									rs.getInt("customer_id"),
									new ArrayList<>(),
									rs.getString("status_name"),
									rs.getTimestamp("date_created")
											.toLocalDateTime(),
									rs.getTimestamp("last_updated")
											.toLocalDateTime());
							orderMap.put(orderId, order);
						}
						OrderItem item = new OrderItem(
								rs.getInt("product_id"),
								rs.getString("product_name"),
								rs.getDouble("price"),
								rs.getInt("quantity"));
						order.items().add(item);
					} while (rs.next());
					return new ArrayList<>(orderMap.values());
				}).single();
	}
}
