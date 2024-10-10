package com.mattoi.frambo_mock.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
    private final JdbcClient jdbcClient;
    private static final Logger log = LoggerFactory.getLogger(OrderRepository.class);

    public OrderRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public boolean create(Order order) {
        jdbcClient
                .sql("INSERT INTO Orders(client_id, total_amount, status_name, date_created, last_updated) values(?,?,?,?,?)")
                .params(
                        order.clientId(),
                        order.totalAmount(),
                        order.status(),
                        order.dateCreated(),
                        order.lastUpdated()) // TODO check timestamp entries
                .update();
        int updated = 0;
        for (var item : order.items()) {
            updated += jdbcClient
                    .sql("INSERT INTO OrderItems(order_id, product_id, quantity) values(?,?,?)")
                    .params(order.id(), item.productId(), item.quantity()).update();
        }
        return updated == order.items().size();
    }

    public boolean updateOrderStatus(Integer id, String newStatus) {
        // todo update last_updated
        var updated = jdbcClient.sql("UPDATE Orders SET status_name = ? WHERE order_id = ?")
                .params(newStatus, id).update();
        return updated == 1;
    }

    public List<Order> findAll() {
        return jdbcClient.sql(
                "SELECT o.order_id, c.client_name, o.total_amount, o.status_name, o.date_created, o_last_updated, oi.product_id, p.product_name, oi.quantity "
                        + "FROM Orders o INNER JOIN Clients c ON o.client_id = c.client_id "
                        + "INNER JOIN OrderItems oi WHERE o.order_id = oi.order_id "
                        + "INNER JOIN Products p WHERE p.product_id = oi.product_id "
                        + "ORDER BY o.order_id")
                .query((rs, rowNum) -> {
                    Map<Integer, Order> orderMap = new HashMap<>();
                    while (rs.next()) {
                        int orderId = rs.getInt("order_id");
                        Order order = orderMap.get(orderId);
                        if (order == null) {
                            order = new Order(
                                    rs.getInt("order_id"),
                                    rs.getInt("client_id"),
                                    rs.getFloat("total_amount"),
                                    new ArrayList<>(),
                                    rs.getString("status_name"),
                                    rs.getTimestamp("date_created").toLocalDateTime(),
                                    rs.getTimestamp("last_updated").toLocalDateTime());
                            orderMap.put(orderId, order);
                        }
                        OrderItem item = new OrderItem(
                                rs.getInt("product_id"),
                                rs.getString("product_name"),
                                rs.getInt("quantity"));
                        order.items().add(item);
                    }
                    return new ArrayList<>(orderMap.values());
                }).single();
    }

    public Order findById(Integer id) {
        return jdbcClient.sql(
                "SELECT o.order_id, c.client_name, o.total_amount, o.status_name, o.date_created, o_last_updated, oi.product_id, p.product_name, oi.quantity "
                        + "FROM Orders o INNER JOIN Clients c ON o.client_id = c.client_id "
                        + "INNER JOIN OrderItems oi WHERE o.order_id = oi.order_id "
                        + "INNER JOIN Products p WHERE p.product_id = oi.product_id "
                        + "WHERE o.order_id = ?")
                .param(id)
                .query((rs, rowNum) -> {
                    Order order = null;
                    List<OrderItem> orderItems = new ArrayList<>();

                    while (rs.next()) {
                        if (order == null) {
                            order = new Order(
                                    rs.getInt("order_id"),
                                    rs.getInt("client_id"),
                                    rs.getFloat("total_amount"),
                                    orderItems,
                                    rs.getString("status_name"),
                                    rs.getTimestamp("date_created").toLocalDateTime(),
                                    rs.getTimestamp("last_updated").toLocalDateTime());
                        }
                        OrderItem item = new OrderItem(
                                rs.getInt("product_id"),
                                rs.getString("product_name"),
                                rs.getInt("quantity"));

                        orderItems.add(item);
                    }

                    return order;
                }).single();
    }

    public List<Order> findAllByClientId(Integer id) {
        return jdbcClient.sql(
                "SELECT o.order_id, c.client_name, o.total_amount, o.status_name, o.date_created, o_last_updated, oi.product_id, p.product_name, oi.quantity "
                        + "FROM Orders o INNER JOIN Clients c ON o.client_id = c.client_id "
                        + "INNER JOIN OrderItems oi WHERE o.order_id = oi.order_id "
                        + "INNER JOIN Products p WHERE p.product_id = oi.product_id "
                        + "WHERE o.client_id = ?"
                        + "ORDER BY o.order_id")
                .param(id)
                .query((rs, rowNum) -> {
                    Map<Integer, Order> orderMap = new HashMap<>();
                    while (rs.next()) {
                        int orderId = rs.getInt("order_id");
                        Order order = orderMap.get(orderId);
                        if (order == null) {
                            order = new Order(
                                    rs.getInt("order_id"),
                                    rs.getInt("client_id"),
                                    rs.getFloat("total_amount"),
                                    new ArrayList<>(),
                                    rs.getString("status_name"),
                                    rs.getTimestamp("date_created").toLocalDateTime(),
                                    rs.getTimestamp("last_updated").toLocalDateTime());
                            orderMap.put(orderId, order);
                        }
                        OrderItem item = new OrderItem(
                                rs.getInt("product_id"),
                                rs.getString("product_name"),
                                rs.getInt("quantity"));
                        order.items().add(item);
                    }
                    return new ArrayList<>(orderMap.values());
                }).single();
    }

    public List<Order> findAllByStatus(String status) {
        return jdbcClient.sql(
                "SELECT o.order_id, c.client_name, o.total_amount, o.status_name, o.date_created, o_last_updated, oi.product_id, p.product_name, oi.quantity "
                        + "FROM Orders o INNER JOIN Clients c ON o.client_id = c.client_id "
                        + "INNER JOIN OrderItems oi WHERE o.order_id = oi.order_id "
                        + "INNER JOIN Products p WHERE p.product_id = oi.product_id "
                        + "WHERE o.status_name = ?"
                        + "ORDER BY o.order_id")
                .param(status)
                .query((rs, rowNum) -> {
                    Map<Integer, Order> orderMap = new HashMap<>();
                    while (rs.next()) {
                        int orderId = rs.getInt("order_id");
                        Order order = orderMap.get(orderId);
                        if (order == null) {
                            order = new Order(
                                    rs.getInt("order_id"),
                                    rs.getInt("client_id"),
                                    rs.getFloat("total_amount"),
                                    new ArrayList<>(),
                                    rs.getString("status_name"),
                                    rs.getTimestamp("date_created").toLocalDateTime(),
                                    rs.getTimestamp("last_updated").toLocalDateTime());
                            orderMap.put(orderId, order);
                        }
                        OrderItem item = new OrderItem(
                                rs.getInt("product_id"),
                                rs.getString("product_name"),
                                rs.getInt("quantity"));
                        order.items().add(item);
                    }
                    return new ArrayList<>(orderMap.values());
                }).single();
    }
}
